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

public class BigHeader extends ShiinaModule{

    private final String STATS_SQL = "SELECT (SELECT COUNT(`id`) FROM `scores`) AS `scores`,(SELECT COUNT(`id`) FROM `maps`) AS `maps`, (SELECT COUNT(`id`) FROM `clans`) AS `clans`;";

    @Override
    public String moduleName() {
        return "BigHeader";
    }

    @Override
    public String moduleDescription() {
       return "A big header for the home page";
    }

    @Override
    public String handle(Request request, Response response, ShiinaRequest shiina) {
        BanchoStats banchoStats = new BanchoStats();
        shiina.data.put("playerCount", banchoStats.getPlayerCount());
        
        CustomCountResponse customCountResponse = banchoStats.new CustomCountResponse();

        ResultSet statsResultSet = shiina.mysql.Query(STATS_SQL);

        try {
            if (statsResultSet.next()) {
                customCountResponse.setPlays(statsResultSet.getInt("scores"));
                customCountResponse.setBeatmaps(statsResultSet.getInt("maps"));
                customCountResponse.setClans(statsResultSet.getInt("clans"));
            }
        } catch (SQLException e) {
            App.log.error("Error getting stats", e);
        }

        shiina.data.put("customCount", customCountResponse);
        
        return renderModuleTemplate("home/bigheader.html",shiina);
    }
    


}
