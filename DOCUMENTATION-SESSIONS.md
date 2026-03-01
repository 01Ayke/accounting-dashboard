# Documentation - Système de Sessions

## Vue d'ensemble

Ce projet utilise un système de sessions basé sur des tokens UUID pour gérer l'authentification des utilisateurs. Les sessions ont une durée de vie limitée et sont automatiquement renouvelées à chaque utilisation (sliding sessions).

## Architecture

### Composants principaux

1. **SessionManager** - Gestionnaire central des sessions
2. **SessionUser** - Modèle représentant une session utilisateur
3. **Controller** - Classe de base fournissant la méthode de vérification des tokens
4. **UserController** - Contrôleur gérant le login/logout

---

## Fonctionnement des sessions

### 1. Création de session (Login)

Lors de la connexion d'un utilisateur :

```
POST /login
Content-Type: application/x-www-form-urlencoded

mail=utilisateur@example.com
password=motdepasse
```

**Processus :**
1. Les identifiants sont récupérés via `LoginRequest`
2. Validation des credentials dans la base de données
3. Si valide, création d'une session via `SessionManager.createSession(userId)`
4. Génération d'un token UUID unique
5. Retour du token au client dans la réponse JSON

**Réponse en cas de succès :**
```json
{
  "status": "success",
  "message": "Login successful",
  "sID": "550e8400-e29b-41d4-a716-446655440000"
}
```

**Réponse en cas d'échec :**
```json
{
  "status": "error",
  "message": "Invalid email or password"
}
```

### 2. Paramètres de session

- **TTL (Time To Live)** : 30 minutes par défaut
- **Type de session** : Sliding session (le TTL se renouvelle à chaque requête)
- **Stockage** : En mémoire avec `ConcurrentHashMap` (thread-safe)
- **Limitation** : Un utilisateur ne peut avoir qu'une seule session active

### 3. Gestion automatique

Le `SessionManager` inclut un système de nettoyage automatique :

- **Intervalle** : Toutes les 5 minutes
- **Action** : Supprime les sessions expirées
- **Thread** : `ScheduledExecutorService` en arrière-plan

### 4. Arrêt propre

Lors de l'arrêt de l'application, un shutdown hook arrête proprement le scheduler :

```java
Runtime.getRuntime().addShutdownHook(new Thread(sessionManager::shutdown));
```

---

## Vérification des requêtes authentifiées

### Prérequis obligatoires

Pour toute requête nécessitant une authentification, le client **DOIT** :

1. **Inclure le header Authorization** avec le token reçu lors du login
2. **Utiliser le bon format** : le token brut (UUID)

**Exemple de requête authentifiée :**
```
POST /logout
Authorization: 550e8400-e29b-41d4-a716-446655440000
```

### Méthode de vérification

La classe `Controller` fournit une méthode protected `checkToken(Context ctx)` que tous les contrôleurs peuvent utiliser :

```java
public boolean checkToken(Context ctx) {
    String token = ctx.header("Authorization");
    if(token != null && sessionManager.validate(token) != null) {
        return true;
    } else {
        Response response = new Response("error", "Invalid or missing token", 401);
        response.setContext(ctx);
        return false;
    }
}
```

**Processus de validation :**
1. Récupération du token depuis le header `Authorization`
2. Vérification que le token existe
3. Validation via `SessionManager.validate(token)` qui :
   - Vérifie que la session existe
   - Vérifie que la session n'est pas expirée
   - Renouvelle automatiquement le TTL (sliding session)
4. Retourne `true` si valide, `false` sinon
5. En cas d'échec, envoie automatiquement une réponse HTTP 401

### Utilisation dans un contrôleur

Pour protéger un endpoint, il suffit d'appeler `checkToken()` au début de la méthode handler :

```java
public void handleProtectedEndpoint(Context ctx) {
    if (!super.checkToken(ctx)) {
        return; // La réponse d'erreur est déjà envoyée
    }
    
    // Code métier pour les utilisateurs authentifiés
    // ...
}
```

**Exemple concret (logout) :**
```java
public void handleLogout(Context ctx) {
    LogoutRequest logoutRequest = new LogoutRequest(ctx);
    if (super.checkToken(ctx)) {
        String token = logoutRequest.getToken();
        sessionManager.invalidate(token);
        Response response = new Response("success", "Logout successful", 200);
        response.setContext(ctx);
    } 
}
```

### Réponse en cas de token invalide

```json
{
  "status": "error",
  "message": "Invalid or missing token"
}
```

**Code HTTP** : `401 Unauthorized`

---

## Cas d'usage typiques

### 1. Flux complet d'authentification

```
Client                          Serveur
  |                                |
  |-- POST /login ---------------->|
  |   (mail, password)             |
  |                                |
  |<-- 200 OK ---------------------|
  |    {sID: "token-uuid"}         |
  |                                |
  |-- POST /protected-endpoint --->|
  |   Authorization: token-uuid    |
  |                                |
  |<-- 200 OK ---------------------|
  |    {données protégées}         |
  |                                |
  |-- POST /logout --------------->|
  |   Authorization: token-uuid    |
  |                                |
  |<-- 200 OK ---------------------|
  |    {logout successful}         |
```

### 2. Session expirée

Si un utilisateur attend plus de 30 minutes sans activité :

```
Client                          Serveur
  |                                |
  |-- POST /protected-endpoint --->|
  |   Authorization: token-expired |
  |                                |
  |<-- 401 Unauthorized -----------|
  |    {Invalid or missing token}  |
```

Le client doit alors redemander un login.

### 3. Renouvellement automatique (Sliding session)

Chaque requête valide renouvelle le TTL :

```
T=0min  : Login → Session valide jusqu'à T=30min
T=20min : Requête X → Session prolongée jusqu'à T=50min
T=40min : Requête Y → Session prolongée jusqu'à T=70min
```

---

## Sécurité

### Points importants

✅ **Une seule session par utilisateur** - Toute nouvelle connexion invalide l'ancienne
✅ **Expiration automatique** - Les sessions inutilisées expirent après 30 minutes
✅ **Nettoyage périodique** - Les sessions expirées sont supprimées de la mémoire
✅ **Thread-safe** - Utilisation de `ConcurrentHashMap`
✅ **Validation systématique** - Vérification de l'expiration à chaque utilisation

### Limitations actuelles

⚠️ **Stockage en mémoire** - Les sessions sont perdues au redémarrage du serveur
⚠️ **Pas de HTTPS obligatoire** - Les tokens transitent en clair (à sécuriser en production)
⚠️ **Format du token simple** - UUID sans signatures (considérer JWT pour plus de sécurité)

---

## Checklist pour développeurs

Lors de l'ajout d'un nouvel endpoint protégé :

- [ ] Le contrôleur étend `Controller` et reçoit `SessionManager` dans son constructeur
- [ ] La méthode handler appelle `super.checkToken(ctx)` en premier
- [ ] En cas de `checkToken()` retournant `false`, la méthode fait `return` immédiatement
- [ ] La route est enregistrée correctement dans la classe Routes correspondante
- [ ] Le `SessionManager` est passé lors de l'enregistrement si nécessaire

### Exemple de template

```java
public class MonController extends Controller {
    
    public MonController(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void handleMonEndpoint(Context ctx) {
        // Vérification obligatoire pour les endpoints protégés
        if (!super.checkToken(ctx)) {
            return;
        }
        
        // Logique métier
        // ...
        
        // Réponse
        Response response = new Response("success", "Operation successful", 200);
        response.setContext(ctx);
    }
}
```

---

## Configuration

Les constantes de session se trouvent dans `SessionManager.java` :

```java
private static final long TTL = 30 * 60 * 1000;          // 30 minutes
private static final long CLEAN_INTERVAL = 5 * 60 * 1000; // 5 minutes
```

Pour modifier la durée de vie des sessions, ajustez ces valeurs (en millisecondes).

---

## Dépannage

### Le token est rejeté alors qu'il semble valide

- Vérifiez que le header s'appelle exactement `Authorization`
- Vérifiez qu'il n'y a pas de préfixe (`Bearer`, etc.) - envoyez uniquement l'UUID
- Vérifiez que la session n'a pas expiré (30 min d'inactivité)

### Sessions multiples pour un même utilisateur

- Le système limite automatiquement à une session par utilisateur
- Une nouvelle connexion invalide l'ancienne session

### Les sessions ne se nettoient pas

- Vérifiez que le `ScheduledExecutorService` n'est pas arrêté
- Vérifiez les logs pour d'éventuelles exceptions dans la tâche de nettoyage

---

## DTOs (Data Transfer Objects)

### Qu'est-ce qu'un DTO ?

Un **DTO** (Data Transfer Object) est une classe utilisée pour **transférer des données** entre différentes couches de l'application, notamment entre le client et le serveur. Les DTOs permettent de :

✅ **Encapsuler** les données entrantes/sortantes  
✅ **Valider** et transformer les données  
✅ **Découpler** les modèles de base de données des formats d'API  
✅ **Standardiser** les échanges HTTP  

### Quand créer un nouveau DTO ?

#### 1. DTO de Requête (Request)

Créez un DTO de type **Request** quand vous devez **recevoir des données du client** :

**Situations typiques :**
- Formulaire de connexion (LoginRequest)
- Formulaire d'inscription (RegisterRequest)
- Création/modification d'une ressource (CreateDocumentRequest, UpdateDocumentRequest)
- Recherche avec filtres (SearchRequest)
- Toute requête complexe avec plusieurs paramètres

**Exemple - LoginRequest :**
```java
public class LoginRequest {
    private String mail;
    private String password;

    public LoginRequest(Context ctx) {
        // Récupération depuis le formulaire HTML
        this.mail = ctx.formParam("mail");
        this.password = ctx.formParam("password");
    }
    
    // Getters uniquement (immutable après construction)
    public String getMail() { return mail; }
    public String getPassword() { return password; }
}
```

**Quand créer un Request DTO :**
- ✅ Vous recevez plus de 2 paramètres
- ✅ Vous devez valider ou transformer les données
- ✅ Vous voulez réutiliser la même structure dans plusieurs endpoints
- ❌ Pour un simple ID ou un paramètre unique (utilisez directement `ctx.pathParam()`)

#### 2. DTO de Réponse (Response)

Créez un DTO de type **Response** pour **standardiser les réponses au client** :

**Situations typiques :**
- Retour d'erreur standardisé
- Retour de succès avec données
- Réponse avec métadonnées (pagination, etc.)

**Exemple - Response générique :**
```java
public class Response {
    public String token;
    public String status;
    public String message;
    public Integer code;
    
    // Constructeur avec token (pour le login)
    public Response(String token, String status, String message, Integer code) {
        this.token = token;
        this.status = status;
        this.message = message;
        this.code = code;
    }

    // Constructeur sans token (pour les autres cas)
    public Response(String status, String message, Integer code) {
        this.token = null;
        this.status = status;
        this.message = message;
        this.code = code;
    }

    // Méthode utilitaire pour envoyer la réponse
    public void setContext(Context ctx) {
        if (code >= 200 && code < 300) {
            ctx.status(code).json(Map.of("status", status, "message", message, "sID", token));
        } else {
            ctx.status(code).json(Map.of("status", status, "message", message));
        }
    }
}
```

**Quand créer un Response DTO :**
- ✅ Vous retournez toujours le même format (status, message, code)
- ✅ Vous voulez ajouter des métadonnées (pagination, timestamp, etc.)
- ✅ Vous devez sérialiser des objets complexes
- ❌ Pour retourner directement un objet du modèle (utilisez l'entité elle-même)

### Structure recommandée pour un DTO Request

```java
package org.pfa.models.dto;

import io.javalin.http.Context;

public class MonActionRequest {
    
    // Attributs privés
    private String param1;
    private Integer param2;
    
    // Constructeur qui extrait les données du Context
    public MonActionRequest(Context ctx) {
        // Depuis un formulaire HTML (POST form-encoded)
        this.param1 = ctx.formParam("param1");
        
        // Depuis un JSON (POST/PUT avec Content-Type: application/json)
        // this.param1 = ctx.bodyAsClass(MonActionRequest.class).param1;
        
        // Depuis l'URL (query params)
        // this.param1 = ctx.queryParam("param1");
        
        // Depuis le path (/resource/:id)
        // this.param2 = ctx.pathParamAsClass("id", Integer.class).get();
        
        // Conversion de type
        String param2Str = ctx.formParam("param2");
        this.param2 = param2Str != null ? Integer.parseInt(param2Str) : null;
    }
    
    // Getters uniquement (immutabilité recommandée)
    public String getParam1() { return param1; }
    public Integer getParam2() { return param2; }
    
    // Méthode de validation (optionnelle mais recommandée)
    public boolean isValid() {
        return param1 != null && !param1.isEmpty() && param2 != null && param2 > 0;
    }
}
```

### Exemples de DTOs à créer selon les cas d'usage

#### Cas 1 : Création de document

**Besoin :** L'utilisateur soumet un formulaire pour créer un document.

```java
public class CreateDocumentRequest {
    private String titre;
    private String description;
    private Integer categorieId;
    private MultipartFile file; // Pour l'upload de fichier

    public CreateDocumentRequest(Context ctx) {
        this.titre = ctx.formParam("titre");
        this.description = ctx.formParam("description");
        this.categorieId = Integer.parseInt(ctx.formParam("categorieId"));
        this.file = ctx.uploadedFile("file");
    }
    
    public boolean isValid() {
        return titre != null && !titre.isEmpty() 
            && categorieId != null 
            && file != null;
    }
    
    // Getters...
}
```

**Utilisation dans le contrôleur :**
```java
public void handleCreateDocument(Context ctx) {
    if (!super.checkToken(ctx)) return;
    
    CreateDocumentRequest request = new CreateDocumentRequest(ctx);
    
    if (!request.isValid()) {
        Response response = new Response("error", "Invalid request parameters", 400);
        response.setContext(ctx);
        return;
    }
    
    Document doc = documentService.create(request.getTitre(), 
                                          request.getDescription(), 
                                          request.getCategorieId());
    
    Response response = new Response("success", "Document created", 201);
    response.setContext(ctx);
}
```

#### Cas 2 : Recherche avec filtres

**Besoin :** Rechercher des documents avec plusieurs critères optionnels.

```java
public class SearchDocumentRequest {
    private String keywords;
    private Integer categorieId;
    private String dateDebut;
    private String dateFin;
    private Integer page;
    private Integer limit;

    public SearchDocumentRequest(Context ctx) {
        this.keywords = ctx.queryParam("keywords");
        this.categorieId = ctx.queryParamAsClass("categorieId", Integer.class).getOrDefault(null);
        this.dateDebut = ctx.queryParam("dateDebut");
        this.dateFin = ctx.queryParam("dateFin");
        this.page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);
        this.limit = ctx.queryParamAsClass("limit", Integer.class).getOrDefault(20);
    }
    
    // Getters...
}
```

**Endpoint :**
```
GET /documents/search?keywords=facture&categorieId=3&page=1&limit=20
```

#### Cas 3 : Réponse avec données paginées

**Besoin :** Retourner une liste de résultats avec métadonnées de pagination.

```java
public class PaginatedResponse<T> {
    private List<T> data;
    private int page;
    private int totalPages;
    private int totalItems;
    private String status;

    public PaginatedResponse(List<T> data, int page, int totalPages, int totalItems) {
        this.data = data;
        this.page = page;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.status = "success";
    }
    
    public void setContext(Context ctx) {
        ctx.status(200).json(this);
    }
}
```

**Utilisation :**
```java
public void handleListDocuments(Context ctx) {
    if (!super.checkToken(ctx)) return;
    
    SearchDocumentRequest request = new SearchDocumentRequest(ctx);
    List<Document> docs = documentService.search(request);
    int total = documentService.count(request);
    
    PaginatedResponse<Document> response = new PaginatedResponse<>(
        docs, 
        request.getPage(), 
        (int) Math.ceil(total / (double) request.getLimit()),
        total
    );
    
    response.setContext(ctx);
}
```

### Bonnes pratiques

#### ✅ À FAIRE

1. **Placer les DTOs dans `models/dto/`** - Séparez-les des entités JPA
2. **Nommer clairement** - Utilisez suffixes `Request`/`Response` (LoginRequest, UserResponse)
3. **Valider les données** - Ajoutez une méthode `isValid()` ou utilisez des annotations de validation
4. **Immutabilité** - Préférez les getters sans setters après construction
5. **Documentation** - Commentez les champs non-évidents
6. **Constructeur depuis Context** - Centralisez l'extraction des paramètres

```java
// ✅ BON
public class UpdateUserRequest {
    private Integer userId;
    private String newEmail;
    
    public UpdateUserRequest(Context ctx) {
        this.userId = ctx.pathParamAsClass("id", Integer.class).get();
        this.newEmail = ctx.formParam("email");
    }
    
    public boolean isValid() {
        return userId != null && newEmail != null && newEmail.matches("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$");
    }
    
    // Getters uniquement
}
```

#### ❌ À ÉVITER

1. **Ne pas mélanger DTOs et entités** - Un DTO ≠ un modèle JPA
2. **Ne pas surcharger les constructeurs** - Un seul constructeur avec Context
3. **Éviter la logique métier** - Les DTOs sont pour le transfert, pas pour la logique
4. **Ne pas exposer les entités directement** - Créez des DTOs pour masquer les détails internes

```java
// ❌ MAUVAIS - logique métier dans le DTO
public class LoginRequest {
    private String mail;
    private String password;
    
    public LoginRequest(Context ctx) { /* ... */ }
    
    // ❌ NE PAS FAIRE - logique métier ici !
    public User authenticate(UserRepository repo) {
        return repo.findByMailAndPassword(mail, password);
    }
}

// ✅ BON - DTO simple, logique dans le service
public class LoginRequest {
    private String mail;
    private String password;
    
    public LoginRequest(Context ctx) { /* ... */ }
    
    public String getMail() { return mail; }
    public String getPassword() { return password; }
}
```

### Cas spéciaux

#### DTO pour les headers (comme LogoutRequest)

Quand les données viennent des headers HTTP :

```java
public class LogoutRequest {
    private String token;

    public LogoutRequest(Context ctx) {
        // Extraction depuis le header Authorization
        this.token = ctx.header("Authorization");
    }
    
    public String getToken() { return token; }
}
```

#### DTO pour JSON complexe

Avec Javalin, pour parser du JSON automatiquement :

```java
public class CreateUserRequest {
    private String email;
    private String password;
    private Address address; // Objet imbriqué
    
    // Constructeur vide requis pour Jackson
    public CreateUserRequest() {}
    
    // Méthode factory depuis le Context
    public static CreateUserRequest fromContext(Context ctx) {
        return ctx.bodyAsClass(CreateUserRequest.class);
    }
    
    // Getters et setters (requis pour Jackson)
}
```

**Utilisation :**
```java
public void handleCreateUser(Context ctx) {
    CreateUserRequest request = CreateUserRequest.fromContext(ctx);
    // ...
}
```

### Résumé - Checklist pour créer un DTO

**Avant de créer un nouveau DTO, demandez-vous :**

- [ ] Est-ce que je reçois/envoie plus de 2 paramètres ?
- [ ] Est-ce que cette structure sera réutilisée ailleurs ?
- [ ] Est-ce que je dois valider ou transformer les données ?
- [ ] Est-ce que je veux découpler mon API des modèles internes ?

**Si OUI à au moins 2 questions → Créez un DTO !**

**Structure :**
1. Créer le fichier dans `models/dto/NomActionRequest.java` ou `NomActionResponse.java`
2. Déclarer les attributs privés
3. Créer un constructeur qui prend le `Context` (ou vide pour JSON)
4. Ajouter les getters (et setters si JSON)
5. Ajouter une méthode `isValid()` si pertinent
6. Documenter avec des commentaires

---

## Références du code

- [`SessionManager.java`](src/main/java/org/pfa/services/SessionManager.java) - Gestionnaire de sessions
- [`SessionUser.java`](src/main/java/org/pfa/models/SessionUser.java) - Modèle de session
- [`Controller.java`](src/main/java/org/pfa/controllers/Controller.java) - Classe de base avec `checkToken()`
- [`UserController.java`](src/main/java/org/pfa/controllers/UserController.java) - Login/Logout
- [`Response.java`](src/main/java/org/pfa/models/dto/Response.java) - Format de réponse standardisé
- [`LoginRequest.java`](src/main/java/org/pfa/models/dto/LoginRequest.java) - Exemple de DTO Request
- [`LogoutRequest.java`](src/main/java/org/pfa/models/dto/LogoutRequest.java) - Exemple de DTO avec header
