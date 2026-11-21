# Journal d'atelier — GestiTaches (TP2)

## User story 1 — Créer un projet

- **Scénarios d'acceptation (Étape 2)** :

| Scénario | Étant donné / Quand | Alors (résultat attendu) |
| :--- | :--- | :--- |
| Cas nominal (avec description) | Fenêtre ouverte ; saisie d'un nom non vide et d'une description, clic sur « Créer » | Le projet apparaît dans la liste et est présent dans la table `projet` |
| Cas nominal (sans description) | Fenêtre ouverte ; saisie d'un nom non vide sans description, clic sur « Créer » | Le projet est créé et enregistré sans description |
| Cas limite : nom vide | Fenêtre ouverte ; nom vide ou espaces uniquement, clic sur « Créer » | Création refusée, message d'erreur affiché, aucune ligne ajoutée en base |

- **Verdict sur le plan (Étape 3)** : Plan conforme à `architecture.md`, `spec.md`, `schema.sql` et `GEMINI.md`. Différence par rapport à `docs/plan-us1.md` (TP1 sans `architecture.md`) : ajout explicite de `dao/PersistanceException.java` pour traduire `SQLException`, et séparation stricte des imports (`java.sql` interdit hors `dao`, `javax.swing` interdit hors `ui`).
- **Fichiers autorisés (Étape 4)** : `model/Projet.java`, `dao/PersistanceException.java`, `dao/ProjetDAO.java`, `service/ProjetService.java`, `ui/MainFrame.java`, `App.java`.
- **Analyse du diff (`--stat`) (Étape 6)** : 6 fichiers Java dans le périmètre autorisé + `docs/journal-atelier.md`. Couches respectées (`java.sql` uniquement dans `dao`, `javax.swing` uniquement dans `ui`), `PreparedStatement`, `try-with-resources`, `PersistanceException` sur `SQLException`, aucune fonctionnalité en trop.
- **Décision / consigne de correction (Étape 7)** : Proposition acceptée sans correction.
- **Résultats des contrôles (Étape 8)** : `mvn -q compile` OK ; `mvn -q compile exec:java` ouvre la fenêtre Swing ; création avec nom + description OK ; création sans description OK ; nom vide bloqué avec message d'erreur ; enregistrement vérifié en base.
- **Commit (Étape 9)** : `US1 : créer un projet` (à renseigner après `git log --oneline -1`)

## User story 2 — Créer une tâche dans un projet

- **Scénarios d'acceptation (Étape 2)** :

| Scénario | Étant donné / Quand | Alors (résultat attendu) |
| :--- | :--- | :--- |
| Cas nominal (complet) | Un projet est sélectionné ; saisie d'un titre non vide, description et échéance, clic sur « Ajouter la tâche » | La tâche apparaît dans la liste du projet avec le statut `A_FAIRE`, et est enregistrée en base (`projet_id` renseigné, `utilisateur_id` NULL) |
| Cas nominal (minimal) | Un projet est sélectionné ; saisie d'un titre seul sans description ni échéance, clic sur « Ajouter la tâche » | La tâche est créée avec `statut = 'A_FAIRE'`, description et échéance NULL |
| Cas limite : titre vide | Un projet est sélectionné ; titre vide ou espaces uniquement, clic sur « Ajouter la tâche » | Création refusée, message d'erreur affiché, aucune ligne ajoutée en base |
| Cas limite : aucun projet sélectionné | Aucun projet sélectionné dans la liste, clic sur « Ajouter la tâche » | Création refusée, message d'erreur affiché |

- **Verdict sur le plan (Étape 3)** : Plan validé (couches `model`, `dao`, `service`, `ui` respectées, US2 uniquement).
- **Fichiers autorisés (Étape 4)** : `model/Tache.java`, `dao/TacheDAO.java`, `service/TacheService.java`, `ui/TachePanel.java`, `ui/MainFrame.java`, `App.java`.
- **Analyse du diff (`--stat`) (Étape 6)** : 6 fichiers Java dans le périmètre autorisé + `docs/journal-atelier.md`. Couches respectées (`java.sql` uniquement dans `dao`, `javax.swing` uniquement dans `ui`), `PreparedStatement`, `try-with-resources`, `PersistanceException`, aucune fonctionnalité en trop.
- **Décision / consigne de correction (Étape 7)** : Proposition acceptée sans correction.
- **Résultats des contrôles (Étape 8)** : `mvn -q compile` OK ; tests manuels en attente de validation.
- **Commit (Étape 9)** : à compléter après tests et commit

## User story 3 — Assigner une tâche à un membre

## User story 4 — Changer le statut d'une tâche

## User story 5 — Filtrer les tâches par statut ou par membre
