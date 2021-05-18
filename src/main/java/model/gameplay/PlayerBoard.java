package model.gameplay;

import model.enums.ZoneType;
import view.menus.Deck;

import java.util.ArrayList;

public class PlayerBoard {
    CardZone graveyard = new CardZone(ZoneType.GRAVEYARD);
    CardZone deckZone = new CardZone(ZoneType.DECK);
    CardZone fieldZone = new CardZone(ZoneType.FIELD);
    ArrayList<CardZone> monsterZone = new ArrayList<CardZone>(5);
    ArrayList<CardZone> spellAndTrapZone = new ArrayList<CardZone>(5);

    public PlayerBoard(Deck playerDeck) {
        deckZone.addCards(playerDeck.getMainDeck());
    }
}
