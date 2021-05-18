package model.cards.data;

import model.enums.CardType;

public abstract class CardData {
    protected CardType cardType;
    protected String cardName;
    protected int cardNumber;

    public String getCardName() {
        return cardName;
    }

    public CardType getCardType() {
        return cardType;
    }
}
