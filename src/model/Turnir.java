package model;

import java.sql.Date;

public class Turnir {
    private int idTurnir;
    private String naziv;
    private String lokacija;
    private Date datumTur; // Novo polje za datum održavanja
    private int trajanjeTur; // Novo polje za trajanje turnira u satima

    public Turnir(int idTurnir, String naziv, String lokacija, Date datumTur, int trajanjeTur) {
        this.idTurnir = idTurnir;
        this.naziv = naziv;
        this.lokacija = lokacija;
        this.datumTur = datumTur;
        this.trajanjeTur = trajanjeTur;
    }

    public int getIdTurnir() {
        return idTurnir;
    }

    public void setIdTurnir(int idTurnir) {
        this.idTurnir = idTurnir;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public Date getDatumTur() {
        return datumTur;
    }

    public void setDatumTur(Date datumTur) {
        this.datumTur = datumTur;
    }

    public int getTrajanjeTur() {
        return trajanjeTur;
    }

    public void setTrajanjeTur(int trajanjeTur) {
        this.trajanjeTur = trajanjeTur;
    }

    @Override
    public String toString() {
        return "Turnir{" +
                "idTurnir=" + idTurnir +
                ", naziv='" + naziv + '\'' +
                ", lokacija='" + lokacija + '\'' +
                ", datumTur=" + datumTur +
                ", trajanjeTur=" + trajanjeTur +
                '}';
    }
}
