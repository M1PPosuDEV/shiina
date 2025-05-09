package dev.osunolimits.routes.ap.get;

import dev.osunolimits.modules.Shiina;
import dev.osunolimits.modules.ShiinaRoute;
import dev.osunolimits.modules.ShiinaRoute.ShiinaRequest;
import dev.osunolimits.modules.utils.ThemeLoader;
import dev.osunolimits.utils.osu.PermissionHelper;
import lombok.Data;
import spark.Request;
import spark.Response;

public class ModularSettings extends Shiina {

    @Override
    public Object handle(Request req, Response res) throws Exception {
        ShiinaRequest shiina = new ShiinaRoute().handle(req, res);
        shiina.data.put("actNav", 18);

        if(!shiina.loggedIn) {
            res.redirect("/login");
            return notFound(res, shiina);
        }

        if(!PermissionHelper.hasPrivileges(shiina.user.priv, PermissionHelper.Privileges.ADMINISTRATOR)) {
            res.redirect("/");
            return notFound(res, shiina);
        }

        shiina.data.put("themes", ThemeLoader.themes);
        return renderTemplate("ap/settings.html", shiina, res, req);
    }

    @Data
    public class ModularSettingCard {
        private String title;
    }

    @Data
    public class ModularSetting {
        private String type;
        private String key;
        private String value;
    }
    
}
