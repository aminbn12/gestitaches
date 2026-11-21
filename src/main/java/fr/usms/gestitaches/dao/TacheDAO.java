package fr.usms.gestitaches.dao;

import fr.usms.gestitaches.model.Tache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Accès aux données pour l'entité {@link Tache}.
 */
public class TacheDAO {

    /**
     * Insère une nouvelle tâche en base et renseigne son identifiant généré.
     *
     * @param tache la tâche à enregistrer
     */
    public void creer(Tache tache) {
        String sql = "INSERT INTO tache (titre, description, statut, echeance, projet_id, utilisateur_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            requete.setString(1, tache.getTitre());
            requete.setString(2, tache.getDescription());
            requete.setString(3, tache.getStatut());
            requete.setString(4, tache.getEcheance());
            requete.setInt(5, tache.getProjetId());
            if (tache.getUtilisateurId() == null) {
                requete.setNull(6, Types.INTEGER);
            } else {
                requete.setInt(6, tache.getUtilisateurId());
            }
            requete.executeUpdate();
            try (ResultSet cles = requete.getGeneratedKeys()) {
                if (cles.next()) {
                    tache.setId(cles.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new PersistanceException("Impossible de créer la tâche en base.", e);
        }
    }

    /**
     * Retourne toutes les tâches d'un projet donné, triées par identifiant.
     *
     * @param projetId identifiant du projet
     * @return liste des tâches du projet
     */
    public List<Tache> listerParProjet(int projetId) {
        String sql = "SELECT id, titre, description, statut, echeance, projet_id, utilisateur_id FROM tache WHERE projet_id = ? ORDER BY id";
        List<Tache> taches = new ArrayList<>();
        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setInt(1, projetId);
            try (ResultSet resultats = requete.executeQuery()) {
                while (resultats.next()) {
                    taches.add(construireTache(resultats));
                }
            }
            return taches;
        } catch (SQLException e) {
            throw new PersistanceException("Impossible de lister les tâches du projet.", e);
        }
    }

    /**
     * Recherche une tâche par son identifiant.
     *
     * @param tacheId identifiant de la tâche
     * @return la tâche correspondante, ou {@code null} si introuvable
     */
    public Tache trouverParId(int tacheId) {
        String sql = "SELECT id, titre, description, statut, echeance, projet_id, utilisateur_id FROM tache WHERE id = ?";
        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setInt(1, tacheId);
            try (ResultSet resultats = requete.executeQuery()) {
                if (resultats.next()) {
                    return construireTache(resultats);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new PersistanceException("Impossible de rechercher la tâche.", e);
        }
    }

    /**
     * Met à jour le membre attribué à une tâche (ou retire l'attribution si {@code null}).
     *
     * @param tacheId identifiant de la tâche
     * @param utilisateurId identifiant du membre, ou {@code null}
     */
    public void assignerUtilisateur(int tacheId, Integer utilisateurId) {
        String sql = "UPDATE tache SET utilisateur_id = ? WHERE id = ?";
        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(sql)) {
            if (utilisateurId == null) {
                requete.setNull(1, Types.INTEGER);
            } else {
                requete.setInt(1, utilisateurId);
            }
            requete.setInt(2, tacheId);
            requete.executeUpdate();
        } catch (SQLException e) {
            throw new PersistanceException("Impossible d'assigner la tâche.", e);
        }
    }

    /**
     * Met à jour le statut d'une tâche.
     *
     * @param tacheId identifiant de la tâche
     * @param statut nouveau statut ({@code A_FAIRE}, {@code EN_COURS} ou {@code TERMINEE})
     */
    public void changerStatut(int tacheId, String statut) {
        String sql = "UPDATE tache SET statut = ? WHERE id = ?";
        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setString(1, statut);
            requete.setInt(2, tacheId);
            requete.executeUpdate();
        } catch (SQLException e) {
            throw new PersistanceException("Impossible de modifier le statut de la tâche.", e);
        }
    }

    private Tache construireTache(ResultSet resultats) throws SQLException {
        int idUtilisateur = resultats.getInt("utilisateur_id");
        Integer utilisateurId = resultats.wasNull() ? null : idUtilisateur;
        return new Tache(
                resultats.getInt("id"),
                resultats.getString("titre"),
                resultats.getString("description"),
                resultats.getString("statut"),
                resultats.getString("echeance"),
                resultats.getInt("projet_id"),
                utilisateurId
        );
    }
}
