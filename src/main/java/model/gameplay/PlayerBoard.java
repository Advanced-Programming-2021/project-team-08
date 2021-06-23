package model.gameplay;

import model.cards.Card;
import model.cards.MonsterCard;
import model.cards.SpellCard;
import model.cards.TrapCard;
import model.cards.data.CardData;
import model.cards.data.MonsterCardData;
import model.cards.data.SpellCardData;
import model.cards.data.TrapCardData;
import model.enums.CardType;
import model.enums.MonsterAttribute;
import model.enums.SpellTrapProperty;
import model.enums.ZoneType;
import model.Deck;

import java.util.ArrayList;
import java.util.Collections;

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
        Collections.shuffle(deck);
        for (CardData data : deck) {
            deckZone.appendCard(Card.createCardByCardData(data));
        }
    }

    public ArrayList<CardSlot> getMonsterZone() {
        return monsterZone;
    }

    public CardSlot getGraveyard() {
        return graveyard;
    }

    public ArrayList<CardSlot> getSpellAndTrapZone() {
        return spellAndTrapZone;
    }

    public CardSlot getFieldZone() {
        return fieldZone;
    }

    public CardSlot getDeckZone() {
        return deckZone;
    }

    public Card drawCardFromDeck() throws Exception {
        return deckZone.drawTopCard();
    }

    public Card drawParticularMonster(MonsterAttribute monsterAttribute) {
       return deckZone.drawParticularMonster(monsterAttribute);
    }

    public Card drawParticularSpellTrap(CardType cardType, SpellTrapProperty spellTrapProperty) {
        return deckZone.drawParticularSpellTrap(cardType, spellTrapProperty);
    }

    public String getShowingString(boolean isCurrentTurnPlayerBoard) {
        String toShow = "";
        if (isCurrentTurnPlayerBoard) {
            toShow += graveyard.toString() + "\t\t\t\t\t\t" + fieldZone.toString() + "\n";
            toShow += zoneArrayToString(false, monsterZone) + "\n";
            toShow += zoneArrayToString(false, spellAndTrapZone) + "\n";
            toShow += deckZone.toString() + "\n";
        } else {
            toShow += "\t\t\t\t\t\t" + deckZone.toString() + "\n";
            toShow += zoneArrayToString(false, spellAndTrapZone) + "\n";
            toShow += zoneArrayToString(false, monsterZone) + "\n";
            toShow += fieldZone.toString() + "\t\t\t\t\t\t" + graveyard.toString() + "\n";
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
    public boolean isSpellTrapZoneFull() {
        for (int i = 0; i < 5; i++) {
            if (spellAndTrapZone.get(i).isEmpty())
                return false;
        }
        return true;
    }

    public int numberOfMonstersInZone() {
        int res = 0;
        for (int i = 0; i < 5; i++) {
            if (!monsterZone.get(i).isEmpty())
                res++;
        }
        return res;
    }

    public CardSlot addMonsterCardToZone(Card card) {
        for (CardSlot slot : monsterZone) {
            if (slot.isEmpty()) {
                try {
                    slot.setCard(card);
                    return slot;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public CardSlot addSpellTrapCardToZone(Card card){
        for (CardSlot slot : spellAndTrapZone) {
            if (slot.isEmpty()) {
                try {
                    slot.setCard(card);
                    return slot;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
