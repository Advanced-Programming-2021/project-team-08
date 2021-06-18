package model.cards;

import model.cards.data.CardData;
import model.cards.data.MonsterCardData;
import model.enums.CardType;
import model.gameplay.CardSlot;

public abstract class Card {
    protected CardType cardType;
    protected CardData cardData;

    protected CardSlot cardSlot;

    public static Card createCardByName(String cardName) throws Exception {
        CardData data = CardData.getAllCardData().stream().filter(c -> c.getCardName().equals(cardName)).findFirst().orElse(null);
        Card card = null;
        if (data == null) {
            throw new Exception("No card exist with this name!");
        } else {
            switch (data.getCardType()) {
                case MONSTER:
                    card = new MonsterCard((MonsterCardData) data);
                    break;
                case SPELL:
                    break;
                case TRAP:
                    break;
            }
        }
        return card;
    }

    public static Card createCardByCardData(CardData data) {
        Card card = null;

        switch (data.getCardType()) {
            case MONSTER:
                card = new MonsterCard((MonsterCardData) data);
                break;
            case SPELL:
                break;
            case TRAP:
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

    public CardData getCardData() {
        return cardData;
    }

    public CardType getCardType() {
        return cardType;
    }

    public abstract void setup();

    public void onAttacked(){}

    public CardSlot getCardSlot(){
        return cardSlot;
    }
}
