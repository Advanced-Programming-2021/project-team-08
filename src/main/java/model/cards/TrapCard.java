package model.cards;

import model.cards.data.TrapCardData;
import model.enums.CardStatus;
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

    @Override
    public void onSet() {
        cardStatus = CardStatus.TO_BACK;
    }
}
