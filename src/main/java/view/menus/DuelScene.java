package view.menus;

import controller.DuelController;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class DuelScene extends Scene{

    public Label duelMessage;
    public CheckBox threeRound;
    public CheckBox oneRound;
    public TextField playerName;
    public boolean isSinglePlayer;
    public Pane duelPane;
    DuelController duelController;
    @Override
    protected int getUserCommand() {
        return 0;
    }

    public DuelScene() {
        duelController = new DuelController();
    }


    public void threeRoundsAction(ActionEvent actionEvent) {
        oneRound.setSelected(false);
        threeRound.setSelected(true);
    }

    public void oneRoundAction(ActionEvent actionEvent) {
        threeRound.setSelected(false);
        oneRound.setSelected(true);
    }

    public void startDuel(ActionEvent actionEvent) {
        int rounds;
        if (threeRound.isSelected()) rounds = 3;
        else rounds = 1;
        if (isSinglePlayer) {
            duelMessage.setText(duelController.duelSinglePlayer(rounds));
        }else {
            duelMessage.setText(duelController.duelMultiplayer(rounds, playerName.getText()));
        }
    }

    public void singlePlayMenu(ActionEvent actionEvent) {
        duelPane.setVisible(true);
        isSinglePlayer = true;
        playerName.setVisible(false);
    }

    public void multiPlayMenu(ActionEvent actionEvent) {
        duelPane.setVisible(true);
        isSinglePlayer = false;
        playerName.setVisible(true);
    }
}
