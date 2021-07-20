package view.menus;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controller.ApplicationManger;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

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
        JsonObject json =  JsonParser.parseString(response).getAsJsonObject();
        String type = json.get("type").getAsString();
        String responseMessage = json.get("message").getAsString();
        if(type.equals("SUCCESSFUL")){
            System.out.println("GOING TO GAME");
            // TODO: ۲۰/۰۷/۲۰۲۱ goToGame
        } else if(type.equals("WAITING")){
            this.message.setText(responseMessage);
        } else {
            this.message.setText(responseMessage);
        }
    }

    public void back() {
        ApplicationManger.goToScene1(SceneName.DUEL_SCENE, false);
    }
}
