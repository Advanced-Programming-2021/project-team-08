package view.menus;

import controller.GamePlaySceneController;
import controller.User;
import controller.gameplay.GameManager;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
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
import model.gameplay.Player;
import model.graphic.GraphicCard;
import model.graphic.GraphicCardSlot;
import model.graphic.graphicBoard;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GamePlayScene extends Scene {
    public ImageView player1Avatar;
    public ImageView player2Avatar;
    public Label player1Nickname_T;
    public Label player2Nickname_T;
    public Label player1LP_T;
    public Label player2LP_T;
    public AnchorPane board;
    public AnchorPane root;

    private graphicBoard gBoard;


    private GamePlaySceneController sceneController;
    private boolean waitForAI = false;

    public void setWaitForAI(boolean waitForAI) {
        this.waitForAI = waitForAI;
    }

    public GamePlaySceneController getSceneController() {
        return sceneController;
    }

    @FXML
    public void initialize() {
        sceneController = new GamePlaySceneController(this);
        //firstSetupUI();
        gBoard = new graphicBoard(board);

        try {
            new GameManager(false, User.getUserByUsername("Abolfazl").getUserData(), null, this, sceneController);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GamePlayScene() {
        //sceneController = new GamePlaySceneController(this);
    }

    private void firstSetupUI() {
        player1Nickname_T.setText("LP\t\t" + sceneController.getGameManager().getPlayer1().getUserData().getNickname());
        player2Nickname_T.setText("LP\t\t" + sceneController.getGameManager().getPlayer2().getUserData().getNickname());
    }

    public void updateUI() {
        player1LP_T.setText("LP\t\t" + sceneController.getGameManager().getPlayer1().getLP());
        player2LP_T.setText("LP\t\t" + sceneController.getGameManager().getPlayer2().getLP());
    }

    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();
        Matcher matcher;

        if (waitForAI) return 1;

        if (cheatCommand(userInput) == 1) return 1;

        if ((Pattern.compile("^menu enter ([^\n]+)$").matcher(userInput)).find()) {
            System.out.println("menu navigation is not possible");
            return 1;
        }
        if (userInput.equals("menu show-current")) {
            System.out.println("Duel Menu");
            return 1;
        }
        matcher = Pattern.compile("duel ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            sceneController.duel(matcher.group(1));
            return 1;
        }

        if (sceneController.isDuelStarted()) {
            if (userInput.equals("next phase")) {
                sceneController.getGameManager().goToNextPhase();
                return 1;
            }
            if (userInput.equals("select -d")) {
                sceneController.getGameManager().deselect();
                return 1;
            }
            if (userInput.equals("card show --selected")) {
                sceneController.getGameManager().showSelectedCard();
                return 1;
            }
            if (userInput.equals("show graveyard")) {
                sceneController.getGameManager().showGraveyard();
                return 1;
            }
            matcher = Pattern.compile("select ([^\\n]+)").matcher(userInput);
            if (matcher.matches()) {
                try {
                    sceneController.getGameManager().selectCard(matcher.group(1));
                } catch (Exception e) {
                    showError(e.getMessage());
                }
                return 1;
            }
            if (userInput.equals("surrender")) {
                sceneController.getGameManager().surrender();
                return 1;
            }

            switch (sceneController.getGameManager().getCurrentPhase()) {
                case DRAW:
                    drawPhaseCommand(userInput);
                    break;
                case STANDBY:
                    standbyPhaseCommand(userInput);
                    break;
                case MAIN:
                    mainPhaseCommand(userInput);
                    break;
                case BATTLE:
                    battlePhaseCommand(userInput);
                    break;
                case END:
                    endPhaseCommand(userInput);
                    break;
            }
            return 1;
        }

        System.out.println("Invalid Command!");

        return 1;
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

    private void drawPhaseCommand(String userInput) {
        System.out.println("Invalid Command!");
    }

    private void standbyPhaseCommand(String userInput) {
        Matcher matcher;

        System.out.println("Invalid Command!");
    }

    private void mainPhaseCommand(String userInput) {
        Matcher matcher;

        if (userInput.equals("summon")) {
            sceneController.getGameManager().summonCard();
            return;
        }
        if (userInput.equals("flip-summon")) {
            sceneController.getGameManager().flipSummonCard();
            return;
        }
        if (userInput.equals("set")) {
            sceneController.getGameManager().setCard();
            return;
        }
        matcher = Pattern.compile("set --position (attack|defence)").matcher(userInput);
        if (matcher.matches()) {
            sceneController.getGameManager().setPosition(matcher.group(1));
            return;
        }
        if (userInput.equals("activate effect")) {
            sceneController.getGameManager().activateCard();
            return;
        }

        System.out.println("Invalid Command!");
    }

    private void battlePhaseCommand(String userInput) {
        Matcher matcher;

        matcher = Pattern.compile("attack ([1-5])").matcher(userInput);
        if (matcher.matches()) {
            sceneController.getGameManager().attack(Integer.parseInt(matcher.group(1)));
            return;
        }
        if (userInput.equals("attack direct")) {
            sceneController.getGameManager().attackDirect();
            return;
        }

        System.out.println("Invalid Command!");
    }

    private void endPhaseCommand(String userInput) {

    }

    public void showPhase(String phaseName) {
        System.out.println("Phase: " + phaseName);
    }

    public void showBoard(String gameBoard) {
        System.out.println(gameBoard);
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
        int i = 0;
        double z = cards.size();
        for (Card c : cards) {
            GraphicCard gc = new GraphicCard(c);
            gBoard.getPlayerBoard(playerNumber).getDeck().appendCard(gc);
            gBoard.getPlayerBoard(playerNumber).getPlayerBoard().getChildren().add(gc.getShape());
            gc.getShape().setTranslateX(gBoard.getPlayerBoard(playerNumber).getDeck().getImageView().getLayoutX() + 8);
            gc.getShape().setTranslateY(gBoard.getPlayerBoard(playerNumber).getDeck().getImageView().getLayoutY() + 5);
            gc.getShape().setTranslateZ(-(double) i * 3);
            i++;
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
        thisCard.setToX(playerBoard.getHand().getImageView().getLayoutX() + pre.size() * 42 - 70);
        thisCard.setToY(playerBoard.getHand().getImageView().getLayoutY());

        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setDuration(Duration.millis(800));
        rotateTransition.setNode(c.getShape());
        rotateTransition.setAxis(new Point3D(1, 0, 0));
        rotateTransition.setToAngle(45);

        FlipCardAnimation flipCardAnimation = new FlipCardAnimation(c, 300, CardStatus.FACE_UP);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().add(thisCard);
        parallelTransition.getChildren().add(rotateTransition);
        parallelTransition.getChildren().add(flipCardAnimation);

        parallelTransition.setOnFinished(onEnd);
        parallelTransition.play();
    }

    public void summon(int playerNumber, int handCardNumber, int toSlotNumber) {
        graphicBoard.GraphicPlayerBoard playerBoard = gBoard.getPlayerBoard(playerNumber);
        GraphicCard c = playerBoard.getHand().getAllCards().get(handCardNumber - 1);
        GraphicCardSlot slot = playerBoard.getMonster().get(toSlotNumber - 1);

        TranslateTransition thisCard = new TranslateTransition();
        thisCard.setDuration(Duration.millis(800));
        thisCard.setNode(c.getShape());
        thisCard.setToX(slot.getImageView().getLayoutX() + 144);
        thisCard.setToY(slot.getImageView().getLayoutY() + 45);

        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setDuration(Duration.millis(800));
        rotateTransition.setNode(c.getShape());
        rotateTransition.setAxis(new Point3D(1, 0, 0));
        rotateTransition.setToAngle(0);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().add(thisCard);
        parallelTransition.getChildren().add(rotateTransition);

        parallelTransition.play();
    }

    public void set(int playerNumber, int handCardNumber, int toSlotNumber) {
        graphicBoard.GraphicPlayerBoard playerBoard = gBoard.getPlayerBoard(playerNumber);
        GraphicCard c = playerBoard.getHand().getAllCards().get(handCardNumber - 1);
        GraphicCardSlot slot = playerBoard.getMonster().get(toSlotNumber - 1);

        TranslateTransition thisCard = new TranslateTransition();
        thisCard.setDuration(Duration.millis(800));
        thisCard.setNode(c.getShape());
        thisCard.setToX(slot.getImageView().getLayoutX() + 144);
        thisCard.setToY(slot.getImageView().getLayoutY() + 30);

        RotateCenterTransition rotateTransition = new RotateCenterTransition(c.getShape(), 800, 45, Rotate.X_AXIS);

        RotateCenterTransition rotateTransition1 = new RotateCenterTransition(c.getShape(), 800, -90, Rotate.Z_AXIS);

        FlipCardAnimation flipCardAnimation = new FlipCardAnimation(c, 300, CardStatus.TO_BACK);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().add(thisCard);
        parallelTransition.getChildren().add(rotateTransition);
        parallelTransition.getChildren().add(rotateTransition1);

        SequentialTransition s = new SequentialTransition();
        s.getChildren().add(flipCardAnimation);
        s.getChildren().add(parallelTransition);

        s.play();
    }
}
