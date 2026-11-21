package fr.usms.gestitaches.model;

/**
 * Représente un projet dans GestiTaches.
 */
public class Projet {

    private int id;
    private String nom;
    private String description;

    public Projet(int id, String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;
    }

    public Projet(String nom, String description) {
        this(0, nom, description);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        if (description == null || description.isBlank()) {
            return nom;
        }
        return nom + " — " + description;
    }
}
