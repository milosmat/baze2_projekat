package model;

public class Takmici {
    private int idBorac;
    private int idKategorija;

    public Takmici(int idBorac, int idKategorija) {
        this.idBorac = idBorac;
        this.idKategorija = idKategorija;
    }

    public int getIdBorac() { return idBorac; }
    public int getIdKategorija() { return idKategorija; }

    public void setIdBorac(int idBorac) { this.idBorac = idBorac; }
    public void setIdKategorija(int idKategorija) { this.idKategorija = idKategorija; }

    @Override
    public String toString() {
        return "Takmici{" + "idBorac=" + idBorac + ", idKategorija=" + idKategorija + '}';
    }
}
