package model;

public class Sponzor {
    private int idSponzor;
    private String naziv;
    private String vrstaSponzora; // Novo polje

    public Sponzor(int idSponzor, String naziv, String vrstaSponzora) {
        this.idSponzor = idSponzor;
        this.naziv = naziv;
        this.vrstaSponzora = vrstaSponzora;
    }

    public int getIdSponzor() {
        return idSponzor;
    }

    public void setIdSponzor(int idSponzor) {
        this.idSponzor = idSponzor;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getVrstaSponzora() {
        return vrstaSponzora;
    }

    public void setVrstaSponzora(String vrstaSponzora) {
        this.vrstaSponzora = vrstaSponzora;
    }

    @Override
    public String toString() {
        return "Sponzor{" +
                "idSponzor=" + idSponzor +
                ", naziv='" + naziv + '\'' +
                ", vrstaSponzora='" + vrstaSponzora + '\'' +
                '}';
    }
}
