package dev.osunolimits.monetization;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import dev.osunolimits.main.App;
import dev.osunolimits.monetization.models.KofiConfigModel;
import dev.osunolimits.monetization.models.MonetizationConfigModel;

public class MonetizationConfig {

    public final boolean ENABLED;
    public static KofiConfigModel KOFI_CONFIG;

    public MonetizationConfig() {
        File monetizationFile = new File("data/monetization.json");

        // Ensure the directory exists
        createDirectoryIfNotExists("data/monetization");

        if (!monetizationFile.exists()) {
            ENABLED = false;
            createDefaultMonetizationConfig(monetizationFile);
        } else {
            ENABLED = loadMonetizationConfig(monetizationFile);
        }

        if (ENABLED) {
            boolean exEnabled = App.env.get("EXPUBSUBS").equals("true");
            if (!exEnabled) {
                App.log.error(
                        "Monetization is enabled but EXPUBSUBS is not. Please enable EXPUBSUBS in your .env file.");
                System.exit(0);
            }

            createDirectoryIfNotExists("data/monetization");
            File kofiConfig = new File("data/monetization/kofi.json");
            if (!kofiConfig.exists()) {

                try {
                    kofiConfig.createNewFile();
                    KofiConfigModel model = new KofiConfigModel();
                    KOFI_CONFIG = model;
                    String fileContent = new Gson().toJson(model);
                    try (FileWriter writer = new FileWriter(kofiConfig)) {
                        writer.write(fileContent);
                        App.log.info("Created default Kofi configuration at {}", kofiConfig.getPath());
                    } catch (IOException e) {
                        App.log.error("Failed to create default Kofi configuration", e);
                    }
                } catch (IOException e) {
                    App.log.error("Failed to create Kofi configuration file", e);
                }
            } else {
                try {
                    KOFI_CONFIG = new Gson().fromJson(Files.readString(kofiConfig.toPath()), KofiConfigModel.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Method to return the StripeConfigModel
    private void createDirectoryIfNotExists(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            boolean dirCreated = dir.mkdirs();
            if (dirCreated) {
                App.log.info("Created directory at {}", path);
            } else {
                App.log.error("Failed to create directory at {}", path);
            }
        }
    }

    private void createDefaultMonetizationConfig(File monetizationFile) {
        MonetizationConfigModel model = new MonetizationConfigModel();
        model.setEnabled(false);
        String fileContent = new Gson().toJson(model);

        try (FileWriter writer = new FileWriter(monetizationFile)) {
            writer.write(fileContent);
            App.log.info("Created default monetization configuration at {}", monetizationFile.getPath());
        } catch (IOException e) {
            App.log.error("Failed to create default monetization configuration", e);
        }
    }

    private boolean loadMonetizationConfig(File monetizationFile) {
        try {
            String fileContent = Files.readString(monetizationFile.toPath());
            return new Gson().fromJson(fileContent, MonetizationConfigModel.class).isEnabled();
        } catch (IOException e) {
            App.log.error("Failed to read monetization configuration", e);
            return false;
        }
    }

}
