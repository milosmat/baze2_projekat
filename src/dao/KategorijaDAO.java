package dao;

import model.Kategorija;
import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KategorijaDAO {

    public void addKategorija(Kategorija kategorija) {
        String query = "INSERT INTO Kategorija (idkategorija, naziv) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, kategorija.getIdKategorija());
            stmt.setString(2, kategorija.getNaziv());
            stmt.executeUpdate();
            System.out.println("✅ Kategorija dodata u bazu!");
        } catch (SQLException e) {
            System.out.println("❌ Greška pri dodavanju kategorije!");
            e.printStackTrace();
        }
    }

    public List<Kategorija> getAllKategorije() {
        List<Kategorija> kategorije = new ArrayList<>();
        String query = "SELECT * FROM Kategorija";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Kategorija kategorija = new Kategorija(rs.getInt("idkategorija"), rs.getString("naziv"));
                kategorije.add(kategorija);
            }
        } catch (SQLException e) {
            System.out.println("❌ Greška pri dobavljanju kategorija!");
            e.printStackTrace();
        }
        return kategorije;
    }
}
