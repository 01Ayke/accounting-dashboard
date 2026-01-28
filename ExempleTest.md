# Guide de test – Javalin + Hibernate + MySQL

Ce document explique **comment démarrer le serveur Java** et **tester l’API avec Postman** pour le projet `org.pfa`.

---

## 1️⃣ Prérequis

### Logiciels nécessaires

- **Java JDK 17**
- **Maven** (gestion des dépendances et lancement du serveur)
- **Docker & Docker Compose** (pour la base de données MySQL)
- **Postman** (ou `curl`) pour tester l’API

> ℹ️ Maven est fortement recommandé : sans lui, il faudrait gérer manuellement tous les JARs
> (Javalin, Hibernate, MySQL, etc.).

---

## 2️⃣ Compiler le projet Java

Depuis la racine du projet :

```bash
mvn clean compile
```

Résultat attendu :
- pas d’erreurs
- dépendances téléchargées automatiquement

---

## 3️⃣ Lancer le serveur Javalin

Commande **exacte à utiliser** :

```bash
mvn exec:java -Dexec.mainClass="org.pfa.App"
```

Résultat attendu dans la console :

```
Serveur démarré sur http://localhost:7070
Hibernate ORM core version 7.2.2.Final
```

---

## 4️⃣ Tests Postman – API Users

### Base URL complète

```
http://localhost:7070
```

Toutes les requêtes utilisent :

```
Content-Type: application/json
```

---

### 4.1 GET – Liste des utilisateurs

- **Méthode** : GET
- **URL complète** :

```
http://localhost:7070/users
```

Résultat attendu :
```json
[
  { "id": 1, "name": "Alice" },
  { "id": 2, "name": "Bob" }
]
```

---

### 4.2 GET – Utilisateur par ID

- **Méthode** : GET
- **URL complète** :

```
http://localhost:7070/users/1
```

Résultat attendu :
```json
{ "id": 1, "name": "Alice" }
```

---

### 4.3 POST – Créer un utilisateur

- **Méthode** : POST
- **URL complète** :

```
http://localhost:7070/users
```

- **Body (raw JSON)** :

```json
{ "name": "Charlie" }
```

Résultat attendu :
- Status **201**

```json
{ "id": 3, "name": "Charlie" }
```

---

### 4.4 PUT – Mettre à jour un utilisateur

⚠️ **UPDATE = PUT, pas POST**

- **Méthode** : PUT
- **URL complète** :

```
http://localhost:7070/users/1
```

- **Body (raw JSON)** :

```json
{ "name": "Alice Updated" }
```

Résultat attendu :
```json
{ "id": 1, "name": "Alice Updated" }
```

---

### 4.5 DELETE – Supprimer un utilisateur

- **Méthode** : DELETE
- **URL complète** :

```
http://localhost:7070/users/1
```

Résultat attendu :
- Status **204 No Content**

---

## 5️⃣ Ordre de test recommandé

1. GET `http://localhost:7070/users`
2. POST `http://localhost:7070/users`
3. GET `http://localhost:7070/users`
4. PUT `http://localhost:7070/users/{id}`
5. DELETE `http://localhost:7070/users/{id}`
6. GET `http://localhost:7070/users`

---

## 6️⃣ Résumé rapide

```bash
# Lancer / reset MySQL
./launch.sh

# Compiler
mvn clean compile

# Lancer Javalin
mvn exec:java -Dexec.mainClass="org.pfa.App"
```

Tout est maintenant prêt pour les tests Postman et la soutenance 🎉
