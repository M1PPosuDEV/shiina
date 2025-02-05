package dev.osunolimits.routes.api.get;

import java.sql.ResultSet;
import java.util.ArrayList;

import dev.osunolimits.modules.ShiinaRoute.ShiinaRequest;
import dev.osunolimits.modules.utils.MySQLRoute;
import dev.osunolimits.utils.Validation;
import dev.osunolimits.utils.osu.OsuConverter;
import lombok.Data;
import spark.Request;
import spark.Response;

public class GetRankCache extends MySQLRoute {

    private final String GET_RANK_CACHE = "SELECT * FROM `sh_rank_cache` WHERE `mode` = ? AND `user_id` = ?;";

    @Override
    public Object handle(Request req, Response res) throws Exception {
        ShiinaRequest shiina = getRequest();
        ShiinaAPIHandler shiinaAPIHandler = new ShiinaAPIHandler();

        int mode = 0;
        if (OsuConverter.checkForValidMode(req.queryParams("mode"))) {
            mode = Integer.parseInt(req.queryParams("mode"));
        }else {
            shiinaAPIHandler.addRequiredParameter("mode", "int", "missing or invalid");
        }

        Integer id = null;
        if (req.queryParams("id") != null && Validation.isNumeric(req.queryParams("id"))) {
            id = Integer.parseInt(req.queryParams("id"));
        }else {
            shiinaAPIHandler.addRequiredParameter("id", "int", "missing");
        }

        if (shiinaAPIHandler.hasIssues()) {
            return shiinaAPIHandler.renderIssues(shiina, res);
        }

        ResultSet rankCacheResultSet = shiina.mysql.Query(GET_RANK_CACHE, mode, id);
        ArrayList<RankCacheEntry> rankCacheEntries = new ArrayList<>();
        while (rankCacheResultSet.next()) {
            RankCacheEntry rankCacheEntry = new RankCacheEntry();
            rankCacheEntry.setDate(rankCacheResultSet.getString("date"));
            rankCacheEntry.setRank(rankCacheResultSet.getInt("rank"));
            rankCacheEntries.add(rankCacheEntry);
        }
      

        if(rankCacheEntries.size() <= 2) {
            return shiinaAPIHandler.renderJSON(new ArrayList<>(), shiina, res);
        }

        return shiinaAPIHandler.renderJSON(rankCacheEntries, shiina, res);
    }

    @Data
    public class RankCacheEntry {
        private String date;
        private int rank;
    }

}
