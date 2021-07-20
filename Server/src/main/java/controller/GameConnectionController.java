package controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.User;
import model.enums.MessageType;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class GameConnectionController extends ServerController{
    private static final GameConnectionController gameConnectionController = new GameConnectionController();

    public static GameConnectionController getInstance() {
        return gameConnectionController;
    }


    private final HashMap<User, Socket> waitingUsers = new HashMap<>();
    private final ArrayList<WaitingGame> waitingGames = new ArrayList<>();

    @Override
    public String getServerMessage(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        String methodName = jsonObject.get("method").getAsString();
        if (methodName.equals("newGame")) {
            return newGame(input);
        }
        return serverMessage(MessageType.ERROR, "invalid method name", null);
    }

    private String newGame(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        int rounds = Integer.parseInt(jsonObject.get("rounds").getAsString());
        String token = jsonObject.get("token").getAsString();
        User user = ServerController.getUserByToken(token);
        for (WaitingGame gameData : waitingGames) {
            if (gameData.getRounds() == rounds) {
                //TODO start game
                return serverMessage(MessageType.SUCCESSFUL, "you are connected to game", null);
            }
        }
        waitingGames.add(new WaitingGame(user, waitingUsers.get(ServerController.getUserByToken(token)), rounds));
        return serverMessage(MessageType.WAITING, "waiting for a user to connect", null);
    }

    public void addGameWaiter(Socket socket, String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        String token = jsonObject.get("token").getAsString();
        User user = ServerController.getUserByToken(token);
        waitingUsers.put(user, socket);
    }
}

class WaitingGame {
    private User user;
    private int rounds;
    private Socket userSocket;

    public WaitingGame(User user, Socket socket, int rounds) {
        this.user = user;
        this.userSocket = socket;
        this.rounds = rounds;
    }

    public int getRounds() {
        return rounds;
    }

    public Socket getUserSocket() {
        return userSocket;
    }

    public User getUser() {
        return user;
    }
}
