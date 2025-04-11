package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.Database;

public class LoginInterface extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnValider, btnQuitter;
    private JCheckBox chkParametres;

    public LoginInterface() {
        // Configuration de la fenêtre principale
        setTitle("SWECOM GESTION - Connexion");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240));

        // Panel principal avec bordure et padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 240, 240));
        add(mainPanel);

        // Panel de titre
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        titlePanel.setBackground(new Color(70, 130, 180));
        
        JLabel iconLabel = new JLabel(new ImageIcon("resources/computer_icon.png"));
        JLabel lblTitle = new JLabel("CONNEXION AU SYSTÈME");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        
        titlePanel.add(iconLabel);
        titlePanel.add(lblTitle);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Panel du formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Champ Nom d'utilisateur
        JLabel lblUser = new JLabel("Nom d'utilisateur:");
        lblUser.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblUser, gbc);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsername.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        formPanel.add(txtUsername, gbc);

        // Champ Mot de passe
        JLabel lblPassword = new JLabel("Mot de passe:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblPassword, gbc);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        formPanel.add(txtPassword, gbc);

        // Checkbox Paramètres
        chkParametres = new JCheckBox("Sauvegarder les paramètres de connexion");
        chkParametres.setFont(new Font("Arial", Font.PLAIN, 12));
        chkParametres.setBackground(Color.WHITE);
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(chkParametres, gbc);

        // Panel des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        btnValider = new JButton("Valider");
        btnQuitter = new JButton("Quitter");
        
        // Style des boutons
        for (JButton btn : new JButton[]{btnValider, btnQuitter}) {
            btn.setFont(new Font("Arial", Font.PLAIN, 14));
            btn.setPreferredSize(new Dimension(120, 35));
            btn.setFocusPainted(false);
            
            if (btn == btnValider) {
                btn.setBackground(new Color(70, 130, 180));
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(new Color(220, 80, 60));
                btn.setForeground(Color.WHITE);
            }
            
            buttonPanel.add(btn);
        }

        // Organisation des panels
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        btnValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (verifierIdentifiants(txtUsername.getText(), new String(txtPassword.getPassword()))) {
                    JOptionPane.showMessageDialog(LoginInterface.this, 
                        "Connexion réussie !", 
                        "Succès", 
                        JOptionPane.INFORMATION_MESSAGE);
                    new MainInterface();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginInterface.this, 
                        "Nom d'utilisateur ou mot de passe incorrect.", 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private boolean verifierIdentifiants(String username, String password) {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM utilisateurs WHERE nom_utilisateur = ? AND mot_de_passe = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new LoginInterface();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}