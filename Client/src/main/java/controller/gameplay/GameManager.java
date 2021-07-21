package controller.gameplay;

import controller.GamePlaySceneController;
import javafx.application.Platform;
import model.Command;
import model.UserData;
import model.cards.Card;
import model.cards.MonsterCard;
import model.effectSystem.Effect;
import model.enums.*;
import model.event.Event;
import model.event.EventNoParam;
import model.exceptions.ParseCommandException;
import model.gameplay.AttackResult;
import model.gameplay.CardSlot;
import model.gameplay.GameBoard;
import model.gameplay.Player;
import view.menus.GamePlayScene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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

    private static GameManager instance;

    public static GameManager getInstance() {
        return instance;
    }

    private Player player1, player2;
    private int turnNumber, currentPlayerTurn;
    private Phase currentPhase;
    private GameBoard gameBoard;

    private CardSlotAddress currentSelectedCardAddress;
    private Card currentSelectedCard;

    private GamePlayScene scene;
    private GamePlaySceneController sceneController;

    private Event<Card> onAnSpellActivated = new Event<>();
    private Event<AttackResult> onWantAttack = new Event<>();
    private Event<Card> onSummonACard = new Event<>();
    private Event<Card> onFlipSummon = new Event<>();
    private EventNoParam onChangeTurn = new EventNoParam();
    protected Event<AttackResult> destroyAMonster = new Event<>();

    private boolean isFirstSetup = false;

    private boolean canAttack = true;


    private boolean isAI;
    private AI_Player ai;

    public GameManager(boolean isPlayer, UserData user1, UserData user2, GamePlayScene scene, GamePlaySceneController gamePlaySceneController) {
        instance = this;
        this.scene = scene;
        this.sceneController = gamePlaySceneController;

        isAI = !isPlayer;
        if (isAI) {
            ai = new AI_Player(this);
            gameBoard = new GameBoard(user1.getActiveDeck(), AI_Player.getAIUserData().getActiveDeck(), this, scene.board);

            this.player1 = new Player(user1, gameBoard.getPlayer1Board(), this, 1);
            this.player2 = new Player(AI_Player.getAIUserData(), gameBoard.getPlayer2Board(), this, 2);
            ai.setup(player2, player1);
        } else {
            gameBoard = new GameBoard(user1.getActiveDeck(), user2.getActiveDeck(), this, scene.board);

            this.player1 = new Player(user1, gameBoard.getPlayer1Board(), this, 1);
            this.player2 = new Player(user2, gameBoard.getPlayer2Board(), this, 2);
        }

        Effect.setGameManager(this);

        firstSetup();
    }

    public GameManager(GamePlaySceneController.DuelData duelData, GamePlayScene scene) {
        this(duelData.isPlayer(), duelData.getFirstPlayer(), duelData.getSecondPlayer(), scene, null);
    }

    private void setCurrentSelectedCard(Card currentSelectedCard, CardSlotAddress address) {
        this.currentSelectedCard = currentSelectedCard;
        this.currentSelectedCardAddress = address;
    }

    private void clearSelection() {
        this.currentSelectedCard = null;
        this.currentSelectedCardAddress = null;
    }

    public Event<AttackResult> getDestroyAMonster() {
        return destroyAMonster;
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

    public int getTurnNumber() {
        return turnNumber;
    }

    public GamePlayScene getScene() {
        return scene;
    }

    public EventNoParam getOnChangeTurn() {
        return onChangeTurn;
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public int getCurrentPlayerTurn() {
        return currentPlayerTurn;
    }

    public Event<Card> getOnAnSpellActivated() {
        return onAnSpellActivated;
    }

    public Event<AttackResult> getOnWantAttack() {
        return onWantAttack;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void firstSetup() {
        player1.drawCard(5, null);
        EventNoParam e = new EventNoParam();
        e.addListener(this::firstSetupAfterFirstDraw);
        player2.drawCard(5, e);
    }

    public void firstSetupAfterFirstDraw() {
        turnNumber = 1;
        currentPlayerTurn = new Random().nextInt(2) + 1;
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
                if (turnNumber == 1) {
                    startEndPhase();
                } else {
                    startBattlePhase();
                }
                break;
            case BATTLE:
                startEndPhase();
                break;
            case END:
                changeTurn();
                break;
        }
    }

    public void setCurrentPhase(Phase currentPhase) {
        this.currentPhase = currentPhase;
    }

    private void changeTurn() {
        turnNumber++;
        currentPlayerTurn = (currentPlayerTurn == 1) ? 2 : 1;
        player1.onChangeTurn();
        player2.onChangeTurn();
        onChangeTurn.invoke();

        startDrawPhase();
    }

    public void temporaryChangeTurn() {
        currentPlayerTurn = (currentPlayerTurn == 1) ? 2 : 1;
    }

    private void startDrawPhase() {
        currentPhase = Phase.DRAW;
        if (getCurrentTurnPlayer().getBannedCardTurn() > 0) {
            getCurrentTurnPlayer().setBannedCardTurn(getCurrentTurnPlayer().getBannedCardTurn() - 1);
        } else {
            getCurrentTurnPlayer().drawCard(1, null);
        }
        scene.showPhase("Draw");
        Platform.runLater(() -> scene.changePhase(Phase.DRAW, currentPlayerTurn));
        onCardActionDone();

        if (isAI) {
            if (currentPlayerTurn == 2) {
                scene.setWaitForAI(true);
                new Thread(() -> ai.playATurn()).start();
            } else {
                scene.setWaitForAI(false);
            }
        }
    }

    private void startStandbyPhase() {
        currentPhase = Phase.STANDBY;

        scene.showPhase("Standby");
        Platform.runLater(() -> scene.changePhase(Phase.STANDBY, currentPlayerTurn));
    }

    private void startMainPhase() {
        currentPhase = Phase.MAIN;

        scene.showPhase("Main");
        Platform.runLater(() -> scene.changePhase(Phase.MAIN, currentPlayerTurn));
    }

    private void startBattlePhase() {
        currentPhase = Phase.BATTLE;

        scene.showPhase("Battle");
        Platform.runLater(() -> scene.changePhase(Phase.BATTLE, currentPlayerTurn));
    }

    private void startEndPhase() {
        currentPhase = Phase.END;

        scene.showPhase("End");
        Platform.runLater(() -> scene.changePhase(Phase.END, currentPlayerTurn));
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
                currentSelectedCardAddress = new CardSlotAddress(false, ZoneType.HAND, Integer.parseInt(command.getField("hand")));
                setCurrentSelectedCard(temp, currentSelectedCardAddress);
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

    public void summonCard(Integer... args) {
        try {
            CardSlot s = getCurrentTurnPlayer().summonCard(currentSelectedCard, args);
            final int playerNumber = currentPlayerTurn;
            final int a = currentSelectedCardAddress.number;
            final int b = s.getNumber();
            //scene.summon(playerNumber, a, b);
            Platform.runLater(() -> scene.summon(playerNumber, a, b));
            scene.log("summoned successfully");
            onCardActionDone();
            onSummonACard.invoke(currentSelectedCard);
        } catch (Exception e) {
            scene.showError(e.getMessage());
        }
    }

    public void flipSummonCard() {
        if (currentSelectedCardAddress.forOpponent) {
            scene.showError("it is not your card");
            return;
        }
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
            CardSlot s = getCurrentTurnPlayer().setCard(currentSelectedCard);
            if (currentSelectedCard.getCardType() == CardType.MONSTER) {
                final int playerNumber = currentPlayerTurn;
                final int a = currentSelectedCardAddress.number;
                final int b = s.getNumber();
                Platform.runLater(() -> scene.setMonster(playerNumber, a, b));
            }
            scene.log("set successfully");
            onCardActionDone();
        } catch (Exception e) {
            scene.showError(e.getMessage());
        }
    }

    public void attack(int number) {
        if (currentSelectedCardAddress.forOpponent) {
            scene.showError("it is not your card");
            return;
        }
        try {
            AttackResult attackResult = getCurrentTurnPlayer().attack(false, currentSelectedCard, gameBoard.getCardSlot(true, ZoneType.MONSTER, number));
            onWantAttack.invoke(attackResult);
            applyAttackResult(attackResult, currentSelectedCard, gameBoard.getCardSlot(true, ZoneType.MONSTER, number).getCard());
            ((MonsterCard) currentSelectedCard).setAttackedThisTurn(true);
            destroyAMonster.invoke(attackResult);
            onCardActionDone();
        } catch (Exception e) {
            e.printStackTrace();
            scene.showError(e.getMessage());
        }
    }

    public void attackDirect() {
        if (currentSelectedCardAddress.forOpponent) {
            scene.showError("it is not your card");
            return;
        }
        try {
            AttackResult attackResult = getCurrentTurnPlayer().attack(true, currentSelectedCard, null);
            onWantAttack.invoke(attackResult);
            applyDirectAttack(attackResult);
            ((MonsterCard) currentSelectedCard).setAttackedThisTurn(true);
            onCardActionDone();
        } catch (Exception e) {
            scene.showError(e.getMessage());
        }
    }

    public void setPosition(String toPos) {
        if (currentSelectedCardAddress.forOpponent) {
            scene.showError("it is not your card");
            return;
        }
        try {
            getCurrentTurnPlayer().setPosition(currentSelectedCard, toPos);
            scene.log("monster card position changed successfully");
            onCardActionDone();
        } catch (Exception e) {
            scene.showError(e.getMessage());
        }
    }

    public void activateCard() {
        if (currentSelectedCardAddress.forOpponent) {
            scene.showError("it is not your card");
            return;
        }
        try {
            getCurrentTurnPlayer().activateSpellCard(currentSelectedCard);
            scene.log("spell activated");
            onAnSpellActivated.invoke(currentSelectedCard);
            onCardActionDone();
        } catch (Exception e) {
            scene.showError(e.getMessage());
        }
    }

    public void surrender() {
        scene.log("surrender");
        finishGame(scene.getPlayerNumber() == 1 ? 2 : 1);
    }

    public void applyAttackResult(AttackResult result, Card attacker, Card attacked) {
        if (result.isCanceled()) return;
        final int a = attacker.getCardSlot().getNumber();
        final int b = attacked.getCardSlot().getNumber();
        final int playerNumber = currentPlayerTurn;

        getCurrentTurnPlayer().decreaseLP(result.getPlayer1LPDecrease());
        getCurrentTurnOpponentPlayer().decreaseLP(result.getPlayer2LPDecrease());
        if (result.isDestroyCard1()) {
            attacker.moveToGraveyard();
        }
        if (result.isDestroyCard2()) {
            attacked.moveToGraveyard();
        }
        scene.log(result.getResultMessage());

        GamePlayScene.AttackResultJson resultJson = new GamePlayScene.AttackResultJson(result);
        Platform.runLater(() -> scene.applyAttackResultGraphic(resultJson, playerNumber, a, b));
    }

    public void applyDirectAttack(AttackResult result) {
        if (result.isCanceled()) return;
        final int playerNumber = currentPlayerTurn;
        getCurrentTurnOpponentPlayer().decreaseLP(result.getPlayer2LPDecrease());
        scene.log(result.getResultMessage());
        Platform.runLater(() -> scene.applyDirectAttackResult(result, playerNumber));

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
                scene.showError("card is not visible");
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
        //sceneController.gameFinished(winnerNumber, player1.getLP(), player2.getLP());
        scene.gameFinished(winnerNumber, player1.getLP(), player2.getLP());
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

    public Event<Card> getRotate() {
        return ((MonsterCard) currentSelectedCard).getOnFaceUp();
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
