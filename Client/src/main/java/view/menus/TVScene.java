package view.menus;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controller.ApplicationManger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

import java.util.HashMap;


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
        AnchorPane anchorPane = new AnchorPane();
        String result = ApplicationManger.getServerResponse("tv", "replayList", null);
        JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
        if (jsonObject.get("type").getAsString().equals("ERROR")) {
            System.out.println(result);
            return;
        }
        JsonArray jsonArray = JsonParser.parseString(jsonObject.get("returnObject").getAsString()).getAsJsonArray();
        int i = 0;
        for (JsonElement jsonElement : jsonArray) {
            String nickname1 = jsonElement.getAsJsonObject().get("firstPlayerNickname").getAsString();
            String nickname2 = jsonElement.getAsJsonObject().get("secondPlayerNickname").getAsString();
            int id = jsonElement.getAsJsonObject().get("id").getAsInt();
            anchorPane.getChildren().add(i, new tvLabel(nickname1, nickname2, id, i));
        }
        tvPane = anchorPane;
    }
}

class tvLabel extends Label {
    String nickname1;
    String nickname2;
    int id;

    public tvLabel(String nickname1, String nickname2, int id, int index) {
        this.nickname1 = nickname1;
        this.nickname2 = nickname2;
        this.id = id;
        setText(text());
        setPrefHeight(70);
        setPrefWidth(200);
        setFont(new Font(26));
        setLayoutY(index * 100);
        setCursor(Cursor.HAND);
        clickInit();
    }

    void clickInit() {
        setOnMouseClicked(event -> {
            HashMap<String, String> data = new HashMap<>();
            data.put("id", String.valueOf(id));
            System.out.println(ApplicationManger.getServerResponse("tv", "play", data));
        });
    }

    String text() {
        return nickname1 + "   VS   " + nickname2;
    }
}
