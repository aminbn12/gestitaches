package fr.usms.gestitaches.dao;

import fr.usms.gestitaches.model.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Accès aux données pour l'entité {@link Utilisateur}.
 */
public class UtilisateurDAO {

    /**
     * Retourne la liste de tous les membres de l'équipe triés par nom.
     *
     * @return liste des utilisateurs
     */
    public List<Utilisateur> listerTout() {
        String sql = "SELECT id, nom, email FROM utilisateur ORDER BY nom";
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(sql);
             ResultSet resultats = requete.executeQuery()) {
            while (resultats.next()) {
                utilisateurs.add(construireUtilisateur(resultats));
            }
            return utilisateurs;
        } catch (SQLException e) {
            throw new PersistanceException("Impossible de lister les membres.", e);
        }
    }

    /**
     * Recherche un membre par son identifiant.
     *
     * @param id identifiant du membre
     * @return l'utilisateur correspondant, ou {@code null} s'il n'existe pas
     */
    public Utilisateur trouverParId(int id) {
        String sql = "SELECT id, nom, email FROM utilisateur WHERE id = ?";
        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setInt(1, id);
            try (ResultSet resultats = requete.executeQuery()) {
                if (resultats.next()) {
                    return construireUtilisateur(resultats);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new PersistanceException("Impossible de rechercher le membre.", e);
        }
    }

    private Utilisateur construireUtilisateur(ResultSet resultats) throws SQLException {
        return new Utilisateur(
                resultats.getInt("id"),
                resultats.getString("nom"),
                resultats.getString("email")
        );
    }
}
