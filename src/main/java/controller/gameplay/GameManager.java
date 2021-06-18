package controller.gameplay;

import model.Command;
import model.UserData;
import model.cards.Card;
import model.exceptions.ParseCommandException;
import model.gameplay.Player;
import model.gameplay.*;
import model.enums.*;
import view.menus.GamePlayScene;

import java.util.HashMap;

public class GameManager {
    private final static HashMap<String, CommandFieldType> selectMonsterCardCommand = new HashMap<String, CommandFieldType>() {{
        put("opponent", CommandFieldType.BOOLEAN);
        put("monster", CommandFieldType.INT);
    }};
    private final static HashMap<String, CommandFieldType> selectHandCardCommand = new HashMap<String, CommandFieldType>() {{
        put("hand", CommandFieldType.INT);
    }};

    private Player player1, player2;
    private int turn, currentPlayerTurn;
    private Phase currentPhase;
    private GameBoard gameBoard;

    private Card currentSelectedCard;

    private GamePlayScene scene;

    public GameManager(UserData user1, UserData user2, GamePlayScene scene) {
        gameBoard = new GameBoard(user1.getActiveDeck(), user2.getActiveDeck());
        this.player1 = new Player(user1, gameBoard.getPlayer1Board(), this);
        this.player2 = new Player(user2, gameBoard.getPlayer2Board(), this);
        this.scene = scene;

        firstSetup();
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public void firstSetup() {
        turn = 1;
        currentPlayerTurn = 1;
        startDrawPhase();
    }

    public void goToNextPhase() {
        switch (currentPhase) {
            case DRAW:
                startStandbyPhase();
                break;
            case STANDBY:
                startMainPhase();
                break;
            case MAIN:
                startBattlePhase();
                break;
            case BATTLE:
                changeTurn();
                startDrawPhase();
                break;
            case END:
                break;
        }
    }

    private void changeTurn() {
        turn++;
        currentPlayerTurn = (currentPlayerTurn == 1) ? 2 : 1;
        startDrawPhase();
    }

    private void startDrawPhase() {
        currentPhase = Phase.DRAW;
        getCurrentTurnPlayer().drawCard();

        scene.showPhase("Draw");
        scene.showBoard(getGameBoardString());
    }

    private void startStandbyPhase() {
        currentPhase = Phase.STANDBY;

        scene.showPhase("Standby");
        scene.showBoard(getGameBoardString());
    }

    private void startMainPhase() {
        currentPhase = Phase.MAIN;

        scene.showPhase("Main");
        scene.showBoard(getGameBoardString());
    }

    private void startBattlePhase() {
        currentPhase = Phase.BATTLE;

        scene.showPhase("Battle");
        scene.showBoard(getGameBoardString());
    }

    private Player getCurrentTurnPlayer() {
        if (currentPlayerTurn == 1) return player1;
        else return player2;
    }

    private Player getCurrentTurnOpponentPlayer() {
        if (currentPlayerTurn == 2) return player1;
        else return player2;
    }

    public void selectCard(String address) {
        try {
            Command command = Command.parseCommand(address, selectMonsterCardCommand);
            CardSlot currentSelectedZone = gameBoard.getCardSlot(Boolean.parseBoolean(command.getField("opponent")),
                    ZoneType.MONSTER,
                    Integer.parseInt(command.getField("monster")));
            if (currentSelectedZone == null) {
                scene.showError("Invalid selection");
                return;
            }
            if (currentSelectedZone.isEmpty()) {
                scene.showError("No card found in the given position");
                return;
            }
            currentSelectedCard = currentSelectedZone.getCard();

            System.out.println("card selected");
        } catch (ParseCommandException e) {
            try {
                Command command = Command.parseCommand(address, selectHandCardCommand);
                Card temp = getCurrentTurnPlayer().getCardFromHand(Integer.parseInt(command.getField("hand")));

                if (temp == null) {
                    scene.showError("Invalid selection");
                    return;
                }
                currentSelectedCard = temp;

                System.out.println("card selected");
            } catch (ParseCommandException e2) {
                scene.showError("Invalid selection");
            }
        }
    }

    public void deselect() {
        if (currentSelectedCard == null) {
            scene.showError("No card selected yet");
        } else {
            currentSelectedCard = null;
            System.out.println("card deselected");
        }
    }

    public void summonCard() {
        try {
            getCurrentTurnPlayer().summonCard(currentSelectedCard);
            scene.log("summoned successfully");
        } catch (Exception e) {
            scene.showError(e.getMessage());
        }
    }

    public void setCard() {
        try {
            getCurrentTurnPlayer().setCard(currentSelectedCard);
            scene.log("set successfully");
        } catch (Exception e) {
            scene.showError(e.getMessage());
        }
    }

    public void attack(int number) {
        try {
            getCurrentTurnPlayer().attack(currentSelectedCard, gameBoard.getCardSlot(true, ZoneType.MONSTER, number));
        } catch (Exception e) {
            scene.showError(e.getMessage());
        }
    }

    public void applyAttackResult(AttackResult result, Card attacker, Card attacked) {
        getCurrentTurnPlayer().decreaseLP(result.getPlayer1LPDecrease());
        getCurrentTurnOpponentPlayer().decreaseLP(result.getPlayer2LPDecrease());
        // TODO: ۱۸/۰۶/۲۰۲۱ destroy cards
        scene.log(result.getResultMessage());
    }

    public String getGameBoardString() {
        String toShow = "";
        toShow += getCurrentTurnOpponentPlayer().getUserData().getNickname() + ":" + getCurrentTurnOpponentPlayer().getLP() + "\n";

        for (int i = 7; i > getCurrentTurnOpponentPlayer().getHandCards().size(); i--) {
            toShow += "\t";
        }
        for (int i = 0; i < getCurrentTurnOpponentPlayer().getHandCards().size(); i++) {
            toShow += "c\t";
        }
        toShow += "\n";

        toShow += getCurrentTurnOpponentPlayer().getPlayerBoard().getShowingString(false);

        toShow += "-----------------------------\n";

        toShow += getCurrentTurnPlayer().getPlayerBoard().getShowingString(true);

        for (int i = 0; i < getCurrentTurnPlayer().getHandCards().size(); i++) {
            toShow += "c\t";
        }
        toShow += "\n";

        toShow += getCurrentTurnPlayer().getUserData().getNickname() + ":" + getCurrentTurnPlayer().getLP() + "\n";

        return toShow;
    }
}
