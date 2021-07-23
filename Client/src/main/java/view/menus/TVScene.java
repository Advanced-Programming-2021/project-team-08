package view.menus;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controller.ApplicationManger;
import controller.gameplay.StreamController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import view.StreamScene;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class TVScene {

    public AnchorPane tvPane;
    public Button liveButton;
    public Button topButton;
    public Button replayButton;
    public AnchorPane parent;
    public SubScene tv;
    public ScrollPane tvScroll;
    public Button pauseButton;


    @FXML
    void initialize() {
        buttonInit(liveButton, 1.2);
        buttonInit(topButton, 1.2);
        buttonInit(replayButton, 1.2);
        tv.setVisible(false);
    }

    private void buttonInit(Button button, double ratio) {
        button.setOnMouseEntered(event -> setButtonRatio(ratio, button));
        button.setOnMouseExited(event -> setButtonRatio(1 / ratio, button));
    }

    private void setButtonRatio(double ratio, Button node) {
        node.setLayoutX(node.getLayoutX() + node.getPrefWidth() * -0.5 * (ratio - 1));
        node.setLayoutY(node.getLayoutY() + node.getPrefHeight() * -0.5 * (ratio - 1));
        node.setPrefWidth(node.getPrefWidth() * ratio);
        node.setPrefHeight(node.getPrefHeight() * ratio);
        //node.setTextFill(ratio > 1 ? Color.YELLOW : Color.WHITE);
    }

    public void setLiveBattles(ActionEvent actionEvent) {
        tvPane.getChildren().clear();
        tvScroll.setVisible(true);
    }

    public void setTopBattles(ActionEvent actionEvent) {
        tvPane.getChildren().clear();
        tvScroll.setVisible(true);
    }

    public void setReplayBattles(ActionEvent actionEvent) {
        tvPane.getChildren().clear();
        tvScroll.setVisible(true);
        tvPane.setBackground(new Background(new BackgroundFill(Color.YELLOW, new CornerRadii(1), new Insets(1))));
        String result = ApplicationManger.getServerResponse("tv", "replayList", null);
        JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
        System.out.println(jsonObject);
        if (jsonObject.get("type").getAsString().equals("ERROR")) {
            System.out.println(result);
            return;
        }
        JsonArray jsonArray = JsonParser.parseString(jsonObject.get("returnObject").getAsString()).getAsJsonArray();
        System.out.println(jsonArray);
        int i = 0;
        for (JsonElement jsonElement : jsonArray) {
            String nickname1 = jsonElement.getAsJsonObject().get("firstPlayerNickname").getAsString();
            String nickname2 = jsonElement.getAsJsonObject().get("secondPlayerNickname").getAsString();
            int id = jsonElement.getAsJsonObject().get("id").getAsInt();
            System.out.println("first nickname is: " + nickname1 + " second nickname is: " + nickname2 + " id is: " + id);
            tvPane.getChildren().add(i, new tvLabel(nickname1, nickname2, id, i, tv, tvScroll, pauseButton));
            i++;
        }
    }

    public void Back(ActionEvent actionEvent) {
        ApplicationManger.goToScene1(SceneName.MAIN_MENU, false);
    }

    public void pauseResume(ActionEvent actionEvent) {
        if (pauseButton.getText().equals("pause")) {
            pauseButton.setText("resume");
            StreamScene.getInstance().pauseStream();
        }else {
            pauseButton.setText("pause");
            StreamScene.getInstance().resumeStream();
        }
    }
}

class tvLabel extends Label {
    String nickname1;
    String nickname2;
    int id;
    SubScene tv;
    ScrollPane tvScroll;
    Button pauseButton;

    public tvLabel(String nickname1, String nickname2, int id, int index, SubScene tv, ScrollPane tvPane, Button pauseButton) {
        this.nickname1 = nickname1;
        this.nickname2 = nickname2;
        this.id = id;
        this.tv = tv;
        this.tvScroll = tvPane;
        this.pauseButton = pauseButton;
        setText(text());
        setPrefHeight(80);
        setPrefWidth(400);
        setFont(new Font(26));
        setLayoutY(index * 100);
        setCursor(Cursor.HAND);
        clickInit();
    }

    void clickInit() {
        setOnMouseClicked(event -> {
            tvScroll.setVisible(false);
            HashMap<String, String> data = new HashMap<>();
            data.put("id", String.valueOf(id));
            String serverResponse = ApplicationManger.getServerResponse("tv", "play", data);
            if (serverResponse == null) return;
            JsonObject jsonObject = JsonParser.parseString(serverResponse).getAsJsonObject();
            String gameData = jsonObject.get("returnObject").getAsString();
            ArrayList<String> gameOrders = new ArrayList<>(Arrays.asList(gameData.split("\n")));
            StreamController.getInstance().setGameOrders(gameOrders);

            String rootPath = "file:" + System.getProperty("user.dir") + "/src/main/resources/FXML/";
            try {
                FXMLLoader loader = new FXMLLoader(new URL(rootPath + "gameStream.fxml"));
                AnchorPane root = loader.load();
                tv.setRoot(root);
                tv.setScaleX(0.7);
                tv.setScaleY(0.7);
                tv.setCamera(new PerspectiveCamera());
                tv.setVisible(true);
                tv.toFront();
                pauseButton.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    String text() {
        return "   " + nickname1 + "   VS   " + nickname2;
    }
}
