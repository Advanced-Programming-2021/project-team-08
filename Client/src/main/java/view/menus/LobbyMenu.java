package view.menus;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controller.ApplicationManger;
import controller.DuelController;
import controller.GamePlaySceneController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.UserData;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LobbyMenu {
    public CheckBox threeRound;
    public CheckBox oneRound;
    public Label message;
    public Label errorMessage;
    public TextField messageText;
    public VBox messages;
    private static int idCounter = 0;

    private boolean isGameStarted = false;

    public void initialize() {
        String result = ApplicationManger.getServerResponse("lobby", "enter", null);
        JsonElement jsonElement = JsonParser.parseString(result);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        message.setText(jsonObject.get("message").getAsString());
        message.setTextFill(Color.GREEN);
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
                while (!isGameStarted) {
                    try {
                        Thread.sleep(1000);
                        ApplicationManger.getDataOutputStream().writeUTF("alive");
                        ApplicationManger.getDataOutputStream().flush();
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
                isGameStarted = false;
            }).start();
            new Thread(() -> {
                try {
                    String serverMessage = ApplicationManger.getDataInputStream().readUTF();
                    System.out.println(serverMessage);

                    JsonObject json1 = JsonParser.parseString(serverMessage).getAsJsonObject();
                    String type1 = json1.get("type").getAsString();
                    if (type1.equals("SUCCESSFUL")) {
                        isGameStarted = true;
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

    public void sendMessage(ActionEvent actionEvent) {
        HashMap<String, String> data = new HashMap<>();
        data.put("message", messageText.getText());
        data.put("type", "SEND");
        String result = ApplicationManger.getServerResponse("lobby", "send", data);
        JsonElement jsonElement = JsonParser.parseString(result);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (jsonObject.get("type").getAsString().equals("SUCCESSFUL")) {
            HBox hBox = new HBox();
            Label message = new Label();
            Button deleteMessageButton = new Button();
            Button editMessageButton = new Button();
            message.setText(messageText.getText());
            deleteMessageButton.setText("delete");
            editMessageButton.setText("edit");
            hBox.getChildren().add(0, message);
            hBox.getChildren().add(1, deleteMessageButton);
            hBox.getChildren().add(2, editMessageButton);
            messages.getChildren().add(hBox);
            errorMessage.setText(jsonObject.get("message").getAsString());
            errorMessage.setTextFill(Color.GREEN);
        } else {
            errorMessage.setText(jsonObject.get("message").getAsString());
            errorMessage.setTextFill(Color.RED);
        }
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
