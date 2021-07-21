package view.menus;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import controller.ApplicationManger;
import controller.DuelController;
import controller.GamePlaySceneController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.UserData;
import model.enums.ChatType;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        ApplicationManger.getServerResponse("lobby", "enter", null);
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
        messages.setSpacing(10);
        HashMap<String, String> data = new HashMap<>();
        data.put("message", messageText.getText());
        data.put("type", "SEND");
        String result = ApplicationManger.getServerResponse("lobby", "send", data);
        JsonElement jsonElement = JsonParser.parseString(result);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (jsonObject.get("type").getAsString().equals("SUCCESSFUL")) {
            int id=Integer.parseInt(jsonObject.get("returnObject").getAsString());
            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setPrefWidth(435);
            hBox.setPrefHeight(50);
            Label message = new Label();
            Button deleteMessageButton = new Button();
            Button editMessageButton = new Button();
            message.setAlignment(Pos.CENTER);
            message.setStyle("-fx-background-color: white");
            message.setPrefWidth(320);
            deleteMessageButton.setPrefWidth(80);
            editMessageButton.setPrefWidth(50);
            message.setPrefHeight(70);
            deleteMessageButton.setPrefHeight(50);
            editMessageButton.setPrefHeight(50);
            message.setText(messageText.getText());
            deleteMessageButton.setText("delete");
            editMessageButton.setText("edit");
            hBox.getChildren().add(0, message);
            hBox.getChildren().add(1, deleteMessageButton);
            hBox.getChildren().add(2, editMessageButton);
            messages.getChildren().add(hBox);
            errorMessage.setOpacity(1);
            errorMessage.setText(jsonObject.get("message").getAsString());
            errorMessage.setTextFill(Color.GREEN);
            messageText.setText("");
            deleteMessageButton.setOnMouseClicked(event -> {
                HashMap<String, String> data1 = new HashMap<>();
                data1.put("id", String.valueOf(id));
                String result1=ApplicationManger.getServerResponse("lobby","delete",data1);
                JsonElement jsonElement1 = JsonParser.parseString(result1);
                JsonObject jsonObject1 = jsonElement1.getAsJsonObject();
                if (jsonObject1.get("type").getAsString().equals("ERROR")){
                    errorMessage.setText(jsonObject1.get("message").getAsString());
                    errorMessage.setTextFill(Color.RED);
                }
                else {
                    errorMessage.setText(jsonObject1.get("message").getAsString());
                    errorMessage.setTextFill(Color.GREEN);
                    messages.getChildren().remove(hBox);
                }
            });
            editMessageButton.setOnMouseClicked(event -> {

            });
        } else {
            errorMessage.setOpacity(1);
            errorMessage.setText(jsonObject.get("message").getAsString());
            errorMessage.setTextFill(Color.RED);
        }

    }

    public void refresh(ActionEvent actionEvent) {
        messages.setSpacing(10);
        String result=ApplicationManger.getServerResponse("lobby", "updateChat", null);
        JsonElement jsonElement = JsonParser.parseString(result);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (jsonObject.get("type").getAsString().equals("ERROR")){
            errorMessage.setOpacity(1);
            errorMessage.setText(jsonObject.get("message").getAsString());
            errorMessage.setTextFill(Color.RED);
        }
        else if(jsonObject.get("message").getAsString().equals("there is no new message")){
            errorMessage.setOpacity(1);
            errorMessage.setText(jsonObject.get("message").getAsString());
            errorMessage.setTextFill(Color.BLACK);
        }
        else {
            JsonArray jsonArray = JsonParser.parseString(jsonObject.get("returnObject").getAsString()).getAsJsonArray();
            String nickname,message,chatType;
            errorMessage.setOpacity(1);
            errorMessage.setText(jsonObject.get("message").getAsString());
            errorMessage.setTextFill(Color.WHITE);
            for (int j=0;j<jsonArray.size();j++){
                nickname=jsonArray.get(j).getAsJsonObject().get("senderNickname").getAsString();
                message=jsonArray.get(j).getAsJsonObject().get("message").getAsString();
                chatType=jsonArray.get(j).getAsJsonObject().get("chatType").getAsString();
                VBox vBox= new VBox();
                Label nicknameAndChatType=new Label();
                Label message1 = new Label();
                nicknameAndChatType.setStyle("-fx-background-color: white");
                message1.setStyle("-fx-background-color: white");
                nicknameAndChatType.setAlignment(Pos.TOP_LEFT);
                nicknameAndChatType.setTextFill(Color.BLUE);
                message1.setAlignment(Pos.CENTER);
                nicknameAndChatType.setPrefWidth(420);
                message1.setPrefWidth(420);
                if (chatType.equals("EDITED"))
                nicknameAndChatType.setText(nickname+" - "+chatType);
                else    nicknameAndChatType.setText(nickname);
                message1.setText(message);
                vBox.getChildren().add(0,nicknameAndChatType);
                vBox.getChildren().add(1, message1);
                messages.getChildren().add(vBox);
            }
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

    class Message {
        private final String message;
        private final String senderNickname;
        private final ChatType chatType;
//        private final int id;

        public Message(ChatType chatType, String message, String senderNickname) {
            this.message = message;
            this.chatType = chatType;
            this.senderNickname = senderNickname;
//            id = idCounter;
        }

        public ChatType getChatType() {
            return chatType;
        }

        public String getMessage() {
            return message;
        }

        public String getSenderNickname() {
            return senderNickname;
        }
    }
}

class messageBox extends VBox {

}
