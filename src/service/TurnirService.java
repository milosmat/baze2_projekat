package service;

import dao.TurnirDAO;
import model.Turnir;
import java.sql.Date;
import java.util.List;

public class TurnirService {
    private final TurnirDAO turnirDAO;

    public TurnirService() {
        this.turnirDAO = new TurnirDAO();
    }

    // Dodavanje novog turnira
    public void addTurnir(int idTurnir, String naziv, String lokacija, Date datumTur, int trajanjeTur) {
        Turnir turnir = new Turnir(idTurnir, naziv, lokacija, datumTur, trajanjeTur);
        turnirDAO.addTurnir(turnir);
    }

    // Dobavljanje svih turnira
    public List<Turnir> getAllTurniri() {
        return turnirDAO.getAllTurniri();
    }

    // Dobavljanje turnira po ID-u
    public Turnir getTurnirById(int idTurnir) {
        return turnirDAO.getTurnirById(idTurnir);
    }

    // Ažuriranje podataka turnira
    public void updateTurnir(int idTurnir, String naziv, String lokacija, Date datumTur, int trajanjeTur) {
        Turnir turnir = new Turnir(idTurnir, naziv, lokacija, datumTur, trajanjeTur);
        turnirDAO.updateTurnir(turnir);
    }

    // Brisanje turnira po ID-u
    public void deleteTurnir(int idTurnir) {
        turnirDAO.deleteTurnir(idTurnir);
    }

}
