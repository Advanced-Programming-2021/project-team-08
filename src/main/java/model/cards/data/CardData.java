package model.cards.data;

import model.enums.CardType;

import java.util.ArrayList;

public class CardData {
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

    public int getPrice() {
        return 0;
    }

    public int getId() {
        return 0;
    }
}
