package model.cards;

import model.cards.data.MonsterCardData;
import model.enums.CardStatus;
import model.enums.CardType;

public class MonsterCard extends Card {
    private MonsterCardData data;
    private CardStatus cardStatus;
    private boolean isAttackPosition;

    public MonsterCard(MonsterCardData data) {
        this.data = data;
        cardData = data;
        cardType = CardType.MONSTER;
    }

    public CardStatus getCardStatus() {
        return cardStatus;
    }

    public boolean isAttackPosition() {
        return isAttackPosition;
    }

    @Override
    public void setup() {

    }

    public void changePosition(boolean toAttack){

    }

    public void onSummon(){
        cardStatus = CardStatus.FACE_UP;
    }

    public void onSet(){
        cardStatus = CardStatus.TO_BACK;
    }
}
