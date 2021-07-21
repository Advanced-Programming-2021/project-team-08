package model.animation;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class CoinAnimation extends Transition {
    private Image coinImage;
    private ImageView coin;
    private Rectangle2D face1;
    private Rectangle2D face2;
    private double toAngle;
    private final DoubleProperty angle = new SimpleDoubleProperty(0);
    private boolean onFace1;

    public CoinAnimation(Image coinImage, ImageView coin, Rectangle2D face1, Rectangle2D face2, double toAngle) {
        this.coinImage = coinImage;
        this.coin = coin;
        this.face1 = face1;
        this.face2 = face2;
        this.toAngle = toAngle;

        coin.setViewport(face1);
        onFace1 = true;

        setCycleDuration(Duration.millis(2000));
        setInterpolator(Interpolator.EASE_OUT);

        Rotate yRotate = new Rotate(0, Rotate.X_AXIS);
        yRotate.setPivotX(100);
        yRotate.setPivotY(100);
        coin.getTransforms().add(yRotate);
        yRotate.angleProperty().bind(angle);
    }

    @Override
    protected void interpolate(double frac) {
        double nowRotation = angle.get();
        for (int k = 0; k < 10; k++) {
            if (nowRotation >= 90 + 360 * k && nowRotation < 270 + 360 * k && onFace1) {
                coin.setViewport(face2);
                onFace1 = false;
            }
            if (nowRotation >= 270 + 360 * k && nowRotation < 540 + 360 * k && !onFace1) {
                coin.setViewport(face1);
                onFace1 = true;
            }
        }

        angle.set(frac * toAngle);
    }
}
