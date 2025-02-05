package dev.osunolimits.routes.api.get;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import dev.osunolimits.modules.ShiinaRoute.ShiinaRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import spark.Response;

public class ShiinaAPIHandler {

    private final Gson GSON;

    private List<ShiinaAPIParameter> requiredParameters = new ArrayList<>();

    public ShiinaAPIHandler() {
        GSON = new Gson();
    }

    public void addRequiredParameter(String name, String type, String status) {
        requiredParameters.add(new ShiinaAPIParameter(name, type, status));
    }

    public String renderJSON(Object object, ShiinaRequest shiina, Response res) {
        res.type("application/json");
        shiina.mysql.close();
        return GSON.toJson(object);
    }

    public String renderJSONNoSQL(Object object, Response res) {
        res.type("application/json");
        return GSON.toJson(object);
    }

    public List<ShiinaAPIParameter> getIssues() {
        return requiredParameters;
    }

    public boolean hasIssues() {
        return !requiredParameters.isEmpty();
    }
    public String renderIssues(ShiinaRequest shiina, Response res) {
        res.type("application/json");
        shiina.mysql.close();
        return GSON.toJson(requiredParameters);
    }

    public String renderIssuesNoSQL(Response res) {
        res.type("application/json");
        return GSON.toJson(requiredParameters);
    }

    @AllArgsConstructor @Getter
    public class ShiinaAPIParameter {
        private String name;
        private String type;
        private String status;
    }
    
}
