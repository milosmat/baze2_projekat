package service;

import dao.BoracDAO;
import model.Borac;

import java.sql.Date;
import java.util.List;

public class BoracService {
    private final BoracDAO boracDAO;

    public BoracService() {
        this.boracDAO = new BoracDAO();
    }

    public void addBorac(String ime, String prezime, int idTim) {
        Borac borac = new Borac(ime, prezime, idTim);
        boracDAO.addBorac(borac);
    }

    public List<Borac> getAllBorci() {
        return boracDAO.getAllBorci();
    }

    public void prikaziBorcePoKategoriji() {
        // Poziv DAO metode
        boracDAO.prikaziBorcePoKategoriji();
    }

}
