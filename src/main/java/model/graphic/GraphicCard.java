package model.graphic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GraphicCard {
    private static Image back = new Image("file:" + System.getProperty("user.dir") + "/src/main/resources/asset/gameplay/cardBack.png");
    private Image face;
    private ImageView shape;

    public GraphicCard(Image face) {
        this.face = face;
        shape = new ImageView(back);
        shape.setFitWidth(80);
        shape.setFitHeight(120);
        shape.setScaleX(-1);
    }

    public ImageView getShape() {
        return shape;
    }

    public Image getFace() {
        return face;
    }
}
