package dev.osunolimits.monetization.routes;

import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import org.json.JSONObject;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;

import ch.qos.logback.classic.Logger;
import dev.osunolimits.main.App;
import dev.osunolimits.models.UserInfoObject;
import dev.osunolimits.modules.ShiinaRoute.ShiinaRequest;
import dev.osunolimits.modules.utils.MySQLRoute;
import dev.osunolimits.monetization.MonetizationConfig;
import dev.osunolimits.routes.ap.api.PubSubModels;
import dev.osunolimits.routes.ap.api.PubSubModels.GiveDonatorInput;
import spark.Request;
import spark.Response;

public class KofiDonoHandler extends MySQLRoute {
    private final Logger log = (Logger) LoggerFactory.getLogger("KofiDonoHandler");
    private final Gson GSON = new Gson();

    @Override
    public Object handle(Request req, Response res) throws Exception {
        ShiinaRequest shiina = getRequest();

        // Extract and decode the URL-encoded "data" parameter
        String encodedData = req.queryParams("data");
        if (encodedData == null) {
            log.warn("No data received in the request");
            res.status(400);
            return "Bad Request: Missing data";
        }

        String decodedData = URLDecoder.decode(encodedData, StandardCharsets.UTF_8);
        JSONObject data = new JSONObject(decodedData);

        // Extract required fields
        String verificationToken = data.optString("verification_token");
        String transactionId = data.optString("kofi_transaction_id");
        Double amount = Double.parseDouble(data.optString("amount"));
        String fromName = data.optString("from_name");

        if (!MonetizationConfig.KOFI_CONFIG.getVerificationToken().equals(verificationToken)) {
            log.warn("Invalid verification token: {}", verificationToken);
            res.status(400);
            return "Bad Request: Invalid verification token";
        }

        int weeks = ((int) Math.floor(amount / MonetizationConfig.KOFI_CONFIG.getDonationAmount()) * 4);
        if (weeks <= 0) {
            log.warn("Donation amount too low: {}", amount);
            res.status(400);
            return "Bad Request: Donation amount too low";
        }

        String safeName = fromName.toLowerCase().replaceAll(" ", "_");
        ResultSet checkForUser = shiina.mysql.Query("SELECT * FROM `users` WHERE `safe_name` = ?", safeName);
        if (!checkForUser.next()) {
            log.warn("User not found: {}", fromName);
            res.status(400);
            return "Bad Request: User not found";
        }

        int userId = checkForUser.getInt("id");
        GiveDonatorInput giveDonatorInput = new PubSubModels().new GiveDonatorInput();
        giveDonatorInput.setId(userId);
        giveDonatorInput.setDuration(weeks+"w");

        App.jedisPool.publish("givedonator", GSON.toJson(giveDonatorInput));

        UserInfoObject obj = GSON.fromJson(App.jedisPool.get("shiina:user:" + userId), UserInfoObject.class);
        ResultSet privRs = shiina.mysql.Query("SELECT `priv` FROM `users` WHERE `id` = ?", giveDonatorInput.getId());
        obj.priv = privRs.next() ? privRs.getInt("priv") : 0;
        String userJson = GSON.toJson(obj);
        App.jedisPool.set("shiina:user:" + userId, userJson);

        shiina.mysql.Exec("INSERT INTO `sh_payments`(`user_id`, `months`, `total`, `payment_id`) VALUES (?,?,?,?)", userId, (int)(weeks / 4), amount, transactionId);

        log.info("Kofi Donation Sucess: {}", transactionId);

        return "OK";
    }
}
