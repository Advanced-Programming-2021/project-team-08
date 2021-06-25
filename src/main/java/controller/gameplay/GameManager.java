package controller.gameplay;

import controller.GamePlaySceneController;
import model.Command;
import model.UserData;
import model.cards.Card;
import model.cards.MonsterCard;
import model.effectSystem.Effect;
import model.event.Event;
import model.event.EventNoParam;
import model.exceptions.ParseCommandException;
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
    private final static HashMap<String, CommandFieldType> selectSpellCardCommand = new HashMap<String, CommandFieldType>() {{
        put("opponent", CommandFieldType.BOOLEAN);
        put("spell", CommandFieldType.INT);
    }};
    private final static HashMap<String, CommandFieldType> selectFieldCardCommand = new HashMap<String, CommandFieldType>() {{
        put("opponent", CommandFieldType.BOOLEAN);
        put("field", CommandFieldType.BOOLEAN);
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

    private EventNoParam onAnSpellActivated = new EventNoParam();
    private Event<AttackResult> onWantAttack = new Event<>();
    private Event<Card> onSummonACard = new Event<>();
    private Event<Card> onFlipSummon = new Event<>();

    private boolean canAttack = true;

    private boolean isAI;
    private AI_Player ai;

    public GameManager(boolean isPlayer, UserData user1, UserData user2, GamePlayScene scene, GamePlaySceneController gamePlaySceneController) {
        isAI = !isPlayer;
        if (isAI) {
            ai = new AI_Player(this);
            gameBoard = new GameBoard(user1.getActiveDeck(), ai.getAIUserData().getActiveDeck(), this);
            this.player1 = new Player(user1, gameBoard.getPlayer1Board(), this);
            this.player2 = new Player(ai.getAIUserData(), gameBoard.getPlayer2Board(), this);
            ai.setup(player2, player1);
        } else {
            gameBoard = new GameBoard(user1.getActiveDeck(), user2.getActiveDeck(), this);
            this.player1 = new Player(user1, gameBoard.getPlayer1Board(), this);
            this.player2 = new Player(user2, gameBoard.getPlayer2Board(), this);
        }

        this.scene = scene;
        this.sceneController = gamePlaySceneController;
        Effect.setGameManager(this);

        firstSetup();
    }

    private void setCurrentSelectedCard(Card currentSelectedCard, CardSlotAddress address) {
        this.currentSelectedCard = currentSelectedCard;
        this.currentSelectedCardAddress = address;
    }

    private void clearSelection() {
        this.currentSelectedCard = null;
        this.currentSelectedCardAddress = null;
    }

    public Event<Card> getOnFlipSummon() {
        return onFlipSummon;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public Event<Card> getOnSummonACard() {
        return onSummonACard;
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }


    public GamePlayScene getScene() {
        return scene;
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

    public Event<AttackResult> getOnWantAttack() {
        return onWantAttack;
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
                if (turn == 1) {
                    changeTurn();
                    return;
                }
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
        if (isAI) {
            if (currentPlayerTurn == 2) {
                scene.setWaitForAI(true);
                ai.playATurn();
            } else {
                scene.setWaitForAI(false);
            }
        }
    }

    public void temporaryChangeTurn() {
        currentPlayerTurn = (currentPlayerTurn == 1) ? 2 : 1;
    }

    private void startDrawPhase() {
        currentPhase = Phase.DRAW;
        if (turn > 2) getCurrentTurnPlayer().drawCard();

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

    public void selectCard(String address) throws Exception {
        Command command;
        CardSlot currentSelectedZone;
        SelectCommandType type = getSelectCommandType(address);
        if (type == null) {
            scene.showError("Invalid selection");
            return;
        }
        switch (type) {
            case MONSTER:
                command = Command.parseCommand(address, selectMonsterCardCommand);
                currentSelectedCardAddress = new CardSlotAddress(Boolean.parseBoolean(command.getField("opponent")),
                        ZoneType.MONSTER,
                        Integer.parseInt(command.getField("monster")));
                break;
            case SPELL:
                command = Command.parseCommand(address, selectSpellCardCommand);
                currentSelectedCardAddress = new CardSlotAddress(Boolean.parseBoolean(command.getField("opponent")),
                        ZoneType.SPELL_AND_TRAP,
                        Integer.parseInt(command.getField("spell")));
                break;
            case FIELD:
                command = Command.parseCommand(address, selectFieldCardCommand);
                currentSelectedCardAddress = new CardSlotAddress(Boolean.parseBoolean(command.getField("opponent")),
                        ZoneType.FIELD);
                break;
            case HAND:
                command = Command.parseCommand(address, selectHandCardCommand);
                Card temp = getCurrentTurnPlayer().getCardFromHand(Integer.parseInt(command.getField("hand")));
                setCurrentSelectedCard(temp, null);
                scene.log("card selected");
                break;
        }
        switch (type) {
            case MONSTER:
            case SPELL:
            case FIELD:
                currentSelectedZone = gameBoard.getCardSlot(currentSelectedCardAddress);
                if (currentSelectedZone.isEmpty()) {
                    scene.showError("No card found in the given position");
                    return;
                }
                setCurrentSelectedCard(currentSelectedZone.getCard(), currentSelectedCardAddress);
                scene.log("card selected");
                break;
        }
    }

    private SelectCommandType getSelectCommandType(String command) {
        try {
            Command.parseCommand(command, selectMonsterCardCommand);
            return SelectCommandType.MONSTER;
        } catch (ParseCommandException ignored) {
        }
        try {
            Command.parseCommand(command, selectSpellCardCommand);
            return SelectCommandType.SPELL;
        } catch (ParseCommandException ignored) {
        }
        try {
            Command.parseCommand(command, selectHandCardCommand);
            return SelectCommandType.HAND;
        } catch (ParseCommandException ignored) {
        }
        try {
            Command command1 = Command.parseCommand(command, selectFieldCardCommand);
            if (Boolean.parseBoolean(command1.getField("field"))) return SelectCommandType.FIELD;
        } catch (ParseCommandException ignored) {
        }
        return null;
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
            onSummonACard.invoke(currentSelectedCard);
        } catch (Exception e) {
            scene.showError(e.getMessage());
        }
    }

    public void flipSummonCard() {
        try {
            getCurrentTurnPlayer().flipSummonCard(currentSelectedCard);
            scene.log("flip summoned successfully");
            onCardActionDone();
            onFlipSummon.invoke(currentSelectedCard);
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
            AttackResult attackResult = getCurrentTurnPlayer().attack(false, currentSelectedCard, gameBoard.getCardSlot(true, ZoneType.MONSTER, number));
            onWantAttack.invoke(attackResult);
             applyAttackResult(attackResult, currentSelectedCard, gameBoard.getCardSlot(true, ZoneType.MONSTER, number).getCard());
            ((MonsterCard) currentSelectedCard).setAttackedThisTurn(true);
            onCardActionDone();
        } catch (Exception e) {
            scene.showError(e.getMessage());
        }
    }

    public void attackDirect() {
        try {
            AttackResult attackResult =getCurrentTurnPlayer().attack(true, currentSelectedCard, null);
            onWantAttack.invoke(attackResult);
            applyDirectAttack(attackResult);
            ((MonsterCard) currentSelectedCard).setAttackedThisTurn(true);
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

    public void surrender() {
        scene.log(getCurrentTurnPlayer().getUserData().getUsername() + " surrendered");
        finishGame(getCurrentPlayerTurn() == 1 ? 2 : 1);
    }

    public void applyAttackResult(AttackResult result, Card attacker, Card attacked) {
        if(result.isCanceled()) return;
        getCurrentTurnPlayer().decreaseLP(result.getPlayer1LPDecrease());
        getCurrentTurnOpponentPlayer().decreaseLP(result.getPlayer2LPDecrease());
        if (result.isDestroyCard1()) {
            attacker.moveToGraveyard();
        }
        if (result.isDestroyCard2()) {
            attacked.moveToGraveyard();
        }
        scene.log(result.getResultMessage());
    }

    public void applyDirectAttack(AttackResult result) {
        if(result.isCanceled()) return;
        getCurrentTurnOpponentPlayer().decreaseLP(result.getPlayer2LPDecrease());
        scene.log(result.getResultMessage());
    }

    public ArrayList<Integer> getTribute(int numberOfTributes) throws Exception {
        ArrayList<Integer> tributes = new ArrayList<>();
        while (tributes.size() < numberOfTributes) {
            int tNumber = scene.getTributeCommand();
            try {
                CardSlot cardSlot = gameBoard.getCardSlot(false, ZoneType.MONSTER, tNumber);
                if (cardSlot.isEmpty()) {
                    scene.showError("there is no monster in this address");
                } else {
                    tributes.add(tNumber);
                }
            } catch (Exception e) {
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

        toShow.append(getCurrentTurnOpponentPlayer().getPlayerBoard().getShowingString(false));

        toShow.append("-----------------------------\n");

        toShow.append(getCurrentTurnPlayer().getPlayerBoard().getShowingString(true));

        toShow.append(getCurrentTurnPlayer().getUserData().getNickname()).append(":").append(getCurrentTurnPlayer().getLP()).append("\n");

        return toShow.toString();
    }

    private void onCardActionDone() {
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

    public void finishGame(int winnerNumber) {
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

    private enum SelectCommandType {
        MONSTER,
        SPELL,
        HAND,
        FIELD
    }

    public  Event<Card> getRotate() {
        return ((MonsterCard)currentSelectedCard).getFaceUp();
    }

    //// Cheat codes

    public void addCard_C(String cardName) {
        try {
            Card card = Card.createCardByName(cardName);
            card.setup(getCurrentTurnPlayer());
            getCurrentTurnPlayer().getPlayerBoard().getHand().appendCard(card);
        } catch (Exception e) {
            scene.showError("card with this name doesn't exist");
        }
    }

    public void increaseLP_C(int amount) {
        getCurrentTurnPlayer().increaseLP(amount);
    }

    public void setWinner_C(String nickname) {
        if (nickname.equals(player1.getUserData().getNickname())) {
            finishGame(1);
        }
        if (nickname.equals(player2.getUserData().getNickname())) {
            finishGame(2);
        }
    }

}
