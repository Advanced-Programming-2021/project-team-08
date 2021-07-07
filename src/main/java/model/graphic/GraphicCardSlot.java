package model.graphic;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import model.enums.ZoneType;

import java.util.ArrayList;

public class GraphicCardSlot {
    private ArrayList<GraphicCard> cards = new ArrayList<>();
    private ZoneType type;
    private Node imageView;

    public GraphicCardSlot(ZoneType type, Node imageView) {
        this.type = type;
        this.imageView = imageView;
    }

    public Node getImageView() {
        return imageView;
    }

    public ZoneType getType() {
        return type;
    }

    public void appendCard(GraphicCard card){
        card.setSlot(this);
        cards.add(card);
    }
    public void setCard(GraphicCard card){
        card.setSlot(this);
        cards.set(0, card);
    }

    public ArrayList<GraphicCard> getAllCards() {
        return cards;
    }
    public GraphicCard getCard(){
        return cards.get(0);
    }
}
