// ===========================
// FICHIER : ui/MainInterface.java
// ===========================
package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainInterface extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private EmpruntInterface empruntInterface;
    private LivresInterface livresInterface;

    public MainInterface() {
        setTitle("Gestion de Bibliothèque");
        setSize(1300, 710);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        mainPanel = new JPanel(new BorderLayout());

        // Panel de gauche
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(200, 600));
        leftPanel.setBackground(Color.WHITE);

        ImageIcon appIcon = new ImageIcon("L:\\JAVA PROJET\\GestionDeBibliotheque\\src\\ui\\resources\\app_icon.png");

        // === Boutons ===
        JButton btnLivres = new JButton("Livres");
        btnLivres.setMaximumSize(new Dimension(400, 30));
        btnLivres.setAlignmentX(2.0f);
        btnLivres.setFont(new Font("Tahoma", Font.BOLD, 20));

        JButton btnEmprunts = new JButton("Emprunts");
        btnEmprunts.setMaximumSize(new Dimension(400, 30));
        btnEmprunts.setAlignmentX(1.0f);
        btnEmprunts.setFont(new Font("Tahoma", Font.BOLD, 20));

        JButton btnUtilisateurs = new JButton("Utilisateurs");
        btnUtilisateurs.setMaximumSize(new Dimension(400, 30));
        btnUtilisateurs.setAlignmentX(2.0f);
        btnUtilisateurs.setFont(new Font("Tahoma", Font.BOLD, 20));

        // === Bouton Déconnexion ===
        JButton btnDeconnexion = new JButton("Déconnexion");
        btnDeconnexion.setMaximumSize(new Dimension(400, 30));
        btnDeconnexion.setAlignmentX(1.0f);
        btnDeconnexion.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnDeconnexion.setBackground(new Color(255, 128, 128));
        btnDeconnexion.setForeground(Color.BLACK);
        
        // === Bande verticale en haut avec logo + nom de l'appli ===
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setAlignmentX(1.0f);
        logoPanel.setPreferredSize(new Dimension(200, 150));
        logoPanel.setMaximumSize(new Dimension(200, 150));
        logoPanel.setBackground(new Color(70, 130, 180));
        
        JLabel appNameLabel = new JLabel("Ma Bibliothèque", JLabel.CENTER);
        appNameLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        appNameLabel.setForeground(Color.WHITE);
        logoPanel.add(appNameLabel, BorderLayout.CENTER);
        
        // Ajout des composants au leftPanel
        leftPanel.add(logoPanel);
        JLabel iconLabel = new JLabel(appIcon, JLabel.CENTER);
        logoPanel.add(iconLabel, BorderLayout.WEST);
        iconLabel.setAlignmentX(2.0f);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        leftPanel.add(btnLivres);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(btnEmprunts);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(btnUtilisateurs);
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(btnDeconnexion);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // === Panel de droite pour le contenu dynamique ===
        JPanel contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        // Image de fond et message d'accueil
        JLabel backgroundLabel = new JLabel(new ImageIcon("C:\\\\Users\\\\user\\\\Downloads\\\\225932.png"));
        backgroundLabel.setLayout(new BorderLayout());

        // Création des interfaces
        empruntInterface = new EmpruntInterface();
        livresInterface = new LivresInterface();
        
        contentPanel.add(backgroundLabel, "Accueil");
        contentPanel.add(empruntInterface, "Emprunts");
        contentPanel.add(livresInterface, "Livres");
        contentPanel.add(new UtilisateursInterface(), "Utilisateurs");

        // Actions des boutons
        btnLivres.addActionListener(e -> {
            cardLayout.show(contentPanel, "Livres");
            livresInterface.actualiserLivres(); // Actualiser la liste des livres
        });
        
        btnEmprunts.addActionListener(e -> {
            cardLayout.show(contentPanel, "Emprunts");
            empruntInterface.chargerLivres(); // Actualiser la liste des livres disponibles
            empruntInterface.rafraichirTable(); // Rafraîchir la table des emprunts
        });
        
        btnUtilisateurs.addActionListener(e -> cardLayout.show(contentPanel, "Utilisateurs"));

        btnDeconnexion.addActionListener(e -> {
            new LoginInterface();
            dispose();
        });

        // Ajout au panel principal
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        JLabel welcomeMessage = new JLabel("Bienvenue dans l'application de gestion de bibliothèque", JLabel.CENTER);
        contentPanel.add(welcomeMessage, "name_259959879073900");
        welcomeMessage.setIconTextGap(5);
        welcomeMessage.setIcon(new ImageIcon("C:\\Users\\user\\Downloads\\225932.png"));
        welcomeMessage.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeMessage.setForeground(Color.WHITE);

        // Finalisation
        getContentPane().add(mainPanel);
        setVisible(true);
    }

   
}