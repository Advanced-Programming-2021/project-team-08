package controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.User;
import model.enums.MessageType;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class GameConnectionController extends ServerController {
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
                gameData.gameStart(user, waitingUsers.get(ServerController.getUserByToken(token)));
                waitingGames.remove(gameData);
                new GameController(gameData);
                return serverMessage(MessageType.SUCCESSFUL, "you are connected to game", "2");
            }
        }
        System.out.println(waitingUsers.get(ServerController.getUserByToken(token)).getRemoteSocketAddress());
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
    private User user1;
    private User user2;
    private int rounds;
    private Socket user1Socket;
    private Socket user2Socket;


    public WaitingGame(User user, Socket socket, int rounds) {
        this.user1 = user;
        this.user1Socket = socket;
        this.rounds = rounds;
    }

    public int getRounds() {
        return rounds;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public Socket getUser1Socket() {
        return user1Socket;
    }

    public Socket getUser2Socket() {
        return user2Socket;
    }

    public void gameStart(User user2, Socket socket2) {
        user2Socket = socket2;
        this.user2 = user2;
        try {
            DataOutputStream outputStream = new DataOutputStream(user1Socket.getOutputStream());
            outputStream.writeUTF("{\"type\":\"SUCCESSFUL\",\"message\":\"game started\",\"returnObject\":\"1\"}");
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerManager.getIsInGame().put(user1Socket, true);
        ServerManager.getIsInGame().put(user2Socket, true);
    }
}
