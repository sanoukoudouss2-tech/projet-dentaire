package ma.cabinet.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Plage horaire d'un dentiste. Un Creneau "simple" est toujours libre ;
 * quand il est réservé, il est remplacé par un CreneauReserve (même id, mêmes heures).
 */
public class Creneau implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private int idCreneau;
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private TypeCreneau type;
    private Dentiste dentiste;

    public Creneau(int idCreneau, LocalDate date, LocalTime heureDebut, LocalTime heureFin,
                   TypeCreneau type, Dentiste dentiste) {
        if (date == null || heureDebut == null || heureFin == null) {
            throw new IllegalArgumentException("Date et heures obligatoires");
        }
        if (dentiste == null) {
            throw new IllegalArgumentException("Un créneau doit référencer un dentiste");
        }
        this.idCreneau = idCreneau;
        this.date = date;
        definirHoraires(heureDebut, heureFin);
        this.type = type == null ? TypeCreneau.NORMAL : type;
        this.dentiste = dentiste;
    }

    /** Constructeur de copie, utilisé par CreneauReserve. */
    protected Creneau(Creneau source) {
        this(source.idCreneau, source.date, source.heureDebut, source.heureFin, source.type, source.dentiste);
    }

    // ---------- Méthodes métier (UML) ----------

    /** Un créneau simple est libre. CreneauReserve redéfinit cette méthode (polymorphisme). */
    public boolean estLibre() {
        return true;
    }

    /** Vrai si la durée de l'acte tient dans le créneau. */
    public boolean contient(Acte acte) {
        return acte != null && acte.getDuree() <= getDureeMinutes();
    }

    public long getDureeMinutes() {
        return Duration.between(heureDebut, heureFin).toMinutes();
    }

    /** Vrai si les deux créneaux sont le même jour et se recouvrent. */
    public boolean chevauche(Creneau autre) {
        return date.equals(autre.date)
                && heureDebut.isBefore(autre.heureFin)
                && autre.heureDebut.isBefore(heureFin);
    }

    // ---------- Getters / setters ----------

    public int getIdCreneau() {
        return idCreneau;
    }

    public void setIdCreneau(int idCreneau) {
        this.idCreneau = idCreneau;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date == null) throw new IllegalArgumentException("Date obligatoire");
        this.date = date;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    /** Les deux heures se modifient ensemble pour garder heureDebut < heureFin. */
    public void definirHoraires(LocalTime debut, LocalTime fin) {
        if (debut == null || fin == null || !debut.isBefore(fin)) {
            throw new IllegalArgumentException("L'heure de début doit précéder l'heure de fin");
        }
        this.heureDebut = debut;
        this.heureFin = fin;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        definirHoraires(heureDebut, this.heureFin);
    }

    public void setHeureFin(LocalTime heureFin) {
        definirHoraires(this.heureDebut, heureFin);
    }

    public TypeCreneau getType() {
        return type;
    }

    public void setType(TypeCreneau type) {
        this.type = type == null ? TypeCreneau.NORMAL : type;
    }

    public boolean isUrgence() {
        return type == TypeCreneau.URGENCE;
    }

    public Dentiste getDentiste() {
        return dentiste;
    }

    public void setDentiste(Dentiste dentiste) {
        if (dentiste == null) throw new IllegalArgumentException("Dentiste obligatoire");
        this.dentiste = dentiste;
    }

    /** Deux créneaux sont égaux s'ils ont le même id (un Creneau et son CreneauReserve aussi). */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Creneau)) return false;
        return idCreneau == ((Creneau) o).idCreneau;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idCreneau);
    }

    @Override
    public String toString() {
        return date.format(FORMAT_DATE) + " " + heureDebut + "-" + heureFin
                + (isUrgence() ? " (urgence)" : "");
    }
}
