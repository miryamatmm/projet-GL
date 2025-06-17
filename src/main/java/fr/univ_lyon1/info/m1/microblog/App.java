package fr.univ_lyon1.info.m1.microblog;

import fr.univ_lyon1.info.m1.microblog.config.InitialConfig;
import fr.univ_lyon1.info.m1.microblog.controller.Controller;
import fr.univ_lyon1.info.m1.microblog.model.Y;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Main class for the application (structure imposed by JavaFX).
 */
public class App extends Application {

    /**
     * With javafx, start() is called when the application is launched.
     */
    @Override
    public void start(final Stage stage) {
        final Y y = new Y();
        final InitialConfig config = new InitialConfig();

        // charger les données
        config.loadInitialData(y);
        System.out.println("Application démarrée avec les données chargées !");

        // handler pour la fermeture
        stage.setOnCloseRequest((WindowEvent event) -> {
            // sauver les données avant de fermer l'application
            config.saveApplicationState(y);
            System.out.println("État de l'application sauvegardé !");
        });

        // initialisation de la vue via le contrôleur
        Controller controller = new Controller(y, stage);
        controller.initView();
    }

    /**
     * A main method in case the user launches the application using
     * App as the main class.
     */
    public static void main(final String[] args) {
        Application.launch(args);
    }
}
