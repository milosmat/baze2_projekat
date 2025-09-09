package dao;

import model.Borac;
import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoracDAO {

    public void addBorac(Borac borac) {
        // 1) Najpre uzmemo trenutni max idb iz tabele Borac
        String selectMaxId = "SELECT COALESCE(MAX(idb), 0) AS max_id FROM Borac";

        // 2) Insert upit - sada ubacujemo i idb
        String insertQuery = "INSERT INTO Borac (idb, imeb, prezimeb, idtim) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {

            // Prvo dobijamo maxId
            int nextId = 1; // Ako nema nijednog reda, krećemo od 1
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(selectMaxId)) {
                if (rs.next()) {
                    int maxId = rs.getInt("max_id");
                    nextId = maxId + 1;
                }
            }

            // Postavimo taj ručno generisani ID u objekat Borac
            borac.setIdb(nextId);

            // 3) Zatim ubacujemo tog borca u bazu
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                pstmt.setInt(1, borac.getIdb());
                pstmt.setString(2, borac.getIme());
                pstmt.setString(3, borac.getPrezime());
                pstmt.setInt(4, borac.getIdTim());

                pstmt.executeUpdate();
                System.out.println("✅ Borac dodat u bazu sa ID = " + nextId);
            }

        } catch (SQLException e) {
            System.out.println("❌ Greška pri dodavanju borca!");
            e.printStackTrace();
        }
    }


    public List<Borac> getAllBorci() {
        List<Borac> borci = new ArrayList<>();
        String query = "SELECT * FROM Borac";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Borac borac = new Borac(rs.getInt("idb"), rs.getString("imeb"), rs.getString("prezimeb"), rs.getInt("idtim"));
                borci.add(borac);
            }
        } catch (SQLException e) {
            System.out.println("❌ Greška pri dobavljanju boraca!");
            e.printStackTrace();
        }
        return borci;
    }

    public void prikaziBorcePoKategoriji() {
        String query = """
        
                SELECT k.naziv AS kategorija,
               COUNT(b.idB) AS broj_boraca
        FROM Kategorija k
        LEFT JOIN Takmici t ON k.idKategorija = t.idKategorija
        LEFT JOIN Borac b   ON t.idBorac = b.idb
        GROUP BY k.naziv
        ORDER BY broj_boraca DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n===== BROJ BORACA PO KATEGORIJI =====");
            while (rs.next()) {
                String kategorija = rs.getString("kategorija");
                int brojBoraca = rs.getInt("broj_boraca");
                System.out.printf("Kategorija: %s | Broj boraca: %d%n", kategorija, brojBoraca);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBoracTx(model.Borac borac, Connection conn) throws SQLException {
        String selectMaxId = "SELECT COALESCE(MAX(idb), 0) AS max_id FROM Borac";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectMaxId)) {
            int nextId = 1;
            if (rs.next()) nextId = rs.getInt("max_id") + 1;
            borac.setIdb(nextId);
        }

        String insertQuery = "INSERT INTO Borac (idb, imeb, prezimeb, idtim) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertQuery)) {
            ps.setInt(1, borac.getIdb());
            ps.setString(2, borac.getIme());
            ps.setString(3, borac.getPrezime());
            ps.setInt(4, borac.getIdTim());
            ps.executeUpdate();
        }
    }

}
