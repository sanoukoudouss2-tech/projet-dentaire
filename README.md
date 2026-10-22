# Gestion d'un cabinet dentaire

Projet Java 17 + JavaFX 21 + Apache PDFBox 3, construit avec Maven.

## Lancer le projet

Prérequis : JDK 17 ou plus récent. Maven est inclus dans IntelliJ.

1. IntelliJ : File > Open > choisir le dossier (le `pom.xml` est détecté, attendre la fin du téléchargement des dépendances).
2. Fenêtre de test JavaFX : clic droit sur `ma.cabinet.app.Launcher` > Run.
   Ne lancez pas `App` directement : vous auriez l'erreur "JavaFX runtime components are missing".
3. Test console du modèle : clic droit sur `ma.cabinet.app.TestConsole` > Run (écrit aussi `fiche-test.pdf`).
4. Tests JUnit : clic droit sur `src/test/java` > Run All Tests, ou `mvn test`.

En ligne de commande : `mvn javafx:run`.

## Organisation

```
ma.cabinet.model   les 8 classes du sujet + enums Role, TypeCreneau, EtatRdv
ma.cabinet.util    FichePdf (export PDF de la fiche)
ma.cabinet.app     App (JavaFX), Launcher, TestConsole
```

À ajouter : `ma.cabinet.service` (CabinetData, AuthService, ReservationService) et `ma.cabinet.view` (écrans JavaFX).

## Règles de conception à respecter

- Toutes les classes du modèle sont `Serializable` : sauvegarder un seul objet racine conserve tous les liens.
- **Réservation** : `new CreneauReserve(creneau)`, puis `new RendezVous(id, patient, acte, reserve)`, puis `dentiste.remplacerCreneau(creneau, reserve)`.
- **Annulation** : `rdv.annuler()`, puis `dentiste.remplacerCreneau(reserve, reserve.liberer())`.
- Les ids sont fournis par l'appelant (le futur `CabinetData` tiendra les compteurs).
- L'état d'un RDV ne change que par `valider()` et `annuler()`. Pas de `getPwd()` public.
- Couleur dans la grille : `!estLibre()` rouge, sinon `isUrgence()` orange, sinon vert, sélection bleue.
