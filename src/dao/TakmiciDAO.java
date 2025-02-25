package dao;

import model.Takmici;
import database.DatabaseConnection;
import java.sql.*;

public class TakmiciDAO {

    public void addTakmici(Takmici takmici) {
        String query = "INSERT INTO Takmici (idborac, idkategorija) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, takmici.getIdBorac());
            stmt.setInt(2, takmici.getIdKategorija());
            stmt.executeUpdate();
            System.out.println("✅ Veza Takmici dodata u bazu!");
        } catch (SQLException e) {
            System.out.println("❌ Greška pri dodavanju veze Takmici!");
            e.printStackTrace();
        }
    }
    public void prikaziTakmicareIKategorije() {
        String query = "SELECT b.imeb, b.prezimeb, t.naziv AS tim, k.naziv AS kategorija FROM Borac b JOIN Takmici tk ON b.idb = tk.idborac JOIN Kategorija k ON tk.idkategorija = k.idkategorija JOIN Tim t ON b.idtim = t.idtim ORDER BY kategorija, imeb";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\n🏆 Takmičari i njihove kategorije:");
            while (rs.next()) {
                System.out.println("Takmičar: " + rs.getString("imeb") + " " + rs.getString("prezimeb") +
                        " | Tim: " + rs.getString("tim") +
                        " | Kategorija: " + rs.getString("kategorija"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
