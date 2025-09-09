package service;

import dao.BoracDAO;
import dao.TimDAO;
import database.DatabaseConnection;
import model.Borac;

import java.sql.Connection;
import java.sql.SQLException;

public class TransakcijaService {
    private final BoracDAO boracDAO = new BoracDAO();
    private final TimDAO timDAO = new TimDAO();

    public void dodajBorcaIAzurirajTim(String ime, String prezime, int idTim) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            Borac b = new Borac(0, ime, prezime, idTim);
            boracDAO.addBoracTx(b, conn);         // 1) INSERT Borac (ISTA conn)

            timDAO.recalcBrojBoracaTx(idTim, conn); // 2) UPDATE Tim (ISTA conn)
            // ili: timDAO.incBrojBoracaTx(idTim, conn);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException("Transakcija nije uspjela", e);
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
        }
    }
}
