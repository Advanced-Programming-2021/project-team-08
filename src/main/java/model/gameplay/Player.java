package model.gameplay;

import controller.gameplay.GameManager;
import model.UserData;
import model.cards.Card;
import model.cards.MonsterCard;
import model.cards.SpellCard;
import model.cards.data.SpellCardData;
import model.effectSystem.Effect;
import model.effectSystem.EquipEffect;
import model.enums.*;
import model.event.EventNoParam;

import java.util.ArrayList;

public class Player {
    private UserData userData;
    private int LP = 8000;
    private PlayerBoard playerBoard;
    private GameManager gameManager;
    private int bannedCardTurn = 0;
    private int trapBanned = 0;

    private boolean summonOrSetInThisTurn = false;
    private EventNoParam onChangeTurnEvent = new EventNoParam();

    public Player(UserData userData, PlayerBoard playerBoard, GameManager gameManager) {
        this.userData = userData;
        this.playerBoard = playerBoard;
        this.gameManager = gameManager;


        for (Card card : playerBoard.getDeckZone().getAllCards()) {
            card.setup(this);
        }

        for (int i = 0; i < 6; i++) {
            try {
                playerBoard.getHand().appendCard(playerBoard.drawCardFromDeck());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public UserData getUserData() {
        return userData;
    }

    public int getBannedCardTurn() {
        return bannedCardTurn;
    }

    public void setBannedCardTurn(int bannedCardTurn) {
        this.bannedCardTurn = bannedCardTurn;
    }

    public int getTrapBanned() {
        return trapBanned;
    }

    public void setTrapBanned(int trapBanned) {
        this.trapBanned = trapBanned;
    }

    public EventNoParam getOnChangeTurnEvent() {
        return onChangeTurnEvent;
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

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public void drawCard() {
        try {
            Card c = playerBoard.drawCardFromDeck();
            playerBoard.getHand().appendCard(c);
            System.out.println("new card added to the hand: " + c.getCardData().getCardName());
        } catch (Exception e) {
            gameManager.finishGame(gameManager.getCurrentPlayerTurn() == 1 ? 2 : 1);
        }
    }

    public Card getCardFromHand(int number) throws Exception {
        if (number < 1 || number > playerBoard.getHand().getAllCards().size())
            throw new Exception("number out of bounds");
        return playerBoard.getHand().getAllCards().get(number - 1);
    }

    public void summonCard(Card card) throws Exception {
        checkCardSelected(card);
        if (card.getCardSlot().getZoneType() != ZoneType.HAND || card.getCardType() != CardType.MONSTER) {
            throw new Exception("you can't summon this card");
        }
        if (gameManager.getCurrentPhase() != Phase.MAIN) {
            throw new Exception("action not allowed in this phase");
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
                    playerBoard.getMonsterZone().get(n - 1).getCard().moveToGraveyard();
                }
            }
            playerBoard.getHand().removeACard(card);
            playerBoard.addMonsterCardToZone(card);
            ((MonsterCard) card).onSummon();
            summonOrSetInThisTurn = true;
        }
    }

    public void flipSummonCard(Card card) throws Exception {
        checkCardSelected(card);
        if (card.getCardSlot().getZoneType() != ZoneType.MONSTER) {
            throw new Exception("you can't change this card position");
        }
        if (gameManager.getCurrentPhase() != Phase.MAIN) {
            throw new Exception("action not allowed in this phase");
        }
        if (card.getCardStatus() != CardStatus.TO_BACK) {
            throw new Exception("you can't flip-summon this card");
        }
        // TODO: ۲۵/۰۶/۲۰۲۱ set in this turn
        ((MonsterCard) card).onSummon();
    }

    public void setCard(Card card) throws Exception {
        checkCardSelected(card);
        if (card.getCardSlot().getZoneType() != ZoneType.HAND) {
            throw new Exception("you can't set this card");
        }
        if (gameManager.getCurrentPhase() != Phase.MAIN) {
            throw new Exception("action not allowed in this phase");
        }
        if (card.getCardType() == CardType.MONSTER) {
            if (playerBoard.isMonsterZoneFull()) {
                throw new Exception("monster zone is full");
            } else if (summonOrSetInThisTurn) {
                throw new Exception("you already summon/set in this turn");
            } else {
                playerBoard.getHand().removeACard(card);
                playerBoard.addMonsterCardToZone(card);
                card.onSet();
                summonOrSetInThisTurn = true;
            }
        } else {
            if (playerBoard.isSpellTrapZoneFull()) {
                throw new Exception("spell/trap card zone is full");
            }
            playerBoard.getHand().removeACard(card);
            playerBoard.addSpellTrapCardToZone(card);
            card.onSet();
        }

        // TODO: ۲۳/۰۶/۲۰۲۱ get tribute
    }

    public AttackResult attack(boolean direct, Card myCard, CardSlot attackTo) throws Exception {
        checkCardSelected(myCard);
        if (myCard.getCardSlot().getZoneType() != ZoneType.MONSTER) {
            throw new Exception("you can't attack with this card");
        }
        if (gameManager.getCurrentPhase() != Phase.BATTLE) {
            throw new Exception("action not allowed in this phase");
        }
        MonsterCard monsterCard = (MonsterCard) myCard;
        if(monsterCard.isAttackedThisTurn()){
            throw new Exception("this card already attacked");
        }

        if (direct) {
            if (gameManager.getCurrentTurnOpponentPlayer().getPlayerBoard().numberOfMonstersInZone() > 0) {
                throw new Exception("you can't attack the opponent directly");
            }
            return new AttackResult(monsterCard);
        } else {
            if (attackTo.isEmpty()) {
                throw new Exception("there is no card to attack here");
            }
            return new AttackResult(monsterCard, (MonsterCard) attackTo.getCard());
        }
    }

    public void setPosition(Card myCard, String toPos) throws Exception {
        boolean toAttack = toPos.equals("attack");
        checkCardSelected(myCard);
        if (myCard.getCardSlot().getZoneType() != ZoneType.MONSTER) {
            throw new Exception("you can't change this card position");
        }
        if (gameManager.getCurrentPhase() != Phase.MAIN) {
            throw new Exception("action not allowed in this phase");
        }
        // TODO: ۱۷/۰۶/۲۰۲۱ already changed
        if (((MonsterCard) myCard).isAttackPosition() == toAttack) {
            throw new Exception("this card is already in the wanted position");
        }
        ((MonsterCard) myCard).changePosition(toAttack);
    }

    public void activateSpellCard(Card myCard) throws Exception {
        checkCardSelected(myCard);
        if (myCard.getCardType() != CardType.SPELL) {
            throw new Exception("activate effect is only for spell cards");
        }
        if (gameManager.getCurrentPhase() != Phase.MAIN) {
            throw new Exception("action not allowed in this phase");
        }
        SpellCard spellCard = (SpellCard) myCard;
        if (spellCard.isActivated()) {
            throw new Exception("you have already activated this card");
        }
        Effect effect = myCard.getCardData().getEffects().get(0);
        if (effect != null) {
            if (!effect.entryCondition()) throw new Exception("preparations of this spell are not done yet");
        }
        if (spellCard.getData().getSpellProperty() == SpellTrapProperty.FIELD) {
            if (spellCard.getCardSlot().getZoneType() != ZoneType.FIELD) {
                if (!playerBoard.getFieldZone().isEmpty()) {
                    playerBoard.getFieldZone().getCard().moveToGraveyard();
                }
                playerBoard.getHand().removeACard(myCard);
                playerBoard.getFieldZone().setCard(myCard);
            }
        } else {
            if (spellCard.getCardSlot().getZoneType() != ZoneType.SPELL_AND_TRAP) {
                if (playerBoard.isSpellTrapZoneFull()) {
                    throw new Exception("spell card zone is full");
                }
                playerBoard.getHand().removeACard(myCard);
                playerBoard.addSpellTrapCardToZone(myCard);
            }
        }
        if (((SpellCardData)spellCard.getCardData()).getSpellProperty().equals(SpellTrapProperty.EQUIP)) {
            Card card = gameManager.getScene().getSelectedCard((EquipEffect) spellCard.getCardData().getEffects().get(0), this);
            for (Effect equipEffect :  spellCard.getCardData().getEffects()) {
                ((EquipEffect) equipEffect).setSelectedMonster((MonsterCard) card);
            }
        }
        spellCard.onActivate();
    }

    private void checkCardSelected(Card card) throws Exception {
        if (card == null) {
            throw new Exception("no card selected yet");
        }
    }

    public void onChangeTurn() {
        summonOrSetInThisTurn = false;
        onChangeTurnEvent.invoke();
    }

    public void addCardToHand(Card card) {
        playerBoard.getHand().appendCard(card);
    }
}
