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
            toShow += "GY\t\t\t\t\t\tFZ\n";
            toShow += zoneArrayToString(false, monsterZone) + "\n";
            toShow += zoneArrayToString(false, spellAndTrapZone) + "\n";
            toShow += "DN\n";
        } else {
            toShow += "\t\t\t\t\t\tDN\n";
            toShow += zoneArrayToString(false, spellAndTrapZone) + "\n";
            toShow += zoneArrayToString(false, monsterZone) + "\n";
            toShow += "FZ\t\t\t\t\t\tGY\n";
        }

        return toShow;
    }

    private String zoneArrayToString(boolean isMirror, ArrayList<CardZone> zoneArray) {
        String result = "";
        int[] sequence = {5, 3, 1, 2, 4};
        int[] mirroredSequence = {4, 2, 1, 3, 5};

        if (!isMirror) {
            for (int i : sequence) {
                result += "\t" + zoneArray.get(i - 1).toString();
            }
        } else {
            for (int i : mirroredSequence) {
                result += "\t" + zoneArray.get(i - 1).toString();
            }
        }
        return result;
    }
}
