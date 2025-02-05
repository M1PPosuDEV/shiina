package dev.osunolimits.routes.get.modular;

import java.util.ArrayList;
import java.util.List;

import dev.osunolimits.main.App;
import dev.osunolimits.modules.Shiina;
import dev.osunolimits.modules.ShiinaRoute;
import dev.osunolimits.modules.ShiinaRoute.ShiinaRequest;
import dev.osunolimits.modules.utils.SEOBuilder;

import spark.Request;
import spark.Response;

public class Home extends Shiina {

    @Override
    public Object handle(Request req, Response res) throws Exception {
        ShiinaRequest shiina = new ShiinaRoute().handle(req, res);
        shiina.data.put("actNav", 1);

 

        List<String> modulesRaw = new ArrayList<>();

        for (ShiinaModule module : ModuleRegister.getModulesForPage("home")) {
            modulesRaw.add(module.handle(req, res, shiina));
        }

         if(req.queryParams("payment") != null) {
            if(req.queryParams("payment").equals("success")) {
                shiina.data.put("info", "Payment successful");
            } else if(req.queryParams("payment").equals("cancel")) {
                shiina.data.put("error", "Payment cancelled");
            }
        }

        if(req.queryParams("login") != null && shiina.loggedIn == true) {
            shiina.data.put("info", "You have successfully logged in");
        }

        SEOBuilder seo = new SEOBuilder("Home", App.customization.get("homeDescription").toString());
        shiina.data.put("seo", seo);

        shiina.data.put("modules", modulesRaw);
        
        return renderTemplate("page.html", shiina, res, req);
    }
    
}
