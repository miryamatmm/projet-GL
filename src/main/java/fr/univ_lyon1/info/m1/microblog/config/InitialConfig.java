package fr.univ_lyon1.info.m1.microblog.config;

import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fr.univ_lyon1.info.m1.microblog.dto.YData;
import fr.univ_lyon1.info.m1.microblog.model.Y;

/**
 * Initial Configuration class.
 */
public class InitialConfig {

    private static final String CONFIG_FILE = "config.json"; // nom du fichier de configuration
    private static final String CONFIG_DIR = "config"; // répertoire de configuration

    /**
     * Load Initial Data.
     */
    public void loadInitialData(final Y application) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            File configFile = new File(CONFIG_DIR, CONFIG_FILE);

            // on vérifie si le fichier existe
            if (!configFile.exists()) {
                System.err.println("Fichier de configuration non trouvé dans le répertoire "
                        + CONFIG_DIR);
                System.err.println(configFile);
                return;
            }

            // on lit et on importe les données
            YData data = objectMapper.readValue(configFile, YData.class);
            application.importData(data);

            System.out.println("Configuration initiale chargée depuis " + configFile);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des données : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Save the app state.
     */
    public void saveApplicationState(final Y application) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            // exporte les données de l'application
            YData data = application.exportData();

            File configFile = new File(CONFIG_DIR, CONFIG_FILE); // Utilisation d'un chemin relatif

            // on s'assure que le répertoire existe, sinon le créer
            if (!configFile.getParentFile().exists()) {
                configFile.getParentFile().mkdirs();
            }

            // écrit les données dans le fichier config
            objectMapper.writeValue(configFile, data);

            System.out.println("Configuration sauvegardée dans " + configFile);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des données : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
