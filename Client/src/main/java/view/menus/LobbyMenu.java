package view.menus;

import com.google.gson.*;
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

import java.io.IOException;
import java.util.ArrayList;
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
    public TextField editedMessage;
    public Label editedMessageLabel;
    public Button editButton;
    public Button cancelButton;
    private final ArrayList<Integer> idOfMessage = new ArrayList<>();

    private static final int idCounter = 0;

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
            cancelButton.setVisible(true);
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
                    } else if (type1.equals("CANCEL")) {
                        this.message.setText("");
                        cancelButton.setVisible(false);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            this.message.setText(responseMessage);
        }
    }

    public void cancel() {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("controller", "newGame");
            jsonObject.addProperty("method", "cancel");
            jsonObject.addProperty("token", ApplicationManger.getToken());
            ApplicationManger.getDataOutputStream().writeUTF(jsonObject.toString());
            ApplicationManger.getDataOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
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
            int id = Integer.parseInt(jsonObject.get("returnObject").getAsString());
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
            idOfMessage.add(id);
            System.out.println(idOfMessage);
            errorMessage.setOpacity(1);
            errorMessage.setText(jsonObject.get("message").getAsString());
            errorMessage.setTextFill(Color.GREEN);
            messageText.setText("");
            deleteMessageButton.setOnMouseClicked(event -> {
                HashMap<String, String> data1 = new HashMap<>();
                data1.put("actionId", String.valueOf(id));
                data1.put("message", "deleted a message");
                data1.put("type", "DELETED");
                String result1 = ApplicationManger.getServerResponse("lobby", "send", data1);
                JsonElement jsonElement1 = JsonParser.parseString(result1);
                JsonObject jsonObject1 = jsonElement1.getAsJsonObject();
                if (jsonObject1.get("type").getAsString().equals("ERROR")) {
                    errorMessage.setText(jsonObject1.get("message").getAsString());
                    errorMessage.setTextFill(Color.RED);
                } else {
                    errorMessage.setText(jsonObject1.get("message").getAsString());
                    errorMessage.setTextFill(Color.GREEN);
                }
                messages.getChildren().remove(idOfMessage.indexOf(id));
                Label delete = new Label();
                delete.setPrefWidth(435);
                delete.setPrefHeight(50);
                delete.setAlignment(Pos.CENTER);
                delete.setTextFill(Color.BLUE);
                delete.setText("you delete this message");
                messages.getChildren().add(delete);
                idOfMessage.add(Integer.parseInt(jsonObject1.get("returnObject").getAsString()));
                idOfMessage.remove((Integer) idOfMessage.indexOf(id));
                System.out.println(idOfMessage);
                errorMessage.setText("message deleted successfully");
            });
            editMessageButton.setOnMouseClicked(event -> {
                editedMessage.setOpacity(1);
                editedMessageLabel.setOpacity(1);
                editButton.setOpacity(1);
                editButton.setOnMouseClicked(event1 -> {
                    HashMap<String, String> data2 = new HashMap<>();
                    data2.put("actionId", String.valueOf(id));
                    data2.put("message", editedMessage.getText());
                    data2.put("type", "EDITED");
                    String result2 = ApplicationManger.getServerResponse("lobby", "send", data2);
                    JsonElement jsonElement1 = JsonParser.parseString(result2);
                    JsonObject jsonObject1 = jsonElement1.getAsJsonObject();
                    if (jsonObject1.get("type").getAsString().equals("ERROR")) {
                        errorMessage.setText(jsonObject1.get("message").getAsString());
                        errorMessage.setTextFill(Color.RED);
                    } else {
                        errorMessage.setText(jsonObject1.get("message").getAsString());
                        errorMessage.setTextFill(Color.GREEN);
                    }
                    HBox hBox1 = (HBox) messages.getChildren().get(idOfMessage.indexOf(id));
                    hBox1.getChildren().remove(0);
                    messages.getChildren().remove(idOfMessage.indexOf(id));
                    message.setText(editedMessage.getText());
                    hBox1.getChildren().add(0, message);
                    messages.getChildren().add(hBox1);
                    idOfMessage.remove((Integer) idOfMessage.indexOf(id));
                    System.out.println(idOfMessage);
                    errorMessage.setText("message edited successfully");
                    editedMessage.setText("");
                    editedMessage.setOpacity(0);
                    editedMessageLabel.setOpacity(0);
                    editButton.setOpacity(0);
                });
            });
        } else {
            errorMessage.setOpacity(1);
            errorMessage.setText(jsonObject.get("message").getAsString());
            errorMessage.setTextFill(Color.RED);
        }

    }

    public void refresh(ActionEvent actionEvent) {
        messages.setSpacing(10);
        String result = ApplicationManger.getServerResponse("lobby", "updateChat", null);
        JsonElement jsonElement = JsonParser.parseString(result);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (jsonObject.get("type").getAsString().equals("ERROR")) {
            errorMessage.setOpacity(1);
            errorMessage.setText(jsonObject.get("message").getAsString());
            errorMessage.setTextFill(Color.RED);
        } else if (jsonObject.get("message").getAsString().equals("there is no new message")) {
            errorMessage.setOpacity(1);
            errorMessage.setText(jsonObject.get("message").getAsString());
            errorMessage.setTextFill(Color.BLACK);
        } else {
            JsonArray jsonArray = JsonParser.parseString(jsonObject.get("returnObject").getAsString()).getAsJsonArray();
            String nickname, message, chatType, id;
            errorMessage.setOpacity(1);
            errorMessage.setText(jsonObject.get("message").getAsString());
            errorMessage.setTextFill(Color.BLACK);
            for (int j = 0; j < jsonArray.size(); j++) {
                nickname = jsonArray.get(j).getAsJsonObject().get("senderNickname").getAsString();
                message = jsonArray.get(j).getAsJsonObject().get("message").getAsString();
                chatType = jsonArray.get(j).getAsJsonObject().get("chatType").getAsString();
                id = jsonArray.get(j).getAsJsonObject().get("id").getAsString();

                VBox vBox = new VBox();
                Label nicknameAndChatType = new Label();
                Label message1 = new Label();
                nicknameAndChatType.setStyle("-fx-background-color: white");
                message1.setStyle("-fx-background-color: white");
                nicknameAndChatType.setAlignment(Pos.TOP_LEFT);
                nicknameAndChatType.setTextFill(Color.BLUE);
                message1.setAlignment(Pos.CENTER);
                nicknameAndChatType.setPrefWidth(420);
                message1.setPrefWidth(420);
                if (chatType.equals("EDITED"))
                    nicknameAndChatType.setText("  " + nickname + " - " + chatType);
                else nicknameAndChatType.setText(nickname);
                message1.setText(message);
                if (chatType.equals("DELETED")) {
                    message1.setTextFill(Color.BLUE);
                }
                vBox.getChildren().add(0, nicknameAndChatType);
                vBox.getChildren().add(1, message1);
                messages.getChildren().add(vBox);
                idOfMessage.add(Integer.parseInt(id));
                System.out.println(idOfMessage);
                if (chatType.equals("EDITED")) {
                    Integer id1 = Integer.parseInt(jsonArray.get(j).getAsJsonObject().get("actionId").getAsString());
                    messages.getChildren().remove(idOfMessage.indexOf(id1));
                    idOfMessage.remove((Integer) idOfMessage.indexOf(id1));
                    System.out.println(idOfMessage);
                } else if (chatType.equals("DELETED")) {
                    Integer id2 = Integer.parseInt(jsonArray.get(j).getAsJsonObject().get("actionId").getAsString());
                    messages.getChildren().remove(idOfMessage.indexOf(id2));
                    idOfMessage.remove((Integer) idOfMessage.indexOf(id2));
                    System.out.println(idOfMessage);
                }
            }
        }
    }


    public class DuelData {
        private final UserData user1Data;
        private final UserData user2Data;
        private final int rounds;

        public DuelData(UserData user1Data, UserData user2Data, int rounds) {
            this.user1Data = user1Data;
            this.user2Data = user2Data;
            this.rounds = rounds;
        }

        public UserData getUser1Data() {
            return user1Data;
        }

        public UserData getUser2Data() {
            return user2Data;
        }

        public int getRounds() {
            return rounds;
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
