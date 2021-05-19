package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Deck;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import view.menus.ApplicationManger;
import view.menus.DeckMenu;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeckController {

    private User activeUser;
    private DeckMenu deckMenu;

    public DeckController(DeckMenu deckMenu) {
        this.activeUser=ApplicationManger.getLoggedInUser();
        this.deckMenu=deckMenu;
    }

    public void deckCreate(Matcher matcher) {
        if (Deck.isThereADeckWithThisName(matcher.group(1))) {
            System.out.println("deck with name " + matcher.group(1) + "already exists");
        } else {
            System.out.println("deck created successfully!");
            String username = ApplicationManger.getLoggedInUser().getUsername();
            Deck deck = new Deck(matcher.group(1), username);
            ApplicationManger.getLoggedInUser().addDeck(matcher.group(1));

            try {
                File fileToBeModified = new File("decks.json");
                FileWriter writer;
                writer = new FileWriter(fileToBeModified);
                writer.write(new Gson().toJson(Deck.getDecks()));
                writer.close();

                File fileToBeModified1=new File("users/"+ApplicationManger.getLoggedInUser().getUsername()+".json");
                FileWriter writer1;
                writer1 = new FileWriter(fileToBeModified1);
                writer1.write(new Gson().toJson(User.getUserByUsername(ApplicationManger.getLoggedInUser().getUsername())));
                writer1.close();
            }
             catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void deckDelete(Matcher matcher){
        if (!Deck.isThereADeckWithThisName(matcher.group(1))) {
            System.out.println("deck with name " + matcher.group(1) + " does not exist");
        } else {
            System.out.println("deck deleted successfully");
            User.getCardsThatThereIsNotInAnyDeck().addAll(Deck.getDeckWithName(matcher.group(1)).getMainDeck());
            User.getCardsThatThereIsNotInAnyDeck().addAll(Deck.getDeckWithName(matcher.group(1)).getSideDeck());
            Deck.removeADeck(matcher.group(1));

            try {
                File fileToBeModified = new File("decks.json");
                FileWriter writer;
                writer = new FileWriter(fileToBeModified);
                writer.write(new Gson().toJson(Deck.getDecks()));
                writer.close();

                File fileToBeModified1=new File("users/"+ApplicationManger.getLoggedInUser().getUsername()+".json");
                FileWriter writer1;
                writer1 = new FileWriter(fileToBeModified1);
                writer1.write(new Gson().toJson(User.getUserByUsername(ApplicationManger.getLoggedInUser().getUsername())));
                writer1.close();
            }
            catch (Exception e){

            }
        }
    }

    public void deckSetActive(Matcher matcher){
        if (!Deck.isThereADeckWithThisName(matcher.group(1))) {
            System.out.println("deck with name " + matcher.group(1) + " does not exist");
        } else {
            System.out.println("deck activated successfully");
            ApplicationManger.getLoggedInUser().setActiveDeck(matcher.group(1));
            try {
                File fileToBeModified = new File("users/" + ApplicationManger.getLoggedInUser().getUsername() + ".json");
                FileWriter writer;
                writer = new FileWriter(fileToBeModified);
                writer.write(new Gson().toJson(User.getUserByUsername(ApplicationManger.getLoggedInUser().getUsername())));
                writer.close();
            }
            catch (Exception e){

            }
        }
    }

    public void addCard(Matcher matcher,String userInput){
        Matcher matcher1;
        if (!User.getCardsThatThereIsNotInAnyDeck().contains(matcher.group(1))) {
            System.out.println("card with name " + matcher.group(1) + " does not exist");
        } else if (!Deck.isThereADeckWithThisName(matcher.group(2))) {
            System.out.println("deck with name " + matcher.group(2) + " does not exist");
        } else {
            matcher1 = Pattern.compile("--side").matcher(userInput);
            if (matcher1.find()) {
                if (Deck.isSideDeckFull(matcher.group(2))) {
                    System.out.println("side deck is full");
                } else if (Deck.isThereAreThreeCardsOfThisCardInDeck(matcher.group(1), matcher.group(2))) {
                    System.out.println("there are already three cards with name " + matcher.group(1) + " in deck " + matcher.group(2));
                } else {
                    System.out.println("card added to deck successfully");
                    Deck.addCard(matcher.group(1), matcher.group(2), "side");
                    User.getCardsThatThereIsNotInAnyDeck().remove(matcher.group(1));
                    try {
                        File fileToBeModified = new File("decks.json");
                        FileWriter writer;
                        writer = new FileWriter(fileToBeModified);
                        writer.write(new Gson().toJson(Deck.getDecks()));
                        writer.close();

                        File fileToBeModified1=new File("users/"+ApplicationManger.getLoggedInUser().getUsername()+".json");
                        FileWriter writer1;
                        writer1 = new FileWriter(fileToBeModified1);
                        writer1.write(new Gson().toJson(User.getUserByUsername(ApplicationManger.getLoggedInUser().getUsername())));
                        writer1.close();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (Deck.isMainDeckFull(matcher.group(2))) {
                    System.out.println("main deck is full");
                } else if (Deck.isThereAreThreeCardsOfThisCardInDeck(matcher.group(1), matcher.group(2))) {
                    System.out.println("there are already three cards with name " + matcher.group(1) + " in deck " + matcher.group(2));
                } else {
                    System.out.println("card added to deck successfully");
                    Deck.addCard(matcher.group(1), matcher.group(2), "main");
                    User.getCardsThatThereIsNotInAnyDeck().remove(matcher.group(1));
                    try {
                        File fileToBeModified = new File("decks.json");
                        FileWriter writer;
                        writer = new FileWriter(fileToBeModified);
                        writer.write(new Gson().toJson(Deck.getDecks()));
                        writer.close();

                        File fileToBeModified1=new File("users/"+ApplicationManger.getLoggedInUser().getUsername()+".json");
                        FileWriter writer1;
                        writer1 = new FileWriter(fileToBeModified1);
                        writer1.write(new Gson().toJson(User.getUserByUsername(ApplicationManger.getLoggedInUser().getUsername())));
                        writer1.close();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void removeCard(Matcher matcher,String userInput){
        Matcher matcher1;
        if (!Deck.isThereADeckWithThisName(matcher.group(2))) {
            System.out.println("deck with name " + matcher.group(2) + " does not exist");
        } else {
            matcher1 = Pattern.compile("--side").matcher(userInput);
            if (matcher1.find()) {
                if (Deck.isThereThisCardInSideDeckOfThisDeck(matcher.group(1), matcher.group(2))) {
                    System.out.println("card with name " + matcher.group(1) + " does not exist in side deck");
                } else {
                    System.out.println("card removed form deck successfully");
                    //Transfer deck cards to player cards
                    Deck.removeCardFromDeck(matcher.group(1), matcher.group(2), "side");
                }
            } else {
                if (Deck.isThereThisCardInMainDeckOfThisDeck(matcher.group(1), matcher.group(2))) {
                    System.out.println("card with name " + matcher.group(1) + " does not exist in main deck");
                } else {
                    System.out.println("card removed form deck successfully");
                    //Transfer deck cards to player cards
                    Deck.removeCardFromDeck(matcher.group(1), matcher.group(2), "main");
                }
            }
        }
    }

    public void showAllCards(){
        ArrayList<String> decksName = ApplicationManger.getLoggedInUser().getDecksName();
        System.out.println("Decks:");
        System.out.println("Active deck:");
        if (ApplicationManger.getLoggedInUser().getActiveDeck() != null) {
            System.out.println(ApplicationManger.getLoggedInUser().getActiveDeck());
        }
        System.out.println("Other decks:");
        decksName.sort(Comparator.naturalOrder());
        for (String item : decksName) {
            if (Deck.isThisDeckValid(item)) {
                System.out.println(item + ": main deck " + Deck.numberOfMainDeckCards(item) + ", side deck " + Deck.numberOfSideDeckCards(item) + ", valid");
            } else {
                System.out.println(item + ": main deck " + Deck.numberOfMainDeckCards(item) + ", side deck " + Deck.numberOfSideDeckCards(item) + ", invalid");
            }
        }
    }

    public static Object readJsonSimpleDemo(String filename) throws Exception {
        FileReader reader = new FileReader(filename);
        JSONParser jsonParser = new JSONParser();
        return jsonParser.parse(reader);
    }
}
