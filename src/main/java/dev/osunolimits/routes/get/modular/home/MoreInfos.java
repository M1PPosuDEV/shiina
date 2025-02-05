package dev.osunolimits.routes.get.modular.home;

import java.sql.ResultSet;
import java.sql.SQLException;

import dev.osunolimits.api.BanchoStats;
import dev.osunolimits.api.BanchoStats.CustomCountResponse;
import dev.osunolimits.main.App;
import dev.osunolimits.modules.ShiinaRoute.ShiinaRequest;
import dev.osunolimits.routes.get.modular.ShiinaModule;
import spark.Request;
import spark.Response;

public class MoreInfos extends ShiinaModule {

    private final String STATS_SQL = "SELECT (SELECT COUNT(`id`) FROM `ingame_logins`) AS `total_logins`, (SELECT COUNT(`id`) FROM `ingame_logins` WHERE DATE(`datetime`) = CURDATE()) AS `logins_today`,(SELECT MAX(`datetime`) FROM `startups`) AS `startup`, (SELECT COUNT(`id`) FROM `users` WHERE (`priv` & (4096 | 8192 | 16384 | 2048 | 1024)) > 0) AS `staff_count`;";

    @Override
    public String moduleName() {
        return "MoreInfos";
    }

    @Override
    public String moduleDescription() {
        return "MoreInfos for the home page";
    }

    @Override
    public String handle(Request request, Response response, ShiinaRequest shiina) {
        BanchoStats banchoStats = new BanchoStats();
        shiina.data.put("playerCount", banchoStats.getPlayerCount());

        CustomCountResponse customCountResponse = banchoStats.new CustomCountResponse();

        ResultSet statsResultSet = shiina.mysql.Query(STATS_SQL);

        try {
            if(!statsResultSet.next()) {
                return null;
            }
            customCountResponse.setTotalLogins(statsResultSet.getInt("total_logins"));
            customCountResponse.setLoginsToday(statsResultSet.getInt("logins_today"));
            customCountResponse.setStaff(statsResultSet.getInt("staff_count"));
            shiina.data.put("startup", statsResultSet.getString("startup"));

        } catch (SQLException e) {
            App.log.error("Error getting stats", e);
        }

        shiina.data.put("customCount", customCountResponse);

        return renderModuleTemplate("home/moreinfos.html", shiina);
    }

}
