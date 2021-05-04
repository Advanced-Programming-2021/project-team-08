package view.menus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Deck  {
    private static ArrayList<Deck> decks=new ArrayList<>();
    private  ArrayList<String> mainDeck;
    private  ArrayList<String> sideDeck;
    private  String name;
    public Deck(String name){
        this.name=name;
        decks.add(this);
    }

    public static boolean isThereADeckWithThisName(String name){
        for (Deck deck: decks){
            if (deck.name.equals(name)){
                return true;
            }
        }
        return false;
    }
    public static void removeADeck(String name){
        for (Deck deck: decks) {
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
    public static boolean isMainDeckFull(String name){
        Deck deck=getDeckWithName(name);
        if (deck.mainDeck.size()==60) return true;
        else return false;
    }
    public static boolean isSideDeckFull(String name){
        Deck deck=getDeckWithName(name);
        if (deck.sideDeck.size()==15) return true;
        else return false;
    }
    public static boolean isThereAreThreeCardsOfThisCardInDeck(String nameOfCard, String nameOfDeck){
        Deck deck=getDeckWithName(nameOfDeck);
        if (deck.numberOfThisCardInMainDeck(nameOfCard,nameOfDeck)+ deck.numberOfThisCardInSideDeck(nameOfCard,nameOfDeck)==3){
            return true;
        }
        else return false;
    }
    public int numberOfThisCardInMainDeck(String nameOfCard, String nameOfDeck){
        Deck deck=getDeckWithName(nameOfDeck);
        return Collections.frequency(deck.mainDeck,nameOfCard);
    }
    public int numberOfThisCardInSideDeck(String nameOfCard, String nameOfDeck){
        Deck deck=getDeckWithName(nameOfDeck);
        return Collections.frequency(deck.sideDeck,nameOfCard);
    }
    public static void addCard(String nameOfCard, String nameOfDeck, String mainOrSide){
        if (mainOrSide.equals("main")){
            Deck.getDeckWithName(nameOfDeck).mainDeck.add(nameOfCard);
        }
        else if (mainOrSide.equals("side")){
            Deck.getDeckWithName(nameOfDeck).sideDeck.add(nameOfCard);
        }
    }
    public static boolean isThereThisCardInSideDeckOfThisDeck(String nameOfCard, String nameOfDeck){
        Deck deck=getDeckWithName(nameOfDeck);
        if (deck.sideDeck.contains(nameOfCard)) return true;
        else return false;
    }
    public static boolean isThereThisCardInMainDeckOfThisDeck(String nameOfCard, String nameOfDeck){
        Deck deck=getDeckWithName(nameOfDeck);
        if (deck.mainDeck.contains(nameOfCard)) return true;
        else return false;
    }
    public static void removeCardFromDeck(String nameOfCard, String nameOfDeck, String mainOrSide){
        if (mainOrSide.equals("main")){
            Deck.getDeckWithName(nameOfDeck).mainDeck.remove(nameOfCard);
        }
        else if (mainOrSide.equals("side")){
            Deck.getDeckWithName(nameOfDeck).sideDeck.remove(nameOfCard);
        }
    }
    public static boolean isThisDeckValid(String name){
        return Deck.getDeckWithName(name).mainDeck.size() >= 40;
    }
    public static int numberOfMainDeckCards(String name){
        return Deck.getDeckWithName(name).mainDeck.size();
    }
    public static int numberOfSideDeckCards(String name){
        return Deck.getDeckWithName(name).sideDeck.size();
    }
}
