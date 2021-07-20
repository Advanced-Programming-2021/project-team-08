package view.menus;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controller.ApplicationManger;
import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.HashMap;

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
            ApplicationManger.goToScene("gamePlayScene.fxml");
        } else if (type.equals("WAITING")) {
            this.message.setText(responseMessage);
            new Thread(() -> {
                try {
                    String serverMessage = ApplicationManger.getDataInputStream().readUTF();
                    System.out.println(serverMessage);

                    JsonObject json1 = JsonParser.parseString(serverMessage).getAsJsonObject();
                    String type1 = json1.get("type").getAsString();
                    //String responseMessage1 = json1.get("message").getAsString();
                    if(type1.equals("SUCCESSFUL")){
                        Platform.runLater(()-> ApplicationManger.goToScene("gamePlayScene.fxml"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            this.message.setText(responseMessage);
        }
    }

    public void back() {
        ApplicationManger.goToScene1(SceneName.DUEL_SCENE, false);
    }
}
