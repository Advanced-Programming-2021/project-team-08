package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.UserData;
import model.enums.MessageType;

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
            case "live":
                return liveMatches(input);
            case "top":
                return topMatches(input);
            case "replay":
                return replayMatches(input);
            case "play" :
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

    }

    private String replayMatches(String input) {

        return null;
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
            allGameData.add(new GameData(gameController));
        }
        return serverMessage(MessageType.SUCCESSFUL, "here is the game list", new Gson().toJson(allGameData));
    }
}

class GameData {
    private int id;
    private String firstPlayerNickname;
    private String secondPlayerNickname;

    public GameData(GameController gameController) {
        this.id = gameController.getGameId();
        this.firstPlayerNickname = gameController.getGameManager().getPlayer1().getUserData().getNickname();
        this.secondPlayerNickname = gameController.getGameManager().getPlayer2().getUserData().getNickname();
    }
}

class GameSort implements Comparator<GameController> {
    public int compare(GameController a, GameController b) {
        int aPlayersPoint = a.getGameManager().getPlayer1().getUserData().getPoint() + a.getGameManager().getPlayer2().getUserData().getPoint();
        int bPlayersPoint = b.getGameManager().getPlayer1().getUserData().getPoint() + b.getGameManager().getPlayer2().getUserData().getPoint();
        return bPlayersPoint - aPlayersPoint;
    }


}

