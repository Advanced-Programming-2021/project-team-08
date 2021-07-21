package controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.User;
import model.UserData;
import model.enums.MessageType;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class ScoreboardController extends ServerController {

    private static final ScoreboardController scoreboardController = new ScoreboardController();

    public static ScoreboardController getInstance() {
        return scoreboardController;
    }

    @Override
    public String getServerMessage(String input) {
        JsonElement jsonElement = JsonParser.parseString(input);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (jsonObject.get("method").getAsString().equals("scoreboard")) {
            return getScoreboard();
        }
        return serverMessage(MessageType.ERROR, "invalid command", null);
    }

    private String getScoreboard() {
        UserData[] sortedUsers = User.getAllUserData();
        Arrays.sort(sortedUsers, new sort());
        HashMap<String, Integer> scoreboard = new HashMap<>();
        for (UserData userData : sortedUsers) {
            scoreboard.put(userData.getNickname(), userData.getPoint());
        }
        return ServerController.serverMessage(MessageType.SUCCESSFUL, "", scoreboard.toString());
    }
}

class sort implements Comparator<UserData> {
    public int compare(UserData a, UserData b) {
        if (a.getPoint() != b.getPoint())
            return b.getPoint() - a.getPoint();
        else return a.getNickname().compareTo(b.getNickname());
    }
}