package model.cards;

import model.cards.data.CardData;
import model.cards.data.MonsterCardData;
import model.cards.data.SpellCardData;
import model.cards.data.TrapCardData;
import model.effectSystem.Effect;
import model.enums.CardStatus;
import model.enums.CardType;
import model.event.EventNoParam;
import model.gameplay.CardSlot;
import model.gameplay.Player;

public abstract class Card {
    protected CardType cardType;
    protected CardData cardData;

    protected CardSlot cardSlot;

    protected CardStatus cardStatus;

    protected Player cardOwner;

    protected EventNoParam onDestroy = new EventNoParam();

    public static Card createCardByName(String cardName) throws Exception {
        CardData data = CardData.getAllCardData().stream().filter(c -> c.getCardName().equals(cardName)).findFirst().orElse(null);
        if (data == null) {
            throw new Exception("No card exist with this name!");
        }

        return createCardByCardData(data);
    }

    public static Card createCardByCardData(CardData data) {
        Card card = null;

        switch (data.getCardType()) {
            case MONSTER:
                card = new MonsterCard((MonsterCardData) data);
                break;
            case SPELL:
                card = new SpellCard((SpellCardData) data);
                break;
            case TRAP:
                card = new TrapCard((TrapCardData) data);
                break;
        }

        return card;
    }

    public static int getCardIdByName(String cardName) throws Exception {
        CardData data = CardData.getAllCardData().stream().filter(c -> c.getCardName().equals(cardName)).findFirst().orElse(null);

        if (data == null) {
            throw new Exception("No card exist with this name!");
        } else {
            return data.getCardId();
        }
    }

    public Card(CardData cardData) {
        this.cardData = cardData;
        for (Effect effect : cardData.getEffects()) {
            effect.setCard(this);
        }
    }

    public Player getCardOwner() {
        return cardOwner;
    }

    public CardStatus getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(CardStatus cardStatus) {
        this.cardStatus = cardStatus;
    }

    public CardData getCardData() {
        return cardData;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setup(Player owner){
        cardOwner = owner;
        for (Effect effect: cardData.getEffects()){
            effect.setup();
        }
    }

    public abstract void onSet();

    public CardSlot getCardSlot() {
        return cardSlot;
    }

    public void setCardSlot(CardSlot cardSlot) {
        this.cardSlot = cardSlot;
    }

    @Override
    public String toString() {
        return cardData.getCardName() + ":" + cardData.getCardDescription();
    }
}
