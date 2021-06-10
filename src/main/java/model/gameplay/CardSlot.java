package model.gameplay;

import model.cards.Card;
import model.enums.ZoneType;

import java.util.ArrayList;

public class CardSlot {
    private ZoneType zoneType;
    private ArrayList<Card> cards = new ArrayList<>();
    private boolean isSingular;

    public CardSlot(ZoneType zoneType) {
        this.zoneType = zoneType;
        switch (zoneType) {
            case GRAVEYARD:
            case DECK:
                isSingular = false;
                break;
            case FIELD:
            case MONSTER:
            case SPELL_AND_TRAP:
                isSingular = true;
                break;
        }
    }

    public void setCard(Card card) throws Exception {
        if (isSingular) {
            if (cards.size() > 0) throw new Exception("this zone already had card");
            return;
        }
    }

    public boolean isEmpty() {
        return (cards.size() == 0);
    }

    //for singular zone


    //for non-singular zone
    public void addCards(ArrayList<Card> cards) {
        this.cards.addAll(cards);
    }

    public void appendCard(Card card){
        cards.add(card);
    }

    public Card drawTopCard() {
        Card c = cards.get(cards.size() - 1);
        cards.remove(cards.size() - 1);
        return c;
    }

    @Override
    public String toString() {
        return "O";
    }
}
