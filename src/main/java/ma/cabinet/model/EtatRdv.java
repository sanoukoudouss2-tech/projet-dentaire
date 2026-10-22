package ma.cabinet.model;

/** États possibles d'un rendez-vous. */
public enum EtatRdv {
    EN_ATTENTE("En attente"),
    VALIDE("Validé"),
    ANNULE("Annulé");

    private final String libelle;

    EtatRdv(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
