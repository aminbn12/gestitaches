package fr.usms.gestitaches.service;

import fr.usms.gestitaches.dao.TacheDAO;
import fr.usms.gestitaches.model.Tache;

import java.util.List;

/**
 * Service métier gérant les opérations sur les tâches.
 */
public class TacheService {

    private static final String STATUT_INITIAL = "A_FAIRE";

    private final TacheDAO tacheDAO;

    public TacheService() {
        this(new TacheDAO());
    }

    public TacheService(TacheDAO tacheDAO) {
        this.tacheDAO = tacheDAO;
    }

    /**
     * Crée une tâche dans un projet existant avec le statut initial {@code A_FAIRE}.
     *
     * @param projetId identifiant du projet (obligatoire, > 0)
     * @param titre titre de la tâche (obligatoire, non vide)
     * @param description description facultative
     * @param echeance date d'échéance facultative
     * @return la tâche créée avec son identifiant
     */
    public Tache creerTache(int projetId, String titre, String description, String echeance) {
        if (projetId <= 0) {
            throw new IllegalArgumentException("Veuillez sélectionner un projet valide.");
        }
        if (titre == null || titre.trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre de la tâche est obligatoire.");
        }
        String descriptionNormalisee = normaliserChampFacultatif(description);
        String echeanceNormalisee = normaliserChampFacultatif(echeance);
        Tache tache = new Tache(titre.trim(), descriptionNormalisee, STATUT_INITIAL, echeanceNormalisee, projetId, null);
        tacheDAO.creer(tache);
        return tache;
    }

    /**
     * Retourne toutes les tâches d'un projet.
     *
     * @param projetId identifiant du projet
     * @return liste des tâches du projet
     */
    public List<Tache> listerTachesDuProjet(int projetId) {
        if (projetId <= 0) {
            throw new IllegalArgumentException("Veuillez sélectionner un projet valide.");
        }
        return tacheDAO.listerParProjet(projetId);
    }

    private String normaliserChampFacultatif(String valeur) {
        if (valeur == null || valeur.trim().isEmpty()) {
            return null;
        }
        return valeur.trim();
    }
}
