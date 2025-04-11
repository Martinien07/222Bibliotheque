package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_bibliotheque?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Mets ton mot de passe ici

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
