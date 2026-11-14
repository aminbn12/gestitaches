# GestiTaches — spécification

## User story 1 — Créer un projet

En tant que cheffe de projet, je veux créer un projet avec un nom et une description facultative afin d'organiser le travail de l'équipe.

Critères d'acceptation :

- Étant donné que je suis sur l'écran de gestion des projets
  Quand je saisis un nom de projet non vide (avec ou sans description) et que je valide
  Alors le projet est créé et apparaît dans la liste des projets.

Cas limite : si le nom du projet est vide ou absent, la création est refusée et un message d'erreur est affiché.

## User story 2 — Créer une tâche dans un projet

En tant que membre de l'équipe, je veux ajouter une tâche dans un projet existant avec un titre obligatoire, une description et une date d'échéance facultatives afin de recenser le travail à réaliser.

Critères d'acceptation :

- Étant donné qu'un projet existe et est sélectionné
  Quand je saisis un titre de tâche non vide (avec ou sans description ni date d'échéance) et que je valide
  Alors la tâche est créée dans ce projet avec le statut initial « à faire », sans membre attribué par défaut.

Cas limite : si le titre est vide ou si aucun projet n'est sélectionné, la création est refusée et un message d'erreur est affiché.

## User story 3 — Assigner une tâche à un membre

En tant que cheffe de projet, je veux attribuer une tâche à un unique membre de l'équipe afin de clarifier qui en est responsable.

Critères d'acceptation :

- Étant donné une tâche existante dans un projet et une liste de membres connus
  Quand je sélectionne un membre pour cette tâche et que je valide
  Alors la tâche est attribuée uniquement à ce membre et son nom s'affiche sur la tâche.

Cas limite : si le membre sélectionné n'existe pas ou si l'on tente d'attribuer plusieurs membres à une même tâche, l'opération est refusée.

## User story 4 — Changer le statut d'une tâche

En tant que membre de l'équipe à qui une tâche est attribuée, je veux modifier le statut de cette tâche parmi « à faire », « en cours » et « terminée » afin de refléter son avancement.

Critères d'acceptation :

- Étant donné une tâche qui m'est attribuée
  Quand je change son statut vers « à faire », « en cours » ou « terminée »
  Alors le nouveau statut est enregistré et affiché immédiatement.

Cas limite : si le statut demandé ne fait pas partie des trois valeurs autorisées, ou si la tâche n'est attribuée à aucun membre / est modifiée par un autre membre, le changement est refusé.

## User story 5 — Filtrer les tâches par statut ou par membre

En tant que cheffe de projet, je veux filtrer les tâches d'un projet par statut ou par membre attribué afin de suivre facilement l'avancement du projet.

Critères d'acceptation :

- Étant donné un projet contenant plusieurs tâches avec différents statuts et membres attribués
  Quand je sélectionne un filtre sur un statut ou sur un membre
  Alors seules les tâches de ce projet correspondant au critère choisi sont affichées.

Cas limite : si aucune tâche ne correspond au filtre sélectionné, une liste vide s'affiche avec un message indiquant qu'aucune tâche ne correspond.

## Contraintes techniques

- Langage : Java 25
- Interface graphique : Swing
- Persistance : SQLite via JDBC
- Build : Maven
- Conventions de nommage : packages sous `fr.usms.gestitaches`, PascalCase pour les classes, camelCase pour les méthodes et variables
