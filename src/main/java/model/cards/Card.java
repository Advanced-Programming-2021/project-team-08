package model.cards;

import controller.DataManager;
import model.cards.data.CardData;
import model.cards.data.MonsterCardData;
import model.enums.CardType;

import java.util.ArrayList;

public abstract class Card {
    protected CardType cardType;
    protected CardData cardData;

    public static Card createCardByName(String cardName) throws Exception {
        CardData data = DataManager.getAllCardData().stream().filter(c -> c.getCardName().equals(cardName)).findFirst().orElse(null);
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

    public CardData getCardData() {
        return cardData;
    }

    public abstract void setup();
}
