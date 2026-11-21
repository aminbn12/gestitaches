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
                    int idUtilisateur = resultats.getInt("utilisateur_id");
                    Integer utilisateurId = resultats.wasNull() ? null : idUtilisateur;
                    taches.add(new Tache(
                            resultats.getInt("id"),
                            resultats.getString("titre"),
                            resultats.getString("description"),
                            resultats.getString("statut"),
                            resultats.getString("echeance"),
                            resultats.getInt("projet_id"),
                            utilisateurId
                    ));
                }
            }
            return taches;
        } catch (SQLException e) {
            throw new PersistanceException("Impossible de lister les tâches du projet.", e);
        }
    }
}
