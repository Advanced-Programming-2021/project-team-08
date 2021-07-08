package model.animation;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import model.enums.CardStatus;
import model.graphic.GraphicCard;

public class RotateCenterTransition extends Transition {
    private Node node;
    private int duration;
    private double to;

    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    public RotateCenterTransition(Node node, int duration, double toAngle, Point3D axis) {
        to = toAngle;
        this.node = node;
        this.duration = duration;
        setCycleDuration(Duration.millis(duration));
        setInterpolator(Interpolator.LINEAR);

        Rotate rotate = new Rotate(0, axis);
        rotate.setPivotX(40);
        rotate.setPivotY(60);
        node.getTransforms().add(rotate);
        rotate.angleProperty().bind(angleY);
    }

    @Override
    protected void interpolate(double frac) {
        double nowRotation = angleY.get();
        if (Math.abs(nowRotation-to) <= 0.1) {
            angleY.set(to);
            return;
        }
        angleY.set(frac * to);
    }
}
