package model.cards.data;

import model.enums.CardType;

import java.util.ArrayList;

public abstract class CardData {
    protected CardType cardType;
    protected String cardName;
    protected int CardId;
    private static ArrayList<CardData> allCardData = new ArrayList<>();

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

    public abstract int getPrice();

    public abstract int getCardId();

    public static void addCardData(CardData cardData) {
        allCardData.add(cardData);
    }

    public static ArrayList<CardData> getAllCardData() {
        return allCardData;
    }

    public abstract String getName();

    public abstract String getCardDescription();
}
