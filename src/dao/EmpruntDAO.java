package dao;

import model.Emprunt;
import util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpruntDAO {

    public void ajouterEmprunt(Emprunt emprunt) {
        String sql = "INSERT INTO emprunts(livre_id, emprunteur, date_emprunt, date_retour) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, emprunt.getLivreId());
            stmt.setString(2, emprunt.getEmprunteur());
            stmt.setDate(3, new java.sql.Date(emprunt.getDateEmprunt().getTime()));
            stmt.setDate(4, new java.sql.Date(emprunt.getDateRetour().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        // actualisation de la disponibilité de livre
        
        String sql2 = "UPDATE livres SET  disponible = FALSE WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql2)) {

            stmt.setInt(1, emprunt.getLivreId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
    }

    public void modifierEmprunt(Emprunt emprunt) {
        String sql = "UPDATE emprunts SET livre_id = ?, emprunteur = ?, date_emprunt = ?, date_retour = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, emprunt.getLivreId());
            stmt.setString(2, emprunt.getEmprunteur());
            stmt.setDate(3, new java.sql.Date(emprunt.getDateEmprunt().getTime()));
            stmt.setDate(4, new java.sql.Date(emprunt.getDateRetour().getTime()));
            stmt.setInt(5, emprunt.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprimerEmprunt(int id) {
        String sql = "DELETE FROM emprunts WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Emprunt> getTousLesEmprunts() {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunts";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Emprunt emprunt = new Emprunt(
                    rs.getInt("id"),
                    rs.getInt("livre_id"),
                    rs.getString("emprunteur"),
                    rs.getDate("date_emprunt"),
                    rs.getDate("date_retour")
                );
                emprunts.add(emprunt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emprunts;
    }
    
    
    
    public Emprunt getEmpruntParId(int id) {
        Emprunt emprunt = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();
            String query = "SELECT * FROM emprunts WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                emprunt = new Emprunt(id, id, query, null, null);
                emprunt.setId(resultSet.getInt("id"));
                emprunt.setLivreId(resultSet.getInt("livre_id"));
                emprunt.setEmprunteur(resultSet.getString("emprunteur"));
                emprunt.setDateEmprunt(resultSet.getDate("date_emprunt"));
                emprunt.setDateRetour(resultSet.getDate("date_retour"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return emprunt;
    }
    
    
    
    
    
    
    
}
