package model.cards.data;

import model.enums.CardType;

import java.util.ArrayList;

public abstract class CardData {
    protected CardType cardType;
    protected String cardName;
    protected int cardId;
    protected int price;
    protected String cardDescription;
    private static ArrayList<CardData> allCardData = new ArrayList<>();

    public String getCardName() {
        return cardName;
    }

    public CardType getCardType() {
        return cardType;
    }

    public static CardData getCardByName(String cardName) {
        for (CardData cardData : allCardData) {
            if (cardData.getName().equals(cardName)) return cardData;
        }
        return null;
    }

    public int getPrice(){
        return  price;
    }

    public int getCardId(){
        return cardId;
    }

    public static void addCardData(CardData cardData) {
        allCardData.add(cardData);
    }

    public static ArrayList<CardData> getAllCardData() {
        return allCardData;
    }

    public  String getName() {
        return cardName;
    }

    public void setName(String name) {
        this.cardName = name;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setId(int id) {
        this.cardId = id;
    }
}
