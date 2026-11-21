package fr.usms.gestitaches.dao;

import fr.usms.gestitaches.model.Projet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Accès aux données pour l'entité {@link Projet}.
 */
public class ProjetDAO {

    /**
     * Insère un nouveau projet en base et renseigne son identifiant généré.
     *
     * @param projet le projet à enregistrer
     */
    public void creer(Projet projet) {
        String sql = "INSERT INTO projet (nom, description) VALUES (?, ?)";
        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            requete.setString(1, projet.getNom());
            requete.setString(2, projet.getDescription());
            requete.executeUpdate();
            try (ResultSet cles = requete.getGeneratedKeys()) {
                if (cles.next()) {
                    projet.setId(cles.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new PersistanceException("Impossible de créer le projet en base.", e);
        }
    }

    /**
     * Retourne la liste de tous les projets enregistrés, triés par identifiant.
     *
     * @return la liste des projets
     */
    public List<Projet> listerTout() {
        String sql = "SELECT id, nom, description FROM projet ORDER BY id";
        List<Projet> projets = new ArrayList<>();
        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(sql);
             ResultSet resultats = requete.executeQuery()) {
            while (resultats.next()) {
                projets.add(new Projet(
                        resultats.getInt("id"),
                        resultats.getString("nom"),
                        resultats.getString("description")
                ));
            }
            return projets;
        } catch (SQLException e) {
            throw new PersistanceException("Impossible de lister les projets.", e);
        }
    }
}
