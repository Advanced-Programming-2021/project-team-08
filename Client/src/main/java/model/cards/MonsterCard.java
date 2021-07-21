package model.cards;

import model.cards.data.MonsterCardData;
import model.enums.CardStatus;
import model.enums.CardType;
import model.event.Event;
import model.gameplay.AttackResult;
import model.gameplay.Player;

public class MonsterCard extends Card {
    private boolean isAttackPosition;
    private boolean attackedThisTurn;

    protected Event<Card> onFaceUp = new Event<>();

    private final Event<AttackResult> onAttacked = new Event<>();

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

    public void setAttackedThisTurn(boolean attackedThisTurn) {
        this.attackedThisTurn = attackedThisTurn;
    }

    public boolean isAttackedThisTurn() {
        return attackedThisTurn;
    }

    public void changePosition(boolean toAttack) {
        isAttackPosition = toAttack;
    }

    public void onSummon() {
        shape.toFront();
        cardStatus = CardStatus.FACE_UP;
        onFaceUp.invoke(this);
        isAttackPosition = true;
    }

    public Event<Card> getOnFaceUp() {
        return onFaceUp;
    }

    public void onAttacked(AttackResult result) {
        if (cardStatus == CardStatus.TO_BACK) {
            cardStatus = CardStatus.FACE_UP;
        }
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
