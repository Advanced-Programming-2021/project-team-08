package model.cards;

import model.cards.data.MonsterCardData;
import model.enums.CardStatus;
import model.enums.CardType;
import model.gameplay.CardSlot;

public class MonsterCard extends Card {
    private MonsterCardData data;
    private boolean isAttackPosition;

    public MonsterCard(MonsterCardData data) {
        this.data = data;
        cardData = data;
        cardType = CardType.MONSTER;
    }

    public MonsterCardData getData() {
        return data;
    }

    public boolean isAttackPosition() {
        return isAttackPosition;
    }

    public int getTributeNumber(){
        if(data.getLevel() <= 4){
            return 0;
        }else if(data.getLevel() <= 6){
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
