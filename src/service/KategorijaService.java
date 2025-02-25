package service;

import dao.KategorijaDAO;
import model.Kategorija;
import java.util.List;

public class KategorijaService {
    private final KategorijaDAO kategorijaDAO;

    public KategorijaService() {
        this.kategorijaDAO = new KategorijaDAO();
    }

    public void addKategorija(int idKategorija, String naziv) {
        Kategorija kategorija = new Kategorija(idKategorija, naziv);
        kategorijaDAO.addKategorija(kategorija);
    }

    public List<Kategorija> getAllKategorije() {
        return kategorijaDAO.getAllKategorije();
    }
}
