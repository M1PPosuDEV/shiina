package dev.osunolimits.routes.get.modular;

import java.io.IOException;
import java.io.StringWriter;

import dev.osunolimits.main.App;
import dev.osunolimits.main.WebServer;
import dev.osunolimits.modules.ShiinaRoute.ShiinaRequest;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import spark.Request;
import spark.Response;

public abstract class ShiinaModule {

    public abstract String moduleName();
    public abstract String moduleDescription();

    public abstract String handle(Request request, Response response, ShiinaRequest shiina);

    public String renderModuleTemplate(String template, ShiinaRequest shiina) {
        try {
            Template templateFree = WebServer.freemarkerCfg.getTemplate("modules/" + template);
             try (StringWriter out = new StringWriter()) {
                templateFree.process(shiina.data, out);
                return out.toString();
            } catch (TemplateException e) {
                App.log.error("Error processing module template (" + template + ")", e);
                return null;
            }
        } catch (IOException e) {
            App.log.error("Error loading module template (" + template + ")", e);
                return null;
        }
    }

    public static ShiinaModule fromRawHtml(String name, String desc, String rawHtmlFile) {
        ShiinaModule module = new ShiinaModule() {
            @Override
            public String moduleName() {
                return name;
            }

            @Override
            public String moduleDescription() {
                return desc;
            }

            @Override
            public String handle(Request request, Response response, ShiinaRequest shiina) {
                return renderModuleTemplate(rawHtmlFile, shiina);
            }
        };


        return module;
    }



    
}
