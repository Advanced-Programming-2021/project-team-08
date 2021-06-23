package model.cards;

import model.event.Event;
import model.cards.data.MonsterCardData;
import model.enums.CardStatus;
import model.enums.CardType;
import model.gameplay.AttackResult;

public class MonsterCard extends Card {
    private boolean isAttackPosition;

    private Event<AttackResult> onAttacked;

    public MonsterCard(MonsterCardData data) {
        super(data);
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

    public Event<AttackResult> getOnAttacked() {
        return onAttacked;
    }

    public void onAttacked(AttackResult result) {
        onAttacked.invoke(result);
    }
}
