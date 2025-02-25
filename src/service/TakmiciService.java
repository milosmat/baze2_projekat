package service;

import dao.TakmiciDAO;
import model.Takmici;

public class TakmiciService {
    private final TakmiciDAO takmiciDAO;

    public TakmiciService() {
        this.takmiciDAO = new TakmiciDAO();
    }

    public void addTakmici(int idBorac, int idKategorija) {
        Takmici takmici = new Takmici(idBorac, idKategorija);
        takmiciDAO.addTakmici(takmici);
    }
}
