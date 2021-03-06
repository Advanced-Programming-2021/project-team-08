package model;

import model.cards.Card;
import model.cards.data.CardData;
import model.cards.data.SpellCardData;
import model.cards.data.TrapCardData;
import model.enums.CardType;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private static User activeUser;
    private final ArrayList<Integer> mainDeck = new ArrayList<>();
    private final ArrayList<Integer> sideDeck = new ArrayList<>();
    private final String name;
    private final String username;

    public Deck(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public static boolean isThereADeckWithThisName(String name) {
        for (Deck deck : activeUser.getUserData().getDecks()) {
            if (deck.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }


    public static void removeADeck(String name) {
        Deck deck = getDeckWithName(name);
        if (deck == null) return;

        activeUser.getUserData().removeDeck(deck);
    }

    public static Deck getDeckWithName(String name) {
        return activeUser.getUserData().getDecks().stream().filter(e -> e.name.equals(name)).findFirst().orElse(null);
    }

    public static boolean isMainDeckFull(String name) {
        Deck deck = getDeckWithName(name);
        if (deck == null) return false;
        return deck.mainDeck.size() == 60;
    }

    public static boolean isSideDeckFull(String name) {
        Deck deck = getDeckWithName(name);
        if (deck == null) return false;
        return deck.sideDeck.size() == 15;
    }

    public static boolean isThereAreThreeCardsOfThisCardInDeck(String nameOfCard, String nameOfDeck) {
        Deck deck = getDeckWithName(nameOfDeck);
        if (deck == null) return false;
        return deck.numberOfThisCardInMainDeck(nameOfCard, nameOfDeck) + deck.numberOfThisCardInSideDeck(nameOfCard, nameOfDeck) >= 3;
    }

    public static boolean isThereAreOneCardsOfThisCardInDeck(String nameOfCard, String nameOfDeck) {
        Deck deck = getDeckWithName(nameOfDeck);
        if (deck == null) return false;
        return deck.numberOfThisCardInMainDeck(nameOfCard, nameOfDeck) + deck.numberOfThisCardInSideDeck(nameOfCard, nameOfDeck) == 1;
    }

    public static ArrayList<CardData> getCardDataArrayFromIdArray(ArrayList<Integer> cardIds) {
        ArrayList<CardData> result = new ArrayList<>();
        CardData temp;
        for (Integer id : cardIds) {
            temp = CardData.getAllCardData().stream().filter(c -> c.getCardId() == id).findFirst().orElse(null);
            if (temp != null) result.add(temp);
        }
        return result;
    }

    public int numberOfThisCardInMainDeck(String nameOfCard, String nameOfDeck) {
        Deck deck = getDeckWithName(nameOfDeck);
        if (deck == null) return 0;
        try {
            return Collections.frequency(deck.mainDeck, Card.getCardIdByName(nameOfCard));
        } catch (Exception e) {
            return 0;
        }
    }

    public int numberOfThisCardInSideDeck(String nameOfCard, String nameOfDeck) {
        Deck deck = getDeckWithName(nameOfDeck);
        try {
            return Collections.frequency(deck.sideDeck, Card.getCardIdByName(nameOfCard));
        } catch (Exception e) {
            return 0;
        }
    }

    public static void addCard(String nameOfCard, String nameOfDeck, String mainOrSide) {
        try {
            int cardId = Card.getCardIdByName(nameOfCard);
            if (mainOrSide.equals("main")) {
                Deck.getDeckWithName(nameOfDeck).mainDeck.add(cardId);
            } else if (mainOrSide.equals("side")) {
                Deck.getDeckWithName(nameOfDeck).sideDeck.add(cardId);
            }
        } catch (Exception e) {
            //TODO: show error
        }

    }

    public static boolean isThereThisCardInSideDeckOfThisDeck(String nameOfCard, String nameOfDeck) {
        Deck deck = getDeckWithName(nameOfDeck);
        if (deck == null) return false;
        else return deck.sideDeck.contains(nameOfCard);
    }

    public static boolean isThereThisCardInMainDeckOfThisDeck(String nameOfCard, String nameOfDeck) {
        Deck deck = getDeckWithName(nameOfDeck);
        try {
            if (deck == null) return false;
            else return deck.mainDeck.contains(Card.getCardIdByName(nameOfCard));
        } catch (Exception e) {
            return false;
        }
    }

    public static void addCardGraphic(String cardName, String mainOrSide, String deckName) throws Exception {
        Integer cardId = Card.getCardIdByName(cardName);
        CardData cardData = CardData.getCardByName(cardName);
        if (mainOrSide.equals("main")) {
            if (Deck.isMainDeckFull(deckName)) {
                throw new Exception("main deck is full");
            } else if (Deck.isThereAreThreeCardsOfThisCardInDeck(cardName, deckName)) {
                throw new Exception("there are already three cards with name " + cardName + " in deck " + deckName);
            } else if (cardData.getCardType().equals(CardType.SPELL) && ((SpellCardData) cardData).isLimited() &&
                    Deck.isThereAreOneCardsOfThisCardInDeck(cardName, deckName)) {
                throw new Exception("there are already one card with name " + cardName + " in deck " + deckName);
            } else if (cardData.getCardType().equals(CardType.TRAP) && ((TrapCardData) cardData).isLimited() &&
                    Deck.isThereAreOneCardsOfThisCardInDeck(cardName, deckName)) {
                throw new Exception("there are already one card with name " + cardName + " in deck " + deckName);
            } else {
                Deck.addCard(cardName, deckName, "main");
                activeUser.getUserData().save();
            }
        } else if (mainOrSide.equals("side")) {
            if (Deck.isSideDeckFull(deckName)) {
                throw new Exception("side deck is full");
            } else if (Deck.isThereAreThreeCardsOfThisCardInDeck(cardName, deckName)) {
                throw new Exception("there are already three cards with name " + cardName + " in deck " + deckName);
            } else if (cardData.getCardType().equals(CardType.SPELL) && ((SpellCardData) cardData).isLimited() &&
                    Deck.isThereAreOneCardsOfThisCardInDeck(cardName, deckName)) {
                throw new Exception("there are already one card with name " + cardName + " in deck " + deckName);
            } else if (cardData.getCardType().equals(CardType.TRAP) && ((TrapCardData) cardData).isLimited() &&
                    Deck.isThereAreOneCardsOfThisCardInDeck(cardName, deckName)) {
                throw new Exception("there are already one card with name " + cardName + " in deck " + deckName);
            } else {
                Deck.addCard(cardName, deckName, "side");
                activeUser.getUserData().save();
            }
        }
    }

    public static void removeCardFromDeck(String nameOfCard, String nameOfDeck, String mainOrSide) {
        try {
            int cardId = Card.getCardIdByName(nameOfCard);
            if (mainOrSide.equals("main")) {
                Deck.getDeckWithName(nameOfDeck).mainDeck.remove(Integer.valueOf(cardId));
            } else if (mainOrSide.equals("side")) {
                Deck.getDeckWithName(nameOfDeck).sideDeck.remove(Integer.valueOf(cardId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean isThisDeckValid(Deck deck) {
        return deck.mainDeck.size() >= 40;
    }

    public static int numberOfMainDeckCards(String name) {
        if (Deck.getDeckWithName(name) == null) return 0;
        return Deck.getDeckWithName(name).mainDeck.size();
    }

    public static int numberOfSideDeckCards(String name) {
        if (Deck.getDeckWithName(name) == null) return 0;
        return Deck.getDeckWithName(name).sideDeck.size();
    }

    public ArrayList<CardData> getMainDeck() {
        return getCardDataArrayFromIdArray(mainDeck);
    }

    public ArrayList<CardData> getSideDeck() {
        return getCardDataArrayFromIdArray(sideDeck);
    }

    public ArrayList<Integer> getMainDeckIds() {
        return mainDeck;
    }

    public ArrayList<Integer> getSideDeckIds() {
        return sideDeck;
    }
}
