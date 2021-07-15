package controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.User;
import model.enums.MessageType;

import java.util.HashMap;

public abstract class ServerController {

    private HashMap<String, User> activeUsers = new HashMap<>();

    public static ServerController getController(String input) {
        try {
            JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
            String controllerName = jsonObject.get("controller").getAsString();
            switch (controllerName) {
                case "register" : return RegisterController.getInstance();

                default: return null;
            }
        }catch (Exception e) {
            return null;
        }
    }

    public abstract String getServerMessage(String input);

    protected static String serverMessage(MessageType messageType, String message, String returnObject) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", messageType.toString());
        jsonObject.addProperty("message", message);
        jsonObject.addProperty("returnObject", returnObject);
        return jsonObject.toString();
    }

    public User getUserByToken(String token) {
        return activeUsers.get(token);
    }

    protected void addUser(String token, User user) {
        activeUsers.put(token, user);
    }


}
