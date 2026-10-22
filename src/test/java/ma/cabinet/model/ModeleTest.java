package ma.cabinet.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ModeleTest {

    private Dentiste dentiste;
    private Patient patient;
    private Acte detartrage;
    private Acte devitalisation;
    private Creneau creneau;

    @BeforeEach
    void setUp() {
        dentiste = new Dentiste(1, "Alaoui", "Karim", "Généraliste");
        patient = new Patient("p.bennani", "1234", "AB12345", "Bennani", "Sara",
                "Rabat", "0600000000", LocalDate.of(1998, 5, 12));
        detartrage = new Acte(1, "Détartrage", 30, 300);
        devitalisation = new Acte(2, "Dévitalisation", 90, 1200);
        creneau = new Creneau(10, LocalDate.of(2026, 11, 2), LocalTime.of(9, 0), LocalTime.of(10, 0),
                TypeCreneau.NORMAL, dentiste);
        dentiste.ajouterCreneau(creneau);
    }

    @Test
    void authentification() {
        Administrateur admin = new Administrateur("admin", "admin");
        assertTrue(admin.authentifier("admin", "admin"));
        assertFalse(admin.authentifier("admin", "faux"));
        assertEquals(Role.PATIENT, patient.getRole());
    }

    @Test
    void dureeActe() {
        assertTrue(creneau.contient(detartrage));
        assertFalse(creneau.contient(devitalisation));
    }

    @Test
    void chevauchementRefuse() {
        Creneau chevauche = new Creneau(11, creneau.getDate(), LocalTime.of(9, 30), LocalTime.of(10, 30),
                TypeCreneau.NORMAL, dentiste);
        assertThrows(IllegalArgumentException.class, () -> dentiste.ajouterCreneau(chevauche));
    }

    @Test
    void cycleReservationValidationAnnulation() {
        // Réservation
        CreneauReserve reserve = new CreneauReserve(creneau);
        RendezVous rdv = new RendezVous(100, patient, detartrage, reserve);
        dentiste.remplacerCreneau(creneau, reserve);

        assertFalse(dentiste.trouverCreneau(10).estLibre());
        assertSame(rdv, reserve.getRendezVous());
        assertEquals(EtatRdv.EN_ATTENTE, rdv.getEtat());

        // Double réservation impossible
        assertThrows(IllegalArgumentException.class, () -> new CreneauReserve(reserve));

        // Validation puis annulation
        new Administrateur("admin", "admin").validerRdv(rdv);
        assertEquals(EtatRdv.VALIDE, rdv.getEtat());
        assertThrows(IllegalStateException.class, rdv::valider);

        rdv.annuler();
        dentiste.remplacerCreneau(reserve, reserve.liberer());
        assertTrue(dentiste.trouverCreneau(10).estLibre());
    }

    @Test
    void acteTropLongRefuse() {
        CreneauReserve reserve = new CreneauReserve(creneau);
        assertThrows(IllegalArgumentException.class, () -> new RendezVous(101, patient, devitalisation, reserve));
    }

    @Test
    void serialisationConserveLesLiens(@TempDir Path dossier) throws Exception {
        CreneauReserve reserve = new CreneauReserve(creneau);
        new RendezVous(100, patient, detartrage, reserve);
        dentiste.remplacerCreneau(creneau, reserve);

        File fichier = dossier.resolve("test.ser").toFile();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fichier))) {
            out.writeObject(dentiste);
        }
        Dentiste relu;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichier))) {
            relu = (Dentiste) in.readObject();
        }

        CreneauReserve creneauRelu = (CreneauReserve) relu.trouverCreneau(10);
        RendezVous rdvRelu = creneauRelu.getRendezVous();
        assertEquals("Bennani", rdvRelu.getPatient().getNom());
        assertSame(relu, rdvRelu.getCreneau().getDentiste()); // le lien circulaire est conservé
    }

    @Test
    void exportPdf(@TempDir Path dossier) throws Exception {
        CreneauReserve reserve = new CreneauReserve(creneau);
        RendezVous rdv = new RendezVous(100, patient, detartrage, reserve);

        assertTrue(rdv.genererFiche().contains("Sara Bennani"));
        File pdf = dossier.resolve("fiche.pdf").toFile();
        rdv.exporterPDF(pdf);
        assertTrue(pdf.length() > 0);
    }
}
