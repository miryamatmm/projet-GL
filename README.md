# 🐦 Y Microblogging — Projet Java (MIF01 - Génie Logiciel)

> Application de microblogging en Java inspirée de Twitter, développée dans le cadre de l’UE Génie Logiciel (décembre 2024).

## ✨ Présentation

**Y Microblogging** permet à des utilisateurs de publier, supprimer, trier, bookmarker et consulter des messages. L'application garantit la **persistance des données**, le **tri personnalisé** des messages, et repose sur les principes de **programmation orientée objet** : encapsulation, héritage, composition.

Les messages sont enrichis par un **système de scoring** et de **tri dynamique**.

---

## ⚙️ Technologies

- **Langage** : Java 
- **Interface graphique** : JavaFX
- **Build tool** : Maven
- **Tests** : JUnit 5, Hamcrest, TestFX

---

## 🧠 Architecture

L’application est structurée selon le pattern **MVC (Modèle-Vue-Contrôleur)** avec un **modèle passif** :

### 📦 Modèle (Model)
- Gestion des utilisateurs et messages
- Système de scoring (bonus/malus)
- Stratégies de tri
- Persistance des données (import/export)

### 🖥 Vue (View)
- Interface utilisateur JavaFX
- Affichage des messages et interactions
- Déléguée au contrôleur

### 🧩 Contrôleur (Controller)
- Communication entre Vue et Modèle
- Gestion des événements utilisateurs
- Sélection d’utilisateur, publication, tri

---

## 🧱 Design Patterns utilisés

### 🔁 MVC
Séparation claire entre logique métier, affichage et gestion des interactions.

### 🛠 Creator
- Création des utilisateurs et messages via la classe `Y`
- Création des vues via le contrôleur

### 🎯 Strategy
- `ScoringStrategy` : permet d’appliquer plusieurs règles de score (pertinence, date, mots interdits…)
- `SortingStrategy` : tri par pertinence ou date

---

## 🧪 Tests

Des tests **manuels** et **automatisés** ont été réalisés :

### ✅ Vue
- Affichage, publication, bookmark, suppression
- Mise à jour dynamique
- Déconnexion

### ✅ Contrôleur
- Sélection d’utilisateur
- Actualisation de la vue
- Application des filtres de visibilité
- Navigation entre vues

---

## 🔐 Éthique & algorithmes

Une réflexion a été menée sur l’usage éthique des systèmes de recommandation :

- Mise en place d’un **filtrage basique de contenus interdits**
- Système de scoring contrôlé, sans exploitation abusive des données
- Sensibilisation aux **bulles de filtres** et à leurs impacts

📚 *Voir sources dans le rapport, notamment Amnesty International & Le Monde sur TikTok.*

---

## 🧠 Leçons tirées

- Maîtrise de l’architecture MVC
- Refactorisation propre d’un squelette existant
- Mise en œuvre de design patterns
- Tests robustes avec JUnit et TestFX
- Prise en compte des enjeux éthiques dans le design logiciel

---

## 👩‍💻 Auteurs

- **Atamna Miryam**
- **El Ouarrad Mariam**

Université Claude Bernard Lyon 1 — MIF01 — Décembre 2024

---

## 📁 Structure du projet

```
├── src/
│   ├── model/
│   ├── view/
│   ├── controller/
│   └── strategy/
├── resources/
├── test/
│   ├── model/
│   └── controller/
└── README.md
```

---

## 🏁 Lancer le projet

```bash
# Compiler le projet
mvn compile

# Lancer les tests
mvn test

# Nettoyer les fichiers générés
mvn clean

# Compiler + tester + packager (build complet)
mvn clean install

# Exécuter l’application
mvn compile
mvn exec:java
