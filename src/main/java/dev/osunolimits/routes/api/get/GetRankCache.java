package dev.osunolimits.routes.api.get;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.google.gson.Gson;

import dev.osunolimits.modules.ShiinaRoute.ShiinaRequest;
import dev.osunolimits.modules.utils.MySQLRoute;
import dev.osunolimits.utils.Validation;
import lombok.Data;
import spark.Request;
import spark.Response;

public class GetRankCache extends MySQLRoute {

    private final Gson GSON;

    private final String GET_RANK_CACHE = "SELECT * FROM `sh_rank_cache` WHERE `mode` = ? AND `user_id` = ?;";

    public GetRankCache() {
        GSON = new Gson();
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        ShiinaRequest shiina = getRequest();

        int mode = 0;
        if (req.queryParams("mode") != null && Validation.isNumeric(req.queryParams("mode"))) {
            mode = Integer.parseInt(req.queryParams("mode"));
        }

        Integer id = null;
        if (req.queryParams("id") != null && Validation.isNumeric(req.queryParams("id"))) {
            id = Integer.parseInt(req.queryParams("id"));
        }

        if (id == null) {
            shiina.mysql.close();
            return notFound(res, shiina);
        }

        ResultSet rankCacheResultSet = shiina.mysql.Query(GET_RANK_CACHE, mode, id);
        ArrayList<RankCacheEntry> rankCacheEntries = new ArrayList<>();
        while (rankCacheResultSet.next()) {
            RankCacheEntry rankCacheEntry = new RankCacheEntry();
            rankCacheEntry.setDate(rankCacheResultSet.getString("date"));
            rankCacheEntry.setRank(rankCacheResultSet.getInt("rank"));
            rankCacheEntries.add(rankCacheEntry);
        }
        res.type("application/json");
        shiina.mysql.close();

        if(rankCacheEntries.size() <= 2) {
            return GSON.toJson(new ArrayList<>());
        }

        return GSON.toJson(rankCacheEntries);
    }

    @Data
    public class RankCacheEntry {
        private String date;
        private int rank;
    }

}
