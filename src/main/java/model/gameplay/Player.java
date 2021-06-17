package model.gameplay;

import model.UserData;
import model.cards.Card;
import model.cards.MonsterCard;
import model.enums.CardType;

import java.util.ArrayList;

public class Player {
    private UserData userData;
    private int LP = 8000;
    private ArrayList<Card> handCards = new ArrayList<>();
    private PlayerBoard playerBoard;

    private boolean summonOrSetInThisTurn = false;

    public Player(UserData userData, PlayerBoard playerBoard) {
        this.userData = userData;
        this.playerBoard = playerBoard;

        for (int i = 0; i < 6; i++) {
            drawCard();
        }
    }

    public UserData getUserData() {
        return userData;
    }

    public int getLP() {
        return LP;
    }

    public ArrayList<Card> getHandCards() {
        return handCards;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public void drawCard() {
        handCards.add(playerBoard.drawCardFromDeck());
    }

    public Card getCardFromHand(int number) {
        return handCards.get(number);
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
            playerBoard.summonMonster(card);
            ((MonsterCard)card).onSummon();
        }
    }

    public void setCard(Card card) throws Exception {
        if (card == null) {
            throw new Exception("No card selected yet");
        } else if (card.getCardType() != CardType.MONSTER) {
            throw new Exception("You can't set this card");
        } else if (playerBoard.isMonsterZoneFull()) {
            throw new Exception("Monster zone is full");
        }else if (summonOrSetInThisTurn) {
            throw new Exception("You already summon/set in this turn");
        } else {
            handCards.remove(card);
            playerBoard.setMonster(card);
            ((MonsterCard)card).onSet();
        }
    }
}
