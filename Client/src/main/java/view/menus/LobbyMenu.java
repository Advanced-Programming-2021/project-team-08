package view.menus;

import controller.ApplicationManger;
import javafx.scene.control.CheckBox;

import java.util.HashMap;

public class LobbyMenu {
    public CheckBox threeRound;
    public CheckBox oneRound;

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
        data.put("round", oneRound.isSelected() ? "1" : "3");
        ApplicationManger.getServerResponse("lobby", "newGame", data);

        // TODO: ۱۹/۰۷/۲۰۲۱ new game
    }

    public void back() {
        ApplicationManger.goToScene1(SceneName.DUEL_SCENE, false);
    }
}
