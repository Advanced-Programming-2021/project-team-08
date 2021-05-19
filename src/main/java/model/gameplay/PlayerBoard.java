package model.gameplay;

import model.enums.ZoneType;
import view.menus.Deck;

import java.util.ArrayList;

public class PlayerBoard {
    private CardZone graveyard = new CardZone(ZoneType.GRAVEYARD);
    private CardZone deckZone = new CardZone(ZoneType.DECK);
    private CardZone fieldZone = new CardZone(ZoneType.FIELD);
    private ArrayList<CardZone> monsterZone = new ArrayList<CardZone>(5) {{
        for (int i = 0; i < 5; i++) add(new CardZone(ZoneType.MONSTER));
    }};
    private ArrayList<CardZone> spellAndTrapZone = new ArrayList<CardZone>(5) {{
        for (int i = 0; i < 5; i++) add(new CardZone(ZoneType.SPELL_AND_TRAP));
    }};

    public PlayerBoard(Deck playerDeck) {
        deckZone.addCards(playerDeck.getMainDeck());
    }

    public ArrayList<CardZone> getMonsterZone() {
        return monsterZone;
    }

    public String getShowingString(boolean isCurrentTurnPlayerBoard) {
        String toShow = "";
        if (isCurrentTurnPlayerBoard) {

        } else {
            toShow += "DN\n";
            toShow += "\t" + spellAndTrapZone.toString() + "\n";
            toShow += "\t" + monsterZone.toString() + "\n";
            toShow += "GY\t\t\t\t\t\tFZ\n";
        }

        return toShow;
    }
}
