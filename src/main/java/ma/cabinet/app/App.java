package ma.cabinet.app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Point d'entrée JavaFX. Pour l'instant une simple fenêtre de test :
 * si elle s'ouvre, l'installation JavaFX fonctionne sur votre PC.
 * Elle sera remplacée par LoginView.
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        Label titre = new Label("Cabinet dentaire");
        titre.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        Label info = new Label("JavaFX fonctionne. Prochaine étape : LoginView.");

        VBox racine = new VBox(12, titre, info);
        racine.setPadding(new Insets(30));

        stage.setTitle("Cabinet dentaire");
        stage.setScene(new Scene(racine, 420, 160));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
