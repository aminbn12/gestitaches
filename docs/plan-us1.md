# Plan d'implémentation — User story 1 : Créer un projet

## Plan proposé par l'assistant

Objectif : implémenter uniquement la **User story 1** (*Créer un projet*) en respectant `spec.md`, `schema.sql` et `GEMINI.md` (Java 25, Swing, SQLite via JDBC avec `PreparedStatement`, une classe publique par fichier sous `fr.usms.gestitaches`).

1. **Modèle — `src/main/java/fr/usms/gestitaches/model/Projet.java`**
   - **Rôle** : représenter l'entité `projet` (`id`, `nom`, `description`).
   - **Détails** : constructeur(s), getters/setters en camelCase, `toString()` affichant le nom du projet.
   - **Points à vérifier** : `nom` et `description` correspondent bien aux colonnes de la table `projet` dans `schema.sql`.

2. **Accès aux données — `src/main/java/fr/usms/gestitaches/dao/ProjetDao.java`**
   - **Rôle** : gérer la persistance SQLite des projets via `DatabaseConnection.getConnection()`.
   - **Détails** :
     - `insert(Projet projet)` : requête `INSERT INTO projet (nom, description) VALUES (?, ?)` via `PreparedStatement` avec récupération de l'`id` généré.
     - `findAll()` : requête `SELECT id, nom, description FROM projet ORDER BY id` via `PreparedStatement` pour alimenter la liste des projets.
   - **Points à vérifier** : aucune concaténation SQL (uniquement `PreparedStatement`), fermeture propre des ressources (`try-with-resources`), gestion de `description` facultative (`null` ou vide).

3. **Logique métier — `src/main/java/fr/usms/gestitaches/service/ProjetService.java`**
   - **Rôle** : valider les règles métier de la US 1 avant d'appeler `ProjetDao`.
   - **Détails** :
     - `creerProjet(String nom, String description)` : vérifier que `nom != null && !nom.trim().isEmpty()` ; lever une `IllegalArgumentException` avec un message explicite dans le cas limite (nom vide ou absent).
     - `listerProjets()` : retourner la liste des projets via `ProjetDao.findAll()`.
   - **Points à vérifier** : le cas limite (nom vide ou composé uniquement d'espaces) est bien bloqué avant toute insertion en base.

4. **Interface graphique Swing — `src/main/java/fr/usms/gestitaches/ui/MainFrame.java`**
   - **Rôle** : fenêtre principale Swing permettant de créer un projet et d'afficher la liste des projets existants.
   - **Détails** :
     - Formulaire simple : champ texte pour le nom (obligatoire), zone texte pour la description (facultative), bouton « Créer le projet ».
     - Liste (`JList` ou `JTable`) affichant les projets enregistrés, mise à jour immédiatement après création.
     - Affichage d'une boîte de dialogue d'erreur (`JOptionPane.ERROR_MESSAGE`) si `ProjetService` rejette un nom vide.
   - **Points à vérifier** : aucun code SQL dans la couche UI, rafraîchissement immédiat de la liste après validation.

5. **Point d'entrée — `src/main/java/fr/usms/gestitaches/App.java`**
   - **Rôle** : lancer `MainFrame` sur l'EDT Swing (`SwingUtilities.invokeLater(...)`) après vérification de la connexion à la base.
   - **Points à vérifier** : le projet compile et s'exécute avec `mvn -q compile exec:java`, et aucun élément des US 2 à 5 (tâches, membres, statuts, filtres) n'est ajouté prématurément.

---

## Vérification du plan

| Question | Constat |
| :--- | :--- |
| Le plan respecte-t-il la pile technique et les conventions du projet ? | Oui : Java 25, Swing, SQLite via JDBC (`PreparedStatement`), packages `model`, `dao`, `service`, `ui` sous `fr.usms.gestitaches`, une classe publique par fichier. |
| Couvre-t-il chaque critère d'acceptation de la user story 1, cas limite compris ? | Oui : création avec nom obligatoire + description facultative, affichage immédiat dans la liste, et refus avec message d'erreur si le nom est vide. |
| Ajoute-t-il des éléments que personne n'a demandés ? | Non : il se limite strictement à la création et à l'affichage des projets (aucune gestion de tâches, utilisateurs ou mots de passe). |
| L'ordre de réalisation proposé est-il logique ? | Oui : modèle → DAO → service → interface Swing → point d'entrée `App`. |
| Les fichiers et packages proposés sont-ils cohérents avec vos instructions ? | Oui : tous les packages et noms de classes suivent `GEMINI.md`. |

---

## Mon analyse

Le plan proposé est cohérent et respecte strictement les règles définies dans `GEMINI.md` : la séparation en quatre couches (`model`, `dao`, `service`, `ui`) est claire, les requêtes passent exclusivement par `PreparedStatement` et aucune fonctionnalité hors périmètre (comme les tâches ou l'authentification) n'a été ajoutée. L'ordre de réalisation (du modèle vers l'interface graphique) permet de tester chaque couche proprement. Avant d'autoriser la génération de code en Séance 2, je demanderais simplement de vérifier que la table `projet` existe bien au démarrage (ou que `gestitaches.db` a déjà été initialisée avec `schema.sql`), afin d'éviter une erreur SQL au premier lancement sur un poste vierge.
