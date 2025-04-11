package ui;

import dao.LivreDAO;
import dao.EmpruntDAO;
import model.Livre;
import model.Emprunt;
import util.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;


public class MainInterface2 extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private LivreDAO livreDAO = new LivreDAO();
    private EmpruntDAO empruntDAO = new EmpruntDAO();

    public MainInterface2() {
        setTitle("Gestion des Livres et Emprunts");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(new String[]{"ID", "Titre", "Auteur", "Disponible"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton ajouterLivreBtn = new JButton("Ajouter Livre");
        ajouterLivreBtn.addActionListener(e -> ajouterLivre());
        panel.add(ajouterLivreBtn);

        JButton modifierLivreBtn = new JButton("Modifier Livre");
        modifierLivreBtn.addActionListener(e -> modifierLivre());
        panel.add(modifierLivreBtn);

        JButton supprimerLivreBtn = new JButton("Supprimer Livre");
        supprimerLivreBtn.addActionListener(e -> supprimerLivre());
        panel.add(supprimerLivreBtn);

        JButton ajouterEmpruntBtn = new JButton("Ajouter Emprunt");
        ajouterEmpruntBtn.addActionListener(e -> ajouterEmprunt());
        panel.add(ajouterEmpruntBtn);

        JButton modifierEmpruntBtn = new JButton("Modifier Emprunt");
        modifierEmpruntBtn.addActionListener(e -> modifierEmprunt());
        panel.add(modifierEmpruntBtn);

        JButton supprimerEmpruntBtn = new JButton("Supprimer Emprunt");
        supprimerEmpruntBtn.addActionListener(e -> supprimerEmprunt());
        panel.add(supprimerEmpruntBtn);

        add(panel, BorderLayout.SOUTH);

        rafraichirTable();
        setVisible(true);
    }

    // Ajouter un livre
    private void ajouterLivre() {
        String titre = JOptionPane.showInputDialog(this, "Titre :");
        String auteur = JOptionPane.showInputDialog(this, "Auteur :");
        if (titre != null && auteur != null) {
            livreDAO.ajouterLivre(new Livre(titre, auteur, true));
            rafraichirTable();
        }
    }

    // Modifier un livre
    private void modifierLivre() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int id = (int) model.getValueAt(row, 0);
            String titre = JOptionPane.showInputDialog(this, "Nouveau Titre :");
            String auteur = JOptionPane.showInputDialog(this, "Nouvel Auteur :");
            boolean disponible = (JOptionPane.showConfirmDialog(this, "Disponible ?") == JOptionPane.YES_OPTION);
            if (titre != null && auteur != null) {
                Livre livre = new Livre(id, titre, auteur, disponible);
                livreDAO.modifierLivre(livre);
                rafraichirTable();
            }
        }
    }

    // Supprimer un livre
    private void supprimerLivre() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int id = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce livre ?");
            if (confirm == JOptionPane.YES_OPTION) {
                livreDAO.supprimerLivre(id);
                rafraichirTable();
            }
        }
    }

    // Ajouter un emprunt
    private void ajouterEmprunt() {
        String emprunteur = JOptionPane.showInputDialog(this, "Nom de l'emprunteur :");
        int livreId = Integer.parseInt(JOptionPane.showInputDialog(this, "ID du livre :"));
        Date dateEmprunt = new Date();
        Date dateRetour = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000); // Retour dans 7 jours
        if (emprunteur != null) {
            Emprunt emprunt = new Emprunt(livreId, emprunteur, dateEmprunt, dateRetour);
            empruntDAO.ajouterEmprunt(emprunt);
        }
    }

    // Modifier un emprunt
    private void modifierEmprunt() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int id = (int) model.getValueAt(row, 0);
            String emprunteur = JOptionPane.showInputDialog(this, "Nouvel emprunteur :");
            Date dateEmprunt = new Date();
            Date dateRetour = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000); // Retour dans 7 jours
            Emprunt emprunt = new Emprunt(id, 1, emprunteur, dateEmprunt, dateRetour); // LivreID est un exemple
            empruntDAO.modifierEmprunt(emprunt);
        }
    }

    // Supprimer un emprunt
    private void supprimerEmprunt() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int id = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cet emprunt ?");
            if (confirm == JOptionPane.YES_OPTION) {
                empruntDAO.supprimerEmprunt(id);
            }
        }
    }

    private void rafraichirTable() {
        model.setRowCount(0);
        List<Livre> livres = livreDAO.getTousLesLivres();
        for (Livre l : livres) {
            model.addRow(new Object[]{l.getId(), l.getTitre(), l.getAuteur(), l.isDisponible() ? "Oui" : "Non"});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainInterface2::new);
    }
}
