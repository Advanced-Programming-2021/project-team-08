package model.cards;

import model.cards.data.MonsterCardData;
import model.enums.CardStatus;
import model.enums.CardType;
import model.gameplay.CardSlot;

public class MonsterCard extends Card {
    private boolean isAttackPosition;

    public MonsterCard(MonsterCardData data) {
        cardData = data;
        cardType = CardType.MONSTER;
    }

    public MonsterCardData getData() {
        return (MonsterCardData) cardData;
    }

    public boolean isAttackPosition() {
        return isAttackPosition;
    }

    public void setAttackPosition(boolean attackPosition) {
        isAttackPosition = attackPosition;
    }

    public int getTributeNumber(){
        if(getData().getLevel() <= 4){
            return 0;
        }else if(getData().getLevel() <= 6){
            return 1;
        }else {
            return 2;
        }
    }

    @Override
    public void setup() {

    }

    public void changePosition(boolean toAttack) {
        // TODO: ۱۸/۰۶/۲۰۲۱
    }

    public void onSummon(CardSlot cardSlot) {
        this.cardSlot = cardSlot;
        cardStatus = CardStatus.FACE_UP;
        isAttackPosition = true;
    }

    public void onSet(CardSlot cardSlot) {
        this.cardSlot = cardSlot;
        cardStatus = CardStatus.TO_BACK;
        isAttackPosition = false;
    }
}
