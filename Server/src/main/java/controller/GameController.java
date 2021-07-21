package controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controller.gameplay.GameManager;
import model.UserData;
import model.cards.Card;
import model.enums.Phase;
import view.menus.GamePlayScene;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameController {
    private static ArrayList<GameController> allGames = new ArrayList<>();
    private static int idCounter = 1;

    private GameManager gameManager;
    private Socket player1Socket, player2Socket;
    private String player1Token, player2Token;
    private int gameId;

    private DuelData duelData;
    private int currentRound;

    private File saveGame;

    static {
        File directoryPath = new File("src/resources/gameData");
        File[] filesList = directoryPath.listFiles();
        idCounter = filesList.length + 1;
    }

    public GameController(WaitingGame gameData, String duelGameData) {
        player1Socket = gameData.getUser1Socket();
        player2Socket = gameData.getUser2Socket();

        player1Token = gameData.getUser1Token();
        player2Token = gameData.getUser2Token();

        setupNetwork(player1Socket);
        setupNetwork(player2Socket);
        allGames.add(this);
        this.gameId = idCounter++;

        saveGame = new File("src/resources/gameData/" + gameId + ".txt");
        savaGameData(duelGameData);

        duelData = new DuelData(gameData.getRounds(), gameData.getUser1().getUserData(), gameData.getUser2().getUserData());
        currentRound = 1;

        new Thread(() -> gameManager = new GameManager(gameData.getUser1().getUserData(), gameData.getUser2().getUserData(), new GamePlayScene(), this)).start();

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
                        if (processClientMessage(serverMessage) == 0) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public synchronized int processClientMessage(String input) {
        System.out.println(input);
        String token, command;
        Matcher matcher;

        try {
            JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
            token = jsonObject.get("token").getAsString();
            command = jsonObject.get("command").getAsString();
        } catch (Exception e) {
            return 1;
        }
        int senderNumber = getSender(token);
        if (senderNumber == 0) {
            System.out.println("invalid token");
            return 1;
        }
        if (command.equals("surrender")) {
            gameManager.surrender(senderNumber);
            return 1;
        }
        if (command.equals("exit game")) {
            System.out.println("Exit game " + senderNumber);
            return 0;
        }

        if (gameManager.getCurrentPlayerTurn() != senderNumber) return 1;

        if (command.equals("next phase")) {
            gameManager.goToNextPhase();
            return 1;
        }
        matcher = Pattern.compile("select ([^,]+),([^$]+)").matcher(command);
        if (matcher.matches()) {
            try {
                gameManager.selectCard(matcher.group(1));
                switch (matcher.group(2)) {
                    case "summon":
                        gameManager.summonCard();
                        break;
                    case "set":
                        gameManager.setCard();
                        break;
                    default:
                        System.out.println("wrong command");
                }
            } catch (Exception e) {
                System.out.println("wrong address");
            }
        }
        return 1;
    }

    private int getSender(String token) {
        if (token.equals(player1Token)) {
            return 1;
        } else if (token.equals(player2Token)) {
            return 2;
        } else {
            return 0;
        }
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
        savaGameData(message);
    }

    private void savaGameData(String data) {
        try {
            FileWriter fileWriter = new FileWriter(saveGame, true);
            fileWriter.write(data + "\n");
            fileWriter.close();
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

    public void draw(int playerNumber, int deckCardNumber) {
        HashMap<String, String> data = new HashMap<>();
        data.put("playerNumber", Integer.valueOf(playerNumber).toString());
        data.put("deckCardNumber", Integer.valueOf(deckCardNumber).toString());

        sendMessageToBoth(getMessage("draw", data));
    }

    public void goToPhase(Phase toPhase, int currentPlayer) {
        HashMap<String, String> data = new HashMap<>();
        data.put("toPhase", toPhase.toString());
        data.put("currentPlayer", Integer.valueOf(currentPlayer).toString());
        sendMessageToBoth(getMessage("changePhase", data));
    }

    public void summon(int playerNumber, int handCardNumber, int toSlotNumber) {
        HashMap<String, String> data = new HashMap<>();
        data.put("playerNumber", Integer.valueOf(playerNumber).toString());
        data.put("handCardNumber", Integer.valueOf(handCardNumber).toString());
        data.put("toSlotNumber", Integer.valueOf(toSlotNumber).toString());
        sendMessageToBoth(getMessage("summon", data));
    }

    public void setMonster(int playerNumber, int handCardNumber, int toSlotNumber) {
        HashMap<String, String> data = new HashMap<>();
        data.put("playerNumber", Integer.valueOf(playerNumber).toString());
        data.put("handCardNumber", Integer.valueOf(handCardNumber).toString());
        data.put("toSlotNumber", Integer.valueOf(toSlotNumber).toString());
        sendMessageToBoth(getMessage("set", data));
    }

    public void gameFinished(int winnerNumber, int player1LP, int player2LP) {
        //isDuelStarted = false;
        String result = duelData.setRoundResult(winnerNumber, player1LP, player2LP);
        System.out.println(result);
        HashMap<String, String> data = new HashMap<>();
        data.put("resultMessage", result);
        data.put("winnerNumber", Integer.valueOf(duelData.getWinnerNumber()).toString());
        sendMessageToBoth(getMessage("gameFinishUI", data));

        if (!duelData.isFinished()) {
            currentRound++;
            System.out.println("Round " + currentRound);
            //gameManager = new GameManager(currentDuelData.firstPlayer, currentDuelData.secondPlayer, scene, this);

            //isDuelStarted = true;
            return;
        }

        duelData.applyDuelResult();
        System.out.println(duelData.getResultString());
    }

    public static ArrayList<GameController> getAllGames() {
        return allGames;
    }

    public int getGameId() {
        return gameId;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public class DuelData {
        private int rounds;
        private UserData firstPlayer, secondPlayer;

        private int firstPlayerWins, secondPlayerWins;
        private int maxLP1, maxLP2;

        private int winnerNumber;

        public DuelData(int rounds, UserData firstPlayer, UserData secondPlayer) {
            this.rounds = rounds;
            this.firstPlayer = firstPlayer;
            this.secondPlayer = secondPlayer;
            this.firstPlayerWins = 0;
            this.secondPlayerWins = 0;
            this.maxLP1 = 0;
            this.maxLP2 = 0;
        }

        public UserData getFirstPlayer() {
            return firstPlayer;
        }

        public UserData getSecondPlayer() {
            return secondPlayer;
        }

        public int getWinnerNumber() {
            return winnerNumber;
        }

        public String setRoundResult(int winnerNumber, int player1LP, int player2LP) {
            this.winnerNumber = winnerNumber;
            if (player1LP > maxLP1) maxLP1 = player1LP;
            if (player2LP > maxLP2) maxLP2 = player2LP;

            if (winnerNumber == 1) {
                firstPlayerWins++;
                return firstPlayer.getUsername() + " won the game and the score is: " + firstPlayerWins + "-" + secondPlayerWins;
            } else {
                secondPlayerWins++;

                return secondPlayer.getUsername() + " won the game and the score is: " + firstPlayerWins + "-" + secondPlayerWins;
            }
        }

        public String getResultString() {
            if (firstPlayerWins > secondPlayerWins) {
                return firstPlayer.getUsername() + " won the whole match with score: " + firstPlayerWins + "-" + secondPlayerWins;
            } else {

                return secondPlayer.getUsername() + " won the whole match with score: " + firstPlayerWins + "-" + secondPlayerWins;
            }
        }

        public void applyDuelResult() {
            if (firstPlayerWins > secondPlayerWins) {
                firstPlayer.addPoint(rounds * 1000);
                firstPlayer.addMoney(rounds * (1000 + maxLP1));
                secondPlayer.addMoney(rounds * (100));
            } else {
                secondPlayer.addPoint(rounds * 1000);
                secondPlayer.addMoney(rounds * (1000 + maxLP2));
                firstPlayer.addMoney(rounds * (100));
            }
        }

        public boolean isFinished() {
            if (firstPlayerWins == 2 || secondPlayerWins == 2) return true;
            return currentRound >= rounds;
        }
    }
}
