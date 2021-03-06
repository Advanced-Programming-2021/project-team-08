package model.gameplay;

import controller.gameplay.GameManager;
import javafx.scene.layout.AnchorPane;
import model.Deck;
import model.cards.Card;
import model.cards.data.CardData;
import model.enums.CardType;
import model.enums.MonsterAttribute;
import model.enums.SpellTrapProperty;
import model.enums.ZoneType;

import java.util.ArrayList;
import java.util.Collections;

public class PlayerBoard {
    private final CardSlot graveyard = new CardSlot(ZoneType.GRAVEYARD);
    private final CardSlot deckZone = new CardSlot(ZoneType.DECK);
    private final CardSlot fieldZone = new CardSlot(ZoneType.FIELD);
    private final ArrayList<CardSlot> monsterZone = new ArrayList<CardSlot>(5) {{
        for (int i = 0; i < 5; i++) add(new CardSlot(i + 1, ZoneType.MONSTER));
    }};
    private final ArrayList<CardSlot> spellAndTrapZone = new ArrayList<CardSlot>(5) {{
        for (int i = 0; i < 5; i++) add(new CardSlot(i + 1, ZoneType.SPELL_AND_TRAP));
    }};
    private final CardSlot hand = new CardSlot(ZoneType.HAND);

    private final int playerNumber;

    public PlayerBoard(Deck playerDeck, int playerNumber, GameManager gameManager) {
        this.playerNumber = playerNumber;

        ArrayList<CardData> deck = playerDeck.getMainDeck();
        Collections.shuffle(deck);

        ArrayList<Card> deckCards = new ArrayList<>();
        for (CardData data : deck) {
            Card c = Card.createCardByCardData(data);
            deckCards.add(c);
            deckZone.appendCard(c);
        }
        gameManager.getGameController().firstSetupBoardGraphic(playerNumber, deckCards);
    }

    public int getPlayerNumber() {
        return playerNumber;
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

    public CardSlot getHand() {
        return hand;
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
        StringBuilder toShow = new StringBuilder();
        if (isCurrentTurnPlayerBoard) {
            toShow.append(graveyard.toString()).append("\t\t\t\t\t\t").append(fieldZone.toString()).append("\n");
            toShow.append(zoneArrayToString(false, monsterZone)).append("\n");
            toShow.append(zoneArrayToString(false, spellAndTrapZone)).append("\n");
            toShow.append(deckZone.toString()).append("\n");
            for (int i = 0; i < hand.getAllCards().size(); i++) {
                toShow.append("c\t");
            }
            toShow.append("\n");
        } else {
            for (int i = 7; i > hand.getAllCards().size(); i--) {
                toShow.append("\t");
            }
            for (int i = 0; i < hand.getAllCards().size(); i++) {
                toShow.append("c\t");
            }
            toShow.append("\n");
            toShow.append("\t\t\t\t\t\t").append(deckZone.toString()).append("\n");
            toShow.append(zoneArrayToString(true, spellAndTrapZone)).append("\n");
            toShow.append(zoneArrayToString(true, monsterZone)).append("\n");
            toShow.append(fieldZone.toString()).append("\t\t\t\t\t\t").append(graveyard.toString()).append("\n");
        }

        return toShow.toString();
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

    public CardSlot addSpellTrapCardToZone(Card card) {
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

    public void setGraphic(AnchorPane group) {
        deckZone.setSlotView(group.lookup("#deck" + playerNumber));
        hand.setSlotView(group.lookup("#hand" + playerNumber));
        for (int i = 0; i < 5; i++) {
            monsterZone.get(i).setSlotView(group.lookup("#monster" + playerNumber + "" + i));
        }
    }
}
