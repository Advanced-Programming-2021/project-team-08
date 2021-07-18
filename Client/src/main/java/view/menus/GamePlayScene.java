package view.menus;

import controller.DuelController;
import controller.GamePlaySceneController;
import controller.gameplay.GameManager;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import model.animation.FlipCardAnimation;
import model.animation.RotateCenterTransition;
import model.cards.Card;
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

    public Label currentPhase;

    private graphicBoard gBoard;


    private GamePlaySceneController sceneController;
    private boolean waitForAI = false;
    Scanner scanner = new Scanner(System.in);

    public void setWaitForAI(boolean waitForAI) {
        this.waitForAI = waitForAI;
    }

    public GamePlaySceneController getSceneController() {
        return sceneController;
    }

    @FXML
    public void initialize() {
        sceneController = new GamePlaySceneController(this);
        gBoard = new graphicBoard(board);
        GamePlaySceneController.DuelData data = DuelController.getCurrentDuelData();

        String rootPath = "file:" + System.getProperty("user.dir") + "/src/main/resources/FXML/";
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(new URL(rootPath + "cardView.fxml"));
            AnchorPane anchorPane = loader.load();
            showCard.setRoot(anchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new Thread(() -> {
                new GameManager(data, this);
            }).start();
            firstSetupUI(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void firstSetupUI(GamePlaySceneController.DuelData data) {
        player1Nickname_T.setText(data.getFirstPlayer().getUsername() + " (" + data.getFirstPlayer().getNickname() + ")");
        player2Nickname_T.setText(data.getSecondPlayer().getUsername() + " (" + data.getSecondPlayer().getNickname() + ")");
        player1Avatar.setImage(data.getFirstPlayer().getProfileImage());
        player2Avatar.setImage(data.getSecondPlayer().getProfileImage());
        player1LP_T.setText("LP      " + sceneController.getGameManager().getPlayer1().getLP());
        player2LP_T.setText("LP      " + sceneController.getGameManager().getPlayer2().getLP());
    }

    public void changePhase(Phase toPhase) {
        currentPhase.setText(toPhase.toString().replace("_", " "));
        if (GameManager.getInstance().getCurrentPlayerTurn() == 1) {
            currentPhase.getStyleClass().clear();
            currentPhase.getStyleClass().add("bluePlayer");
        } else {
            currentPhase.getStyleClass().clear();
            currentPhase.getStyleClass().add("redPlayer");
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

    public int trowAwayACardFromHand(int numberOfCardsInHand) {
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
        GameManager.getInstance().goToNextPhase();
    }

    public void firstSetupBoardGraphic(int playerNumber, ArrayList<Card> cards) {
        double z = cards.size();
        for (int i = 0; i < z; i++) {
            GraphicCard gc = new GraphicCard(cards.get(i));
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

        FlipCardAnimation flipCardAnimation = new FlipCardAnimation(c, 300, CardStatus.FACE_UP);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(thisCard, rotateTransition, flipCardAnimation);

        parallelTransition.setOnFinished(onEnd);
        parallelTransition.play();
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
        thisCard.setToY(slot.getImageView().getLayoutY() + 45);

        RotateCenterTransition rotateTransition = new RotateCenterTransition(c.getShape(), 800, 45, Rotate.X_AXIS);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(thisCard, rotateTransition);

        parallelTransition.play();
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
        thisCard.setToY(slot.getImageView().getLayoutY() + 35);

        RotateCenterTransition rotateTransition = new RotateCenterTransition(c.getShape(), 800, 45, Rotate.X_AXIS);

        RotateCenterTransition rotateTransition1 = new RotateCenterTransition(c.getShape(), 800, -90, Rotate.Z_AXIS);

        FlipCardAnimation flipCardAnimation = new FlipCardAnimation(c, 300, CardStatus.TO_BACK);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(thisCard, rotateTransition, rotateTransition1);

        SequentialTransition s = new SequentialTransition();
        s.getChildren().add(flipCardAnimation);
        s.getChildren().add(parallelTransition);

        s.play();
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

    public void applyAttackResultGraphic(AttackResult result, int playerNumber, int attacker, int defender) {
        graphicBoard.GraphicPlayerBoard attackerBoard = gBoard.getPlayerBoard(playerNumber);
        graphicBoard.GraphicPlayerBoard defenderBoard = gBoard.getPlayerBoard(playerNumber == 1 ? 2 : 1);

        if (result.isAttackedFlip()) {
            GraphicCard gc = defenderBoard.getMonster(defender).getCard();
            FlipCardAnimation flipCardAnimation = new FlipCardAnimation(gc, 300, CardStatus.FACE_UP);
            flipCardAnimation.play();
        }

        changePlayerLP(result.getAttackerPlayer().getPlayerNumber(), -result.getPlayer1LPDecrease());
        changePlayerLP(result.getAttackedPlayer().getPlayerNumber(), -result.getPlayer2LPDecrease());

        if (result.isDestroyCard1()) {
            attackerBoard.moveToGraveyard(attacker);
        }
        if (result.isDestroyCard2()) {
            defenderBoard.moveToGraveyard(defender);
        }
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

    public void log(String setRoundResult) {

    }

    public void showPhase(String draw) {

    }

    public void showCard(Card currentSelectedCard) {

    }

    public void showBoard(String gameBoardString) {

    }
}
