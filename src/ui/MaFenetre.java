package ui;

import javax.swing.*;
import java.awt.*;

public class MaFenetre extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MaFenetre() {
        setTitle("Gestion des Livres");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Définir le CardLayout et le panel principal
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Créer les panneaux pour chaque interface
        JPanel menuPanel = createMenuPanel(); // Le panneau principal avec le bouton
        EmpruntInterface empruntInterface = new EmpruntInterface(); // Le panneau emprunt
        
        // Ajouter les panneaux au mainPanel
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(empruntInterface, "Emprunt");

        // Ajouter le mainPanel au JFrame
        add(mainPanel);

        // Afficher la fenêtre
        setVisible(true);
    }

    // Créer un menu avec un bouton pour l'emprunt
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        JButton btnEmprunts = new JButton("Emprunts");

        // Action pour changer la vue vers EmpruntInterface
        btnEmprunts.addActionListener(e -> cardLayout.show(mainPanel, "Emprunt"));

        panel.add(btnEmprunts);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MaFenetre());
    }
}
