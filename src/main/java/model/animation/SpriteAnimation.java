package model.animation;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import view.menus.MainScene;

public class SpriteAnimation extends Transition {

    private final ImageView imageView;
    private final int count;
    private final int columns;
    private int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;
    private int lastIndex;

    public SpriteAnimation(
            ImageView imageView,
            Duration duration,
            int count, int columns,
            int offsetX, int offsetY,
            int width, int height) {
        this.imageView = imageView;
        this.count = count;
        this.columns = columns;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    protected void interpolate(double k) {
        final int index = Math.min((int) (k * count), count - 1);
        if (index != lastIndex) {
            final int x;
            x = (index % columns) * width + offsetX;
            final int y = (index / columns) * height + offsetY;
            //System.out.println("x is : " + x + " y is : " + y + "   index is : " + index);
            imageView.setViewport(new Rectangle2D(x, y, width, height));
            lastIndex = index;
        }
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

}

