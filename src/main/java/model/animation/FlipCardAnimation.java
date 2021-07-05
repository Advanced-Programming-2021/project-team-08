package model.animation;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import model.cards.Card;

public class FlipCardAnimation extends Transition {
    private Card card;
    private int duration;
    private boolean flipped = false;

    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    public FlipCardAnimation(Card card, int duration) {
        this.card = card;
        this.duration = duration;
        setCycleDuration(Duration.millis(duration));
        setInterpolator(Interpolator.LINEAR);

        Rotate yRotate;
        card.getShape().getTransforms().add(yRotate = new Rotate(0, Rotate.Y_AXIS));
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
            card.getShape().setImage(card.getCardData().getCardImage());
        }

        //card.getShape().setRotate(frac * 180);
        //card.getShape().getTransforms().add(new Rotate(frac * 180, Rotate.Y_AXIS));

        angleY.set(frac * 180);
    }
}
