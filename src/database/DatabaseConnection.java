package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // URL konekcije sa PostgreSQL bazom
    private static final String URL = "jdbc:postgresql://localhost:5432/kickbox_turnir";
    private static final String USER = "postgres";  // Postavi svog korisnika
    private static final String PASSWORD = "super";  // Postavi svoju lozinku

    // Metoda za dobijanje konekcije
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Uspostavljena konekcija sa bazom!");
            return conn;
        } catch (SQLException e) {
            System.out.println("❌ Greška pri povezivanju sa bazom!");
            e.printStackTrace();
            return null;
        }
    }
}
