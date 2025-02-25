package service;

import dao.SponzorDAO;
import model.Sponzor;
import java.util.List;

public class SponzorService {
    private final SponzorDAO sponzorDAO;

    public SponzorService() {
        this.sponzorDAO = new SponzorDAO();
    }

    // Dodavanje novog sponzora
    public void addSponzor(int idSponzor, String naziv, String vrstaSponzora) {
        Sponzor sponzor = new Sponzor(idSponzor, naziv, vrstaSponzora);
        sponzorDAO.addSponzor(sponzor);
    }

    // Dobavljanje svih sponzora
    public List<Sponzor> getAllSponzori() {
        return sponzorDAO.getAllSponzori();
    }

    // Dobavljanje sponzora po ID-u
    public Sponzor getSponzorById(int idSponzor) {
        return sponzorDAO.getSponzorById(idSponzor);
    }

    // Ažuriranje podataka o sponzoru
    public void updateSponzor(int idSponzor, String naziv, String vrstaSponzora) {
        Sponzor sponzor = new Sponzor(idSponzor, naziv, vrstaSponzora);
        sponzorDAO.updateSponzor(sponzor);
    }

    // Brisanje sponzora po ID-u
    public void deleteSponzor(int idSponzor) {
        sponzorDAO.deleteSponzor(idSponzor);
    }
}
