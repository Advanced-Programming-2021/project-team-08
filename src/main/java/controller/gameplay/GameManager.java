package controller.gameplay;

import controller.GamePlaySceneController;
import model.Command;
import model.event.Event;
import model.UserData;
import model.cards.Card;
import model.effects.Effect;
import model.event.EventNoParam;
import model.gameplay.Player;
import model.gameplay.*;
import model.enums.*;
import view.menus.GamePlayScene;

import java.util.ArrayList;
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

    private CardSlotAddress currentSelectedCardAddress;
    private Card currentSelectedCard;

    private GamePlayScene scene;
    private GamePlaySceneController sceneController;

    private EventNoParam onAnSpellActivated;

    public GameManager(UserData user1, UserData user2, GamePlayScene scene, GamePlaySceneController gamePlaySceneController) {
        gameBoard = new GameBoard(user1.getActiveDeck(), user2.getActiveDeck(), this);
        this.player1 = new Player(user1, gameBoard.getPlayer1Board(), this);
        this.player2 = new Player(user2, gameBoard.getPlayer2Board(), this);
        this.scene = scene;
        this.sceneController = gamePlaySceneController;
        Effect.setGameManager(this);

        firstSetup();
    }

    private void setCurrentSelectedCard(Card currentSelectedCard, CardSlotAddress address) {
        this.currentSelectedCard = currentSelectedCard;
        this.currentSelectedCardAddress = address;
    }
    private void clearSelection(){
        this.currentSelectedCard = null;
        this.currentSelectedCardAddress = null;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public int getCurrentPlayerTurn() {
        return currentPlayerTurn;
    }

    public EventNoParam getOnAnSpellActivated() {
        return onAnSpellActivated;
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
                break;
            case END:
                break;
        }
    }

    private void changeTurn() {
        turn++;
        currentPlayerTurn = (currentPlayerTurn == 1) ? 2 : 1;
        player1.onChangeTurn();
        player2.onChangeTurn();
        startDrawPhase();
    }

    private void startDrawPhase() {
        currentPhase = Phase.DRAW;
        getCurrentTurnPlayer().drawCard();

        scene.showPhase("Draw");
        onCardActionDone();
    }

    private void startStandbyPhase() {
        currentPhase = Phase.STANDBY;

        scene.showPhase("Standby");
    }

    private void startMainPhase() {
        currentPhase = Phase.MAIN;

        scene.showPhase("Main");
    }

    private void startBattlePhase() {
        currentPhase = Phase.BATTLE;

        scene.showPhase("Battle");
    }

    public Player getCurrentTurnPlayer() {
        if (currentPlayerTurn == 1) return player1;
        else return player2;
    }

    public Player getCurrentTurnOpponentPlayer() {
        if (currentPlayerTurn == 2) return player1;
        else return player2;
    }

    public void selectCard(String address) {
        try {
            Command command = Command.parseCommand(address, selectMonsterCardCommand);
            currentSelectedCardAddress = new CardSlotAddress(Boolean.parseBoolean(command.getField("opponent")),
                    ZoneType.MONSTER,
                    Integer.parseInt(command.getField("monster")));
            CardSlot currentSelectedZone = gameBoard.getCardSlot(currentSelectedCardAddress);
            if (currentSelectedZone == null) {
                scene.showError("Invalid selection");
                return;
            }
            if (currentSelectedZone.isEmpty()) {
                scene.showError("No card found in the given position");
                return;
            }
            setCurrentSelectedCard(currentSelectedZone.getCard(), currentSelectedCardAddress);

            scene.log("card selected");
        } catch (Exception e) {
            try {
                Command command = Command.parseCommand(address, selectHandCardCommand);
                Card temp = getCurrentTurnPlayer().getCardFromHand(Integer.parseInt(command.getField("hand")));

                if (temp == null) {
                    scene.showError("Invalid selection");
                    return;
                }
                setCurrentSelectedCard(temp, null);

                scene.log("card selected");
            } catch (Exception e2) {
                scene.showError("Invalid selection");
            }
        }
    }

    public void deselect() {
        if (currentSelectedCard == null) {
            scene.showError("No card selected yet");
        } else {
            clearSelection();
            System.out.println("card deselected");
        }
    }

    public void summonCard() {
        try {
            getCurrentTurnPlayer().summonCard(currentSelectedCard);
            scene.log("summoned successfully");
            onCardActionDone();
        } catch (Exception e) {
            scene.showError(e.getMessage());
        }
    }

    public void setCard() {
        try {
            getCurrentTurnPlayer().setCard(currentSelectedCard);
            scene.log("set successfully");
            onCardActionDone();
        } catch (Exception e) {
            scene.showError(e.getMessage());
        }
    }

    public void attack(int number) {
        try {
            getCurrentTurnPlayer().attack(false, currentSelectedCard, gameBoard.getCardSlot(true, ZoneType.MONSTER, number));
            onCardActionDone();
        } catch (Exception e) {
            scene.showError(e.getMessage());
        }
    }

    public void attackDirect(){
        try {
            getCurrentTurnPlayer().attack(true, currentSelectedCard, null);
            onCardActionDone();
        } catch (Exception e) {
            scene.showError(e.getMessage());
        }
    }

    public void setPosition(String toPos) {
        try {
            getCurrentTurnPlayer().setPosition(currentSelectedCard, toPos);
            scene.log("monster card position changed successfully");
            onCardActionDone();
        } catch (Exception e) {
            scene.showError(e.getMessage());
        }
    }

    public void activateCard() {
        try {
            getCurrentTurnPlayer().activateSpellCard(currentSelectedCard);
            scene.log("spell activated");
            onAnSpellActivated.invoke();
            onCardActionDone();
        } catch (Exception e) {
            scene.showError(e.getMessage());
        }
    }

    public void applyAttackResult(AttackResult result, Card attacker, Card attacked) {
        getCurrentTurnPlayer().decreaseLP(result.getPlayer1LPDecrease());
        getCurrentTurnOpponentPlayer().decreaseLP(result.getPlayer2LPDecrease());
        if (result.isDestroyCard1()) {
            try {
                CardSlot.moveToGraveyard(attacker.getCardSlot(), gameBoard.getCardSlot(false, ZoneType.GRAVEYARD, 0));
            } catch (Exception e) {
            }
        }
        if (result.isDestroyCard2()) {
            try {
                CardSlot.moveToGraveyard(attacked.getCardSlot(), gameBoard.getCardSlot(true, ZoneType.GRAVEYARD, 0));
            } catch (Exception e) {
            }
        }
        scene.log(result.getResultMessage());
    }

    public void applyDirectAttack(int damage){
        getCurrentTurnOpponentPlayer().decreaseLP(damage);
        scene.log("your opponent received "+ damage + " battle damage");
    }

    public ArrayList<Integer> getTribute(int numberOfTributes) throws Exception {
        ArrayList<Integer> tributes = new ArrayList<>();
        while (tributes.size() < numberOfTributes){
            int tNumber = scene.getTributeCommand();
            try {
                CardSlot cardSlot = gameBoard.getCardSlot(false, ZoneType.MONSTER, tNumber);
                if(cardSlot.isEmpty()){
                    scene.showError("there is no monster in this address");
                }else {
                    tributes.add(tNumber);
                }
            }catch (Exception e){
                scene.showError(e.getMessage());
            }
        }
        return tributes;
    }

    public void showGraveyard() {
        try {
            scene.showGraveyard(gameBoard.getCardSlot(false, ZoneType.GRAVEYARD, 0).getAllCards());
        } catch (Exception e) {

        }
    }

    public void showSelectedCard() {
        if (currentSelectedCard == null) {
            scene.showError("no card is selected yet");
            return;
        }

        if (currentSelectedCardAddress != null) {
            if (currentSelectedCard.getCardStatus() == CardStatus.TO_BACK && currentSelectedCardAddress.forOpponent) {
                scene.showError("no card is selected yet");
                return;
            }
        }
        scene.showCard(currentSelectedCard);
    }

    public String getGameBoardString() {
        StringBuilder toShow = new StringBuilder();
        toShow.append(getCurrentTurnOpponentPlayer().getUserData().getNickname()).append(":").append(getCurrentTurnOpponentPlayer().getLP()).append("\n");

        for (int i = 7; i > getCurrentTurnOpponentPlayer().getHandCards().size(); i--) {
            toShow.append("\t");
        }
        for (int i = 0; i < getCurrentTurnOpponentPlayer().getHandCards().size(); i++) {
            toShow.append("c\t");
        }
        toShow.append("\n");

        toShow.append(getCurrentTurnOpponentPlayer().getPlayerBoard().getShowingString(false));

        toShow.append("-----------------------------\n");

        toShow.append(getCurrentTurnPlayer().getPlayerBoard().getShowingString(true));

        for (int i = 0; i < getCurrentTurnPlayer().getHandCards().size(); i++) {
            toShow.append("c\t");
        }
        toShow.append("\n");

        toShow.append(getCurrentTurnPlayer().getUserData().getNickname()).append(":").append(getCurrentTurnPlayer().getLP()).append("\n");

        return toShow.toString();
    }

    private void onCardActionDone(){
        clearSelection();
        scene.showBoard(getGameBoardString());
    }

    public void checkGameOver() {
        if (player1.getLP() <= 0) {
            finishGame(2);
        }
        if (player2.getLP() <= 0) {
            finishGame(1);
        }
        // TODO: ۱۹/۰۶/۲۰۲۱ draw
    }

    private void finishGame(int winnerNumber) {
        sceneController.gameFinished(winnerNumber, player1.getLP(), player2.getLP());
        scene.log("Game Over");
    }

    public class CardSlotAddress {
        private boolean forOpponent;
        private ZoneType zone;
        private int number;

        public CardSlotAddress(boolean forOpponent, ZoneType zone, int number) {
            this.forOpponent = forOpponent;
            this.zone = zone;
            this.number = number;
        }

        public CardSlotAddress(boolean forOpponent, ZoneType zone) {
            this.forOpponent = forOpponent;
            this.zone = zone;
            this.number = 0;
        }

        public int getNumber() {
            return number;
        }

        public ZoneType getZone() {
            return zone;
        }

        public boolean isForOpponent() {
            return forOpponent;
        }
    }


    //// Cheat codes

    public void addCard_C(String cardName) {
        try {
            getCurrentTurnPlayer().getHandCards().add(Card.createCardByName(cardName));
        } catch (Exception e) {
            scene.showError("card with this name doesn't exist");
        }
    }

    public void increaseLP_C(int amount) {
        getCurrentTurnPlayer().increaseLP(amount);
    }
}
