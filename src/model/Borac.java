package model;

public class Borac {
    private int idb;
    private String ime;
    private String prezime;
    private int idTim;

    public Borac(int idb, String ime, String prezime, int idTim) {
        this.idb = idb;
        this.ime = ime;
        this.prezime = prezime;
        this.idTim = idTim;
    }

    public Borac( String ime, String prezime, int idTim) {
        this.ime = ime;
        this.prezime = prezime;
        this.idTim = idTim;
    }

    public int getIdb() { return idb; }
    public String getIme() { return ime; }
    public String getPrezime() { return prezime; }
    public int getIdTim() { return idTim; }

    public void setIdb(int idb) { this.idb = idb; }
    public void setIme(String ime) { this.ime = ime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }
    public void setIdTim(int idTim) { this.idTim = idTim; }

    @Override
    public String toString() {
        return "Borac{" + "idb=" + idb + ", ime='" + ime + '\'' + ", prezime='" + prezime + '\'' + ", idTim=" + idTim + '}';
    }
}
