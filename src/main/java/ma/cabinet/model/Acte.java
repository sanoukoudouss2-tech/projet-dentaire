package ma.cabinet.model;

import java.io.Serializable;

/** Acte médical : détartrage, extraction, dévitalisation... */
public class Acte implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idActe;
    private String libelle;
    private int duree;      // en minutes
    private double tarif;   // en dirhams

    public Acte(int idActe, String libelle, int duree, double tarif) {
        this.idActe = idActe;
        setLibelle(libelle);
        setDuree(duree);
        setTarif(tarif);
    }

    public int getIdActe() {
        return idActe;
    }

    public void setIdActe(int idActe) {
        this.idActe = idActe;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        if (libelle == null || libelle.isBlank()) {
            throw new IllegalArgumentException("Le libellé est obligatoire");
        }
        this.libelle = libelle.trim();
    }

    /** Durée en minutes. */
    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        if (duree <= 0) {
            throw new IllegalArgumentException("La durée doit être positive");
        }
        this.duree = duree;
    }

    public double getTarif() {
        return tarif;
    }

    public void setTarif(double tarif) {
        if (tarif < 0) {
            throw new IllegalArgumentException("Le tarif ne peut pas être négatif");
        }
        this.tarif = tarif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Acte)) return false;
        return idActe == ((Acte) o).idActe;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idActe);
    }

    @Override
    public String toString() {
        return libelle + " (" + duree + " min, " + String.format("%.2f", tarif) + " DH)";
    }
}
