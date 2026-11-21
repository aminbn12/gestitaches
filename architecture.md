# Architecture de GestiTaches

## Couches : ui -> service -> dao -> base

- `ui` : Swing uniquement. Ni SQL, ni règle métier.
- `service` : règles métier. Appelle les DAO, ignore Swing.
- `dao` : seul endroit du SQL. Une classe par entité.
- `model` : classes simples, utilisées par les 3 couches.

## Règles de persistance

- Requêtes paramétrées (`PreparedStatement`) uniquement.
- Ressources JDBC ouvertes dans un `try-with-resources`.
- `SQLException` traduite en `PersistanceException`.

## Règles métier, par user story

| User story | Règle métier principale | Couche et classe |
| :--- | :--- | :--- |
| 1. Créer un projet | Nom du projet obligatoire (non vide), description facultative | `service` (`ProjetService`) |
| 2. Créer une tâche dans un projet | Titre obligatoire, projet existant obligatoire, statut initial `A_FAIRE`, échéance facultative | `service` (`TacheService`) |
| 3. Assigner une tâche à un membre | Une tâche est assignée à un seul membre existant (ou reste non assignée) | `service` (`TacheService`) et `dao` (`UtilisateurDAO`, `TacheDAO`) |
| 4. Changer le statut d'une tâche | Statut restreint aux valeurs `A_FAIRE`, `EN_COURS`, `TERMINEE` | `service` (`TacheService`) |
| 5. Filtrer les tâches par statut ou par membre | Filtrage des tâches d'un projet par statut ou par membre via requête SQL paramétrée | `dao` (`TacheDAO`), appelé par `service` (`TacheService`) |
