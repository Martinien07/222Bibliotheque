package ui;

import dao.LivreDAO;
import model.Livre;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class LivresInterface extends JPanel {
    private JTextField txtTitre, txtAuteur;
    private JCheckBox chkDisponible;
    private JButton btnSave, btnClear, btnQuitter;
    private JTable table;
    private DefaultTableModel model;
    private LivreDAO livreDAO;
    private int idEnCoursDeModification = -1;

    public LivresInterface() {
        livreDAO = new LivreDAO();
        
        // Configuration du panel principal
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Création du titre
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JLabel title = new JLabel("📚 Gestion des Livres");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);

        // 2. Panel du formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15), 
                "Formulaire Livre"));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Champ texte pour le titre
        txtTitre = new JTextField();
        txtTitre.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTitre.setPreferredSize(new Dimension(300, 30));

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Titre:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtTitre, gbc);

        // Champ texte pour l'auteur
        txtAuteur = new JTextField();
        txtAuteur.setFont(new Font("Arial", Font.PLAIN, 14));
        txtAuteur.setPreferredSize(new Dimension(300, 30));

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Auteur:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtAuteur, gbc);

        // Checkbox pour disponibilité
        chkDisponible = new JCheckBox("Disponible");
        chkDisponible.setFont(new Font("Arial", Font.PLAIN, 14));
        chkDisponible.setBackground(Color.WHITE);

        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(chkDisponible, gbc);

        // Boutons
        btnSave = new JButton("Enregistrer");
        btnClear = new JButton("Effacer");
        btnQuitter = new JButton("Quitter");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(240, 240, 240));

        for (JButton btn : new JButton[]{btnSave, btnClear, btnQuitter}) {
            btn.setFont(new Font("Arial", Font.PLAIN, 14));
            btn.setPreferredSize(new Dimension(120, 35));
            buttonPanel.add(btn);
        }

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Configuration du tableau
        model = new DefaultTableModel(
            new String[]{"ID", "Titre", "Auteur", "Disponible", "Actions"}, 
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Seule la colonne Actions est éditable
            }
        };

        table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row)) {
                    c.setBackground(new Color(220, 240, 255));
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                }
                return c;
            }
        };
        
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 5));
        
        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des livres"));
        scrollPane.setPreferredSize(new Dimension(800, 300));

        // Organisation finale
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.setBackground(Color.WHITE);

        add(centerPanel, BorderLayout.CENTER);

        // Listeners
        btnSave.addActionListener(e -> enregistrerLivre());
        btnClear.addActionListener(e -> viderChamps());
        btnQuitter.addActionListener(e -> retourMenu());

        rafraichirTable();
    }

    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton btnEdit, btnDelete;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            setOpaque(true);
            
            btnEdit = new JButton("Modifier");
            btnDelete = new JButton("Supprimer");
            
            btnEdit.setFont(new Font("Arial", Font.PLAIN, 12));
            btnDelete.setFont(new Font("Arial", Font.PLAIN, 12));
            btnEdit.setPreferredSize(new Dimension(80, 25));
            btnDelete.setPreferredSize(new Dimension(80, 25));
            
            add(btnEdit);
            add(btnDelete);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(new Color(220, 240, 255));
            } else {
                setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
            }
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton btnEdit, btnDelete;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(true);
            
            btnEdit = new JButton("Modifier");
            btnDelete = new JButton("Supprimer");
            
            btnEdit.setFont(new Font("Arial", Font.PLAIN, 12));
            btnDelete.setFont(new Font("Arial", Font.PLAIN, 12));
            btnEdit.setPreferredSize(new Dimension(80, 25));
            btnDelete.setPreferredSize(new Dimension(80, 25));
            
            btnEdit.addActionListener(e -> {
                fireEditingStopped();
                modifierLivre(currentRow);
            });
            
            btnDelete.addActionListener(e -> {
                fireEditingStopped();
                supprimerLivre(currentRow);
            });
            
            panel.add(btnEdit);
            panel.add(btnDelete);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, 
                boolean isSelected, int row, int column) {
            currentRow = table.convertRowIndexToModel(row);
            panel.setBackground(isSelected ? new Color(220, 240, 255) : Color.WHITE);
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    private void modifierLivre(int row) {
        int id = (int) model.getValueAt(row, 0);
        Livre livre = livreDAO.getLivreParId(id);
        
        if (livre != null) {
            idEnCoursDeModification = id;
            txtTitre.setText(livre.getTitre());
            txtAuteur.setText(livre.getAuteur());
            chkDisponible.setSelected(livre.isDisponible());
            btnSave.setText("Mettre à jour");
        }
    }

    private void supprimerLivre(int row) {
        int id = (int) model.getValueAt(row, 0);
        
        int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Êtes-vous sûr de vouloir supprimer ce livre ?", 
                "Confirmation de suppression", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            livreDAO.supprimerLivre(id);
            rafraichirTable();
            if (idEnCoursDeModification == id) {
                viderChamps();
                idEnCoursDeModification = -1;
            }
        }
    }

    private void enregistrerLivre() {
        String titre = txtTitre.getText().trim();
        String auteur = txtAuteur.getText().trim();
        boolean disponible = chkDisponible.isSelected();

        if (titre.isEmpty() || auteur.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez remplir tous les champs obligatoires.",
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (idEnCoursDeModification != -1) {
            // Mise à jour d'un livre existant
            Livre livre = new Livre(idEnCoursDeModification, titre, auteur, disponible);
            livreDAO.modifierLivre(livre);
            idEnCoursDeModification = -1;
            btnSave.setText("Enregistrer");
        } else {
            // Ajout d'un nouveau livre
            Livre livre = new Livre(titre, auteur, disponible);
            livreDAO.ajouterLivre(livre);
        }
        
        rafraichirTable();
        viderChamps();
    }

    private void rafraichirTable() {
        model.setRowCount(0);
        List<Livre> livres = livreDAO.getTousLesLivres();
        for (Livre livre : livres) {
            model.addRow(new Object[]{
                    livre.getId(),
                    livre.getTitre(),
                    livre.getAuteur(),
                    livre.isDisponible() ? "Oui" : "Non",
                    "" // Colonne Actions - laissée vide car les boutons sont rendus
            });
        }
    }

    private void viderChamps() {
        txtTitre.setText("");
        txtAuteur.setText("");
        chkDisponible.setSelected(false);
        btnSave.setText("Enregistrer");
        idEnCoursDeModification = -1;
    }

    private void retourMenu() {
        ((CardLayout) getParent().getLayout()).show(getParent(), "Accueil");
    }

    public void actualiserLivres() {
        // 1. Rafraîchir la liste des livres dans le modèle de table
        rafraichirTable();
     // Ajouter toutes les actualisations

        
        // 3. Optionnel: message de confirmation
       //JOptionPane.showMessageDialog(this, "Liste des livres actualisée", 
       //    "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
}