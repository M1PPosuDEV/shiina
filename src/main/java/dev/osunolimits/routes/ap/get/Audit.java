package dev.osunolimits.routes.ap.get;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dev.osunolimits.models.AuditLogEntry;
import dev.osunolimits.modules.Shiina;
import dev.osunolimits.modules.ShiinaRoute;
import dev.osunolimits.modules.ShiinaRoute.ShiinaRequest;
import dev.osunolimits.utils.osu.PermissionHelper;
import spark.Request;
import spark.Response;

public class Audit extends Shiina {

    private final String AUDIT_SQL = "SELECt `a`.`id`, `action`, `user_id`, `u`.`name` AS `user_name`, `target_id`, `t`.`name` AS `target_name`, `status`, `reason`, `mode`, `privs` FROM `sh_audit` AS `a` INNER JOIN `users` AS `u` ON `u`.`id` = `a`.`user_id` INNER JOIN `users` AS `t` ON `t`.`id` = `a`.`target_id` ORDER BY `a`.`id` DESC LIMIT ? OFFSET ?;";

    @Override
    public Object handle(Request req, Response res) throws Exception {
        ShiinaRequest shiina = new ShiinaRoute().handle(req, res);
        shiina.data.put("actNav", 17);

        if (!shiina.loggedIn) {
            res.redirect("/login");
            return notFound(res, shiina);
        }

        if (!PermissionHelper.hasPrivileges(shiina.user.priv, PermissionHelper.Privileges.ADMINISTRATOR)) {
            res.redirect("/");
            return notFound(res, shiina);
        }
        int page = 0;
        if (req.queryParams("page") != null) {
            page = Integer.parseInt(req.queryParams("page"));
        }

        int offset = page * 10;

        ResultSet statsResultSet = shiina.mysql.Query(AUDIT_SQL, 11, offset);
        List<AuditLogEntry> auditLogEntries = new ArrayList<>();
        boolean hasNextPage = false;
        while (statsResultSet.next()) {
            AuditLogEntry entry = new AuditLogEntry();
            entry.setAction(statsResultSet.getString("action"));
            entry.setUserId(statsResultSet.getInt("user_id"));
            entry.setUserName(statsResultSet.getString("user_name"));
            entry.setTargetId(statsResultSet.getInt("target_id"));
            entry.setTargetName(statsResultSet.getString("target_name"));
            entry.setStatus(statsResultSet.getInt("status"));
            entry.setReason(statsResultSet.getString("reason"));
            entry.setMode(statsResultSet.getInt("mode"));
            entry.setPrivs(statsResultSet.getString("privs"));
            auditLogEntries.add(entry);

        }

        if (auditLogEntries.size() == 11) {
            hasNextPage = true;
            auditLogEntries.remove(10);
        }

        shiina.data.put("auditLog", auditLogEntries);
        shiina.data.put("hasNextPage", hasNextPage);
        shiina.data.put("page", page);
        return renderTemplate("ap/audit.html", shiina, res, req);
    }

}
