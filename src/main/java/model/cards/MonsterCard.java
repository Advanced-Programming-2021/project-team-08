package model.cards;

import model.cards.data.MonsterCardData;
import model.enums.CardStatus;
import model.enums.CardType;
import model.event.Event;
import model.gameplay.AttackResult;
import model.gameplay.Player;

public class MonsterCard extends Card {
    protected Event<Card> faceUp = new Event<>();
    private boolean isAttackPosition;
    private boolean attackedThisTurn;
    private Event<AttackResult> onAttacked = new Event<>();

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

    public Event<AttackResult> getOnAttacked() {
        return onAttacked;
    }

    public int getTributeNumber() {
        if (getData().getLevel() <= 4) {
            return 0;
        } else if (getData().getLevel() <= 6) {
            return 1;
        } else {
            return 2;
        }
    }

    public boolean isAttackedThisTurn() {
        return attackedThisTurn;
    }

    public void setAttackedThisTurn(boolean attackedThisTurn) {
        this.attackedThisTurn = attackedThisTurn;
    }

    public void changePosition(boolean toAttack) {
        isAttackPosition = toAttack;
    }

    public void onSummon() {
        shape.toFront();
        cardStatus = CardStatus.FACE_UP;
        faceUp.invoke(this);
        isAttackPosition = true;
    }

    public Event<Card> getFaceUp() {
        return faceUp;
    }

    public void onAttacked(AttackResult result) {
        onAttacked.invoke(result);
    }

    @Override
    public void onSet() {
        cardStatus = CardStatus.TO_BACK;
        isAttackPosition = false;
    }

    @Override
    public void setup(Player owner) {
        super.setup(owner);
        cardOwner.getOnChangeTurnEvent().addListener(() -> {
            attackedThisTurn = false;
        });
    }
}
