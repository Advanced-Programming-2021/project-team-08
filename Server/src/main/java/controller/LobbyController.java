package controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.User;
import model.enums.MessageType;

import java.util.ArrayList;

public class LobbyController extends ServerController{

    private static final LobbyController lobbyController = new LobbyController();

    public static LobbyController getInstance() {
        return lobbyController;
    }

    private static ArrayList<User> lobbyUsers = new ArrayList<>();

    @Override
    public String getServerMessage(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        String methodName = jsonObject.get("method").getAsString();
        switch (methodName) {
            case "enter":
                return enterLobby(input);
            case "send":
                return sendMessage(input);
            case "edit" :
                return editMessage(input);
            case "delete" :
                return deleteMessage(input);
            default:
                return serverMessage(MessageType.ERROR, "invalid method name", null);
        }
    }

    private String enterLobby(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        User user = ServerController.getUserByToken(jsonObject.get("token").getAsString());
        lobbyUsers.add(user);
        return ServerController.serverMessage(MessageType.SUCCESSFUL, "you entered the lobby successfully", null);
    }

    private String sendMessage(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        User user = ServerController.getUserByToken(jsonObject.get("token").getAsString());
        if (!lobbyUsers.contains(user)) {
            return ServerController.serverMessage(MessageType.ERROR, "you can send message from lobby", null);
        }
        //TODO
        return null;
    }

    private String editMessage(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        User user = ServerController.getUserByToken(jsonObject.get("token").getAsString());
        if (!lobbyUsers.contains(user)) {
            return ServerController.serverMessage(MessageType.ERROR, "you can send message from lobby", null);
        }
        //TODO
        return null;
    }

    private String deleteMessage(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        User user = ServerController.getUserByToken(jsonObject.get("token").getAsString());
        if (!lobbyUsers.contains(user)) {
            return ServerController.serverMessage(MessageType.ERROR, "you can send message from lobby", null);
        }
        //TODO
        return null;
    }
}
