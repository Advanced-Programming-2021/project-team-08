package model.cards;

import model.cards.data.MonsterCardData;
import model.enums.CardType;

public class MonsterCard extends Card{
    private MonsterCardData data;

    public MonsterCard(MonsterCardData data) {
        this.data = data;
        cardType = CardType.MONSTER;
    }
}
