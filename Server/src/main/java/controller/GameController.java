package controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import controller.gameplay.GameManager;
import model.cards.Card;
import view.menus.GamePlayScene;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class GameController {
    private static ArrayList<GameController> allGames = new ArrayList<>();

    private GameManager gameManager;
    private Socket player1Socket, player2Socket;

    public GameController(WaitingGame gameData) {
        player1Socket = gameData.getUser1Socket();
        player2Socket = gameData.getUser2Socket();

        //setupNetwork(player1Socket);
        //setupNetwork(player2Socket);
        allGames.add(this);

        new Thread(()->{
            gameManager = new GameManager(gameData.getUser1().getUserData(), gameData.getUser2().getUserData(), new GamePlayScene(), this);
        }).start();

        /*new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    sendMessageToBoth("Test");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }

    private void setupNetwork(Socket socket) {
        new Thread(() -> {
            try {
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                while (true) {
                    try {
                        String serverMessage = inputStream.readUTF();
                        processClientMessage(serverMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void processClientMessage(String input) {
        System.out.println(input);
    }

    private void sendMessageToBoth(String message) {
        System.out.println(message);
        DataOutputStream dataOutputStream;
        try {
            dataOutputStream = new DataOutputStream(player1Socket.getOutputStream());
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
            dataOutputStream = new DataOutputStream(player2Socket.getOutputStream());
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getMessage(String methodName, HashMap<String, String> data) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", methodName);
        if (data != null) {
            for (String key : data.keySet()) {
                jsonObject.addProperty(key, data.get(key));
            }
        }
        return jsonObject.toString();
    }

    public void firstSetupBoardGraphic(int playerNumber, ArrayList<Card> cards) {
        JsonArray cardIds = new JsonArray();

        for (Card c : cards) {
            cardIds.add(c.getCardData().getCardId());
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("playerNumber", Integer.valueOf(playerNumber).toString());
        data.put("cardIds", cardIds.toString());

        sendMessageToBoth(getMessage("firstSetupBoardGraphic", data));
    }
}
