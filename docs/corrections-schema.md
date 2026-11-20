# Corrections du schéma de base de données (`schema.sql`)

## 1. Validation croisée avec les user stories

| User story | Ce que le schéma doit permettre | Ce qui le garantit dans `schema.sql` (corrigé) |
| :--- | :--- | :--- |
| 1. Créer un projet | Enregistrer un projet avec un nom obligatoire et une description facultative | Table `projet` avec `nom TEXT NOT NULL` et `description TEXT` (nullable) |
| 2. Créer une tâche dans un projet | Enregistrer une tâche rattachée obligatoirement à un projet, avec un titre obligatoire, une description et une échéance facultatives, un statut initial « à faire » et sans membre assigné au départ | Table `tache` avec `titre TEXT NOT NULL`, `description TEXT`, `echeance TEXT` (nullable), `statut TEXT NOT NULL DEFAULT 'à faire'`, `projet_id INTEGER NOT NULL REFERENCES projet(id)`, et `utilisateur_id INTEGER REFERENCES utilisateur(id)` (nullable) |
| 3. Assigner une tâche à un membre | Associer une tâche à un seul membre connu par son nom et son e-mail unique | Clé étrangère `utilisateur_id INTEGER REFERENCES utilisateur(id)` dans `tache` et table `utilisateur` avec `nom TEXT NOT NULL` et `email TEXT NOT NULL UNIQUE` |
| 4. Changer le statut d'une tâche | Modifier le statut d'une tâche uniquement parmi les trois valeurs autorisées (« à faire », « en cours », « terminée ») | Colonne `statut TEXT NOT NULL DEFAULT 'à faire' CHECK (statut IN ('à faire', 'en cours', 'terminée'))` dans `tache` |
| 5. Filtrer par statut ou par membre | Rechercher et filtrer les tâches d'un projet par `projet_id`, `statut` ou `utilisateur_id` | Présence des colonnes `projet_id`, `statut` et `utilisateur_id` dans la table `tache` |

## 2. Les six erreurs identifiées et corrigées

| # | Table et colonne | Erreur constatée | Règle ou user story concernée | Correction |
| :-: | :--- | :--- | :--- | :--- |
| 1 | `tache.projet_id` | Colonne manquante (aucun lien entre une tâche et son projet) | « Une tâche appartient toujours à un projet, et à un seul » (US 2, US 5) | Ajout de `projet_id INTEGER NOT NULL REFERENCES projet(id)` |
| 2 | `utilisateur.email` | Contrainte manquante (`UNIQUE` absent) | « Deux personnes ne peuvent pas avoir la même adresse » (US 3) | Ajout de `UNIQUE` : `email TEXT NOT NULL UNIQUE` |
| 3 | `projet.nom` | Contrainte manquante (`NOT NULL` absent) | « un projet a toujours un nom » (US 1) | Ajout de `NOT NULL` : `nom TEXT NOT NULL` |
| 4 | `tache.statut` | Contrainte manquante (aucune restriction sur les valeurs autorisées ni valeur par défaut) | « elle est "à faire" quand on la crée, puis "en cours", puis "terminée". Rien d'autre » (US 2, US 4) | Ajout de `DEFAULT 'à faire' CHECK (statut IN ('à faire', 'en cours', 'terminée'))` |
| 5 | `tache.echeance` | Contrainte en trop (`NOT NULL` présent alors que la date est facultative) | « une date d'échéance, mais ce n'est pas obligatoire » (US 2) | Suppression de `NOT NULL` : `echeance TEXT` |
| 6 | `tache.utilisateur_id` | Contrainte en trop (`NOT NULL` présent alors qu'une tâche peut être créée sans être attribuée) | « Au moment où on crée une tâche, on ne sait pas toujours qui va s'en occuper » (US 2, US 3) | Suppression de `NOT NULL` : `utilisateur_id INTEGER REFERENCES utilisateur(id)` |
