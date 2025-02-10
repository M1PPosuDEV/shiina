package dev.osunolimits.routes.ap.get;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dev.osunolimits.modules.Shiina;
import dev.osunolimits.modules.ShiinaRoute;
import dev.osunolimits.modules.ShiinaRoute.ShiinaRequest;
import dev.osunolimits.utils.osu.PermissionHelper;
import lombok.Data;
import spark.Request;
import spark.Response;

public class Multiaccounts extends Shiina {

    private final String MULTI_SQL = "SELECT `user`, `u`.`name` AS `user_name`, `target`, `t`.`name` AS `target_name`, `d`.`detection`, `d`.`score` FROM `sh_detections` AS `d` INNER JOIN `users` AS `u` ON `u`.`id` = `d`.`user` INNER JOIN `users` AS `t` ON `t`.`id` = `d`.`target` ORDER BY `d`.`detection` DESC LIMIT ? OFFSET ?;";

    @Override
    public Object handle(Request req, Response res) throws Exception {
        ShiinaRequest shiina = new ShiinaRoute().handle(req, res);
        shiina.data.put("actNav", 11);

        if(!shiina.loggedIn) {
            res.redirect("/login");
            return notFound(res, shiina);
        }

        if(!PermissionHelper.hasPrivileges(shiina.user.priv, PermissionHelper.Privileges.MODERATOR)) {
            res.redirect("/");
            return notFound(res, shiina);
        }
        int page = 0;
        if(req.queryParams("page") != null) {
            page = Integer.parseInt(req.queryParams("page"));
        }

        int offset = page * 10;

        ResultSet statsResultSet = shiina.mysql.Query(MULTI_SQL, 11, offset);
        List<MultiAccountDetection> multiAccountDetections = new ArrayList<>();
        boolean hasNextPage = false;
        while (statsResultSet.next()) {
            MultiAccountDetection multiAccountDetection = new MultiAccountDetection();
            multiAccountDetection.setUser1(statsResultSet.getInt("user"));
            multiAccountDetection.setUser2(statsResultSet.getInt("target"));
            multiAccountDetection.setUser1_name(statsResultSet.getString("user_name"));
            multiAccountDetection.setUser2_name(statsResultSet.getString("target_name"));
            multiAccountDetection.setLevel(statsResultSet.getInt("score"));
            multiAccountDetection.setDetection(statsResultSet.getString("detection"));

            if(multiAccountDetections.size() == 10) {
                hasNextPage = true;
                continue;
            }
            multiAccountDetections.add(multiAccountDetection);
           
        }
        shiina.data.put("multiAccountDetections", multiAccountDetections);
        shiina.data.put("hasNextPage", hasNextPage);
        shiina.data.put("page", page);
        return renderTemplate("ap/multiaccs.html", shiina, res, req);
    }

    @Data
    public class MultiAccountDetection {
        private int user1;
        private int user2;
        private String user1_name;
        private String user2_name;
        private int level;
        private String detection;
    }
    
}
