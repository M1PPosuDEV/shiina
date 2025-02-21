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

public class ChatExplorer extends Shiina {

    private final String SELECT_CHAT_SQL = "SELECT `from_id`, `u1`.`name` AS `from_name`, `to_id`, `u2`.`name` AS `to_name`, msg, time FROM `mail` LEFT JOIN `users` AS `u1` ON `from_id` = `u1`.`id` LEFT JOIN `users` AS `u2` ON `to_id` = `u2`.`id`";
    private final String SELECT_CHAT_SEARCH_SQL = "SELECT `from_id`, `u1`.`name` AS `from_name`, `to_id`, `u2`.`name` AS `to_name`, `msg`, `time` FROM `mail` LEFT JOIN `users` AS `u1` ON `from_id` = `u1`.`id` LEFT JOIN `users` AS `u2` ON `to_id` = `u2`.`id` WHERE (? REGEXP '^[0-9]+$' AND (from_id = CAST(? AS UNSIGNED) OR to_id = CAST(? AS UNSIGNED))) OR (? NOT REGEXP '^[0-9]+$' AND (`u1`.`name` LIKE ? OR `u2`.`name` LIKE ? OR `msg` LIKE ?)) ORDER BY `time` DESC LIMIT ? OFFSET ?;";
    public static int pageSize=10;

    @Data
    public class ApChatInstance {
        private int from_id;
        private String from_name;
        private int to_id;
        private String to_name;
        private String msg;
        private Long time;
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        ShiinaRequest shiina = new ShiinaRoute().handle(req, res);
        shiina.data.put("actNav", 15);

        if (!shiina.loggedIn) {
            res.redirect("/login");
            return notFound(res, shiina);
        }

        if (!PermissionHelper.hasPrivileges(shiina.user.priv, PermissionHelper.Privileges.MODERATOR)) {
            res.redirect("/");
            return notFound(res, shiina);
        }



        int page = 0;
        if (req.queryParams("page") != null) {
            page = Integer.parseInt(req.queryParams("page"));
        }

        String sql;
        ResultSet rs;

        int offset = page * 10;
        String searchQuery = req.queryParams("search");
        boolean hasNextPage = false;

        if(searchQuery == null) {
            sql = SELECT_CHAT_SQL + " ORDER BY `time` DESC LIMIT ? OFFSET ?;";
            rs = shiina.mysql.Query(sql, pageSize + 1, offset);
        }else {
            sql = SELECT_CHAT_SEARCH_SQL;
            rs = shiina.mysql.Query(sql, 
    searchQuery,  // Used to check if it's a number
    searchQuery, searchQuery,  // Used in `from_id = ? OR to_id = ?` (as numbers)
    searchQuery,  // Used to check if it's NOT a number
    "%" + searchQuery + "%", "%" + searchQuery + "%", "%" + searchQuery + "%",  // Used for text search
    pageSize + 1, 
    offset
);
        
        }

        List<ApChatInstance> chats = new ArrayList<>();
        while(rs.next()) {
            ApChatInstance chat = new ApChatInstance();
            chat.setFrom_id(rs.getInt("from_id"));
            chat.setFrom_name(rs.getString("from_name"));
            chat.setTo_id(rs.getInt("to_id"));
            chat.setTo_name(rs.getString("to_name"));
            chat.setMsg(rs.getString("msg"));
            chat.setTime(rs.getLong("time"));
            chats.add(chat);
        }

        if(chats.size() == pageSize + 1) {
            hasNextPage = true;
            chats.remove(chats.size() - 1);
        }


        shiina.data.put("chats", chats);
        shiina.data.put("hasNextPage", hasNextPage);
        shiina.data.put("page", page);
        shiina.data.put("search", searchQuery);

        return renderTemplate("ap/chatexp.html", shiina, res, req);
    }


}
