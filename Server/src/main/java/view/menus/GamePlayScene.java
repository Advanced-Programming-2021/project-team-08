package view.menus;

import controller.GamePlaySceneController;
import controller.gameplay.GameManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.cards.Card;
import model.cards.data.MonsterCardData;
import model.effectSystem.EquipEffect;
import model.enums.Phase;
import model.gameplay.AttackResult;
import model.gameplay.Player;

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

    private int playerNumber = 1;

    private GamePlaySceneController sceneController;
    private boolean waitForAI = false;
    Scanner scanner = new Scanner(System.in);

    public void setWaitForAI(boolean waitForAI) {
        this.waitForAI = waitForAI;
    }

    public GamePlaySceneController getSceneController() {
        return sceneController;
    }

    private void firstSetupUI(GamePlaySceneController.DuelData1 data) {
        player1Nickname_T.setText(data.getFirstPlayer().getUsername() + " (" + data.getFirstPlayer().getNickname() + ")");
        player2Nickname_T.setText(data.getSecondPlayer().getUsername() + " (" + data.getSecondPlayer().getNickname() + ")");
        player1Avatar.setImage(data.getFirstPlayer().getProfileImage());
        player2Avatar.setImage(data.getSecondPlayer().getProfileImage());
        player1LP_T.setText("LP      8000");
        player2LP_T.setText("LP      8000");
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
    }

    public void draw(int playerNumber, int deckCardNumber, EventHandler<ActionEvent> onEnd) {
    }

    public void summon(int playerNumber, int handCardNumber, int toSlotNumber) {
    }

    public void setMonster(int playerNumber, int handCardNumber, int toSlotNumber) {
    }

    public void applyDirectAttackResult(AttackResult result, int playerNumber) {
        changePlayerLP(result.getAttackedPlayer().getPlayerNumber(), -result.getPlayer2LPDecrease());
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
