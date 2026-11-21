package fr.usms.gestitaches.ui;

import fr.usms.gestitaches.dao.PersistanceException;
import fr.usms.gestitaches.model.Projet;
import fr.usms.gestitaches.model.Tache;
import fr.usms.gestitaches.model.Utilisateur;
import fr.usms.gestitaches.service.TacheService;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panneau Swing dédié à la création, à l'affichage et à l'attribution des tâches d'un projet.
 */
public class TachePanel extends JPanel {

    private final TacheService tacheService;
    private final DefaultListModel<Tache> modeleListeTaches;
    private final JList<Tache> listeTaches;
    private final JTextField champTitre;
    private final JTextArea champDescription;
    private final JTextField champEcheance;
    private final JLabel etiquetteProjetSelectionne;
    private final JComboBox<Utilisateur> comboMembres;
    private final JComboBox<String> comboStatuts;
    private final Map<Integer, String> nomsMembresParId;
    private Projet projetSelectionne;

    public TachePanel(TacheService tacheService) {
        this.tacheService = tacheService;
        this.modeleListeTaches = new DefaultListModel<>();
        this.listeTaches = new JList<>(modeleListeTaches);
        this.champTitre = new JTextField(20);
        this.champDescription = new JTextArea(2, 20);
        this.champEcheance = new JTextField(12);
        this.etiquetteProjetSelectionne = new JLabel("Aucun projet sélectionné");
        this.comboMembres = new JComboBox<>();
        this.comboStatuts = new JComboBox<>(new String[]{"A_FAIRE", "EN_COURS", "TERMINEE"});
        this.nomsMembresParId = new HashMap<>();

        initialiserInterface();
        chargerMembres();
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

        listeTaches.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listeTaches.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Tache tache) {
                    String nomMembre = (tache.getUtilisateurId() == null)
                            ? "Non assignée"
                            : nomsMembresParId.getOrDefault(tache.getUtilisateurId(), "Membre #" + tache.getUtilisateurId());
                    setText(tache + " — " + nomMembre);
                }
                return this;
            }
        });
        JScrollPane scrollListe = new JScrollPane(listeTaches);

        JPanel panneauActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        panneauActions.setBorder(BorderFactory.createTitledBorder("Actions sur la tâche sélectionnée"));
        panneauActions.add(new JLabel("Membre :"));
        panneauActions.add(comboMembres);
        JButton boutonAssigner = new JButton("Assigner");
        boutonAssigner.addActionListener(e -> actionAssignerTache());
        panneauActions.add(boutonAssigner);

        panneauActions.add(new JLabel("Statut :"));
        panneauActions.add(comboStatuts);
        JButton boutonStatut = new JButton("Changer statut");
        boutonStatut.addActionListener(e -> actionChangerStatut());
        panneauActions.add(boutonStatut);

        add(panneauFormulaire, BorderLayout.NORTH);
        add(scrollListe, BorderLayout.CENTER);
        add(panneauActions, BorderLayout.SOUTH);
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

    private void chargerMembres() {
        try {
            comboMembres.removeAllItems();
            nomsMembresParId.clear();
            List<Utilisateur> membres = tacheService.listerMembres();
            for (Utilisateur membre : membres) {
                comboMembres.addItem(membre);
                nomsMembresParId.put(membre.getId(), membre.getNom());
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

    private void actionAssignerTache() {
        Tache tacheSelectionnee = listeTaches.getSelectedValue();
        if (tacheSelectionnee == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Veuillez sélectionner une tâche à assigner.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        Utilisateur membreSelectionne = (Utilisateur) comboMembres.getSelectedItem();
        if (membreSelectionne == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Aucun membre disponible à assigner.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        try {
            tacheService.assignerTache(tacheSelectionnee.getId(), membreSelectionne.getId());
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

    private void actionChangerStatut() {
        Tache tacheSelectionnee = listeTaches.getSelectedValue();
        if (tacheSelectionnee == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Veuillez sélectionner une tâche pour changer son statut.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        String nouveauStatut = (String) comboStatuts.getSelectedItem();
        try {
            tacheService.changerStatut(tacheSelectionnee.getId(), nouveauStatut);
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
