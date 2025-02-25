package dao;

import model.Turnir;
import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TurnirDAO {

    // Metoda za dodavanje novog turnira
    public void addTurnir(Turnir turnir) {
        String query = "INSERT INTO Turnir (idturnir, naziv, lokacija, \"datumTur\", trajanjeTur) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, turnir.getIdTurnir());
            stmt.setString(2, turnir.getNaziv());
            stmt.setString(3, turnir.getLokacija());
            stmt.setDate(4, turnir.getDatumTur()); // Postavljanje datuma
            stmt.setInt(5, turnir.getTrajanjeTur()); // Postavljanje trajanja u satima
            stmt.executeUpdate();
            System.out.println("✅ Turnir dodat u bazu!");
        } catch (SQLException e) {
            System.out.println("❌ Greška pri dodavanju turnira!");
            e.printStackTrace();
        }
    }

    // Metoda za dobavljanje svih turnira
    public List<Turnir> getAllTurniri() {
        List<Turnir> turniri = new ArrayList<>();
        String query = "SELECT idturnir, naziv, lokacija, \"datumTur\", trajanjeTur FROM Turnir";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Turnir turnir = new Turnir(
                        rs.getInt("idturnir"),
                        rs.getString("naziv"),
                        rs.getString("lokacija"),
                        rs.getDate("datumTur"),
                        rs.getInt("trajanjeTur")
                );
                turniri.add(turnir);
            }
        } catch (SQLException e) {
            System.out.println("❌ Greška pri dobavljanju turnira!");
            e.printStackTrace();
        }
        return turniri;
    }

    // Metoda za pronalaženje turnira po ID-u
    public Turnir getTurnirById(int id) {
        String query = "SELECT idturnir, naziv, lokacija, \"datumTur\", trajanjeTur FROM Turnir WHERE idturnir = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Turnir(
                        rs.getInt("idturnir"),
                        rs.getString("naziv"),
                        rs.getString("lokacija"),
                        rs.getDate("datumTur"),
                        rs.getInt("trajanjeTur")
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Greška pri pretrazi turnira po ID-u!");
            e.printStackTrace();
        }
        return null; // Ako ne postoji turnir sa datim ID-om
    }

    // Metoda za ažuriranje podataka o turniru
    public void updateTurnir(Turnir turnir) {
        String query = "UPDATE Turnir SET naziv = ?, lokacija = ?, \"datumTur\" = ?, trajanjeTur = ? WHERE idturnir = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, turnir.getNaziv());
            stmt.setString(2, turnir.getLokacija());
            stmt.setDate(3, turnir.getDatumTur());
            stmt.setInt(4, turnir.getTrajanjeTur());
            stmt.setInt(5, turnir.getIdTurnir());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Turnir uspešno ažuriran!");
            } else {
                System.out.println("⚠️ Turnir sa datim ID-em ne postoji!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Greška pri ažuriranju turnira!");
            e.printStackTrace();
        }
    }

    // Metoda za brisanje turnira po ID-u
    public void deleteTurnir(int idTurnir) {
        String query = "DELETE FROM Turnir WHERE idturnir = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idTurnir);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Turnir uspešno obrisan!");
            } else {
                System.out.println("⚠️ Turnir sa datim ID-em ne postoji!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Greška pri brisanju turnira!");
            e.printStackTrace();
        }
    }
}
