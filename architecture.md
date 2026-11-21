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

- US1 : <à compléter>
- US2 : <à compléter>
- US3 : <à compléter>
- US4 : <à compléter>
- US5 : <à compléter>
