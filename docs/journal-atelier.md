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
- **Résultats des contrôles (Étape 8)** : `mvn -q compile` OK ; tests manuels validés (création avec/sans description et échéance, refus si titre vide ou sans projet, vérification en base).
- **Commit (Étape 9)** : `US2 : créer une tâche dans un projet`

## User story 3 — Assigner une tâche à un membre

- **Scénarios d'acceptation (Étape 2)** :

| Scénario | Étant donné / Quand | Alors (résultat attendu) |
| :--- | :--- | :--- |
| Cas nominal (assignation) | Une tâche existe et la liste des membres (`donnees-test.sql`) est chargée ; je sélectionne la tâche, choisis un membre et clique sur « Assigner » | La tâche est mise à jour en base (`utilisateur_id` renseigné) et le nom du membre s'affiche sur la tâche |
| Cas nominal (réassignation) | Une tâche déjà assignée à un membre ; je choisis un autre membre et clique sur « Assigner » | La tâche passe uniquement au nouveau membre |
| Cas limite : membre inexistant ou aucune tâche sélectionnée | Aucune tâche sélectionnée (ou membre inexistant en service) | L'opération est refusée et un message d'erreur s'affiche |

- **Verdict sur le plan (Étape 3)** : Plan validé (couches `model`, `dao`, `service`, `ui` respectées, US3 uniquement).
- **Fichiers autorisés (Étape 4)** : `model/Utilisateur.java`, `dao/UtilisateurDAO.java`, `dao/TacheDAO.java`, `service/TacheService.java`, `ui/TachePanel.java`.
- **Analyse du diff (`--stat`) (Étape 6)** : 5 fichiers Java dans le périmètre autorisé + `docs/journal-atelier.md`. Couches respectées (`java.sql` uniquement dans `dao`, `javax.swing` uniquement dans `ui`), `PreparedStatement`, `try-with-resources`, `PersistanceException`, aucune fonctionnalité en trop.
- **Décision / consigne de correction (Étape 7)** : Proposition acceptée sans correction.
- **Résultats des contrôles (Étape 8)** : `mvn -q compile` OK ; tests manuels validés (assignation à un membre, réassignation à un autre membre, refus si aucune tâche sélectionnée, vérification SQL avec `LEFT JOIN`).
- **Commit (Étape 9)** : `US3 : assigner une tâche à un membre`

## User story 4 — Changer le statut d'une tâche

- **Scénarios d'acceptation (Étape 2)** :

| Scénario | Étant donné / Quand | Alors (résultat attendu) |
| :--- | :--- | :--- |
| Cas nominal | Une tâche existe ; je sélectionne la tâche, choisis un nouveau statut (`EN_COURS` ou `TERMINEE` ou `A_FAIRE`) et clique sur « Changer statut » | Le nouveau statut s'affiche immédiatement sur la tâche et est enregistré en base |
| Cas limite : statut invalide | Appel au service avec un statut hors `{A_FAIRE, EN_COURS, TERMINEE}` (ou `null`/vide) | Le changement est refusé (`IllegalArgumentException`), aucune modification en base |
| Cas limite : aucune tâche sélectionnée | Aucune tâche sélectionnée dans la liste, clic sur « Changer statut » | Un message d'erreur s'affiche |

- **Verdict sur le plan (Étape 3)** : Plan validé (couches `dao`, `service`, `ui` respectées, US4 uniquement).
- **Fichiers autorisés (Étape 4)** : `dao/TacheDAO.java`, `service/TacheService.java`, `ui/TachePanel.java`.
- **Analyse du diff (`--stat`) (Étape 6)** : 3 fichiers Java dans le périmètre autorisé + `docs/journal-atelier.md`. Couches respectées (`java.sql` uniquement dans `dao`, `javax.swing` uniquement dans `ui`), `PreparedStatement`, `try-with-resources`, `PersistanceException`, statuts limités à `A_FAIRE`, `EN_COURS`, `TERMINEE`.
- **Décision / consigne de correction (Étape 7)** : Proposition acceptée sans correction.
- **Résultats des contrôles (Étape 8)** : `mvn -q compile` OK ; tests manuels validés (changement vers `EN_COURS` / `TERMINEE`, refus si aucune tâche sélectionnée, vérification en base).
- **Commit (Étape 9)** : `US4 : changer le statut d'une tâche`

## User story 5 — Filtrer les tâches par statut ou par membre

- **Scénarios d'acceptation (Étape 2)** :

| Scénario | Étant donné / Quand | Alors (résultat attendu) |
| :--- | :--- | :--- |
| Cas nominal (filtre par statut) | Un projet contient plusieurs tâches avec différents statuts ; je sélectionne un statut (ex. `EN_COURS`) et applique le filtre | Seules les tâches de ce projet ayant ce statut s'affichent (via requête SQL paramétrée) |
| Cas nominal (filtre par membre) | Un projet contient des tâches assignées à différents membres ; je sélectionne un membre et applique le filtre | Seules les tâches de ce projet assignées à ce membre s'affichent |
| Cas nominal (toutes les tâches) | Filtres sur « Tous » | Toutes les tâches du projet s'affichent |
| Cas limite : aucun résultat | Un filtre ne correspond à aucune tâche du projet | La liste affichée est vide et un message informatif indique qu'aucune tâche ne correspond |

- **Verdict sur le plan (Étape 3)** : Plan validé (filtrage SQL paramétré dans `TacheDAO`, pas de tri côté UI).
- **Fichiers autorisés (Étape 4)** : `dao/TacheDAO.java`, `service/TacheService.java`, `ui/TachePanel.java`.
- **Analyse du diff (`--stat`) (Étape 6)** : 3 fichiers Java dans le périmètre autorisé + `docs/journal-atelier.md`. Couches respectées (aucun `java.sql` hors `dao`, aucun `javax.swing` hors `ui`), `PreparedStatement`, `try-with-resources`, `PersistanceException`.
- **Décision / consigne de correction (Étape 7)** : Proposition acceptée sans correction.
- **Résultats des contrôles (Étape 8)** : `mvn -q compile` OK ; tests manuels en attente de validation.
- **Commit (Étape 9)** : `US5 : filtrer les tâches par statut ou par membre` (à committer après tests)
