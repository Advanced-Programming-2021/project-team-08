package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.enums.MessageType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class TVController extends ServerController {
    private static final int topPointValue = 1000;

    private static final TVController tvController = new TVController();

    public static TVController getInstance() {
        return tvController;
    }

    @Override
    public String getServerMessage(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        String methodName = jsonObject.get("method").getAsString();
        switch (methodName) {
            case "liveList":
                return liveMatches(input);
            case "topList":
                return topMatches(input);
            case "replayList":
                return replayMatches(input);
            case "play":
                return playAMatch(input);
            default:
                return serverMessage(MessageType.ERROR, "invalid method name", null);
        }
    }

    private String playAMatch(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        int index = Integer.parseInt(jsonObject.get("id").getAsString());
        GameController gameController = GameController.getAllGames().get(index);
        if (gameController == null) {
            return serverMessage(MessageType.ERROR, "invalid game id", null);
        }
        try {
            String data = gameController.getGameSave();
            return serverMessage(MessageType.SUCCESSFUL, "it is game data", data);
        } catch (IOException e) {
            return ServerController.serverMessage(MessageType.ERROR, "ERROR in opening file", null);
        }
    }

    private String replayMatches(String input) {
        ArrayList<GameData> savedGameData = GameController.getGameList();
        if (savedGameData == null) return serverMessage(MessageType.ERROR, "couldn't load game data", null);
        return serverMessage(MessageType.SUCCESSFUL, "here is the list", new Gson().toJson(savedGameData));
    }

    private String topMatches(String input) {
        return null;
    }

    private String liveMatches(String input) {
        ArrayList<GameController> allGames = GameController.getAllGames();
        GameController[] games = new GameController[allGames.size()];
        for (int i = 0; i < allGames.size(); i++) {
            games[i] = allGames.get(i);
        }
        Arrays.sort(games, new GameSort());
        ArrayList<GameData> allGameData = new ArrayList<>();
        for (GameController gameController : games) {
            allGameData.add(new GameData(gameController.getGameId(), gameController.getGameManager().getPlayer1().getUserData().getNickname(), gameController.getGameManager().getPlayer2().getUserData().getNickname()));
        }
        return serverMessage(MessageType.SUCCESSFUL, "here is the game list", new Gson().toJson(allGameData));
    }
}

class GameData {
    private final int id;
    private final String firstPlayerNickname;
    private final String secondPlayerNickname;

    public GameData(int id, String firstPlayerNickname, String secondPlayerNickname) {
        this.id = id;
        this.firstPlayerNickname = firstPlayerNickname;
        this.secondPlayerNickname = secondPlayerNickname;
    }
}

class GameSort implements Comparator<GameController> {
    public int compare(GameController a, GameController b) {
        int aPlayersPoint = a.getGameManager().getPlayer1().getUserData().getPoint() + a.getGameManager().getPlayer2().getUserData().getPoint();
        int bPlayersPoint = b.getGameManager().getPlayer1().getUserData().getPoint() + b.getGameManager().getPlayer2().getUserData().getPoint();
        return bPlayersPoint - aPlayersPoint;
    }
}

