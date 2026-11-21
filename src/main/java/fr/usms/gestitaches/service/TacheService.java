package fr.usms.gestitaches.service;

import fr.usms.gestitaches.dao.TacheDAO;
import fr.usms.gestitaches.dao.UtilisateurDAO;
import fr.usms.gestitaches.model.Tache;
import fr.usms.gestitaches.model.Utilisateur;

import java.util.List;
import java.util.Set;

/**
 * Service métier gérant les opérations sur les tâches.
 */
public class TacheService {

    private static final String STATUT_INITIAL = "A_FAIRE";
    private static final Set<String> STATUTS_AUTORISES = Set.of("A_FAIRE", "EN_COURS", "TERMINEE");

    private final TacheDAO tacheDAO;
    private final UtilisateurDAO utilisateurDAO;

    public TacheService() {
        this(new TacheDAO(), new UtilisateurDAO());
    }

    public TacheService(TacheDAO tacheDAO, UtilisateurDAO utilisateurDAO) {
        this.tacheDAO = tacheDAO;
        this.utilisateurDAO = utilisateurDAO;
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

    /**
     * Retourne la liste des membres de l'équipe.
     *
     * @return liste des utilisateurs
     */
    public List<Utilisateur> listerMembres() {
        return utilisateurDAO.listerTout();
    }

    /**
     * Attribue une tâche à un unique membre de l'équipe (ou la rend non assignée si {@code null}).
     *
     * @param tacheId identifiant de la tâche
     * @param utilisateurId identifiant du membre, ou {@code null}
     */
    public void assignerTache(int tacheId, Integer utilisateurId) {
        if (tacheId <= 0 || tacheDAO.trouverParId(tacheId) == null) {
            throw new IllegalArgumentException("La tâche sélectionnée n'existe pas.");
        }
        if (utilisateurId != null && utilisateurDAO.trouverParId(utilisateurId) == null) {
            throw new IllegalArgumentException("Le membre sélectionné n'existe pas.");
        }
        tacheDAO.assignerUtilisateur(tacheId, utilisateurId);
    }

    /**
     * Modifie le statut d'une tâche parmi {@code A_FAIRE}, {@code EN_COURS} et {@code TERMINEE}.
     *
     * @param tacheId identifiant de la tâche
     * @param nouveauStatut nouveau statut souhaité
     */
    public void changerStatut(int tacheId, String nouveauStatut) {
        if (tacheId <= 0 || tacheDAO.trouverParId(tacheId) == null) {
            throw new IllegalArgumentException("La tâche sélectionnée n'existe pas.");
        }
        if (nouveauStatut == null || !STATUTS_AUTORISES.contains(nouveauStatut)) {
            throw new IllegalArgumentException("Statut invalide : valeurs autorisées A_FAIRE, EN_COURS, TERMINEE.");
        }
        tacheDAO.changerStatut(tacheId, nouveauStatut);
    }

    /**
     * Retourne les tâches d'un projet filtrées par statut et/ou par membre attribué.
     *
     * @param projetId identifiant du projet
     * @param statut statut souhaité ou {@code null} pour tous
     * @param utilisateurId identifiant du membre ou {@code null} pour tous
     * @return liste des tâches filtrées
     */
    public List<Tache> filtrerTachesDuProjet(int projetId, String statut, Integer utilisateurId) {
        if (projetId <= 0) {
            throw new IllegalArgumentException("Veuillez sélectionner un projet valide.");
        }
        if (statut != null && !STATUTS_AUTORISES.contains(statut)) {
            throw new IllegalArgumentException("Statut de filtre invalide : valeurs autorisées A_FAIRE, EN_COURS, TERMINEE.");
        }
        if (utilisateurId != null && utilisateurDAO.trouverParId(utilisateurId) == null) {
            throw new IllegalArgumentException("Le membre de filtre sélectionné n'existe pas.");
        }
        return tacheDAO.listerParProjetEtFiltres(projetId, statut, utilisateurId);
    }

    private String normaliserChampFacultatif(String valeur) {
        if (valeur == null || valeur.trim().isEmpty()) {
            return null;
        }
        return valeur.trim();
    }
}
