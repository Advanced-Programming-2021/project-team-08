package model.animation;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import model.enums.CardStatus;
import model.graphic.GraphicCard;

public class FlipCardAnimation extends Transition {
    private GraphicCard card;
    private int duration;
    private boolean flipped = false;
    private CardStatus toStatus;

    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    public FlipCardAnimation(GraphicCard card, int duration, CardStatus to) {
        toStatus = to;
        this.card = card;
        this.duration = duration;
        setCycleDuration(Duration.millis(duration));
        setInterpolator(Interpolator.LINEAR);

        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        yRotate.setPivotX(40);
        yRotate.setPivotY(60);
        card.getShape().getTransforms().add(yRotate);
        yRotate.angleProperty().bind(angleY);
    }

    @Override
    protected void interpolate(double frac) {
        double nowRotation = angleY.get();
        if (nowRotation >= 180) {
            angleY.set(180);
            return;
        }
        if (nowRotation >= 90 && !flipped) {
            card.getShape().setImage(toStatus == CardStatus.TO_BACK ? GraphicCard.getBack() : card.getFace());
        }
        angleY.set(frac * 180);
    }
}
