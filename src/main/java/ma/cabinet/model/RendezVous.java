package ma.cabinet.model;

import ma.cabinet.util.FichePdf;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Rendez-vous : un patient, un acte, un créneau réservé.
 */
public class RendezVous implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String NOM_CABINET = "Cabinet Dentaire";
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm");

    private int idRdv;
    private LocalDateTime dateReservation;
    private LocalDateTime dateRdv;
    private EtatRdv etat;
    private Patient patient;
    private Acte acte;
    private CreneauReserve creneau;

    public RendezVous(int idRdv, Patient patient, Acte acte, CreneauReserve creneau) {
        if (patient == null || acte == null || creneau == null) {
            throw new IllegalArgumentException("Patient, acte et créneau sont obligatoires");
        }
        if (!creneau.contient(acte)) {
            throw new IllegalArgumentException("L'acte (" + acte.getDuree() + " min) ne tient pas dans le créneau ("
                    + creneau.getDureeMinutes() + " min)");
        }
        this.idRdv = idRdv;
        this.patient = patient;
        this.acte = acte;
        this.creneau = creneau;
        this.dateReservation = LocalDateTime.now();
        this.dateRdv = LocalDateTime.of(creneau.getDate(), creneau.getHeureDebut());
        this.etat = EtatRdv.EN_ATTENTE;
        creneau.setRendezVous(this);   // lien dans les deux sens
    }

    // ---------- Méthodes métier (UML) ----------

    public void valider() {
        if (etat != EtatRdv.EN_ATTENTE) {
            throw new IllegalStateException("Seul un rendez-vous en attente peut être validé (état actuel : " + etat + ")");
        }
        etat = EtatRdv.VALIDE;
    }

    /** Passe le RDV à l'état annulé. Le service doit ensuite remettre creneau.liberer() dans le planning. */
    public void annuler() {
        if (etat == EtatRdv.ANNULE) {
            throw new IllegalStateException("Ce rendez-vous est déjà annulé");
        }
        etat = EtatRdv.ANNULE;
    }

    public boolean estActif() {
        return etat != EtatRdv.ANNULE;
    }

    /** Texte de la fiche : sert à la fenêtre, à l'impression et au PDF. */
    public String genererFiche() {
        return String.join("\n",
                NOM_CABINET,
                "Fiche de rendez-vous n° " + idRdv,
                "",
                "Patient  : " + patient.getPrenom() + " " + patient.getNom(),
                "Dentiste : " + creneau.getDentiste().getNomComplet(),
                "Acte     : " + acte.getLibelle(),
                "Date     : " + dateRdv.format(FORMAT),
                "Durée    : " + acte.getDuree() + " min",
                "Tarif    : " + String.format("%.2f DH", acte.getTarif()),
                "État     : " + etat);
    }

    public void exporterPDF(File fichier) throws IOException {
        FichePdf.exporter(genererFiche(), fichier);
    }

    // ---------- Getters / setters ----------

    public int getIdRdv() {
        return idRdv;
    }

    public void setIdRdv(int idRdv) {
        this.idRdv = idRdv;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public LocalDateTime getDateRdv() {
        return dateRdv;
    }

    public EtatRdv getEtat() {
        return etat;
    }

    /** Pas de setEtat() public : l'état change seulement via valider() et annuler(). */

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        if (patient == null) throw new IllegalArgumentException("Patient obligatoire");
        this.patient = patient;
    }

    public Acte getActe() {
        return acte;
    }

    public void setActe(Acte acte) {
        if (acte == null || !creneau.contient(acte)) {
            throw new IllegalArgumentException("Cet acte ne tient pas dans le créneau");
        }
        this.acte = acte;
    }

    public CreneauReserve getCreneau() {
        return creneau;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RendezVous)) return false;
        return idRdv == ((RendezVous) o).idRdv;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idRdv);
    }

    @Override
    public String toString() {
        return "RDV " + idRdv + " - " + patient.getNomComplet() + " - " + acte.getLibelle()
                + " - " + dateRdv.format(FORMAT) + " - " + etat;
    }
}
