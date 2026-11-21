package fr.usms.gestitaches.ui;

import fr.usms.gestitaches.dao.PersistanceException;
import fr.usms.gestitaches.model.Projet;
import fr.usms.gestitaches.service.ProjetService;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

/**
 * Fenêtre principale de l'application GestiTaches (User Story 1 : gestion des projets).
 */
public class MainFrame extends JFrame {

    private final ProjetService projetService;
    private final DefaultListModel<Projet> modeleListeProjets;
    private final JTextField champNomProjet;
    private final JTextArea champDescriptionProjet;

    public MainFrame(ProjetService projetService) {
        super("GestiTaches");
        this.projetService = projetService;
        this.modeleListeProjets = new DefaultListModel<>();
        this.champNomProjet = new JTextField(25);
        this.champDescriptionProjet = new JTextArea(3, 25);

        initialiserInterface();
        chargerProjets();
    }

    private void initialiserInterface() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panneauFormulaire = new JPanel(new GridBagLayout());
        panneauFormulaire.setBorder(BorderFactory.createTitledBorder("Nouveau projet"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panneauFormulaire.add(new JLabel("Nom * :"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panneauFormulaire.add(champNomProjet, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        panneauFormulaire.add(new JLabel("Description :"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        champDescriptionProjet.setLineWrap(true);
        champDescriptionProjet.setWrapStyleWord(true);
        panneauFormulaire.add(new JScrollPane(champDescriptionProjet), gbc);

        JButton boutonCreer = new JButton("Créer le projet");
        boutonCreer.addActionListener(e -> actionCreerProjet());
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panneauFormulaire.add(boutonCreer, gbc);

        JList<Projet> listeProjets = new JList<>(modeleListeProjets);
        JScrollPane scrollListe = new JScrollPane(listeProjets);
        scrollListe.setBorder(BorderFactory.createTitledBorder("Projets existants"));

        add(panneauFormulaire, BorderLayout.NORTH);
        add(scrollListe, BorderLayout.CENTER);
    }

    private void actionCreerProjet() {
        try {
            projetService.creerProjet(champNomProjet.getText(), champDescriptionProjet.getText());
            champNomProjet.setText("");
            champDescriptionProjet.setText("");
            chargerProjets();
        } catch (IllegalArgumentException | PersistanceException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void chargerProjets() {
        try {
            modeleListeProjets.clear();
            List<Projet> projets = projetService.listerProjets();
            for (Projet projet : projets) {
                modeleListeProjets.addElement(projet);
            }
        } catch (PersistanceException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erreur de base de données",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
