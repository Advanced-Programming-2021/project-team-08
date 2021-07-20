package view.menus;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controller.ApplicationManger;
import controller.DuelController;
import controller.GamePlaySceneController;
import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import model.UserData;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LobbyMenu {
    public CheckBox threeRound;
    public CheckBox oneRound;
    public Label message;

    public void initialize() {
    }

    public void threeRoundsAction() {
        oneRound.setSelected(false);
        threeRound.setSelected(true);
    }

    public void oneRoundAction() {
        threeRound.setSelected(false);
        oneRound.setSelected(true);
    }

    public void newGame() {
        HashMap<String, String> data = new HashMap<>();
        data.put("rounds", oneRound.isSelected() ? "1" : "3");
        String response = ApplicationManger.getServerResponse("newGame", "newGame", data);

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
        String type = json.get("type").getAsString();
        String responseMessage = json.get("message").getAsString();

        if (type.equals("SUCCESSFUL")) {
            startGame(response);
        } else if (type.equals("WAITING")) {
            this.message.setText(responseMessage);
            new Thread(() -> {
                try {
                    String serverMessage = ApplicationManger.getDataInputStream().readUTF();
                    System.out.println(serverMessage);

                    JsonObject json1 = JsonParser.parseString(serverMessage).getAsJsonObject();
                    String type1 = json1.get("type").getAsString();
                    if (type1.equals("SUCCESSFUL")) {
                        startGame(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            this.message.setText(responseMessage);
        }
    }

    private void startGame(String serverMessage) {
        System.out.println(serverMessage);

        JsonObject json = JsonParser.parseString(serverMessage).getAsJsonObject();
        String message = json.get("message").getAsString();
        JsonObject returnObject = JsonParser.parseString(json.get("returnObject").getAsString()).getAsJsonObject();
        Matcher matcher = Pattern.compile("player (1|2)").matcher(message);
        matcher.find();
        int playerNumber = Integer.parseInt(matcher.group(1));

        DuelData duelData = new Gson().fromJson(returnObject, DuelData.class);

        DuelController.setCurrentDuelData(new GamePlaySceneController.DuelData(duelData.rounds, true, duelData.user1Data, duelData.user2Data));

        Platform.runLater(() -> ApplicationManger.goToScene("gamePlayScene" + playerNumber + ".fxml"));
    }

    class DuelData {
        private UserData user1Data;
        private UserData user2Data;
        private int rounds;

        public DuelData(UserData user1Data, UserData user2Data, int rounds) {
            this.user1Data = user1Data;
            this.user2Data = user2Data;
            this.rounds = rounds;
        }
    }

    public void back() {
        ApplicationManger.goToScene1(SceneName.DUEL_SCENE, false);
    }
}
