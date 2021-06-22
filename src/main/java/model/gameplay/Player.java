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
    public void increaseLP(int amount) {
        LP += amount;
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

    public Card getCardFromHand(int number) throws Exception {
        if (number < 1 || number > handCards.size()) throw new Exception("number out of bounds");
        ;
        return handCards.get(number - 1);
    }

    public void summonCard(Card card) throws Exception {
        if (card == null) {
            throw new Exception("no card selected yet");
        } else if (card.getCardType() != CardType.MONSTER) {
            throw new Exception("you can't summon this card");
        } else if (playerBoard.isMonsterZoneFull()) {
            throw new Exception("monster zone is full");
        } else if (summonOrSetInThisTurn) {
            throw new Exception("you already summon/set in this turn");
        } else {
            int tributeNumber = ((MonsterCard) card).getTributeNumber();
            if (tributeNumber > 0) {
                if (playerBoard.numberOfMonstersInZone() < tributeNumber) {
                    throw new Exception("there are not enough cards to tribute");
                }

                ArrayList<Integer> tributes = gameManager.getTribute(tributeNumber);
                for (int n : tributes) {
                    CardSlot.moveToGraveyard(playerBoard.getMonsterZone().get(n - 1), playerBoard.getGraveyard());
                }
            }
            handCards.remove(card);
            ((MonsterCard) card).onSummon(playerBoard.summonMonster(card));
            summonOrSetInThisTurn = true;
        }

        // TODO: ۱۷/۰۶/۲۰۲۱ not allowed in this phase
    }

    public void setCard(Card card) throws Exception {
        if (card == null) {
            throw new Exception("no card selected yet");
        } else if (card.getCardType() != CardType.MONSTER) {
            throw new Exception("you can't set this card");
        } else if (playerBoard.isMonsterZoneFull()) {
            throw new Exception("monster zone is full");
        } else if (summonOrSetInThisTurn) {
            throw new Exception("you already summon/set in this turn");
        } else {
            handCards.remove(card);
            ((MonsterCard) card).onSet(playerBoard.setMonster(card));
            summonOrSetInThisTurn = true;
        }

        // TODO: ۱۷/۰۶/۲۰۲۱ not allowed in this phase
    }

    public void attack(Card myCard, CardSlot attackTo) throws Exception {
        if (myCard == null) {
            throw new Exception("no card selected yet");
        }
        if (myCard.getCardSlot() == null) {
            throw new Exception("you can't attack with this card");
        }
        if (myCard.getCardSlot().getZoneType() != ZoneType.MONSTER) {
            throw new Exception("you can't attack with this card");
        }
        if (attackTo.isEmpty()) {
            throw new Exception("there is no card to attack here");
        }
        AttackResult attackResult = new AttackResult((MonsterCard) myCard, (MonsterCard) attackTo.getCard());
        gameManager.applyAttackResult(attackResult, myCard, attackTo.getCard());


        // TODO: ۱۷/۰۶/۲۰۲۱ not allowed in this phase
        // TODO: ۱۷/۰۶/۲۰۲۱ already attacked
    }

    public void onChangeTurn() {
        summonOrSetInThisTurn = false;
    }

    public void addCardToHand(Card card) {
        handCards.add(card);
    }
}
