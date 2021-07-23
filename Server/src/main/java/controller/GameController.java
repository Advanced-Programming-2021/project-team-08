package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controller.gameplay.GameManager;
import model.GameData;
import model.UserData;
import model.cards.Card;
import model.enums.Phase;
import model.gameplay.AttackResult;
import view.menus.GamePlayScene;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameController {
    private static final ArrayList<GameController> allGames = new ArrayList<>();
    private static int idCounter = 1;

    private GameManager gameManager;
    private final Socket player1Socket;
    private final Socket player2Socket;
    private final String player1Token;
    private final String player2Token;
    private final int gameId;

    private final DuelData duelData;
    private int currentRound;

    private final File saveGame;

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
                        String clientMessage = inputStream.readUTF();
                        System.out.println(clientMessage);
                        if (processClientMessage(clientMessage) == 0) break;
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

    public int processClientMessage(String input) {
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
            exitGame(senderNumber);
            return 0;
        }

        if (gameManager.getCurrentPlayerTurn() != senderNumber) return 1;

        if (command.equals("next phase")) {
            gameManager.goToNextPhase();
            return 1;
        }
        matcher = Pattern.compile("select ([^,]+),([^ $]+)( ([1-5]))*").matcher(command);
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
                    case "attack":
                        gameManager.attack(Integer.parseInt(matcher.group(4)));
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

    private void exitGame(int senderNumber) {
        System.out.println("Exit game " + senderNumber);
        if (senderNumber == 1) {
            sendMessageToOne("successful exit", senderNumber);
            ServerManager.getIsInGame().put(player1Socket.getRemoteSocketAddress().toString(), false);
            ServerThread serverThread = new ServerThread(player1Socket, ServerManager.getServerSocket());
            serverThread.start();
        } else if (senderNumber == 2) {
            sendMessageToOne("successful exit", senderNumber);
            ServerManager.getIsInGame().put(player2Socket.getRemoteSocketAddress().toString(), false);
            ServerThread serverThread = new ServerThread(player2Socket, ServerManager.getServerSocket());
            serverThread.start();
        }
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

    private void sendMessageToOne(String message, int playerNumber) {
        System.out.println(message);
        DataOutputStream dataOutputStream;
        if (playerNumber == 1) {
            try {
                dataOutputStream = new DataOutputStream(player1Socket.getOutputStream());
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (playerNumber == 2) {
            try {
                dataOutputStream = new DataOutputStream(player2Socket.getOutputStream());
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public void applyAttackResultGraphic(AttackResult result, int playerNumber, int attackerCardNumber, int defenderCardNumber) {
        AttackResultJson resultJson = new AttackResultJson(result);
        HashMap<String, String> data = new HashMap<>();
        data.put("result", new Gson().toJson(resultJson));
        data.put("playerNumber", Integer.valueOf(playerNumber).toString());
        data.put("attackerCardNumber", Integer.valueOf(attackerCardNumber).toString());
        data.put("defenderCardNumber", Integer.valueOf(defenderCardNumber).toString());
        sendMessageToBoth(getMessage("applyAttackResultGraphic", data));
    }

    class AttackResultJson {
        private final int attackerPlayerNumber;
        private final int attackedPlayerNumber;
        private final int player1LPDecrease;
        private final int player2LPDecrease;
        private final boolean destroyCard1;
        private final boolean destroyCard2;
        private final boolean attackedFlip;

        public AttackResultJson(AttackResult attackResult) {
            attackerPlayerNumber = attackResult.getAttackerPlayer().getPlayerNumber();
            attackedPlayerNumber = attackResult.getAttackedPlayer().getPlayerNumber();
            player1LPDecrease = attackResult.getPlayer1LPDecrease();
            player2LPDecrease = attackResult.getPlayer2LPDecrease();
            destroyCard1 = attackResult.isDestroyCard1();
            destroyCard2 = attackResult.isDestroyCard2();
            attackedFlip = attackResult.isAttackedFlip();
        }
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
        savaGameData(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "|end");
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
        private final int rounds;
        private final UserData firstPlayer;
        private final UserData secondPlayer;

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

    public String getGameSave() throws IOException {
        return new String(Files.readAllBytes(Paths.get(saveGame.getPath())));
    }

    public static ArrayList<GameData> getGameList() {
        ArrayList<GameData> savedGameData = new ArrayList<>();
        System.out.println(idCounter);
        try {
            for (int i = 1; i < idCounter; i++) {
                String gameData = new String(Files.readAllBytes(Paths.get("src/resources/gameData/" + i + ".txt")));
                if (!gameData.endsWith("end")) continue;
                String data = new Scanner(gameData).nextLine();
                SendDuelData saveData = new Gson().fromJson(data, SendDuelData.class);
                GameData gameData1 = new GameData(i, saveData.getUser1Data().getNickname(), saveData.getUser2Data().getNickname());
                savedGameData.add(gameData1);
                System.out.println("one time loop done");
            }
            return savedGameData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getGameData(int id) throws IOException {
        String gameData = new String(Files.readAllBytes(Paths.get("src/resources/gameData/" + id + ".txt")));
        if (!gameData.endsWith("end")) return null;
        return gameData;
    }
}
