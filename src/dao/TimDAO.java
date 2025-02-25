package dao;

import model.Tim;
import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TimDAO {

    // Metoda za dodavanje novog tima
    public void addTim(Tim tim) {
        String query = "INSERT INTO Tim (idtim, naziv, trenertim, brojboracatim) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, tim.getIdTim());
            stmt.setString(2, tim.getNaziv());
            stmt.setString(3, tim.getTrenerTim());
            stmt.setInt(4, tim.getBrojBoracaTim());
            stmt.executeUpdate();
            System.out.println("✅ Tim dodat u bazu!");
        } catch (SQLException e) {
            System.out.println("❌ Greška pri dodavanju tima!");
            e.printStackTrace();
        }
    }

    // Metoda za dobavljanje svih timova
    public List<Tim> getAllTimovi() {
        List<Tim> timovi = new ArrayList<>();
        String query = "SELECT idtim, naziv, trenertim, brojboracatim FROM Tim";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Tim tim = new Tim(
                        rs.getInt("idtim"),
                        rs.getString("naziv"),
                        rs.getString("trenertim"),
                        rs.getInt("brojboracatim")
                );
                timovi.add(tim);
            }
        } catch (SQLException e) {
            System.out.println("❌ Greška pri dobavljanju timova!");
            e.printStackTrace();
        }
        return timovi;
    }

    // Metoda za pronalaženje tima po ID-u
    public Tim getTimById(int id) {
        String query = "SELECT idtim, naziv, trenertim, brojboracatim FROM Tim WHERE idtim = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Tim(
                        rs.getInt("idtim"),
                        rs.getString("naziv"),
                        rs.getString("trenertim"),
                        rs.getInt("brojboracatim")
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Greška pri pretrazi tima po ID-u!");
            e.printStackTrace();
        }
        return null; // Ako ne postoji tim sa datim ID-om
    }

    // Metoda za ažuriranje tima
    public void updateTim(Tim tim) {
        String query = "UPDATE Tim SET naziv = ?, trenertim = ?, brojboracatim = ? WHERE idtim = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tim.getNaziv());
            stmt.setString(2, tim.getTrenerTim());
            stmt.setInt(3, tim.getBrojBoracaTim());
            stmt.setInt(4, tim.getIdTim());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Tim uspešno ažuriran!");
            } else {
                System.out.println("⚠️ Tim sa datim ID-em ne postoji!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Greška pri ažuriranju tima!");
            e.printStackTrace();
        }
    }

    // Metoda za brisanje tima po ID-u
    public void deleteTim(int id) {
        String query = "DELETE FROM Tim WHERE idtim = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Tim uspešno obrisan!");
            } else {
                System.out.println("⚠️ Tim sa datim ID-em ne postoji!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Greška pri brisanju tima!");
            e.printStackTrace();
        }
    }

    // Metoda za prikaz broja boraca po timu
    public void brojBoracaPoTimu() {
        String query = "SELECT t.naziv AS tim, COUNT(b.idb) AS broj_boraca " +
                "FROM Tim t LEFT JOIN Borac b ON t.idtim = b.idtim " +
                "GROUP BY t.naziv";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\n📊 Broj boraca po timu:");
            while (rs.next()) {
                System.out.println("Tim: " + rs.getString("tim") + " | Broj boraca: " + rs.getInt("broj_boraca"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void prikaziBrojBoracaPoTimuIKategoriji(String katFilter, String timFilter) {
        // Počećemo sa osnovnim SELECT-om, GROUP BY i ORDER BY
        // a WHERE ćemo graditi dinamički na osnovu unosa

        StringBuilder sb = new StringBuilder("""
        SELECT t.naziv AS Naziv_Tima,
               k.naziv AS Naziv_Kategorije,
               COUNT(b.idB) AS Broj_Boraca
        FROM Tim t
        LEFT JOIN Borac b ON t.idTim = b.idTim
        LEFT JOIN Takmici tk ON b.idB = tk.idBorac
        LEFT JOIN Kategorija k ON tk.idKategorija = k.idKategorija
    """);

        // Moramo da vidimo da li je korisnik uneo filter ili ne
        // Napravićemo listu parametara koja će se mapirati na znakove '?' u upitu
        List<String> params = new ArrayList<>();

        // Postavićemo WHERE 1=1 kao polaznu tačku da je kasnije jednostavno
        sb.append(" WHERE 1=1 ");

        // Ako korisnik hoće da filtrira po kategoriji, dodajemo uslov
        if (katFilter != null && !katFilter.isBlank()) {
            sb.append(" AND k.nazivKat = ? ");
            params.add(katFilter);
        }

        // Ako korisnik hoće da filtrira po timu, dodajemo uslov
        if (timFilter != null && !timFilter.isBlank()) {
            sb.append(" AND t.naziv = ? ");
            params.add(timFilter);
        }

        // Sada dodajemo grupisanje, having i sortiranje
        sb.append("""
        GROUP BY t.naziv, k.naziv
        HAVING COUNT(b.idB) > 0
        ORDER BY Broj_Boraca DESC
    """);

        // Konačna SQL komanda:
        String query = sb.toString();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Postavljanje parametara u PreparedStatement
            int index = 1;
            for (String p : params) {
                stmt.setString(index++, p);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("\n===== BROJ BORACA PO TIMU I KATEGORIJI =====");
                if (params.isEmpty()) {
                    System.out.println("(Bez filtera ili su filteri prazni)");
                } else {
                    System.out.println("(Filter kategorija: \"" + katFilter + "\", Filter tim: \"" + timFilter + "\")");
                }
                while (rs.next()) {
                    String nazivTima       = rs.getString("Naziv_Tima");
                    String nazivKategorije = rs.getString("Naziv_Kategorije");
                    int brojBoraca         = rs.getInt("Broj_Boraca");

                    // Ako je kategorija NULL (jer je LEFT JOIN i borac možda nema kategoriju), prikažemo neku oznaku
                    if (nazivKategorije == null) nazivKategorije = "Nema kategoriju";

                    System.out.printf("Tim: %s | Kategorija: %s | Broj boraca: %d%n",
                            (nazivTima == null ? "Nema tim" : nazivTima),
                            nazivKategorije,
                            brojBoraca
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
