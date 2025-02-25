package dao;

import model.Sponzor;
import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SponzorDAO {

    // Metoda za dodavanje novog sponzora
    public void addSponzor(Sponzor sponzor) {
        String query = "INSERT INTO Sponzor (idsponzor, naziv, vrstaSponzora) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, sponzor.getIdSponzor());
            stmt.setString(2, sponzor.getNaziv());
            stmt.setString(3, sponzor.getVrstaSponzora());
            stmt.executeUpdate();
            System.out.println("✅ Sponzor dodat u bazu!");
        } catch (SQLException e) {
            System.out.println("❌ Greška pri dodavanju sponzora!");
            e.printStackTrace();
        }
    }

    // Metoda za dobijanje svih sponzora iz baze
    public List<Sponzor> getAllSponzori() {
        List<Sponzor> sponzori = new ArrayList<>();
        String query = "SELECT idsponzor, naziv, vrstaSponzora FROM Sponzor";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Sponzor sponzor = new Sponzor(
                        rs.getInt("idsponzor"),
                        rs.getString("naziv"),
                        rs.getString("vrstaSponzora")
                );
                sponzori.add(sponzor);
            }
        } catch (SQLException e) {
            System.out.println("❌ Greška pri dobavljanju sponzora!");
            e.printStackTrace();
        }
        return sponzori;
    }

    // Metoda za pretragu sponzora po ID-u
    public Sponzor getSponzorById(int id) {
        String query = "SELECT idsponzor, naziv, vrstaSponzora FROM Sponzor WHERE idsponzor = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Sponzor(
                        rs.getInt("idsponzor"),
                        rs.getString("naziv"),
                        rs.getString("vrstaSponzora")
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Greška pri pretrazi sponzora po ID-u!");
            e.printStackTrace();
        }
        return null; // Ako ne postoji sponzor sa datim ID-om
    }

    // Metoda za ažuriranje podataka o sponzoru
    public void updateSponzor(Sponzor sponzor) {
        String query = "UPDATE Sponzor SET naziv = ?, vrstaSponzora = ? WHERE idsponzor = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, sponzor.getNaziv());
            stmt.setString(2, sponzor.getVrstaSponzora());
            stmt.setInt(3, sponzor.getIdSponzor());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Sponzor uspešno ažuriran!");
            } else {
                System.out.println("⚠️ Sponzor sa datim ID-em ne postoji!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Greška pri ažuriranju sponzora!");
            e.printStackTrace();
        }
    }

    // Metoda za brisanje sponzora po ID-u
    public void deleteSponzor(int id) {
        String query = "DELETE FROM Sponzor WHERE idsponzor = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Sponzor uspešno obrisan!");
            } else {
                System.out.println("⚠️ Sponzor sa datim ID-em ne postoji!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Greška pri brisanju sponzora!");
            e.printStackTrace();
        }
    }
}
