package controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.User;
import model.enums.MessageType;

import java.util.HashMap;

public abstract class ServerController {

    private static HashMap<String, User> activeUsers = new HashMap<>();

    public static ServerController getController(String input) {
        try {
            JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
            String controllerName = jsonObject.get("controller").getAsString();
            switch (controllerName) {
                case "register" : return RegisterController.getInstance();
                case "shop" : return ShopController.getInstance();
                case "scoreboard" : return ScoreboardController.getInstance();
                default: return null;
            }
        }catch (Exception e) {
            return null;
        }
    }

    public static String checkToken(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        String controllerName = jsonObject.get("controller").getAsString();
        if (!controllerName.equals("register") && jsonObject.get("token") == null) return serverMessage(MessageType.ERROR, "no token input", null);
        if (!controllerName.equals("register") && getUserByToken(jsonObject.get("token").getAsString()) == null) return serverMessage(MessageType.ERROR, "invalid token", null);
        return null;
    }

    public abstract String getServerMessage(String input);

    protected static String serverMessage(MessageType messageType, String message, String returnObject) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", messageType.toString());
        jsonObject.addProperty("message", message);
        jsonObject.addProperty("returnObject", returnObject);
        return jsonObject.toString();
    }

    public static User getUserByToken(String token) {
        if (token == null) return null;
        return activeUsers.get(token);
    }

    protected void addUser(String token, User user) {
        activeUsers.put(token, user);
    }


}
