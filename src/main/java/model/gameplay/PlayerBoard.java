package model.gameplay;

import controller.ApplicationManger;
import controller.gameplay.GameManager;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import model.Deck;
import model.cards.Card;
import model.cards.data.CardData;
import model.enums.CardType;
import model.enums.MonsterAttribute;
import model.enums.SpellTrapProperty;
import model.enums.ZoneType;
import view.menus.GamePlayScene;

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
    private CardSlot hand = new CardSlot(ZoneType.HAND);

    private int playerNumber;

    public PlayerBoard(Deck playerDeck, int playerNumber, Group group) {
        this.playerNumber = playerNumber;
        setGraphic(group);

        ArrayList<CardData> deck = playerDeck.getMainDeck();
        Collections.shuffle(deck);
        for (CardData data : deck) {
            Card c = Card.createCardByCardData(data);
            group.getChildren().add(c.getShape());
            deckZone.appendCard(c);
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

    public void setGraphic(Group group) {
        deckZone.setSlotView((ImageView) group.lookup("#deck" + playerNumber));
        hand.setSlotView((ImageView) group.lookup("#Field" + playerNumber));

    }
}
