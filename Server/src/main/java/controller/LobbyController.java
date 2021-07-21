package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.User;
import model.enums.ChatType;
import model.enums.MessageType;

import java.util.ArrayList;
import java.util.HashMap;

public class LobbyController extends ServerController{

    private static final LobbyController lobbyController = new LobbyController();
    private final ArrayList<Message> allMessage = new ArrayList<>();

    public static LobbyController getInstance() {
        return lobbyController;
    }

    private static final HashMap<User, Integer> lobbyUsers = new HashMap<>();

    @Override
    public String getServerMessage(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        String methodName = jsonObject.get("method").getAsString();
        switch (methodName) {
            case "enter":
                return enterLobby(input);
            case "exit":
                return exitLobby(input);
            case "send":
                return sendMessage(input);
            case "updateChat":
                return updateChat(input);
            default:
                return serverMessage(MessageType.ERROR, "invalid method name", null);
        }
    }

    private String enterLobby(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        User user = ServerController.getUserByToken(jsonObject.get("token").getAsString());
        lobbyUsers.put(user, Message.getIdCounter());
        return ServerController.serverMessage(MessageType.SUCCESSFUL, "you entered the lobby successfully", null);
    }

    private String exitLobby(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        User user = ServerController.getUserByToken(jsonObject.get("token").getAsString());
        if (!lobbyUsers.containsKey(user)) {
            return ServerController.serverMessage(MessageType.ERROR, "you are not in lobby", null);
        }
        lobbyUsers.remove(user);
        return ServerController.serverMessage(MessageType.SUCCESSFUL, "you exited from lobby", null);
    }

    private String sendMessage(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        User user = ServerController.getUserByToken(jsonObject.get("token").getAsString());
        if (!lobbyUsers.containsKey(user)) {
            return ServerController.serverMessage(MessageType.ERROR, "you can send message from lobby", null);
        }
        String message = jsonObject.get("message").getAsString();
        String type = jsonObject.get("type").getAsString();
        Message chat = new Message(ChatType.valueOf(type), message, user.getUserData().getNickname());
        allMessage.add(chat);
        return ServerController.serverMessage(MessageType.SUCCESSFUL, "your message is send", null);
    }

    private String updateChat(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        User user = ServerController.getUserByToken(jsonObject.get("token").getAsString());
        if (!lobbyUsers.containsKey(user)) {
            return ServerController.serverMessage(MessageType.ERROR, "you can send message from lobby", null);
        }
        ArrayList<Message> newMessages = new ArrayList<>();
        int lastSeenMessage = lobbyUsers.get(user);
        if (Message.getIdCounter() <= lastSeenMessage) {
            return ServerController.serverMessage(MessageType.SUCCESSFUL, "there is no new message", null);
        }
        for (int i = lobbyUsers.get(user); i < Message.getIdCounter(); i++) {
            newMessages.add(allMessage.get(i));
        }
        lobbyUsers.replace(user, Message.getIdCounter());
        return ServerController.serverMessage(MessageType.SUCCESSFUL, "here is the new messages", new Gson().toJson(newMessages));
    }

}

class Message {
    private final String message;
    private final String senderNickname;
    private final ChatType chatType;
    private final int id;
    private static int idCounter = 0;

    public Message(ChatType chatType, String message, String senderNickname) {
        this.message = message;
        this.chatType = chatType;
        this.senderNickname = senderNickname;
        id = idCounter;
        idCounter++;
    }

    public static int getIdCounter() {
        return idCounter;
    }
}
