package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.User;
import model.UserData;
import model.enums.MessageType;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import static controller.ServerController.serverMessage;

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
        if (methodName.equals("cancel")) {
            return cancel(input);
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
                SendDuelData duelData = new SendDuelData(gameData.getUser1().getUserData(), user.getUserData(), gameData.getRounds());
                String duelDataJson = new Gson().toJson(duelData);

                gameData.gameStart(user, token, waitingUsers.get(ServerController.getUserByToken(token)), duelDataJson);
                waitingGames.remove(gameData);
                new GameController(gameData, duelDataJson);
                return serverMessage(MessageType.SUCCESSFUL, "player 2", duelDataJson);
            }
        }
        waitingGames.add(new WaitingGame(user, token, waitingUsers.get(ServerController.getUserByToken(token)), rounds));
        return serverMessage(MessageType.WAITING, "waiting for a user to connect", null);
    }

    private String cancel(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        String token = jsonObject.get("token").getAsString();
        waitingGames.removeIf(e -> e.getUser1Token().equals(token));
        return serverMessage(MessageType.CANCEL, "game canceled", null);
    }

    public void addGameWaiter(Socket socket, String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        String token = jsonObject.get("token").getAsString();
        User user = ServerController.getUserByToken(token);
        waitingUsers.put(user, socket);
    }
}

class WaitingGame {
    private final User user1;
    private User user2;
    private final int rounds;
    private final Socket user1Socket;
    private Socket user2Socket;
    private final String user1Token;
    private String user2Token;

    public WaitingGame(User user, String user1Token, Socket socket, int rounds) {
        this.user1 = user;
        this.user1Token = user1Token;
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

    public String getUser1Token() {
        return user1Token;
    }

    public String getUser2Token() {
        return user2Token;
    }

    public void gameStart(User user2, String user2Token, Socket socket2, String duelDataJson) {
        user2Socket = socket2;
        this.user2Token = user2Token;
        this.user2 = user2;
        try {
            DataOutputStream outputStream = new DataOutputStream(user1Socket.getOutputStream());
            String s = serverMessage(MessageType.SUCCESSFUL, "player 1", duelDataJson);
            outputStream.writeUTF(s);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerManager.getIsInGame().put(user1Socket.getRemoteSocketAddress().toString(), true);
        ServerManager.getIsInGame().put(user2Socket.getRemoteSocketAddress().toString(), true);
    }
}

class SendDuelData {
    private final UserData user1Data;
    private final UserData user2Data;
    private final int rounds;

    public SendDuelData(UserData user1Data, UserData user2Data, int rounds) {
        this.user1Data = user1Data;
        this.user2Data = user2Data;
        this.rounds = rounds;
    }

    public int getRounds() {
        return rounds;
    }

    public UserData getUser1Data() {
        return user1Data;
    }

    public UserData getUser2Data() {
        return user2Data;
    }
}
