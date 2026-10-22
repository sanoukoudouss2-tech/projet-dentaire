package ma.cabinet.model;

/**
 * Secrétaire du cabinet.
 *
 * Les méthodes gererPatients(), gererDentistes(), gererActes() et gererCreneaux()
 * du diagramme UML correspondent aux écrans CRUD de l'interface : elles ne sont
 * pas codées ici, car le modèle ne doit pas dépendre de JavaFX.
 */
public class Administrateur extends Utilisateur {

    private static final long serialVersionUID = 1L;

    public Administrateur(String login, String pwd) {
        super(login, pwd);
    }

    @Override
    public Role getRole() {
        return Role.ADMINISTRATEUR;
    }

    public void validerRdv(RendezVous rdv) {
        rdv.valider();
    }

    public void annulerRdv(RendezVous rdv) {
        rdv.annuler();
    }

    @Override
    public String toString() {
        return "Administrateur " + getLogin();
    }
}
