-- GestiTaches - schema de la base de donnees

CREATE TABLE utilisateur (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  nom TEXT NOT NULL,
  email TEXT NOT NULL UNIQUE
);

CREATE TABLE projet (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  nom TEXT NOT NULL,
  description TEXT
);

CREATE TABLE tache (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  titre TEXT NOT NULL,
  description TEXT,
  statut TEXT NOT NULL DEFAULT 'à faire' CHECK (statut IN ('à faire', 'en cours', 'terminée')),
  echeance TEXT,
  projet_id INTEGER NOT NULL REFERENCES projet(id),
  utilisateur_id INTEGER REFERENCES utilisateur(id)
);
