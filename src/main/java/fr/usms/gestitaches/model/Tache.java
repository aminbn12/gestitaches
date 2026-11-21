package fr.usms.gestitaches.model;

/**
 * Représente une tâche rattachée à un projet dans GestiTaches.
 */
public class Tache {

    private int id;
    private String titre;
    private String description;
    private String statut;
    private String echeance;
    private int projetId;
    private Integer utilisateurId;

    public Tache(int id, String titre, String description, String statut, String echeance, int projetId, Integer utilisateurId) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.statut = statut;
        this.echeance = echeance;
        this.projetId = projetId;
        this.utilisateurId = utilisateurId;
    }

    public Tache(String titre, String description, String statut, String echeance, int projetId, Integer utilisateurId) {
        this(0, titre, description, statut, echeance, projetId, utilisateurId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getEcheance() {
        return echeance;
    }

    public void setEcheance(String echeance) {
        this.echeance = echeance;
    }

    public int getProjetId() {
        return projetId;
    }

    public void setProjetId(int projetId) {
        this.projetId = projetId;
    }

    public Integer getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Integer utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    @Override
    public String toString() {
        StringBuilder libelle = new StringBuilder("[").append(statut).append("] ").append(titre);
        if (echeance != null && !echeance.isBlank()) {
            libelle.append(" (échéance : ").append(echeance).append(")");
        }
        return libelle.toString();
    }
}
