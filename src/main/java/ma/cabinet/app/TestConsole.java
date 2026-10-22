package ma.cabinet.app;

import ma.cabinet.model.*;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;

/** Test en console du modèle (recommandé par le sujet en semaine 1). */
public class TestConsole {

    public static void main(String[] args) throws Exception {
        Dentiste dentiste = new Dentiste(1, "Alaoui", "Karim", "Généraliste");
        Patient patient = new Patient("p.bennani", "1234", "AB12345", "Bennani", "Sara",
                "Rabat", "0600000000", LocalDate.of(1998, 5, 12));
        Acte acte = new Acte(1, "Détartrage", 30, 300);

        LocalDate lundi = LocalDate.now().with(java.time.DayOfWeek.MONDAY).plusWeeks(1);
        Creneau c1 = new Creneau(1, lundi, LocalTime.of(9, 0), LocalTime.of(10, 0), TypeCreneau.NORMAL, dentiste);
        Creneau c2 = new Creneau(2, lundi, LocalTime.of(10, 0), LocalTime.of(11, 0), TypeCreneau.URGENCE, dentiste);
        dentiste.ajouterCreneau(c1);
        dentiste.ajouterCreneau(c2);

        System.out.println("Planning avant réservation :");
        dentiste.getPlanning().forEach(c -> System.out.println("  " + c + " libre=" + c.estLibre()));

        CreneauReserve reserve = new CreneauReserve(c1);
        RendezVous rdv = new RendezVous(1, patient, acte, reserve);
        dentiste.remplacerCreneau(c1, reserve);

        System.out.println("\nPlanning après réservation :");
        dentiste.getPlanning().forEach(c -> System.out.println("  " + c + " libre=" + c.estLibre()));

        new Administrateur("admin", "admin").validerRdv(rdv);
        System.out.println("\n" + rdv.genererFiche());

        File pdf = new File("fiche-test.pdf");
        rdv.exporterPDF(pdf);
        System.out.println("\nPDF écrit : " + pdf.getAbsolutePath());
    }
}
