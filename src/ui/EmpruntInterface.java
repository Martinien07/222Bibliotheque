package ui;

import dao.EmpruntDAO;
import dao.LivreDAO;
import model.Emprunt;
import model.Livre;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EmpruntInterface extends JPanel {
    private JTextField txtEmprunteur;
    private JComboBox<Livre> comboLivres;
    private JSpinner spinnerDateEmprunt;
    private JSpinner spinnerDateRetour;
    private JButton btnSave, btnClear, btnQuitter;
    private JTable table;
    private DefaultTableModel model;
    private EmpruntDAO empruntDAO;
    private LivreDAO livreDAO;
    private int idEnCoursDeModification = -1;

    public EmpruntInterface() {
        // Initialisation des DAO
        empruntDAO = new EmpruntDAO();
        livreDAO = new LivreDAO();

        // Configuration du panel principal
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Création du titre
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JLabel title = new JLabel("📄 Gestion des Emprunts");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);

        // 2. Panel du formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Combo box pour les livres
        comboLivres = new JComboBox<>();
        chargerLivres();
        comboLivres.setFont(new Font("Arial", Font.PLAIN, 14));
        comboLivres.setPreferredSize(new Dimension(300, 30));

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Livre:"), gbc);
        gbc.gridx = 1;
        formPanel.add(comboLivres, gbc);

        // Champ texte pour l'emprunteur
        txtEmprunteur = new JTextField();
        txtEmprunteur.setFont(new Font("Arial", Font.PLAIN, 14));
        txtEmprunteur.setPreferredSize(new Dimension(300, 30));

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Emprunteur:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtEmprunteur, gbc);

        // Configuration des spinners de date
        Date aujourdHui = new Date();
        
        // Date d'emprunt (aujourd'hui, non modifiable)
        spinnerDateEmprunt = new JSpinner(new SpinnerDateModel(aujourdHui, null, null, Calendar.DAY_OF_MONTH));
        spinnerDateEmprunt.setEditor(new JSpinner.DateEditor(spinnerDateEmprunt, "dd/MM/yyyy"));
        spinnerDateEmprunt.setEnabled(false);
        spinnerDateEmprunt.setPreferredSize(new Dimension(300, 30));

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Date Emprunt:"), gbc);
        gbc.gridx = 1;
        formPanel.add(spinnerDateEmprunt, gbc);

        // Date de retour (demain à +3 mois)
        Calendar cal = Calendar.getInstance();
        cal.setTime(aujourdHui);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date demain = cal.getTime();
        
        cal.add(Calendar.MONTH, 3);
        Date dans3Mois = cal.getTime();
        
        spinnerDateRetour = new JSpinner(new SpinnerDateModel(demain, demain, dans3Mois, Calendar.DAY_OF_MONTH));
        spinnerDateRetour.setEditor(new JSpinner.DateEditor(spinnerDateRetour, "dd/MM/yyyy"));
        spinnerDateRetour.setPreferredSize(new Dimension(300, 30));

        // Ajout de la validation en temps réel
        configurerValidationDate();

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Date Retour:"), gbc);
        gbc.gridx = 1;
        formPanel.add(spinnerDateRetour, gbc);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(240, 240, 240));
        
        btnSave = new JButton("Enregistrer");
        btnClear = new JButton("Effacer");
        btnQuitter = new JButton("Quitter");

        for (JButton btn : new JButton[]{btnSave, btnClear, btnQuitter}) {
            btn.setFont(new Font("Arial", Font.PLAIN, 14));
            btn.setPreferredSize(new Dimension(120, 35));
            buttonPanel.add(btn);
        }

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Configuration du tableau
        model = new DefaultTableModel(
            new String[]{"ID", "Livre", "Emprunteur", "Date Emprunt", "Date Retour", "Actions"}, 
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
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
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des emprunts"));
        scrollPane.setPreferredSize(new Dimension(800, 300));

        // Organisation finale
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.setBackground(Color.WHITE);

        add(centerPanel, BorderLayout.CENTER);

        // Listeners
        btnSave.addActionListener(e -> enregistrerEmprunt());
        btnClear.addActionListener(e -> viderChamps());
        btnQuitter.addActionListener(e -> retourMenu());

        rafraichirTable();
    }

    private void configurerValidationDate() {
        ((JSpinner.DefaultEditor) spinnerDateRetour.getEditor()).getTextField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validerDate();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validerDate();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validerDate();
            }
            
            private void validerDate() {
                try {
                    Date dateEmp = (Date) spinnerDateEmprunt.getValue();
                    Date dateRet = (Date) spinnerDateRetour.getValue();
                    
                    Calendar calMin = Calendar.getInstance();
                    calMin.setTime(dateEmp);
                    calMin.add(Calendar.DAY_OF_MONTH, 1);
                    
                    Calendar calMax = Calendar.getInstance();
                    calMax.setTime(dateEmp);
                    calMax.add(Calendar.DAY_OF_MONTH, 90);
                    
                    if (dateRet.before(calMin.getTime())) {
                        spinnerDateRetour.getEditor().getComponent(0).setBackground(new Color(255, 200, 200));
                    } else if (dateRet.after(calMax.getTime())) {
                        spinnerDateRetour.getEditor().getComponent(0).setBackground(new Color(255, 200, 200));
                    } else {
                        spinnerDateRetour.getEditor().getComponent(0).setBackground(Color.WHITE);
                    }
                } catch (Exception e) {
                    // En cas d'erreur de parsing, on ne fait rien
                }
            }
        });
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
                modifierEmprunt(currentRow);
            });
            
            btnDelete.addActionListener(e -> {
                fireEditingStopped();
                supprimerEmprunt(currentRow);
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

    private void modifierEmprunt(int row) {
        int id = (int) model.getValueAt(row, 0);
        Emprunt emprunt = empruntDAO.getEmpruntParId(id);
        
        if (emprunt != null) {
            idEnCoursDeModification = id;
            
            for (int i = 0; i < comboLivres.getItemCount(); i++) {
                Livre livre = comboLivres.getItemAt(i);
                if (livre.getId() == emprunt.getLivreId()) {
                    comboLivres.setSelectedIndex(i);
                    break;
                }
            }
            
            txtEmprunteur.setText(emprunt.getEmprunteur());
            spinnerDateEmprunt.setValue(emprunt.getDateEmprunt());
            spinnerDateRetour.setValue(emprunt.getDateRetour());
            btnSave.setText("Mettre à jour");
        }
    }

    private void supprimerEmprunt(int row) {
        int id = (int) model.getValueAt(row, 0);
        
        int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Êtes-vous sûr de vouloir supprimer cet emprunt ?", 
                "Confirmation de suppression", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            empruntDAO.supprimerEmprunt(id);
            rafraichirTable();
            chargerLivres();
            if (idEnCoursDeModification == id) {
                viderChamps();
                idEnCoursDeModification = -1;
            }
        }
    }

    public void chargerLivres() {
        comboLivres.removeAllItems();
        List<Livre> livres = livreDAO.getTousLesLivresDispo(); 
        for (Livre livre : livres) {
            comboLivres.addItem(livre);
        }
        if (livres.isEmpty()) {
            comboLivres.addItem(new Livre(-1, "Aucun livre disponible", "", false));
        }
    }

    private void enregistrerEmprunt() {
        Livre livre = (Livre) comboLivres.getSelectedItem();
        String emprunteur = txtEmprunteur.getText().trim();
        Date dateEmp = (Date) spinnerDateEmprunt.getValue();
        Date dateRet = (Date) spinnerDateRetour.getValue();

        // Validation des dates
        Calendar calEmp = Calendar.getInstance();
        calEmp.setTime(dateEmp);
        Calendar calRet = Calendar.getInstance();
        calRet.setTime(dateRet);
        
        // Calcul des dates limites
        Calendar calMin = Calendar.getInstance();
        calMin.setTime(dateEmp);
        calMin.add(Calendar.DAY_OF_MONTH, 1); // Date min = date emprunt + 1 jour
        
        Calendar calMax = Calendar.getInstance();
        calMax.setTime(dateEmp);
        calMax.add(Calendar.DAY_OF_MONTH, 90); // Date max = date emprunt + 90 jours
        
        // Vérification de la date de retour
        if (dateRet.before(calMin.getTime())) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            JOptionPane.showMessageDialog(this, 
                "La date de retour doit être au moins le " + sdf.format(calMin.getTime()) + 
                "\n(1 jour après la date d'emprunt)",
                "Date de retour trop proche", 
                JOptionPane.ERROR_MESSAGE);
            spinnerDateRetour.setValue(calMin.getTime());
            return;
        }
        
        if (dateRet.after(calMax.getTime())) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            JOptionPane.showMessageDialog(this, 
                "La date de retour ne peut pas dépasser le " + sdf.format(calMax.getTime()) + 
                "\n(90 jours après la date d'emprunt)",
                "Date de retour trop éloignée", 
                JOptionPane.ERROR_MESSAGE);
            spinnerDateRetour.setValue(calMax.getTime());
            return;
        }

        if (livre != null && livre.getId() != -1 && !emprunteur.isEmpty()) {
            if (idEnCoursDeModification != -1) {
                Emprunt emprunt = new Emprunt(idEnCoursDeModification, livre.getId(), emprunteur, dateEmp, dateRet);
                empruntDAO.modifierEmprunt(emprunt);
                idEnCoursDeModification = -1;
                btnSave.setText("Enregistrer");
            } else {
                Emprunt emprunt = new Emprunt(livre.getId(), emprunteur, dateEmp, dateRet);
                empruntDAO.ajouterEmprunt(emprunt);
            }
            rafraichirTable();
            viderChamps();
            chargerLivres();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un livre valide et remplir tous les champs.",
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void rafraichirTable() {
        model.setRowCount(0);
        List<Emprunt> emprunts = empruntDAO.getTousLesEmprunts();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Emprunt emp : emprunts) {
            Livre livre = livreDAO.getLivreParId(emp.getLivreId());
            model.addRow(new Object[]{
                    emp.getId(),
                    (livre != null ? livre.getTitre() : "Inconnu"),
                    emp.getEmprunteur(),
                    sdf.format(emp.getDateEmprunt()),
                    sdf.format(emp.getDateRetour()),
                    ""
            });
        }
    }

    private void viderChamps() {
        Date aujourdHui = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(aujourdHui);
        cal.add(Calendar.DAY_OF_MONTH, 1); // Demain
        
        spinnerDateEmprunt.setValue(aujourdHui);
        spinnerDateRetour.setValue(cal.getTime());
        
        if (comboLivres.getItemCount() > 0) {
            comboLivres.setSelectedIndex(0);
        }
        txtEmprunteur.setText("");
        btnSave.setText("Enregistrer");
        idEnCoursDeModification = -1;
    }

    private void retourMenu() {
        ((CardLayout) getParent().getLayout()).show(getParent(), "Accueil");
    }
    
    public void chargerLivresDisponibles() {
        chargerLivres();
    }
    
    public void rafraichirTableEmprunts() {
        rafraichirTable();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                JFrame frame = new JFrame("Test EmpruntInterface");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new EmpruntInterface());
                frame.setSize(900, 700);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}