package model;

public class Tim {
    private int idTim;
    private String naziv;
    private String trenerTim; // Novo polje
    private int brojBoracaTim; // Novo polje

    public Tim(int idTim, String naziv, String trenerTim, int brojBoracaTim) {
        this.idTim = idTim;
        this.naziv = naziv;
        this.trenerTim = trenerTim;
        this.brojBoracaTim = brojBoracaTim;
    }

    public int getIdTim() {
        return idTim;
    }

    public void setIdTim(int idTim) {
        this.idTim = idTim;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getTrenerTim() {
        return trenerTim;
    }

    public void setTrenerTim(String trenerTim) {
        this.trenerTim = trenerTim;
    }

    public int getBrojBoracaTim() {
        return brojBoracaTim;
    }

    public void setBrojBoracaTim(int brojBoracaTim) {
        this.brojBoracaTim = brojBoracaTim;
    }

    @Override
    public String toString() {
        return "Tim{" +
                "idTim=" + idTim +
                ", naziv='" + naziv + '\'' +
                ", trenerTim='" + trenerTim + '\'' +
                ", brojBoracaTim=" + brojBoracaTim +
                '}';
    }
}
