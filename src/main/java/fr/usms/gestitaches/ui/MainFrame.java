package fr.usms.gestitaches.ui;

import fr.usms.gestitaches.dao.PersistanceException;
import fr.usms.gestitaches.model.Projet;
import fr.usms.gestitaches.service.ProjetService;
import fr.usms.gestitaches.service.TacheService;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

/**
 * Fenêtre principale de l'application GestiTaches.
 */
public class MainFrame extends JFrame {

    private final ProjetService projetService;
    private final TachePanel tachePanel;
    private final DefaultListModel<Projet> modeleListeProjets;
    private final JList<Projet> listeProjets;
    private final JTextField champNomProjet;
    private final JTextArea champDescriptionProjet;

    public MainFrame(ProjetService projetService, TacheService tacheService) {
        super("GestiTaches");
        this.projetService = projetService;
        this.tachePanel = new TachePanel(tacheService);
        this.modeleListeProjets = new DefaultListModel<>();
        this.listeProjets = new JList<>(modeleListeProjets);
        this.champNomProjet = new JTextField(20);
        this.champDescriptionProjet = new JTextArea(3, 20);

        initialiserInterface();
        chargerProjets();
    }

    private void initialiserInterface() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panneauProjets = new JPanel(new BorderLayout(8, 8));

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

        listeProjets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listeProjets.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                tachePanel.definirProjetSelectionne(listeProjets.getSelectedValue());
            }
        });
        JScrollPane scrollListe = new JScrollPane(listeProjets);
        scrollListe.setBorder(BorderFactory.createTitledBorder("Projets existants"));

        panneauProjets.add(panneauFormulaire, BorderLayout.NORTH);
        panneauProjets.add(scrollListe, BorderLayout.CENTER);

        JSplitPane separateur = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panneauProjets, tachePanel);
        separateur.setResizeWeight(0.45);
        add(separateur, BorderLayout.CENTER);
    }

    private void actionCreerProjet() {
        try {
            Projet cree = projetService.creerProjet(champNomProjet.getText(), champDescriptionProjet.getText());
            champNomProjet.setText("");
            champDescriptionProjet.setText("");
            chargerProjets();
            selectionnerProjet(cree.getId());
        } catch (IllegalArgumentException | PersistanceException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void selectionnerProjet(int projetId) {
        for (int i = 0; i < modeleListeProjets.size(); i++) {
            if (modeleListeProjets.get(i).getId() == projetId) {
                listeProjets.setSelectedIndex(i);
                return;
            }
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
