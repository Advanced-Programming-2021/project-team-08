package model.gameplay;

import model.cards.Card;
import model.enums.ZoneType;

import java.util.ArrayList;

public class CardZone {
    private ZoneType zoneType;
    private ArrayList<Card> cards = new ArrayList<>();
    private boolean isSingular;

    public CardZone(ZoneType zoneType) {
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

    public void addCard(Card card) throws Exception {
        if (isSingular) {
            if (cards.size() > 0) throw new Exception("this zone already had card");
            return;
        }
        cards.add(card);
    }

    public boolean isEmpty() {
        return (cards.size() == 0);
    }

    //for singular zone


    //for non-singular zone
    public void addCards(ArrayList<Card> cards) {
        this.cards.addAll(cards);
    }
}
