package ma.cabinet.model;

/** Type d'un créneau. Un créneau URGENCE s'affiche en orange dans le planning. */
public enum TypeCreneau {
    NORMAL("Normal"),
    URGENCE("Urgence");

    private final String libelle;

    TypeCreneau(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
