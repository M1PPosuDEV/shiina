package dev.osunolimits.modules.utils;

import dev.osunolimits.common.MySQL;
import dev.osunolimits.plugins.events.admin.OnAuditLogEvent;
import dev.osunolimits.routes.ap.api.PubSubHandler.MessageType;
import dev.osunolimits.utils.Auth;

public class AuditLogger {
    private MySQL mysql;
    private String type;

    public AuditLogger(MySQL mysql, MessageType type) {
        this.mysql = mysql;
        this.type = type.toString();
    }

    public void rankMap(Auth.User user, int beatmapId, int status) {
        mysql.Exec("INSERT INTO `sh_audit`(`action`, `user_id`, `target_id`, `status`) VALUES (?,?,?,?);", type, user.id, beatmapId, status);
        fire(user.id, beatmapId, null, null, status, null);
    }

    public void restrictUser(Auth.User user, int userId, String reason) {
        mysql.Exec("INSERT INTO `sh_audit`(`action`, `user_id`, `target_id`, `reason`) VALUES (?,?,?,?);", type, user.id, userId, reason);
        fire(user.id, userId, null, reason, null, null);
    }

    public void unrestrictUser(Auth.User user, int userId, String reason) {
        mysql.Exec("INSERT INTO `sh_audit`(`action`, `user_id`, `target_id`, `reason`) VALUES (?,?,?,?);", type, user.id, userId, reason);
        fire(user.id, userId, null, reason, null, null);
    }

    public void wipeUser(Auth.User user, int targetId, int mode, String reason) {
        mysql.Exec("INSERT INTO `sh_audit`(`action`, `user_id`, `target_id`, `mode`, `reason`) VALUES (?,?,?,?, ?);", type, user.id, targetId, mode, reason);
        fire(user.id, targetId, null, reason, null, mode);
    }

    public void alertAll(Auth.User user, String message) {
        mysql.Exec("INSERT INTO `sh_audit`(`action`, `user_id`, `reason`) VALUES (?,?,?);", type, user.id, message);
        fire(user.id, null, null, message, null, null);
    }

    public void giveDonator(Auth.User user, int targetId, String duration) {
        mysql.Exec("INSERT INTO `sh_audit`(`action`, `user_id`, `target_id`, `reason`) VALUES (?,?,?,?);", type, user.id, targetId, duration);
        fire(user.id, targetId, null, duration, null,  null);
    }

    public void addPriv(Auth.User user, int targetId, String[] privs) {
        mysql.Exec("INSERT INTO `sh_audit`(`action`, `user_id`, `target_id`, `privs`) VALUES (?,?,?,?);", type, user.id, targetId, privs.toString());
        fire(user.id, targetId, privs, null, null, null);
    }

    public void removePriv(Auth.User user, int targetId, String[] privs) {
        mysql.Exec("INSERT INTO `sh_audit`(`action`, `user_id`, `target_id`, `privs`) VALUES (?,?,?,?);", type, user.id, targetId, privs.toString());
        fire(user.id, targetId, privs, null, null, null);
    }

    public void removeProfilePicture(Auth.User user, int targetId, String reason) {
        mysql.Exec("INSERT INTO `sh_audit`(`action`, `user_id`, `target_id`, `reason`) VALUES (?,?,?,?);", type, user.id, targetId, reason);
        fire(user.id, targetId, null, reason, null, null);
    }

    public void fire(Integer userId, Integer targetId, String[] privs, String reason, Integer status, Integer mode) {
        new OnAuditLogEvent(type, userId, targetId, privs, reason, status, mode).callListeners();
    }


    
}
