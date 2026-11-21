package fr.usms.gestitaches;

import fr.usms.gestitaches.dao.DatabaseConnection;
import fr.usms.gestitaches.service.ProjetService;
import fr.usms.gestitaches.ui.MainFrame;

import javax.swing.SwingUtilities;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Point d'entrée de l'application GestiTaches.
 */
public class App {

    public static void main(String[] args) {
        System.out.println("GestiTaches - démarrage");
        try (Connection connexion = DatabaseConnection.getConnection()) {
            System.out.println("Connexion a la base gestitaches.db : OK");
        } catch (SQLException e) {
            System.out.println("Connexion a la base gestitaches.db : ECHEC");
            System.out.println(e.getMessage());
            return;
        }

        SwingUtilities.invokeLater(() -> {
            ProjetService projetService = new ProjetService();
            MainFrame fenetre = new MainFrame(projetService);
            fenetre.setVisible(true);
        });
    }
}
