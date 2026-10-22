package ma.cabinet.model;

/**
 * Créneau occupé par un rendez-vous.
 *
 * Cycle de vie (à appliquer dans le service de réservation) :
 *   réservation : new CreneauReserve(creneauLibre), puis dentiste.remplacerCreneau(libre, reserve)
 *   annulation  : reserve.liberer(), puis dentiste.remplacerCreneau(reserve, creneauLibre)
 */
public class CreneauReserve extends Creneau {

    private static final long serialVersionUID = 1L;

    private RendezVous rendezVous;

    /** Construit la version réservée d'un créneau libre (même id, mêmes horaires, même dentiste). */
    public CreneauReserve(Creneau creneauLibre) {
        super(creneauLibre);
        if (!creneauLibre.estLibre()) {
            throw new IllegalArgumentException("Ce créneau est déjà réservé");
        }
    }

    /** Redéfinition : un créneau réservé n'est jamais libre. */
    @Override
    public boolean estLibre() {
        return false;
    }

    public RendezVous getRendezVous() {
        return rendezVous;
    }

    /** Appelé uniquement par le constructeur de RendezVous (visibilité package). */
    void setRendezVous(RendezVous rendezVous) {
        if (this.rendezVous != null && this.rendezVous != rendezVous) {
            throw new IllegalStateException("Ce créneau est déjà lié à un autre rendez-vous");
        }
        this.rendezVous = rendezVous;
    }

    /** Renvoie un Creneau libre équivalent, à remettre dans le planning après une annulation. */
    public Creneau liberer() {
        return new Creneau(getIdCreneau(), getDate(), getHeureDebut(), getHeureFin(), getType(), getDentiste());
    }

    @Override
    public String toString() {
        return super.toString() + " [réservé]";
    }
}
