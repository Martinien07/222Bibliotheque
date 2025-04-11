package ui;

import dao.UtilisateurDAO;
import model.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class UtilisateursInterface extends JPanel {
    private JTextField txtNomUtilisateur;
    private JPasswordField txtMotDePasse;
    private JCheckBox showPassword;
    private JComboBox<String> comboRole;
    private JButton btnSave, btnClear, btnQuitter;
    private JTable table;
    private DefaultTableModel model;
    private UtilisateurDAO utilisateurDAO;
    private int idEnCoursDeModification = -1;

    public UtilisateursInterface() {
        utilisateurDAO = new UtilisateurDAO();
        
        // Configuration du panel principal
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Création du titre
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        
        JLabel title = new JLabel("👤 Gestion des Utilisateurs");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);

        // 2. Panel du formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15), 
                "Formulaire Utilisateur"));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Champ texte pour le nom d'utilisateur
        txtNomUtilisateur = new JTextField();
        txtNomUtilisateur.setFont(new Font("Arial", Font.PLAIN, 14));
        txtNomUtilisateur.setPreferredSize(new Dimension(300, 30));

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nom d'utilisateur:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNomUtilisateur, gbc);

        // Champ mot de passe
        txtMotDePasse = new JPasswordField();
        txtMotDePasse.setFont(new Font("Arial", Font.PLAIN, 14));
        txtMotDePasse.setPreferredSize(new Dimension(300, 30));

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Mot de passe:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtMotDePasse, gbc);

        // Checkbox afficher mot de passe
        showPassword = new JCheckBox("Afficher le mot de passe");
        showPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        showPassword.setBackground(Color.WHITE);
        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                txtMotDePasse.setEchoChar((char) 0);
            } else {
                txtMotDePasse.setEchoChar('•');
            }
        });

        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(showPassword, gbc);

        // Combo box pour le rôle
        comboRole = new JComboBox<>(new String[]{"admin", "employe"});
        comboRole.setFont(new Font("Arial", Font.PLAIN, 14));
        comboRole.setPreferredSize(new Dimension(300, 30));

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Rôle:"), gbc);
        gbc.gridx = 1;
        formPanel.add(comboRole, gbc);

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

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Configuration du tableau
        model = new DefaultTableModel(
            new String[]{"ID", "Nom d'utilisateur", "Rôle", "Actions"}, 
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Seule la colonne Actions est éditable
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
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des utilisateurs"));
        scrollPane.setPreferredSize(new Dimension(800, 300));

        // Organisation finale
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.setBackground(Color.WHITE);

        add(centerPanel, BorderLayout.CENTER);

        // Listeners
        btnSave.addActionListener(e -> enregistrerUtilisateur());
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
                modifierUtilisateur(currentRow);
            });
            
            btnDelete.addActionListener(e -> {
                fireEditingStopped();
                supprimerUtilisateur(currentRow);
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

    private void modifierUtilisateur(int row) {
        int id = (int) model.getValueAt(row, 0);
        Utilisateur utilisateur = utilisateurDAO.getUtilisateurParId(id);
        
        if (utilisateur != null) {
            idEnCoursDeModification = id;
            txtNomUtilisateur.setText(utilisateur.getNomUtilisateur());
            txtMotDePasse.setText(""); // On ne montre pas le mot de passe existant
            comboRole.setSelectedItem(utilisateur.getRole());
            btnSave.setText("Mettre à jour");
        }
    }

    private void supprimerUtilisateur(int row) {
        int id = (int) model.getValueAt(row, 0);
        
        int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Êtes-vous sûr de vouloir supprimer cet utilisateur ?", 
                "Confirmation de suppression", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            utilisateurDAO.supprimerUtilisateur(id);
            rafraichirTable();
            if (idEnCoursDeModification == id) {
                viderChamps();
                idEnCoursDeModification = -1;
            }
        }
    }

    private void enregistrerUtilisateur() {
        String nom = txtNomUtilisateur.getText().trim();
        String motDePasse = new String(txtMotDePasse.getPassword()).trim();
        String role = (String) comboRole.getSelectedItem();

        if (nom.isEmpty() || motDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Tous les champs doivent être remplis.",
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (idEnCoursDeModification != -1) {
            // Mise à jour d'un utilisateur existant
            Utilisateur utilisateur = new Utilisateur(idEnCoursDeModification, nom, motDePasse, role);
            utilisateurDAO.modifierUtilisateur(utilisateur);
            idEnCoursDeModification = -1;
            btnSave.setText("Enregistrer");
        } else {
            // Ajout d'un nouvel utilisateur
            Utilisateur utilisateur = new Utilisateur(nom, motDePasse, role);
            utilisateurDAO.ajouterUtilisateur(utilisateur);
        }
        
        rafraichirTable();
        viderChamps();
    }

    private void rafraichirTable() {
        model.setRowCount(0);
        List<Utilisateur> utilisateurs = utilisateurDAO.getTousLesUtilisateurs();
        for (Utilisateur u : utilisateurs) {
            model.addRow(new Object[]{
                    u.getId(),
                    u.getNomUtilisateur(),
                    u.getRole(),
                    "" // Colonne Actions - laissée vide car les boutons sont rendus
            });
        }
    }

    private void viderChamps() {
        txtNomUtilisateur.setText("");
        txtMotDePasse.setText("");
        comboRole.setSelectedIndex(0);
        btnSave.setText("Enregistrer");
        idEnCoursDeModification = -1;
    }

    private void retourMenu() {
        ((CardLayout) getParent().getLayout()).show(getParent(), "Accueil");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                JFrame frame = new JFrame("Test UtilisateursInterface");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new UtilisateursInterface());
                frame.setSize(900, 700);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}