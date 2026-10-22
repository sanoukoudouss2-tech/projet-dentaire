package ma.cabinet.model;

import java.time.LocalDate;

/** Patient du cabinet. Il peut se connecter pour réserver un rendez-vous. */
public class Patient extends Utilisateur {

    private static final long serialVersionUID = 1L;

    private String cin;
    private String nom;
    private String prenom;
    private String adresse;
    private String telephone;
    private LocalDate dateNaissance;

    public Patient(String login, String pwd, String cin, String nom, String prenom,
                   String adresse, String telephone, LocalDate dateNaissance) {
        super(login, pwd);
        setCin(cin);
        setNom(nom);
        setPrenom(prenom);
        setAdresse(adresse);
        setTelephone(telephone);
        setDateNaissance(dateNaissance);
    }

    @Override
    public Role getRole() {
        return Role.PATIENT;
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    // ---------- Getters / setters ----------

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        if (cin == null || cin.isBlank()) {
            throw new IllegalArgumentException("Le CIN est obligatoire");
        }
        this.cin = cin.trim().toUpperCase();
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse == null ? "" : adresse.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? "" : telephone.trim();
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        if (dateNaissance != null && dateNaissance.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La date de naissance ne peut pas être dans le futur");
        }
        this.dateNaissance = dateNaissance;
    }

    /** Utilisé tel quel par les ComboBox et ListView JavaFX. */
    @Override
    public String toString() {
        return getNomComplet() + " (" + cin + ")";
    }
}
