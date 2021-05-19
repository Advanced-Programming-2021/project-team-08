package model.cards.data;

import model.enums.CardType;

import java.util.ArrayList;

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
    public static CardData getCardByName(String cardName) {
        CardData cardData = MonsterCardData.getCardByName(cardName);
        if (cardData != null) return cardData;
        return null;
    }
}
