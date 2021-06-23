package model.cards;

import model.cards.data.MonsterCardData;
import model.enums.CardStatus;
import model.enums.CardType;

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

    public void onSummon() {
        cardStatus = CardStatus.FACE_UP;
        isAttackPosition = true;
    }

    @Override
    public void onSet() {
        cardStatus = CardStatus.TO_BACK;
        isAttackPosition = false;
    }

    public void onAttacked() {
    }
}
