package fr.usms.gestitaches.service;

import fr.usms.gestitaches.dao.ProjetDAO;
import fr.usms.gestitaches.model.Projet;

import java.util.List;

/**
 * Service métier gérant les opérations sur les projets.
 */
public class ProjetService {

    private final ProjetDAO projetDAO;

    public ProjetService() {
        this(new ProjetDAO());
    }

    public ProjetService(ProjetDAO projetDAO) {
        this.projetDAO = projetDAO;
    }

    /**
     * Crée un projet après validation du nom obligatoire.
     *
     * @param nom nom du projet (obligatoire, non vide)
     * @param description description facultative du projet
     * @return le projet créé avec son identifiant
     */
    public Projet creerProjet(String nom, String description) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du projet est obligatoire.");
        }
        String descriptionNormalisee = (description == null || description.trim().isEmpty())
                ? null
                : description.trim();
        Projet projet = new Projet(nom.trim(), descriptionNormalisee);
        projetDAO.creer(projet);
        return projet;
    }

    /**
     * Retourne l'ensemble des projets existants.
     *
     * @return liste des projets
     */
    public List<Projet> listerProjets() {
        return projetDAO.listerTout();
    }
}
