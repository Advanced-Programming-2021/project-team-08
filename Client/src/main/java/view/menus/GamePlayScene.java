package view.menus;

import com.google.gson.*;
import controller.ApplicationManger;
import controller.DuelController;
import controller.GamePlaySceneController;
import controller.SoundManager;
import controller.gameplay.GameManager;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import model.animation.CoinAnimation;
import model.animation.FlipCardAnimation;
import model.animation.RotateCenterTransition;
import model.cards.Card;
import model.cards.data.CardData;
import model.cards.data.MonsterCardData;
import model.effectSystem.EquipEffect;
import model.enums.CardStatus;
import model.enums.Phase;
import model.gameplay.AttackResult;
import model.gameplay.Player;
import model.graphic.GraphicCard;
import model.graphic.GraphicCardSlot;
import model.graphic.graphicBoard;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GamePlayScene {
    public ImageView player1Avatar;
    public ImageView player2Avatar;
    public Label player1Nickname_T;
    public Label player2Nickname_T;
    public Label player1LP_T;
    public Label player2LP_T;
    public ImageView player1LP_bar;
    public ImageView player2LP_bar;
    public AnchorPane board;
    public AnchorPane root;
    public SubScene showCard;

    public Label currentPhaseLabel;
    public Label playerNumberLabel;
    public Button nextPhaseButton;

    public Button muteButton;
    public AnchorPane pausePanel;

    public AnchorPane overlayPanel;

    public AnchorPane endGamePanel;
    public Label gameEndMessage;
    public Button gameEndExitButton;

    private static GamePlayScene instance;

    public static GamePlayScene getInstance() {
        return instance;
    }

    private graphicBoard gBoard;
    private int playerNumber;

    private GamePlaySceneController sceneController;
    private boolean waitForAI = false;
    Scanner scanner = new Scanner(System.in);

    private int currentTurnPlayer;
    private Phase currentPhase;

    private boolean isAI;
    private boolean coinFlipped = false;

    private GamePlaySceneController.DuelData thisDuelData;

    public void setWaitForAI(boolean waitForAI) {
        this.waitForAI = waitForAI;
    }

    public GamePlaySceneController getSceneController() {
        return sceneController;
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public boolean isAI() {
        return isAI;
    }

    public graphicBoard getgBoard() {
        return gBoard;
    }

    @FXML
    public void initialize() {
        sceneController = new GamePlaySceneController(this);
        instance = this;
        gBoard = new graphicBoard(board);
        GamePlaySceneController.DuelData data = DuelController.getCurrentDuelData();
        thisDuelData = data;
        this.playerNumber = Integer.parseInt(playerNumberLabel.getText());

        String rootPath = "file:" + System.getProperty("user.dir") + "/src/main/resources/FXML/";
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(new URL(rootPath + "cardView.fxml"));
            AnchorPane anchorPane = loader.load();
            showCard.setRoot(anchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }

        isAI = !data.isPlayer();
        if (isAI) {
            try {
                new Thread(() -> new GameManager(data, this)).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setupNetwork();
        }

        firstSetupUI(data);
    }

    private void setupNetwork() {
        new Thread(() -> {
            while (true) {
                try {
                    String serverMessage = ApplicationManger.getDataInputStream().readUTF();
                    processServerMessage(serverMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
    }

    private void processServerMessage(String message) {
        System.out.println(message);
        JsonObject json = JsonParser.parseString(message).getAsJsonObject();
        String methodName = json.get("method").getAsString();
        int playerNumber, a, b;
        switch (methodName) {
            case "firstSetupBoardGraphic":
                playerNumber = Integer.parseInt(json.get("playerNumber").getAsString());
                JsonArray cardIds = JsonParser.parseString(json.get("cardIds").getAsString()).getAsJsonArray();
                Platform.runLater(() -> firstSetupBoardGraphic(playerNumber, cardIds));
                break;
            case "draw":
                playerNumber = Integer.parseInt(json.get("playerNumber").getAsString());
                int deckCardNumber = Integer.parseInt(json.get("deckCardNumber").getAsString());
                Platform.runLater(() -> draw(playerNumber, deckCardNumber, null));
                break;
            case "changePhase":
                Phase phase = Phase.valueOf(json.get("toPhase").getAsString());
                int currentPlayer = Integer.parseInt(json.get("currentPlayer").getAsString());
                Platform.runLater(() -> changePhase(phase, currentPlayer));
                break;
            case "summon":
                playerNumber = Integer.parseInt(json.get("playerNumber").getAsString());
                a = Integer.parseInt(json.get("handCardNumber").getAsString());
                b = Integer.parseInt(json.get("toSlotNumber").getAsString());
                Platform.runLater(() -> summon(playerNumber, a, b));
                break;
            case "set":
                playerNumber = Integer.parseInt(json.get("playerNumber").getAsString());
                a = Integer.parseInt(json.get("handCardNumber").getAsString());
                b = Integer.parseInt(json.get("toSlotNumber").getAsString());
                Platform.runLater(() -> setMonster(playerNumber, a, b));
                break;
            case "applyAttackResultGraphic":
                AttackResultJson result = new Gson().fromJson(JsonParser.parseString(json.get("result").getAsString()).getAsJsonObject(), AttackResultJson.class);
                playerNumber = Integer.parseInt(json.get("playerNumber").getAsString());
                a = Integer.parseInt(json.get("attackerCardNumber").getAsString());
                b = Integer.parseInt(json.get("defenderCardNumber").getAsString());
                Platform.runLater(() -> applyAttackResultGraphic(result, playerNumber, a, b));
                break;
            case "gameFinishUI":
                String resultMessage = json.get("resultMessage").getAsString();
                int winnerNumber = Integer.parseInt(json.get("winnerNumber").getAsString());
                Platform.runLater(() -> gameFinishUI(resultMessage, winnerNumber));
                break;
            default:
                System.out.println("unknown");
        }
    }

    public void sendMessageToServer(String message) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("token", ApplicationManger.getToken());
        jsonObject.addProperty("command", message);

        System.out.println(jsonObject);
        try {
            ApplicationManger.getDataOutputStream().writeUTF(jsonObject.toString());
            ApplicationManger.getDataOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void firstSetupUI(GamePlaySceneController.DuelData data) {
        player1Nickname_T.setText(data.getFirstPlayer().getUsername() + " (" + data.getFirstPlayer().getNickname() + ")");
        player2Nickname_T.setText(data.getSecondPlayer().getUsername() + " (" + data.getSecondPlayer().getNickname() + ")");
        player1Avatar.setImage(data.getFirstPlayer().getProfileImage());
        player2Avatar.setImage(data.getSecondPlayer().getProfileImage());
        player1LP_T.setText("LP      8000");
        player2LP_T.setText("LP      8000");
    }

    public void coinFlip(int result) {
        overlayPanel.setVisible(true);
        Image coinImage = new Image("file:" + System.getProperty("user.dir") + "/src/main/resources/asset/gameplay/coin.png");
        ImageView coin = new ImageView(coinImage);
        coin.setFitWidth(200);
        coin.setFitHeight(200);
        overlayPanel.getChildren().add(coin);
        coin.setLayoutX(700);
        coin.setLayoutY(350);

        CoinAnimation coinAnimation = new CoinAnimation(coinImage, coin,
                new Rectangle2D(355, 260, 370, 370),
                new Rectangle2D(0, 0, 370, 370),
                1260 + 180 * result);
        coinAnimation.setOnFinished(event -> {
            Label label = new Label("player " + ((result == 1) ? "blue" : "red") + " goes first");
            label.setStyle("-fx-font-size: 40; -fx-font-family: Heebeo; -fx-text-fill: " + ((result == 1) ? "blue" : "red") + ";");
            label.setPrefWidth(600);
            label.setLayoutX(500);
            label.setLayoutY(600);
            label.setAlignment(Pos.CENTER);
            overlayPanel.getChildren().add(label);
            new Thread(() -> {
                try {
                    Thread.sleep(2500);
                    overlayPanel.setVisible(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
        coinAnimation.play();
    }

    public void changePhase(Phase toPhase, int currentPlayer) {
        if (!coinFlipped) {
            new Thread(() -> {
                Platform.runLater(() -> coinFlip(currentPlayer));
                coinFlipped = true;
                try {
                    Thread.sleep(4500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                changePhase(toPhase, currentPlayer);
            }).start();
        }
        currentPhase = toPhase;
        currentTurnPlayer = currentPlayer;
        currentPhaseLabel.setText(toPhase.toString().replace("_", " "));
        if (currentPlayer == 1) {
            currentPhaseLabel.getStyleClass().clear();
            currentPhaseLabel.getStyleClass().add("bluePlayer");
        } else {
            currentPhaseLabel.getStyleClass().clear();
            currentPhaseLabel.getStyleClass().add("redPlayer");
        }
        if (currentPlayer != playerNumber) {
            nextPhaseButton.setVisible(false);
        } else {
            nextPhaseButton.setVisible(true);
        }
    }

    private int cheatCommand(String userInput) {
        if (sceneController.isDuelStarted()) {
            Matcher matcher = Pattern.compile("add card to hand ([^\\n]+)").matcher(userInput);
            if (matcher.matches()) {
                sceneController.getGameManager().addCard_C(matcher.group(1));
                return 1;
            }
            matcher = Pattern.compile("increase --LP ([0-9]+)").matcher(userInput);
            if (matcher.matches()) {
                sceneController.getGameManager().increaseLP_C(Integer.parseInt(matcher.group(1)));
                return 1;
            }
            matcher = Pattern.compile("duel set-winner ([^\\n]+)").matcher(userInput);
            if (matcher.matches()) {
                sceneController.getGameManager().setWinner_C(matcher.group(1));
                return 1;
            }
        }
        return 0;
    }

    public void showGraveyard(ArrayList<Card> cards) {
        if (cards.size() == 0) {
            System.out.println("graveyard empty");
        } else {
            for (int i = 1; i <= cards.size(); i++) {
                System.out.println(i + ". " + cards.get(i - 1).toString());
            }
        }
    }

    public int getTributeCommand() throws Exception {
        System.out.println("enter tribute monster number:");
        String input = scanner.nextLine();
        if (input.equals("cancel")) {
            throw new Exception("operation canceled");
        }
        return Integer.parseInt(input);
    }

    public boolean getActivateTrapCommand() {
        System.out.println("do you want to activate your trap and spell?");
        String input = scanner.nextLine();
        while (!input.equals("yes") && !input.equals("no")) {
            System.out.println("you should enter yes/no");
            input = scanner.nextLine();
        }
        return input.equals("yes");
    }

    public Card getSelectedMonsterCard(EquipEffect equipEffect, Player player) {
        System.out.println("enter the card that you want to equip");
        String input = scanner.nextLine().trim();
        while (!input.equals("1") && !input.equals("2") && !input.equals("3") && !input.equals("4") && !input.equals("5")) {
            System.out.println("you should enter a number between 1 and 5");
            input = scanner.nextLine().trim();
        }
        int number = Integer.parseInt(input) - 1;
        try {
            if (equipEffect.hasCardCondition((MonsterCardData) player.getPlayerBoard().getMonsterZone().get(number).getCard().getCardData())) {
                return player.getPlayerBoard().getMonsterZone().get(number).getCard();
            } else {
                System.out.println("selected card has not the condition");
                return getSelectedMonsterCard(equipEffect, player);
            }
        } catch (Exception e) {
            return getSelectedMonsterCard(equipEffect, player);
        }
    }

    public boolean getDestroyingAMonsterCommand() {
        System.out.println("do you want to destroy an opponent monster?");
        String input = scanner.nextLine();
        while (!input.equals("yes") && !input.equals("no")) {
            System.out.println("you should enter yes/no");
            input = scanner.nextLine();
        }
        return input.equals("yes");
    }

    public int getPlaceOfMonster() {
        System.out.println("insert number of place of monster that you want to destroy.");
        String input = scanner.nextLine();
        while (!input.equals("1") && !input.equals("2") && !input.equals("3") && !input.equals("4") && !input.equals("5")) {
            System.out.println("you should enter a number between 1 to 5.");
            input = scanner.nextLine();
        }
        return Integer.parseInt(input);
    }

    public int getPlaceOfMonsterTransfer() {
        System.out.println("insert number of place of monster that you want to transfer.");
        String input = scanner.nextLine();
        while (!input.equals("1") && !input.equals("2") && !input.equals("3") && !input.equals("4") && !input.equals("5")) {
            System.out.println("you should enter a number between 1 to 5.");
            input = scanner.nextLine();
        }
        return Integer.parseInt(input);
    }

    public int throwAwayACardFromHand(int numberOfCardsInHand) {
        System.out.println("insert number of card in your hand that you want to trow away.");
        String input = scanner.nextLine();
        Matcher matcher;
        matcher = Pattern.compile("([\\d]+)").matcher(input);
        while (!matcher.matches() || Integer.parseInt(matcher.group(1)) > numberOfCardsInHand) {
            System.out.println("you should insert a number from 1 to" + numberOfCardsInHand);
            input = scanner.nextLine();
            matcher = Pattern.compile("([\\d]+)").matcher(input);
        }
        return Integer.parseInt(matcher.group(1));
    }

    public void nextPhase() {
        if (isAI) {
            GameManager.getInstance().goToNextPhase();
        } else {
            sendMessageToServer("next phase");
        }
    }

    public void firstSetupBoardGraphic(int playerNumber, JsonArray cardIds) {
        ArrayList<CardData> cards = new ArrayList<>();
        for (JsonElement id : cardIds) {
            cards.add(CardData.getCardById(id.getAsInt()));
        }
        firstSetupBoardGraphic(playerNumber, cards);
    }

    public void firstSetupBoardGraphic(int playerNumber, ArrayList<CardData> cards) {
        double z = cards.size();
        for (int i = 0; i < z; i++) {
            GraphicCard gc = new GraphicCard(cards.get(i), playerNumber);
            gBoard.getPlayerBoard(playerNumber).getDeck().appendCard(gc);
            gBoard.getPlayerBoard(playerNumber).getPlayerBoard().getChildren().add(gc.getShape());
            gc.getShape().setTranslateX(gBoard.getPlayerBoard(playerNumber).getDeck().getImageView().getLayoutX() + 8);
            gc.getShape().setTranslateY(gBoard.getPlayerBoard(playerNumber).getDeck().getImageView().getLayoutY() + 5);
            gc.getShape().setTranslateZ(-(double) i / 3);
        }
    }

    public void draw(int playerNumber, int deckCardNumber, EventHandler<ActionEvent> onEnd) {
        graphicBoard.GraphicPlayerBoard playerBoard = gBoard.getPlayerBoard(playerNumber);
        GraphicCard c = playerBoard.getDeck().getAllCards().get(deckCardNumber);

        ArrayList<GraphicCard> pre = playerBoard.getHand().getAllCards();

        playerBoard.getDeck().getAllCards().remove(deckCardNumber);
        playerBoard.getHand().appendCard(c);

        for (GraphicCard card : pre) {
            TranslateTransition previousCards = new TranslateTransition();
            previousCards.setDuration(Duration.millis(400));
            previousCards.setNode(card.getShape());
            previousCards.setByX(-42);
            previousCards.play();
        }

        TranslateTransition thisCard = new TranslateTransition();
        thisCard.setDuration(Duration.millis(800));
        thisCard.setNode(c.getShape());
        thisCard.setToX(playerBoard.getHand().getImageView().getLayoutX() + pre.size() * 42 - 73);
        thisCard.setToY(playerBoard.getHand().getImageView().getLayoutY());

        RotateCenterTransition rotateTransition = new RotateCenterTransition(c.getShape(), 800, 45, Rotate.X_AXIS);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(thisCard, rotateTransition);

        if (playerNumber == this.playerNumber) {
            FlipCardAnimation flipCardAnimation = new FlipCardAnimation(c, 300, CardStatus.FACE_UP);
            parallelTransition.getChildren().add(flipCardAnimation);
        }

        parallelTransition.setOnFinished(onEnd);
        parallelTransition.play();
        SoundManager.playSound("draw");
    }

    public void summon(int playerNumber, int handCardNumber, int toSlotNumber) {
        graphicBoard.GraphicPlayerBoard playerBoard = gBoard.getPlayerBoard(playerNumber);
        GraphicCard c = playerBoard.getHand().getAllCards().get(handCardNumber - 1);
        GraphicCardSlot slot = playerBoard.getMonster(toSlotNumber);

        removeCardFromHand(handCardNumber, playerBoard);

        playerBoard.getHand().getAllCards().remove(c);
        playerBoard.getMonster(toSlotNumber).setCard(c);

        TranslateTransition thisCard = new TranslateTransition();
        thisCard.setDuration(Duration.millis(800));
        thisCard.setNode(c.getShape());
        thisCard.setToX(slot.getImageView().getLayoutX() + 144);
        if (playerNumber == this.playerNumber) {
            thisCard.setToY(slot.getImageView().getLayoutY() + 45);
        } else {
            thisCard.setToY(slot.getImageView().getLayoutY() + 35);
        }

        ParallelTransition parallelTransition = new ParallelTransition();

        RotateCenterTransition rotateTransition;
        if (playerNumber != this.playerNumber) {
            rotateTransition = new RotateCenterTransition(c.getShape(), 800, -45, Rotate.X_AXIS);
        } else {
            rotateTransition = new RotateCenterTransition(c.getShape(), 800, 45, Rotate.X_AXIS);
        }
        parallelTransition.getChildren().addAll(thisCard, rotateTransition);

        if (playerNumber != this.playerNumber) {
            FlipCardAnimation flipCardAnimation = new FlipCardAnimation(c, 300, CardStatus.FACE_UP);
            parallelTransition.getChildren().add(flipCardAnimation);
        }

        parallelTransition.play();
        SoundManager.playSound("summon");
    }

    public void setMonster(int playerNumber, int handCardNumber, int toSlotNumber) {
        graphicBoard.GraphicPlayerBoard playerBoard = gBoard.getPlayerBoard(playerNumber);
        GraphicCard c = playerBoard.getHand().getAllCards().get(handCardNumber - 1);
        GraphicCardSlot slot = playerBoard.getMonster(toSlotNumber);

        removeCardFromHand(handCardNumber, playerBoard);

        playerBoard.getHand().getAllCards().remove(c);
        playerBoard.getMonster(toSlotNumber).setCard(c);
        c.setToAttackPosition(false);

        TranslateTransition thisCard = new TranslateTransition();
        thisCard.setDuration(Duration.millis(800));
        thisCard.setNode(c.getShape());
        thisCard.setToX(slot.getImageView().getLayoutX() + 144);
        if (playerNumber == this.playerNumber) {
            thisCard.setToY(slot.getImageView().getLayoutY() + 45);
        } else {
            thisCard.setToY(slot.getImageView().getLayoutY() + 35);
        }

        RotateCenterTransition rotateTransition;
        if (playerNumber != this.playerNumber) {
            rotateTransition = new RotateCenterTransition(c.getShape(), 800, -45, Rotate.X_AXIS);
        } else {
            rotateTransition = new RotateCenterTransition(c.getShape(), 800, 45, Rotate.X_AXIS);
        }

        RotateCenterTransition rotateTransition1 = new RotateCenterTransition(c.getShape(), 800, -90, Rotate.Z_AXIS);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(thisCard, rotateTransition, rotateTransition1);

        if (playerNumber == this.playerNumber) {
            FlipCardAnimation flipCardAnimation = new FlipCardAnimation(c, 300, CardStatus.TO_BACK);
            SequentialTransition s = new SequentialTransition();
            s.getChildren().add(flipCardAnimation);
            s.getChildren().add(parallelTransition);
            s.play();
        } else {
            parallelTransition.play();
        }
        SoundManager.playSound("set");
    }

    private void removeCardFromHand(int handCardNumber, graphicBoard.GraphicPlayerBoard playerBoard) {
        ArrayList<GraphicCard> handCards = playerBoard.getHand().getAllCards();
        int n = handCards.size();

        for (int i = handCardNumber; i < n; i++) {
            TranslateTransition previousCards = new TranslateTransition();
            previousCards.setDuration(Duration.millis(400));
            previousCards.setNode(handCards.get(i).getShape());
            previousCards.setByX(-42);
            previousCards.play();
        }
        for (int i = handCardNumber - 2; i >= 0; i--) {
            TranslateTransition previousCards = new TranslateTransition();
            previousCards.setDuration(Duration.millis(400));
            previousCards.setNode(handCards.get(i).getShape());
            previousCards.setByX(42);
            previousCards.play();
        }
    }

    public void applyAttackResultGraphic(AttackResultJson result, int playerNumber, int attackerCardNumber, int defenderCardNumber) {
        graphicBoard.GraphicPlayerBoard attackerBoard = gBoard.getPlayerBoard(playerNumber);
        graphicBoard.GraphicPlayerBoard defenderBoard = gBoard.getPlayerBoard(playerNumber == 1 ? 2 : 1);

        if (result.isAttackedFlip()) {
            GraphicCard gc = defenderBoard.getMonster(defenderCardNumber).getCard();
            FlipCardAnimation flipCardAnimation = new FlipCardAnimation(gc, 300, CardStatus.FACE_UP);
            flipCardAnimation.play();
        }

        changePlayerLP(result.getAttackerPlayerNumber(), -result.getPlayer1LPDecrease());
        changePlayerLP(result.getAttackedPlayerNumber(), -result.getPlayer2LPDecrease());

        if (result.isDestroyCard1()) {
            attackerBoard.moveToGraveyard(attackerCardNumber);
        }
        if (result.isDestroyCard2()) {
            defenderBoard.moveToGraveyard(defenderCardNumber);
        }
        SoundManager.playSound("attack");
    }

    public static class AttackResultJson {
        private int attackerPlayerNumber;
        private int attackedPlayerNumber;
        private int player1LPDecrease;
        private int player2LPDecrease;
        private boolean destroyCard1;
        private boolean destroyCard2;
        private boolean attackedFlip;

        public AttackResultJson(AttackResult attackResult) {
            attackerPlayerNumber = attackResult.getAttackerPlayer().getPlayerNumber();
            attackedPlayerNumber = attackResult.getAttackedPlayer().getPlayerNumber();
            player1LPDecrease = attackResult.getPlayer1LPDecrease();
            player2LPDecrease = attackResult.getPlayer2LPDecrease();
            destroyCard1 = attackResult.isDestroyCard1();
            destroyCard2 = attackResult.isDestroyCard2();
            attackedFlip = attackResult.isAttackedFlip();
        }

        public int getPlayer2LPDecrease() {
            return player2LPDecrease;
        }

        public int getPlayer1LPDecrease() {
            return player1LPDecrease;
        }

        public int getAttackedPlayerNumber() {
            return attackedPlayerNumber;
        }

        public int getAttackerPlayerNumber() {
            return attackerPlayerNumber;
        }

        public boolean isDestroyCard1() {
            return destroyCard1;
        }

        public boolean isDestroyCard2() {
            return destroyCard2;
        }

        public boolean isAttackedFlip() {
            return attackedFlip;
        }
    }

    public void applyDirectAttackResult(AttackResult result, int playerNumber) {
        changePlayerLP(playerNumber == 1 ? 2 : 1, -result.getPlayer2LPDecrease());
    }

    private void changePlayerLP(int playerNumber, int amount) {
        Label text;
        ImageView bar;
        if (playerNumber == 1) {
            text = player1LP_T;
            bar = player1LP_bar;
        } else {
            text = player2LP_T;
            bar = player2LP_bar;
        }

        int current = Integer.parseInt(text.getText().substring(8));
        text.setText("LP      " + (current + amount));
        double ratio = (double) (current + amount) / 8000;
        bar.setViewport(new Rectangle2D(0, 0, ratio * 284, 32));
        bar.setFitWidth(ratio * 284);
    }

    public void showError(String s) {

    }

    public void log(String s) {
        System.out.println(s);
    }

    public void showPhase(String draw) {

    }

    public void showCard(Card currentSelectedCard) {

    }

    public void showBoard(String gameBoardString) {

    }

    public void gameFinishUI(String resultMessage, int winnerNumber) {
        gameEndMessage.setText(resultMessage);
        if (winnerNumber == 1) {
            gameEndMessage.setStyle("-fx-text-fill: blue");
        } else {
            gameEndMessage.setStyle("-fx-text-fill: red");
        }
        endGamePanel.setVisible(true);
    }

    public void gameFinished(int winnerNumber, int player1LP, int player2LP) {
        /////useless comment
        if (isAI) {
            String res = thisDuelData.setRoundResult(winnerNumber, player1LP, player2LP);

            gameEndMessage.setText(res);
            if (thisDuelData.getWinnerNumber() == 1) {
                gameEndMessage.setStyle("-fx-text-fill: blue");
            } else {
                gameEndMessage.setStyle("-fx-text-fill: red");
            }

            endGamePanel.setVisible(true);

        /*if (!thisDuelData.isFinished()) {
            //currentRound++;
            //System.out.println("Round " + currentRound);
            //gameManager = new GameManager(thisDuelData.isPlayer, thisDuelData.firstPlayer, thisDuelData.secondPlayer, scene, this);
            return;
        }*/

            thisDuelData.applyDuelResult();
        }
    }

    //Pause Menu

    public void pause() {
        if (SoundManager.isMute()) {
            muteButton.setText("Unmute");
        } else {
            muteButton.setText("Mute");
        }
        pausePanel.setVisible(true);
        SoundManager.playSound("pause");
    }

    public void resume() {
        pausePanel.setVisible(false);
    }

    public void muteChange() {
        if (SoundManager.isMute()) {
            SoundManager.setMute(false);
            muteButton.setText("Mute");
        } else {
            SoundManager.setMute(true);
            muteButton.setText("Unmute");
        }
    }

    public void surrender() {
        pausePanel.setVisible(false);
        if (isAI) {
            GameManager.getInstance().surrender();
        } else {
            sendMessageToServer("surrender");
        }
    }

    public void exitGame() {
        if (!isAI) {
            sendMessageToServer("exit game");
        }
        ApplicationManger.goToScene1(SceneName.DUEL_SCENE, false);
    }
}
