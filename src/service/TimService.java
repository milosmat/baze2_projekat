package service;

import dao.TimDAO;
import model.Tim;
import java.util.List;

public class TimService {
    private final TimDAO timDAO;

    public TimService() {
        this.timDAO = new TimDAO();
    }

    public void addTim(int idTim, String naziv, String nazivTrener, int brojBoraca) {
        Tim tim = new Tim(idTim, naziv, nazivTrener,brojBoraca);
        timDAO.addTim(tim);
    }

    public List<Tim> getAllTimovi() {
        return timDAO.getAllTimovi();
    }

    public void prikaziBrojBoracaPoTimuIKategoriji(String katFilter, String timFilter) {
        timDAO.prikaziBrojBoracaPoTimuIKategoriji(katFilter,timFilter);
    }


    public void azurirajBrojBoraca(int idTim) {
    }
}
