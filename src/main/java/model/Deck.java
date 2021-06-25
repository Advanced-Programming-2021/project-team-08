package model;

import controller.ApplicationManger;
import controller.DataManager;
import model.cards.Card;
import model.cards.data.CardData;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Integer> mainDeck = new ArrayList<>();
    private ArrayList<Integer> sideDeck = new ArrayList<>();
    private String name;
    private String username;

    public Deck(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public static boolean isThereADeckWithThisName(String name) {
        for (Deck deck : ApplicationManger.getLoggedInUser().getUserData().getDecks()) {
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

        ApplicationManger.getLoggedInUser().getUserData().getDecks().remove(deck);
    }

    public static Deck getDeckWithName(String name) {
        return ApplicationManger.getLoggedInUser().getUserData().getDecks().stream().filter(e -> e.name.equals(name)).findFirst().orElse(null);
    }

    public static boolean isMainDeckFull(String name) {
        Deck deck = getDeckWithName(name);
        if (deck == null) return false;
        if (deck.mainDeck.size() == 60) return true;
        else return false;
    }

    public static boolean isSideDeckFull(String name) {
        Deck deck = getDeckWithName(name);
        if (deck == null) return false;
        if (deck.sideDeck.size() == 15) return true;
        else return false;
    }

    public static boolean isThereAreThreeCardsOfThisCardInDeck(String nameOfCard, String nameOfDeck) {
        Deck deck = getDeckWithName(nameOfDeck);
        if (deck == null) return false;
        if (deck.numberOfThisCardInMainDeck(nameOfCard, nameOfDeck) + deck.numberOfThisCardInSideDeck(nameOfCard, nameOfDeck) == 3) {
            return true;
        } else return false;
    }

    public static boolean isThereAreOneCardsOfThisCardInDeck(String nameOfCard, String nameOfDeck) {
        Deck deck = getDeckWithName(nameOfDeck);
        if (deck == null) return false;
        if (deck.numberOfThisCardInMainDeck(nameOfCard, nameOfDeck) + deck.numberOfThisCardInSideDeck(nameOfCard, nameOfDeck) == 1) {
            return true;
        } else return false;
    }

    public static ArrayList<CardData> getCardDataArrayFromIdArray(ArrayList<Integer> cardIds) {
        ArrayList<CardData> result = new ArrayList<>();
        CardData temp;
        for (Integer id : cardIds) {
            temp = CardData.getAllCardData().stream().filter(c -> c.getCardId() == id).findFirst().orElse(null);
            if(temp != null) result.add(temp);
        }
        return result;
    }

    public int numberOfThisCardInMainDeck(String nameOfCard, String nameOfDeck) {
        Deck deck = getDeckWithName(nameOfDeck);
        if (deck == null) return 0;
        return Collections.frequency(deck.mainDeck, nameOfCard);
    }

    public int numberOfThisCardInSideDeck(String nameOfCard, String nameOfDeck) {
        Deck deck = getDeckWithName(nameOfDeck);
        return Collections.frequency(deck.sideDeck, nameOfCard);
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
        else if (deck.sideDeck.contains(nameOfCard)) return true;
        else return false;
    }

    public static boolean isThereThisCardInMainDeckOfThisDeck(String nameOfCard, String nameOfDeck) {
        Deck deck = getDeckWithName(nameOfDeck);
        if (deck == null) return false;
        else if (deck.mainDeck.contains(nameOfCard)) return true;
        else return false;
    }

    public static void removeCardFromDeck(String nameOfCard, String nameOfDeck, String mainOrSide) {
        if (mainOrSide.equals("main")) {
            Deck.getDeckWithName(nameOfDeck).mainDeck.remove(nameOfCard);
        } else if (mainOrSide.equals("side")) {
            Deck.getDeckWithName(nameOfDeck).sideDeck.remove(nameOfCard);
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
