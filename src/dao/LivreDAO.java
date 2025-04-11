package dao;

import model.Livre;
import util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivreDAO {

    public void ajouterLivre(Livre livre) {
        String sql = "INSERT INTO livres(titre, auteur, disponible) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setBoolean(3, livre.isDisponible());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Livre> getTousLesLivres() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Livre livre = new Livre(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getBoolean("disponible")
                );
                livres.add(livre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return livres;
    }
    
    
 // Modification d'un livre
    public void modifierLivre(Livre livre) {
        String sql = "UPDATE livres SET titre = ?, auteur = ?, disponible = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setBoolean(3, livre.isDisponible());
            stmt.setInt(4, livre.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Suppression d'un livre
    public void supprimerLivre(int id) {
        String sql = "DELETE FROM livres WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public Livre getLivreParId(int id) {
        Livre livre = null;
        String sql = "SELECT * FROM livres WHERE id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            
            if (rs.next()) {
                livre = new Livre(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getBoolean("disponible")
                   
                );
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return livre;
    }

    public List<Livre> getTousLesLivresDispo() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE disponible = TRUE ";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Livre livre = new Livre(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getBoolean("disponible")
                );
                livres.add(livre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livres;
    }
    
    
    
    
    
    
    public void modifierLivre1(Livre livre) {
        String sql = "UPDATE livres SET titre = ?, auteur = ?, disponible = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setBoolean(3, livre.isDisponible());
            stmt.setInt(4, livre.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprimerLivre1(int id) {
        String sql = "DELETE FROM livres WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Livre> getTousLesLivres1() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Livre livre = new Livre(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getBoolean("disponible")
                );
                livres.add(livre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livres;
    }
    
    
    
  
    
    
    
    
    public List<Livre> getLivreDisponibles() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE disponible = TRUE";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Livre livre = new Livre(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getBoolean("disponible")
                );
                livres.add(livre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return livres;
    }

    
    
    public List<Livre> getLivresDisponibles() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE disponible = TRUE";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Livre livre = new Livre(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getBoolean("disponible")
                );
                livres.add(livre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return livres;
    }

    
    
    
    
    public List<Livre> getLivresEmpruntes() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE disponible = FALSE";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Livre livre = new Livre(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("auteur"),
                    rs.getBoolean("disponible")
                );
                livres.add(livre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return livres;
    }

    
    
    
    

}
