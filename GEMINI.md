# GestiTaches — instructions du projet

## Le projet en deux lignes

Application desktop de gestion de projets et de tâches pour une petite équipe de huit personnes.
Elle permet de créer des projets, d'y ajouter des tâches, de les assigner, de suivre leur statut et de les filtrer.

## Pile technique

- Langage : Java 25 (Eclipse Temurin)
- Interface graphique : Swing
- Base de données : SQLite via JDBC (`gestitaches.db`)
- Outil de build : Maven (`pom.xml`)

## Conventions

- Packages sous `fr.usms.gestitaches` (`model`, `dao`, `ui`, `service`)
- Une seule classe publique par fichier ; PascalCase pour les classes, camelCase pour les méthodes et variables

## Règles à toujours respecter

- Utiliser exclusivement des requêtes SQL paramétrées (`PreparedStatement`), jamais de concaténation de chaînes
- Ne stocker aucun mot de passe, jeton ou clé secrète en dur dans le code source
- Ressources JDBC ouvertes dans un `try-with-resources`.
- Une `SQLException` est traduite en `PersistanceException`, jamais avalée (ni catch vide, ni `printStackTrace` seul).

## Façon de travailler

- Toujours proposer un plan détaillé et attendre validation avant de créer ou modifier des fichiers
- Implémenter une seule user story à la fois, en respectant strictement `spec.md` et `schema.sql`
- Ne modifier que les fichiers listés dans ma demande. Si un autre fichier est nécessaire, s'arrêter et demander.

## Documents de référence

- `spec.md` : user stories et critères d'acceptation
- `schema.sql` : schéma de la base de données
- `architecture.md` : couches et dépendances autorisées
