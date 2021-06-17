package model.gameplay;

import model.cards.Card;
import model.cards.data.CardData;
import model.enums.ZoneType;
import model.Deck;

import java.util.ArrayList;

public class PlayerBoard {
    private CardSlot graveyard = new CardSlot(ZoneType.GRAVEYARD);
    private CardSlot deckZone = new CardSlot(ZoneType.DECK);
    private CardSlot fieldZone = new CardSlot(ZoneType.FIELD);
    private ArrayList<CardSlot> monsterZone = new ArrayList<CardSlot>(5) {{
        for (int i = 0; i < 5; i++) add(new CardSlot(ZoneType.MONSTER));
    }};
    private ArrayList<CardSlot> spellAndTrapZone = new ArrayList<CardSlot>(5) {{
        for (int i = 0; i < 5; i++) add(new CardSlot(ZoneType.SPELL_AND_TRAP));
    }};

    public PlayerBoard(Deck playerDeck) {
        ArrayList<CardData> deck = playerDeck.getMainDeck();
        for (CardData data : deck) {
            deckZone.appendCard(Card.createCardByCardData(data));
        }
    }

    public ArrayList<CardSlot> getMonsterZone() {
        return monsterZone;
    }

    public Card drawCardFromDeck() throws Exception {
        return deckZone.drawTopCard();
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

    private String zoneArrayToString(boolean isMirror, ArrayList<CardSlot> zoneArray) {
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

    public boolean isMonsterZoneFull() {
        for (int i = 0; i < 5; i++) {
            if (monsterZone.get(i).isEmpty())
                return false;
        }
        return true;
    }

    public void summonMonster(Card card) {
        for (CardSlot slot : monsterZone) {
            if (slot.isEmpty()) {
                try {
                    slot.setCard(card);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setMonster(Card card) {
        for (CardSlot slot : monsterZone) {
            if (slot.isEmpty()) {
                try {
                    slot.setCard(card);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
