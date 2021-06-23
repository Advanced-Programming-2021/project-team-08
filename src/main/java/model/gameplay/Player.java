package model.gameplay;

import controller.GamePlaySceneController;
import controller.gameplay.GameManager;
import model.UserData;
import model.cards.Card;
import model.cards.MonsterCard;
import model.cards.SpellCard;
import model.enums.*;

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
        return handCards.get(number - 1);
    }

    public void summonCard(Card card) throws Exception {
        if (card == null) {
            throw new Exception("no card selected yet");
        }
        if (card.getCardSlot() != null) {
            throw new Exception("you can't summon this card");
        }
        if (card.getCardType() != CardType.MONSTER) {
            throw new Exception("you can't summon this card");
        }
        if(gameManager.getCurrentPhase() != Phase.MAIN){
            throw new Exception("action not allowed in this phase");
        }
        else if (playerBoard.isMonsterZoneFull()) {
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
            playerBoard.addMonsterCardToZone(card);
            ((MonsterCard) card).onSummon();
            summonOrSetInThisTurn = true;
        }
    }

    public void setCard(Card card) throws Exception {
        if (card == null) {
            throw new Exception("no card selected yet");
        }
        if (card.getCardSlot() != null) {
            throw new Exception("you can't set this card");
        }
        if(gameManager.getCurrentPhase() != Phase.MAIN){
            throw new Exception("action not allowed in this phase");
        }
        if (card.getCardType() == CardType.MONSTER) {
            if (playerBoard.isMonsterZoneFull()) {
                throw new Exception("monster zone is full");
            } else if (summonOrSetInThisTurn) {
                throw new Exception("you already summon/set in this turn");
            } else {
                handCards.remove(card);
                playerBoard.addMonsterCardToZone(card);
                card.onSet();
                summonOrSetInThisTurn = true;
            }
        } else {
            if (playerBoard.isSpellTrapZoneFull()) {
                throw new Exception("spell/trap card zone is full");
            }
            handCards.remove(card);
            playerBoard.addSpellTrapCardToZone(card);
            card.onSet();
        }

        // TODO: ۲۳/۰۶/۲۰۲۱ get tribute
    }

    public void attack(boolean direct, Card myCard, CardSlot attackTo) throws Exception {
        if (myCard == null) {
            throw new Exception("no card selected yet");
        }
        if (myCard.getCardSlot() == null) {
            throw new Exception("you can't attack with this card");
        }
        if (myCard.getCardSlot().getZoneType() != ZoneType.MONSTER) {
            throw new Exception("you can't attack with this card");
        }
        if(gameManager.getCurrentPhase() != Phase.BATTLE){
            throw new Exception("action not allowed in this phase");
        }

        // TODO: ۱۷/۰۶/۲۰۲۱ already attacked

        if (direct) {
            if (gameManager.getCurrentTurnOpponentPlayer().getPlayerBoard().numberOfMonstersInZone() > 0) {
                throw new Exception("you can't attack the opponent directly");
            }
            gameManager.applyDirectAttack(((MonsterCard) myCard).getData().getAttackPoints());
        } else {
            if (attackTo.isEmpty()) {
                throw new Exception("there is no card to attack here");
            }
            AttackResult attackResult = new AttackResult((MonsterCard) myCard, (MonsterCard) attackTo.getCard());
            gameManager.applyAttackResult(attackResult, myCard, attackTo.getCard());
        }
    }

    public void setPosition(Card myCard, String toPos) throws Exception {
        boolean toAttack = toPos.equals("attack");
        if (myCard == null) {
            throw new Exception("no card selected yet");
        }
        if (myCard.getCardSlot() == null) {
            throw new Exception("you can't change this card position");
        }
        if (myCard.getCardSlot().getZoneType() != ZoneType.MONSTER) {
            throw new Exception("you can't change this card position");
        }
        if(gameManager.getCurrentPhase() != Phase.MAIN){
            throw new Exception("action not allowed in this phase");
        }
        // TODO: ۱۷/۰۶/۲۰۲۱ already changed
        if (((MonsterCard) myCard).isAttackPosition() == toAttack) {
            throw new Exception("this card is already in the wanted position");
        }
        ((MonsterCard) myCard).changePosition(toAttack);
    }

    public void activateSpellCard(Card myCard) throws Exception {
        if (myCard == null) {
            throw new Exception("no card selected yet");
        }
        if (myCard.getCardType() != CardType.SPELL) {
            throw new Exception("activate effect is only for spell cards");
        }
        if(gameManager.getCurrentPhase() != Phase.MAIN){
            throw new Exception("action not allowed in this phase");
        }
        SpellCard spellCard = (SpellCard) myCard;
        if (spellCard.isActivated()) {
            throw new Exception("you have already activated this card");
        }
        // TODO: ۲۳/۰۶/۲۰۲۱ preparation not done
        if (spellCard.getData().getSpellProperty() == SpellTrapProperty.FIELD) {
            if (spellCard.getCardSlot().getZoneType() != ZoneType.FIELD) {
                if (!playerBoard.getFieldZone().isEmpty()) {
                    CardSlot.moveToGraveyard(playerBoard.getFieldZone(), playerBoard.getGraveyard());
                }
                handCards.remove(myCard);
                playerBoard.getFieldZone().setCard(myCard);
            }
        } else {
            if (spellCard.getCardSlot().getZoneType() != ZoneType.SPELL_AND_TRAP) {
                if (playerBoard.isSpellTrapZoneFull()) {
                    throw new Exception("spell card zone is full");
                }
                handCards.remove(myCard);
                playerBoard.addSpellTrapCardToZone(myCard);
            }
        }
        spellCard.onActivate(this);
    }

    public void onChangeTurn() {
        summonOrSetInThisTurn = false;
    }

    public void addCardToHand(Card card) {
        handCards.add(card);
    }
}
