package model;

import model.cards.Card;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private static ArrayList<Deck> decks = new ArrayList<>();
    private ArrayList<Card> mainDeck;
    private ArrayList<Card> sideDeck;
    private String name;
    private String username;

    public Deck(String name, String username) {
        this.name = name;
        this.username = username;
        decks.add(this);
    }

    public static boolean isThereADeckWithThisName(String name) {
        for (Deck deck : decks) {
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
        for (Deck deck : decks) {
            if (deck.name.equals(name)) {
                decks.remove(deck);
                break;
            }
        }
    }

    public static Deck getDeckWithName(String name) {
        for (Deck deck : decks) {
            if (deck.name.equals(name)) {
                return deck;
            }
        }
        return null;
    }

    public static boolean isMainDeckFull(String name) {
        Deck deck = getDeckWithName(name);
        if (deck.mainDeck.size() == 60) return true;
        else return false;
    }

    public static boolean isSideDeckFull(String name) {
        Deck deck = getDeckWithName(name);
        if (deck.sideDeck.size() == 15) return true;
        else return false;
    }

    public static boolean isThereAreThreeCardsOfThisCardInDeck(String nameOfCard, String nameOfDeck) {
        Deck deck = getDeckWithName(nameOfDeck);
        if (deck.numberOfThisCardInMainDeck(nameOfCard, nameOfDeck) + deck.numberOfThisCardInSideDeck(nameOfCard, nameOfDeck) == 3) {
            return true;
        } else return false;
    }

    public int numberOfThisCardInMainDeck(String nameOfCard, String nameOfDeck) {
        Deck deck = getDeckWithName(nameOfDeck);
        return Collections.frequency(deck.mainDeck, nameOfCard);
    }

    public int numberOfThisCardInSideDeck(String nameOfCard, String nameOfDeck) {
        Deck deck = getDeckWithName(nameOfDeck);
        return Collections.frequency(deck.sideDeck, nameOfCard);
    }

    public static void addCard(String nameOfCard, String nameOfDeck, String mainOrSide) {
        try {
            Card card = Card.createCardByName(nameOfCard);
            if (mainOrSide.equals("main")) {
                Deck.getDeckWithName(nameOfDeck).mainDeck.add(card);
            } else if (mainOrSide.equals("side")) {
                Deck.getDeckWithName(nameOfDeck).sideDeck.add(card);
            }
        } catch (Exception e) {
            //TODO: show error
        }

    }

    public static boolean isThereThisCardInSideDeckOfThisDeck(String nameOfCard, String nameOfDeck) {
        Deck deck = getDeckWithName(nameOfDeck);
        if (deck.sideDeck.contains(nameOfCard)) return true;
        else return false;
    }

    public static boolean isThereThisCardInMainDeckOfThisDeck(String nameOfCard, String nameOfDeck) {
        Deck deck = getDeckWithName(nameOfDeck);
        if (deck.mainDeck.contains(nameOfCard)) return true;
        else return false;
    }

    public static void removeCardFromDeck(String nameOfCard, String nameOfDeck, String mainOrSide) {
        if (mainOrSide.equals("main")) {
            Deck.getDeckWithName(nameOfDeck).mainDeck.remove(nameOfCard);
        } else if (mainOrSide.equals("side")) {
            Deck.getDeckWithName(nameOfDeck).sideDeck.remove(nameOfCard);
        }
    }

    public static boolean isThisDeckValid(String name) {
        return Deck.getDeckWithName(name).mainDeck.size() >= 40;
    }

    public static int numberOfMainDeckCards(String name) {
        return Deck.getDeckWithName(name).mainDeck.size();
    }

    public static int numberOfSideDeckCards(String name) {
        return Deck.getDeckWithName(name).sideDeck.size();
    }

    public static void setDecks(ArrayList<Deck> decks) {
        Deck.decks = decks;
    }

    public ArrayList<Card> getMainDeck() {
        return mainDeck;
    }

    public ArrayList<Card> getSideDeck() {
        return sideDeck;
    }

    public static ArrayList<Deck> getDecks() {
        return decks;
    }
}
