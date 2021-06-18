package model.gameplay;

import controller.GamePlaySceneController;
import controller.gameplay.GameManager;
import model.UserData;
import model.cards.Card;
import model.cards.MonsterCard;
import model.enums.CardType;
import model.enums.ZoneType;

import java.util.ArrayList;

public class Player {
    private UserData userData;
    private int LP = 8000;
    private ArrayList<Card> handCards = new ArrayList<>();
    private PlayerBoard playerBoard;
    private GameManager gameManager;

    private boolean summonOrSetInThisTurn = false;

    public Player(UserData userData, PlayerBoard playerBoard, GameManager gameManager) {
        this.userData = userData;
        this.playerBoard = playerBoard;
        this.gameManager = gameManager;

        for (int i = 0; i < 6; i++) {
            try {
                handCards.add(playerBoard.drawCardFromDeck());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public UserData getUserData() {
        return userData;
    }

    public int getLP() {
        return LP;
    }

    public void decreaseLP(int amount) {
        LP -= amount;
        if (LP <= 0) {
            LP = 0;
            gameManager.checkGameOver();
        }
    }

    public ArrayList<Card> getHandCards() {
        return handCards;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public void drawCard() {
        try {
            Card c = playerBoard.drawCardFromDeck();
            handCards.add(c);
            System.out.println("new card added to the hand: " + c.getCardData().getCardName());
        } catch (Exception e) {
            // TODO: ۱۷/۰۶/۲۰۲۱ Game Over
        }
    }

    public Card getCardFromHand(int number) {
        return handCards.get(number - 1);
    }

    public void summonCard(Card card) throws Exception {
        if (card == null) {
            throw new Exception("No card selected yet");
        } else if (card.getCardType() != CardType.MONSTER) {
            throw new Exception("You can't summon this card");
        } else if (playerBoard.isMonsterZoneFull()) {
            throw new Exception("Monster zone is full");
        } else if (summonOrSetInThisTurn) {
            throw new Exception("You already summon/set in this turn");
        } else {
            handCards.remove(card);
            ((MonsterCard) card).onSummon(playerBoard.summonMonster(card));
            summonOrSetInThisTurn = true;
        }

        // TODO: ۱۷/۰۶/۲۰۲۱ not allowed in this phase
    }

    public void setCard(Card card) throws Exception {
        if (card == null) {
            throw new Exception("No card selected yet");
        } else if (card.getCardType() != CardType.MONSTER) {
            throw new Exception("You can't set this card");
        } else if (playerBoard.isMonsterZoneFull()) {
            throw new Exception("Monster zone is full");
        } else if (summonOrSetInThisTurn) {
            throw new Exception("You already summon/set in this turn");
        } else {
            handCards.remove(card);
            ((MonsterCard) card).onSet(playerBoard.setMonster(card));
            summonOrSetInThisTurn = true;
        }

        // TODO: ۱۷/۰۶/۲۰۲۱ not allowed in this phase
    }

    public void attack(Card myCard, CardSlot attackTo) throws Exception {
        if (myCard == null) {
            throw new Exception("No card selected yet");
        } else if (myCard.getCardSlot().getZoneType() != ZoneType.MONSTER) {
            throw new Exception("You can't attack with this card");
        } else if (attackTo.isEmpty()) {
            throw new Exception("There is no card to attack here");
        } else {
            AttackResult attackResult = new AttackResult((MonsterCard) myCard, (MonsterCard) attackTo.getCard());
            gameManager.applyAttackResult(attackResult, myCard, attackTo.getCard());
        }

        // TODO: ۱۷/۰۶/۲۰۲۱ not allowed in this phase
        // TODO: ۱۷/۰۶/۲۰۲۱ already attacked
    }
}
