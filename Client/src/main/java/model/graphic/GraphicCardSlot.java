package model.graphic;

import javafx.scene.Node;
import model.enums.ZoneType;

import java.util.ArrayList;

public class GraphicCardSlot {
    private ArrayList<GraphicCard> cards = new ArrayList<>();
    private ZoneType type;
    private int number;
    private Node imageView;

    public GraphicCardSlot(ZoneType type, Node imageView) {
        this.type = type;
        this.imageView = imageView;
    }

    public GraphicCardSlot(ZoneType type, int number, Node imageView) {
        this.type = type;
        this.number = number;
        this.imageView = imageView;
    }

    public Node getImageView() {
        return imageView;
    }

    public ZoneType getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    public void appendCard(GraphicCard card) {
        card.setSlot(this);
        cards.add(card);
    }

    public void removeCard(GraphicCard card) {
        cards.remove(card);
    }

    public ArrayList<GraphicCard> getAllCards() {
        return cards;
    }

    public GraphicCard getCard() {
        return cards.get(0);
    }

    public void setCard(GraphicCard card) {
        card.setSlot(this);
        cards.clear();
        cards.add(card);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
