package ma.cabinet.model;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dentiste du cabinet. Il possède (1..*) ses créneaux : c'est son planning.
 */
public class Dentiste implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idDentiste;
    private String nom;
    private String prenom;
    private String specialite;
    private final List<Creneau> planning = new ArrayList<>();

    public Dentiste(int idDentiste, String nom, String prenom, String specialite) {
        this.idDentiste = idDentiste;
        setNom(nom);
        setPrenom(prenom);
        setSpecialite(specialite);
    }

    // ---------- Gestion du planning ----------

    /** Liste en lecture seule, triée par date puis heure de début. */
    public List<Creneau> getPlanning() {
        List<Creneau> copie = new ArrayList<>(planning);
        copie.sort(Comparator.comparing(Creneau::getDate).thenComparing(Creneau::getHeureDebut));
        return Collections.unmodifiableList(copie);
    }

    /** Créneaux de la semaine (lundi à dimanche) qui contient le jour donné. Sert à la grille. */
    public List<Creneau> getPlanningSemaine(LocalDate jourDeLaSemaine) {
        LocalDate lundi = jourDeLaSemaine.with(DayOfWeek.MONDAY);
        LocalDate dimanche = lundi.plusDays(6);
        return getPlanning().stream()
                .filter(c -> !c.getDate().isBefore(lundi) && !c.getDate().isAfter(dimanche))
                .collect(Collectors.toList());
    }

    /** Ajoute un créneau en refusant les chevauchements avec un créneau existant. */
    public void ajouterCreneau(Creneau creneau) {
        if (creneau.getDentiste() != this) {
            throw new IllegalArgumentException("Ce créneau appartient à un autre dentiste");
        }
        for (Creneau existant : planning) {
            if (existant.getIdCreneau() == creneau.getIdCreneau()) {
                throw new IllegalArgumentException("Un créneau avec l'id " + creneau.getIdCreneau() + " existe déjà");
            }
            if (existant.chevauche(creneau)) {
                throw new IllegalArgumentException("Ce créneau chevauche le créneau " + existant);
            }
        }
        planning.add(creneau);
    }

    /** Supprime un créneau libre. Un créneau réservé doit d'abord être libéré (annulation du RDV). */
    public boolean supprimerCreneau(Creneau creneau) {
        if (!creneau.estLibre()) {
            throw new IllegalStateException("Impossible de supprimer un créneau réservé");
        }
        return planning.remove(creneau);
    }

    /**
     * Remplace un créneau par un autre de même id.
     * Utilisé par le service de réservation : Creneau -> CreneauReserve à la réservation,
     * CreneauReserve -> Creneau à l'annulation.
     */
    public void remplacerCreneau(Creneau ancien, Creneau nouveau) {
        if (ancien.getIdCreneau() != nouveau.getIdCreneau()) {
            throw new IllegalArgumentException("Les deux créneaux doivent avoir le même id");
        }
        int index = planning.indexOf(ancien);
        if (index < 0) {
            throw new IllegalArgumentException("Créneau introuvable dans le planning de " + this);
        }
        planning.set(index, nouveau);
    }

    public Creneau trouverCreneau(int idCreneau) {
        for (Creneau c : planning) {
            if (c.getIdCreneau() == idCreneau) return c;
        }
        return null;
    }

    public String getNomComplet() {
        return "Dr " + prenom + " " + nom;
    }

    // ---------- Getters / setters ----------

    public int getIdDentiste() {
        return idDentiste;
    }

    public void setIdDentiste(int idDentiste) {
        this.idDentiste = idDentiste;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        this.nom = nom.trim();
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        if (prenom == null || prenom.isBlank()) {
            throw new IllegalArgumentException("Le prénom est obligatoire");
        }
        this.prenom = prenom.trim();
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = (specialite == null || specialite.isBlank()) ? "Généraliste" : specialite.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dentiste)) return false;
        return idDentiste == ((Dentiste) o).idDentiste;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idDentiste);
    }

    @Override
    public String toString() {
        return getNomComplet() + " - " + specialite;
    }
}
