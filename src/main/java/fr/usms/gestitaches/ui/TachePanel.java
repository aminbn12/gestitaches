package fr.usms.gestitaches.ui;

import fr.usms.gestitaches.dao.PersistanceException;
import fr.usms.gestitaches.model.Projet;
import fr.usms.gestitaches.model.Tache;
import fr.usms.gestitaches.service.TacheService;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
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
 * Panneau Swing dédié à la création et à l'affichage des tâches d'un projet.
 */
public class TachePanel extends JPanel {

    private final TacheService tacheService;
    private final DefaultListModel<Tache> modeleListeTaches;
    private final JTextField champTitre;
    private final JTextArea champDescription;
    private final JTextField champEcheance;
    private final JLabel etiquetteProjetSelectionne;
    private Projet projetSelectionne;

    public TachePanel(TacheService tacheService) {
        this.tacheService = tacheService;
        this.modeleListeTaches = new DefaultListModel<>();
        this.champTitre = new JTextField(20);
        this.champDescription = new JTextArea(2, 20);
        this.champEcheance = new JTextField(12);
        this.etiquetteProjetSelectionne = new JLabel("Aucun projet sélectionné");

        initialiserInterface();
    }

    private void initialiserInterface() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createTitledBorder("Tâches du projet"));

        JPanel panneauFormulaire = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panneauFormulaire.add(etiquetteProjetSelectionne, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panneauFormulaire.add(new JLabel("Titre * :"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panneauFormulaire.add(champTitre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        panneauFormulaire.add(new JLabel("Description :"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        champDescription.setLineWrap(true);
        champDescription.setWrapStyleWord(true);
        panneauFormulaire.add(new JScrollPane(champDescription), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        panneauFormulaire.add(new JLabel("Échéance :"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panneauFormulaire.add(champEcheance, gbc);

        JButton boutonAjouter = new JButton("Ajouter la tâche");
        boutonAjouter.addActionListener(e -> actionAjouterTache());
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panneauFormulaire.add(boutonAjouter, gbc);

        JList<Tache> listeTaches = new JList<>(modeleListeTaches);
        JScrollPane scrollListe = new JScrollPane(listeTaches);

        add(panneauFormulaire, BorderLayout.NORTH);
        add(scrollListe, BorderLayout.CENTER);
    }

    /**
     * Met à jour le projet actif et recharge ses tâches.
     *
     * @param projet le projet sélectionné (ou {@code null})
     */
    public void definirProjetSelectionne(Projet projet) {
        this.projetSelectionne = projet;
        if (projet == null) {
            etiquetteProjetSelectionne.setText("Aucun projet sélectionné");
            modeleListeTaches.clear();
            return;
        }
        etiquetteProjetSelectionne.setText("Projet : " + projet.getNom());
        chargerTaches();
    }

    private void actionAjouterTache() {
        if (projetSelectionne == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Veuillez d'abord sélectionner un projet.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        try {
            tacheService.creerTache(
                    projetSelectionne.getId(),
                    champTitre.getText(),
                    champDescription.getText(),
                    champEcheance.getText()
            );
            champTitre.setText("");
            champDescription.setText("");
            champEcheance.setText("");
            chargerTaches();
        } catch (IllegalArgumentException | PersistanceException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void chargerTaches() {
        if (projetSelectionne == null) {
            modeleListeTaches.clear();
            return;
        }
        try {
            modeleListeTaches.clear();
            List<Tache> taches = tacheService.listerTachesDuProjet(projetSelectionne.getId());
            for (Tache tache : taches) {
                modeleListeTaches.addElement(tache);
            }
        } catch (IllegalArgumentException | PersistanceException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
