# Documentation des Routes API

## Vue d'ensemble

Cette documentation est alignée sur les routes effectivement enregistrees dans `src/main/java/org/pfa/App.java`.

- Base URL: `http://localhost:7070`
- Framework HTTP: Javalin
- Authentification: token de session transmis dans le header `Authorization`
- Attention: le backend n'utilise pas le prefixe `Bearer `, il attend directement la valeur du token

## Format des reponses

Le backend n'a pas un format unique pour toutes les routes.

- Format `Response` (le plus frequent):

```json
{
  "status": "success|error|ok",
  "message": "...",
  "data": "... (optionnel)"
}
```

- Format login (specifique):

```json
{
  "status": "success",
  "message": "Login successful",
  "sID": "token-de-session"
}
```

- Certaines routes (notamment `DocumentRoutes` et `FileController.getAll/getOne`) renvoient directement des objets/metiers JSON sans enveloppe `status/message`.

## Authentification et roles

- Header attendu: `Authorization: <token>`
- Token manquant/invalide: `401` avec `{"status":"error","message":"Invalid or missing token"}`
- Controle role insuffisant: `403` avec `{"status":"error","message":"Insufficient privileges"}`
- Bypass debug present dans le code: `Authorization: DEBUG`

Roles enum declares:
- `USER`
- `ADMIN`
- `EXPERT`
- `COMPTA_CONF`
- `COMPTA_JR`
- `ASSISTANT`
- `STAGIAIRE`
- `CLIENT`

---

## 1) `UserRoutes.java`

### POST `/login`

Description: authentifie un utilisateur.

- Auth requise: non
- Content-Type attendu: `application/x-www-form-urlencoded`
- Entrees:
- `mail` (form param, string)
- `password` (form param, string)

Succes `200`:

```json
{
  "status": "success",
  "message": "Login successful",
  "sID": "<token>"
}
```

Erreur `401`:

```json
{
  "status": "error",
  "message": "Invalid email or password"
}
```

### POST `/register`

Description: cree un utilisateur.

- Auth requise: oui (token valide)
- Role requis: `ADMIN`
- Content-Type attendu: `application/x-www-form-urlencoded`
- Entrees:
- token dans `Authorization`
- `mail` (obligatoire)
- `password` (obligatoire)
- `nom` (obligatoire)
- `prenom` (obligatoire)
- `code` (optionnel dans la validation mais lu par le backend)
- `roles` List des roles séparé par une virgule

Succes `201`:

```json
{
  "status": "success",
  "message": "Compte créé avec succès"
}
```

Erreurs:
- `400` si champs obligatoires manquants
- `401` si token absent/invalide
- `409` si email deja utilise

### POST `/logout`

Description: invalide la session courante.

- Auth requise: oui
- Entree: token dans `Authorization`

Succes `200`:

```json
{
  "status": "success",
  "message": "Logout successful"
}
```

### GET `/role/{id}`

Description: recupere la liste des roles d'un utilisateur.

- Auth requise: oui
- Role requis: `ADMIN`
- Path param:
- `id` (int)

Succes `200`:

```json
{
  "status": "success",
  "message": "Rôle récupéré avec succès",
  "data": ["ADMIN", "USER"]
}
```

Erreurs:
- `400` id invalide
- `401` token absent/invalide
- `403` role insuffisant

---

## 2) `RoleRoutes.java`

### GET `/role-manager`

Description: liste tous les utilisateurs.

- Auth requise: oui
- Role requis: `ADMIN`

Succes `200`:

```json
{
  "status": "success",
  "message": "Voici la liste de tous les utilisateurs",
  "data": [
    {
      "id": 1,
      "mail": "..."
    }
  ]
}
```

### POST `/role-manager/{id}`

Description: change les roles d'un utilisateur.

- Auth requise: oui
- Role requis: `ADMIN`
- Content-Type attendu: `application/x-www-form-urlencoded`
- Path param:
- `id` (int)
- Entree:
- `roles` (form param, string CSV), ex: `ADMIN,USER`

Succes `200`:

```json
{
  "status": "success",
  "message": "Rôle de l'utilisateur 5 changé en [ADMIN, USER]"
}
```

Erreur `500` (si echec de mise a jour role):

```json
{
  "status": "error",
  "message": "Failed to change role for user 5"
}
```

### POST `/is-admin`

Description: Vérifie que le user soit ADMIN.

- Auth requise: oui

Succes `200`:

```json
{
  "status": "success",
  "message": "L'utilisateur est un admin"
}
```

Erreur `500` (si l'utilisateur n'est pas ADMIN) :

```json
{
  "status": "error",
  "message": "L'utilisateur n'est pas un admin"
}
```
### GET `/get-all-roles`

Description: Récupérer la listes de tous les roles.

- Auth requise: oui
- Role requis: `ADMIN`

Succes `200`:

```json
{
  "status": "success",
  "message": "Voici la liste de tous les rôles",
  "data": ["ADMIN", "EXPERT", ... ]
}
```
---
## 3) `DocumentRoutes.java`

Ces routes ne font pas de `checkToken` dans le code actuel.

### GET `/documents`

Description: retourne tous les documents.

- Reponse `200`: tableau JSON de `Document`

### GET `/documents/{id}`

Description: retourne un document par id.

- Path param: `id` (int)
- Reponse `200`: objet `Document`
- Reponse `404`: texte brut `Document non trouvé`

### POST `/documents`

Description: cree un document.

- Content-Type attendu: `application/json`
- Body JSON attendu:

```json
{
  "nom": "facture-001.pdf",
  "id_type_document": 1,
  "dateDocument": "2026-03-15",
  "id_utilisateur": 3
}
```

- Reponse `201`: objet `Document` cree
- Reponse `400`: texte `Erreur: ...`

### PUT `/documents/{id}`

Description: met a jour un document.

- Path param: `id` (int)
- Content-Type attendu: `application/json`
- Body JSON accepte les memes champs que `POST /documents`
- Reponse `200`: objet `Document` mis a jour
- Reponse `400`: texte erreur

### DELETE `/documents/{id}`

Description: supprime un document.

- Path param: `id` (int)
- Reponse `204`: sans body

---

## 4) `FileRoutes.java`

### GET `/files`

Description: liste les dossiers.

- Auth requise: non (dans l'implementation actuelle)
- Reponse `200`: tableau JSON de `File`

### GET `/files/{id}`

Description: recupere un dossier par id.

- Auth requise: non
- Path param: `id` (int)
- Reponse `200`: objet `File`
- Reponse `404`:

```json
{
  "status": "error",
  "message": "Dossier introuvable"
}
```

- Reponse `400` si id non numerique

### POST `/file`

Description: cree un dossier puis initialise automatiquement un plan de comptes.

- Auth requise: oui
- Content-Type attendu: `application/json`
- Body JSON attendu: structure compatible avec le modele `File`, incluant au minimum:

```json
{
  "telephone": "0102030405",
  "mail": "societe@example.com",
  "denominationSociale": "Ma Societe",
  "capital": 10000,
  "adresse": {
    "pays": "France",
    "ville": "Paris",
    "rue": "Rue Exemple",
    "numero": "1",
    "complement": "",
    "codePostal": "75001"
  },
  "formeJuridique": { "idFormeJuridique": 1 },
  "regimeFiscal": { "idRegimeFiscal": 1 },
  "regimeTVA": { "idRegimeTVA": 1 }
}
```
- récupérer la liste des valeurs possibles pour la forme juridique, le regime fiscal et le régime TVA avec les routes /formeJuridique, /regimeFiscal et /regimeTVA

Succes `200`:

```json
{
  "status": "ok",
  "message": "Dossier créé"
}
```

Erreurs:
- `400` `Regime fiscal invalide`
- `400` `Regime TVA invalide`
- `400` `Forme juridique invalide`
- `401` token absent/invalide

---

## 5) `DashboardRoutes.java`

### GET `/file/{id}`

Description: retourne les donnees de dashboard d'un dossier (`file`, `turnover`, `sig`).

- Auth requise: oui
- Path param: `id` (int)

Succes `200`:

```json
{
  "status": "success",
  "message": {
    "file": { "id": 1, "denominationSociale": "..." },
    "turnover": [0, 0, 0],
    "sig": { "resultatExercice": 0.0 }
  }
}
```

Erreur `401` si dossier non trouve (implementation actuelle):

```json
{
  "status": "error",
  "message": "dossier non trouvé"
}
```

---

## 6) `GrandLivreRoutes.java`

### GET `/grandlivre`

Description: calcule un grand livre pour une plage de dates.

- Auth requise: oui
- Particularite importante: la route est en `GET` mais lit un body JSON
- Content-Type attendu: `application/json`
- Body attendu:

```json
{
  "idDossier": 1,
  "debut": "2026-01-01T00:00:00.000+00:00",
  "fin": "2026-12-31T23:59:59.000+00:00"
}
```

Succes `200`:

```json
{
  "status": "success",
  "message": {
    "denominationSociale": "Ma Societe",
    "bilanCompte": {
      "401": [
        {
          "dateEcriture": "...",
          "soldeProgressif": 123.45
        }
      ]
    }
  }
}
```

---

## 7) `CompteRoutes.java`

### POST `/compte`

Description: cree un compte auxiliaire rattaché au compte de rattachement indiqué.

- Auth requise: oui
- Content-Type attendu: `application/json`
- Body attendu:

```json
{
  "idTypeCompteAuxiliaire": 1,
  "idCompteRattachement": 2096,
  "numero": "41102001",
  "libelle": "libelle"
}
```

Succes `200`:

```json
{
  "status": "ok",
  "message": "Compte auxiliaire créé"
}
```

Erreurs:
- `400` `Numero de compte invalide` (`numero` doit etre numerique)
- `400` `Dossier invalide`
- `401` token absent/invalide

---

## Exemples cURL coherents

### Login

```bash
curl -X POST http://localhost:7070/login \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "mail=admin@example.com&password=password123"
```

### Liste users (admin)

```bash
curl -X GET http://localhost:7070/role-manager \
  -H "Authorization: VOTRE_TOKEN"
```

### Changement de roles (admin)

```bash
curl -X POST http://localhost:7070/role-manager/5 \
  -H "Authorization: VOTRE_TOKEN" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "roles=ADMIN,USER"
```
