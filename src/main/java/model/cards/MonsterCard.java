package model.cards;

import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point3D;
import javafx.util.Duration;
import model.animation.FlipCardAnimation;
import model.cards.data.MonsterCardData;
import model.enums.CardStatus;
import model.enums.CardType;
import model.event.Event;
import model.gameplay.AttackResult;
import model.gameplay.Player;

public class MonsterCard extends Card {
    private boolean isAttackPosition;
    private boolean attackedThisTurn;

    protected Event<Card> faceUp = new Event<>();

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
        TranslateTransition thisCard = new TranslateTransition();
        thisCard.setDuration(Duration.millis(800));
        thisCard.setNode(shape);
        thisCard.setToX(cardSlot.getSlotView().getLayoutX());
        thisCard.setToY(cardSlot.getSlotView().getLayoutY());

        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setDuration(Duration.millis(800));
        rotateTransition.setNode(shape);
        rotateTransition.setAxis(new Point3D(1, 0, 0));
        rotateTransition.setToAngle(0);

        //FlipCardAnimation flipCardAnimation = new FlipCardAnimation(c, 300);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().add(thisCard);
        parallelTransition.getChildren().add(rotateTransition);
        //parallelTransition.getChildren().add(flipCardAnimation);

        parallelTransition.play();

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
