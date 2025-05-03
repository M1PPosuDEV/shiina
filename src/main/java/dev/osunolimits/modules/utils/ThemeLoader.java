package dev.osunolimits.modules.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import ch.qos.logback.classic.Logger;
import dev.osunolimits.main.App;
import dev.osunolimits.models.Theme;
import dev.osunolimits.utils.Validation;
import net.logicsquad.minifier.MinificationException;
import net.logicsquad.minifier.Minifier;
import net.logicsquad.minifier.css.CSSMinifier;

public class ThemeLoader {

    public static String generatedIdent;

    public static Logger logger = (Logger) LoggerFactory.getLogger("ShiinaThemeLoader");
    public static ArrayList<Theme> themes = new ArrayList<Theme>();
    public static Theme currentTheme;

    public static void loadThemes() {
        File directory = new File("themes/");

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {

                        File f = new File("themes/" + file.getName() + "/theme.yml");
                        if (f.exists() && !f.isDirectory()) {
                            Theme theme = Theme.loadTheme(f);
                            if (theme != null) {
                                themes.add(theme);
                                if (App.customization.get("theme").equals(theme.getName())) {
                                    currentTheme = theme;
                                    if (!theme.isIncluded()) {
                                       notIncluded(theme);
                                    }
                                }
                                logger.info("Loaded theme: [" + file.getName() + "]");
                            }

                        } else {
                            logger.error("Theme [" + file.getName() + "] does not have a theme.yml file.");
                        }
                    }
                }
            } else {
                logger.warn("Themes directory is empty.");
            }
        } else {
            logger.error("Themes directory does not exist.");
        }

    }

    public static void notIncluded(Theme theme) {
        logger.info("Loading style.css for theme [" + theme.getName() + "]");

        generatedIdent = Validation.randomString(5);
        
        File style = new File("themes/" + theme.getName() + "/style.css");
        // Copy style to /static/css/theme.css wiuth overwrite
        try {
            File[] files = new File("static/css/").listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().startsWith("theme-")) {
                        file.delete();
                    }
                }
            }

            Reader input = new FileReader(style);
            Minifier min = new CSSMinifier(input);
            FileWriter output = new FileWriter("static/css/theme-"+generatedIdent+".css");
            min.minify(output);
        } catch (IOException e) {
            logger.error("Failed to copy style-{}.css for theme [" + theme.getName() + "]", generatedIdent);
        } catch (MinificationException e) {
            logger.error("Failed to minify style{}-.css for theme [" + theme.getName() + "]", generatedIdent);
        }
    }

    public static void selectTheme(String theme) {
        for (Theme t : themes) {
            if (t.getName().equals(theme)) {
                App.customization.put("theme", t.getName());
                Yaml yaml = new Yaml();
                String newCustomizations = yaml.dumpAsMap(App.customization);
                currentTheme = t;
                if(!t.isIncluded()) {
                    notIncluded(t);
                }
                try {
                    Files.writeString(Paths.get(".config/customization.yml"), newCustomizations);
                } catch (IOException e) {
                    logger.error("Failed to apply theme change to customizations.yml");
                }
                return;
            }
        }
    }
}
