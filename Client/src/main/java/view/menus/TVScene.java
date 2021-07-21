package view.menus;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;



public class TVScene {

    public AnchorPane tvPane;
    public Button liveButton;
    public Button topButton;
    public Button replayButton;

    @FXML
    void initialize() {
        buttonInit(liveButton, 1.2);
        buttonInit(topButton, 1.2);
        buttonInit(replayButton, 1.2);
    }

    private void buttonInit(Button button, double ratio) {
        button.setOnMouseEntered(event -> setButtonRatio(ratio, button));
        button.setOnMouseExited(event -> setButtonRatio(1 / ratio, button));
    }

    private void setButtonRatio(double ratio, Button node) {
        node.setLayoutX(node.getLayoutX() + node.getPrefWidth() * -0.5 * (ratio - 1));
        node.setLayoutY(node.getLayoutY() + node.getPrefHeight() * - 0.5 * (ratio - 1));
        node.setPrefWidth(node.getPrefWidth() * ratio);
        node.setPrefHeight(node.getPrefHeight() * ratio);
        //node.setTextFill(ratio > 1 ? Color.YELLOW : Color.WHITE);
    }

    public void setLiveBattles(ActionEvent actionEvent) {

    }

    public void setTopBattles(ActionEvent actionEvent) {
    }

    public void setReplayBattles(ActionEvent actionEvent) {
    }
}
