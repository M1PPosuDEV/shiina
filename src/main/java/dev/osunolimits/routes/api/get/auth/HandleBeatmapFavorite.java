package dev.osunolimits.routes.api.get.auth;

import java.sql.ResultSet;

import dev.osunolimits.modules.Shiina;
import dev.osunolimits.modules.ShiinaRoute;
import dev.osunolimits.modules.ShiinaRoute.ShiinaRequest;
import dev.osunolimits.plugins.events.actions.OnBeatmapFavoriteEvent;
import dev.osunolimits.utils.Validation;
import spark.Request;
import spark.Response;

public class HandleBeatmapFavorite extends Shiina {

    private final String BEATMAP_FAVORITE_QUERY = "SELECT * FROM `favourites` WHERE `userid` = ? AND `setid` = ?";
    private final String INSERT_FAVORITE_EXEC = "INSERT INTO `favourites`(`userid`, `setid`, `created_at`) VALUES (?, ?, ?)";

    @Override
    public Object handle(Request req, Response res) throws Exception {

        ShiinaRequest shiina = new ShiinaRoute().handle(req, res);

        if (shiina.user == null) {
            return notFound(res, shiina);
        }

        String beatmapSetId = null;
        if (req.queryParams("set_id") != null && Validation.isNumeric(req.queryParams("set_id"))) {
            beatmapSetId = req.queryParams("set_id");
        }

        int beatmapSetIdNumber = Integer.parseInt(beatmapSetId);

        String beatmapId = null;
        if (req.queryParams("id") != null) {
            beatmapId = req.queryParams("id");
        }

        if (beatmapSetId == null || beatmapId == null) {
            return notFound(res, shiina);
        }

        ResultSet checkFavoriResultSet = shiina.mysql.Query(BEATMAP_FAVORITE_QUERY, shiina.user.id, beatmapSetId);
        if (checkFavoriResultSet.next()) {
            shiina.mysql.Exec("DELETE FROM `favourites` WHERE `userid` = ? AND `setid` = ?", shiina.user.id,
                    beatmapSetId);
            new OnBeatmapFavoriteEvent(shiina.user.id, beatmapSetIdNumber, true).callListeners();
        } else {
            shiina.mysql.Exec(INSERT_FAVORITE_EXEC, shiina.user.id, beatmapSetId, System.currentTimeMillis() / 1000);
            new OnBeatmapFavoriteEvent(shiina.user.id, beatmapSetIdNumber, false).callListeners();
        }

        return redirect(res, shiina, "/b/" + beatmapId);
    }

}
