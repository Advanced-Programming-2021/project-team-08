package model;

import controller.ApplicationManger;
import controller.DeckController;
import model.cards.Card;
import model.cards.data.CardData;
import model.cards.data.SpellCardData;
import model.cards.data.TrapCardData;
import model.enums.CardType;

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

        ApplicationManger.getLoggedInUser().getUserData().removeDeck(deck);
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
        if (deck.numberOfThisCardInMainDeck(nameOfCard, nameOfDeck) + deck.numberOfThisCardInSideDeck(nameOfCard, nameOfDeck) >= 3) {
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
        else if (deck.sideDeck.contains(nameOfCard)) return true;
        else return false;
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

    public static void addCardGraphic(String cardName,String mainOrSide,String deckName) throws Exception {
        Integer cardId = Card.getCardIdByName(cardName);
        CardData cardData=CardData.getCardByName(cardName);
        if (mainOrSide.equals("main")){
            if (Deck.isMainDeckFull(deckName)) {
                DeckController.showMessage("main deck is full");
            } else if (Deck.isThereAreThreeCardsOfThisCardInDeck(cardName, deckName)) {
                DeckController.showMessage("there are already three cards with name " + cardName + " in deck " + deckName);
            } else if (cardData.getCardType().equals(CardType.SPELL) && ((SpellCardData) cardData).isLimited() &&
                    Deck.isThereAreOneCardsOfThisCardInDeck(cardName, deckName)) {
                DeckController.showMessage("there are already one card with name " + cardName + " in deck " + deckName);
            } else if (cardData.getCardType().equals(CardType.TRAP) && ((TrapCardData) cardData).isLimited() &&
                    Deck.isThereAreOneCardsOfThisCardInDeck(cardName, deckName)) {
                DeckController.showMessage("there are already one card with name " + cardName + " in deck " + deckName);
            } else {
                DeckController.showMessage("card added to deck successfully");
                Deck.addCard(cardName, deckName, "main");
                ApplicationManger.getLoggedInUser().getUserData().save();
            }

        }
        else if (mainOrSide.equals("side")){
            if (Deck.isSideDeckFull(deckName)) {
                DeckController.showMessage("side deck is full");
            } else if (Deck.isThereAreThreeCardsOfThisCardInDeck(cardName, deckName)) {
                DeckController.showMessage("there are already three cards with name " + cardName + " in deck " + deckName);
            } else if (cardData.getCardType().equals(CardType.SPELL) && ((SpellCardData) cardData).isLimited() &&
                    Deck.isThereAreOneCardsOfThisCardInDeck(cardName, deckName)) {
                DeckController.showMessage("there are already one card with name " + cardName + " in deck " + deckName);
            } else if (cardData.getCardType().equals(CardType.TRAP) && ((TrapCardData) cardData).isLimited() &&
                    Deck.isThereAreOneCardsOfThisCardInDeck(cardName, deckName)) {
                DeckController.showMessage("there are already one card with name " + cardName + " in deck " + deckName);
            } else {
                DeckController.showMessage("card added to deck successfully");
                Deck.addCard(cardName, deckName, "side");
                ApplicationManger.getLoggedInUser().getUserData().save();
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
