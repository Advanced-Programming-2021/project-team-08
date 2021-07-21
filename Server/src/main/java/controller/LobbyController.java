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
            case "delete":
                return deleteMessage(input);
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

    private synchronized String sendMessage(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        User user = ServerController.getUserByToken(jsonObject.get("token").getAsString());
        if (!lobbyUsers.containsKey(user)) {
            return ServerController.serverMessage(MessageType.ERROR, "you can send message from lobby", null);
        }
        String message = jsonObject.get("message").getAsString();
        String type = jsonObject.get("type").getAsString();
        ChatType chatType = ChatType.valueOf(type);
        Message chat;
        if (chatType.equals(ChatType.SEND)) {
            chat = new Message(ChatType.valueOf(type), message, user.getUserData().getNickname(), -1);
        }else {
            int actionId = Integer.parseInt(jsonObject.get("actionId").getAsString());
            chat = new Message(ChatType.valueOf(type), message, user.getUserData().getNickname(), actionId);
        }
        allMessage.add(chat);
        return ServerController.serverMessage(MessageType.SUCCESSFUL, "your message is send", String.valueOf(chat.getId()));
    }

    private String deleteMessage(String input){
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        User user = ServerController.getUserByToken(jsonObject.get("token").getAsString());
        if (!lobbyUsers.containsKey(user)) {
            return ServerController.serverMessage(MessageType.ERROR, "you can send message from lobby", null);
        }
        allMessage.remove(getMessageById(jsonObject.get("id").getAsInt()));
        return ServerController.serverMessage(MessageType.SUCCESSFUL, "your message is removed", null);
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
            System.out.println("id counter is : " + Message.getIdCounter() + "last seen message is : " + lastSeenMessage);
            return ServerController.serverMessage(MessageType.SUCCESSFUL, "there is no new message", null);
        }
        for (int i = lobbyUsers.get(user); i < Message.getIdCounter(); i++) {
            newMessages.add(allMessage.get(i));
        }
        updateLastSeenMessage(user);
        return ServerController.serverMessage(MessageType.SUCCESSFUL, "here is the new messages", new Gson().toJson(newMessages));
    }

    private void updateLastSeenMessage(User user) {
        System.out.println("old last seen : " + lobbyUsers.get(user));
        lobbyUsers.replace(user, Message.getIdCounter());
        System.out.println("new last seen : " + lobbyUsers.get(user));
    }

    public  Message getMessageById(int id){
        for (Message message: allMessage){
            if (message.getId()==id)
                return message;
        }
        return null;
    }

}

class Message {
    private final String message;
    private final String senderNickname;
    private final ChatType chatType;
    private final int id;
    private final int actionId;
    private static int idCounter = 0;

    public Message(ChatType chatType, String message, String senderNickname, int actionId) {
        this.message = message;
        this.chatType = chatType;
        this.senderNickname = senderNickname;
        this.actionId = actionId;
        id = idCounter;
        idCounter++;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public int getId() {
        return id;
    }

}
