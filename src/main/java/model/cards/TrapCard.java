package model.cards;

import model.cards.data.TrapCardData;
import model.enums.CardType;

public class TrapCard extends Card {
    public TrapCard(TrapCardData data) {
        cardData = data;
        cardType = CardType.TRAP;
    }

    public TrapCardData getData() {
        return (TrapCardData) cardData;
    }

    @Override
    public void setup() {

    }
}
