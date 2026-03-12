# Documentation des Tests

Ce document récapitule les tests automatisés mis en place pour le projet et explique leur rôle pour faciliter la collaboration.

## 🛠️ Configuration Globale
Les tests utilisent **JUnit 5** et **Javalin** en mode test.
- Le serveur Javalin est démarré sur un port aléatoire (`start(0)`) avant chaque série de tests pour éviter les conflits.
- Les requêtes HTTP sont envoyées via le client natif `java.net.http.HttpClient`.

---

## 📂 Fichiers de Tests

### 1. [UserAPITest.java](src/test/java/org/pfa/controllers/UserAPITest.java)
Ce fichier teste tous les points d'entrée (endpoints) liés aux utilisateurs et à l'authentification.

#### Scénarios testés :
- **`testLoginSuccess`** : Vérifie qu'un administrateur avec des identifiants valides reçoit un code 200 et un jeton de session (`sID`).
- **`testLoginFailure`** : Vérifie qu'une tentative avec de mauvais identifiants renvoie une erreur 401.
- **`testLogoutSuccess`** : Vérifie qu'un utilisateur connecté peut invalider sa session.
- **`testRegisterSuccess`** : Vérifie qu'un administrateur peut créer un nouveau compte.
- **`testRegisterUnauthorized`** : Vérifie qu'une tentative d'inscription sans jeton de session est bloquée (401).

#### Scénario Fonctionnel Complet :
- **`testFunctionalRegistrationFlow`** : Simule un flux réel en plusieurs étapes :
    1. Connexion en admin.
    2. Création d'un nouvel utilisateur par l'admin.
    3. Connexion de ce nouvel utilisateur.
    4. **Vérification de sécurité** : On vérifie que ce nouvel utilisateur *ne peut pas* créer de compte (renvoie 403 Forbidden).
    5. Déconnexion de l'utilisateur.

### 2. [SIGTest.java](src/test/java/org/pfa/models/SIGTest.java)
Ce fichier valide les calculs comptables des **Soldes Intermédiaires de Gestion (SIG)**. Ce sont des **tests unitaires** qui vérifient la logique métier pure sans démarrer de serveur.

#### Calculs vérifiés :
- **Marge Commerciale** : Vérifie la différence entre les ventes et les achats de marchandises.
- **Production de l'Exercice** : Somme de la production vendue, stockée et immobilisée.
- **Valeur Ajoutée (VA)** : Mesure la richesse créée par l'entreprise.
- **Excédent Brut d'Exploitation (EBE)** : VA moins les charges de personnel et les impôts/taxes.
- **Résultats** : Vérifie les différents paliers de résultats (Exploitation, Financier, Exceptionnel et Résultat Net de l'Exercice).

---

## 🚀 Comment lancer les tests ?

Pour exécuter tous les tests depuis la racine du projet, utilisez Maven :

```bash
mvn test
```

Pour lancer uniquement les tests utilisateur :
```bash
mvn test -Dtest=UserAPITest
```

Pour lancer uniquement les tests SIG :
```bash
mvn test -Dtest=SIGTest
```

---