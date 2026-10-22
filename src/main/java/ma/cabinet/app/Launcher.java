package ma.cabinet.app;

/**
 * À lancer depuis IntelliJ (clic droit > Run).
 * Lancer App directement provoque l'erreur
 * "JavaFX runtime components are missing" sans module-info : ce lanceur l'évite.
 */
public class Launcher {
    public static void main(String[] args) {
        App.main(args);
    }
}
