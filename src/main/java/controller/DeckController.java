package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Command;
import model.Deck;
import model.cards.Card;
import model.enums.CommandFieldType;
import model.exceptions.ParseCommandException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import view.menus.ApplicationManger;
import view.menus.DeckMenu;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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

    public void deckCreate(String userInput) {
        HashMap<String, CommandFieldType> fieldsOfDeckCreate = new HashMap<>();
        fieldsOfDeckCreate.put("deck name", CommandFieldType.STRING);
        try {
            Command deckCreateCommand = Command.parseCommand(userInput, fieldsOfDeckCreate);
            if (Deck.isThereADeckWithThisName(deckCreateCommand.getField("deck name"))) {
                System.out.println("deck with name " + deckCreateCommand.getField("deck name") + "already exists");
            } else {
                System.out.println("deck created successfully!");
                String username = ApplicationManger.getLoggedInUser().getUsername();
                Deck deck = new Deck(deckCreateCommand.getField("deck name"), username);
                ApplicationManger.getLoggedInUser().addDeck(deckCreateCommand.getField("deck name"));

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
         catch (ParseCommandException e) {
            e.printStackTrace();
        }

    }

    public void deckDelete(String userInput){
        HashMap<String, CommandFieldType> fieldsOfDeckDelete = new HashMap<>();
        fieldsOfDeckDelete.put("deck name", CommandFieldType.STRING);
        try {
            Command deckDeleteCommand = Command.parseCommand(userInput, fieldsOfDeckDelete);
            if (!Deck.isThereADeckWithThisName(deckDeleteCommand.getField("deck name"))) {
                System.out.println("deck with name " + deckDeleteCommand.getField("deck name") + " does not exist");
            } else {
                System.out.println("deck deleted successfully");
                if (Deck.getDeckWithName(deckDeleteCommand.getField("deck name")).getMainDeck()!=null) {
                    for (Card card : Deck.getDeckWithName(deckDeleteCommand.getField("deck name")).getMainDeck())
                        User.getCardsThatThereIsNotInAnyDeck().add(card.getCardData().getCardName());
                }
                if (Deck.getDeckWithName(deckDeleteCommand.getField("deck name")).getSideDeck()!=null) {
                    for (Card card : Deck.getDeckWithName(deckDeleteCommand.getField("deck name")).getSideDeck())
                        User.getCardsThatThereIsNotInAnyDeck().add(card.getCardData().getCardName());
                }
                Deck.removeADeck(deckDeleteCommand.getField("deck name"));

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
        } catch (ParseCommandException e) {
            e.printStackTrace();
        }

    }

    public void deckSetActive(String userInput){
        HashMap<String, CommandFieldType> fieldsOfDeckSetActive = new HashMap<>();
        fieldsOfDeckSetActive.put("deck name", CommandFieldType.STRING);
        try {
            Command deckSetActiveCommand = Command.parseCommand(userInput, fieldsOfDeckSetActive);
            if (!Deck.isThereADeckWithThisName(deckSetActiveCommand.getField("deck name"))) {
                System.out.println("deck with name " + deckSetActiveCommand.getField("deck name") + " does not exist");
            } else {
                System.out.println("deck activated successfully");
                ApplicationManger.getLoggedInUser().setActiveDeck(deckSetActiveCommand.getField("deck name"));
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
        } catch (ParseCommandException e) {
            e.printStackTrace();
        }

    }

    public void addCard(String userInput){
        HashMap<String, CommandFieldType> fieldsOfAddCard = new HashMap<>();
        fieldsOfAddCard.put("card", CommandFieldType.STRING);
        fieldsOfAddCard.put("deck", CommandFieldType.STRING);
        fieldsOfAddCard.put("side", CommandFieldType.STRING);
        try {
            Command addCardCommand = Command.parseCommand(userInput, fieldsOfAddCard);
            Matcher matcher1;
            if (!User.getCardsThatThereIsNotInAnyDeck().contains(addCardCommand.getField("card"))) {
                System.out.println("card with name " + addCardCommand.getField("card") + " does not exist");
            } else if (!Deck.isThereADeckWithThisName(addCardCommand.getField("deck"))) {
                System.out.println("deck with name " + addCardCommand.getField("deck") + " does not exist");
            } else {
                matcher1 = Pattern.compile("--side").matcher(userInput);
                if (matcher1.find()) {
                    if (Deck.isSideDeckFull(addCardCommand.getField("deck"))) {
                        System.out.println("side deck is full");
                    } else if (Deck.isThereAreThreeCardsOfThisCardInDeck(addCardCommand.getField("card"), addCardCommand.getField("deck"))) {
                        System.out.println("there are already three cards with name " + addCardCommand.getField("card") + " in deck " + addCardCommand.getField("deck"));
                    } else {
                        System.out.println("card added to deck successfully");
                        Deck.addCard(addCardCommand.getField("card"), addCardCommand.getField("deck"), "side");
                        User.getCardsThatThereIsNotInAnyDeck().remove(addCardCommand.getField("card"));
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
                    if (Deck.isMainDeckFull(addCardCommand.getField("deck"))) {
                        System.out.println("main deck is full");
                    } else if (Deck.isThereAreThreeCardsOfThisCardInDeck(addCardCommand.getField("card"), addCardCommand.getField("deck"))) {
                        System.out.println("there are already three cards with name " + addCardCommand.getField("card") + " in deck " + addCardCommand.getField("deck"));
                    } else {
                        System.out.println("card added to deck successfully");
                        Deck.addCard(addCardCommand.getField("card"), addCardCommand.getField("deck"), "main");
                        User.getCardsThatThereIsNotInAnyDeck().remove(addCardCommand.getField("card"));
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
        } catch (ParseCommandException e) {
            e.printStackTrace();
        }

    }

    public void removeCard(String userInput){
        HashMap<String, CommandFieldType> fieldsOfRemoveCard = new HashMap<>();
        fieldsOfRemoveCard.put("card", CommandFieldType.STRING);
        fieldsOfRemoveCard.put("deck", CommandFieldType.STRING);
        fieldsOfRemoveCard.put("side", CommandFieldType.STRING);
        try {
            Command removeCardCommand = Command.parseCommand(userInput, fieldsOfRemoveCard);
            Matcher matcher1;
            if (!Deck.isThereADeckWithThisName(removeCardCommand.getField("deck"))) {
                System.out.println("deck with name " + removeCardCommand.getField("deck") + " does not exist");
            } else {
                matcher1 = Pattern.compile("--side").matcher(userInput);
                if (matcher1.find()) {
                    if (Deck.isThereThisCardInSideDeckOfThisDeck(removeCardCommand.getField("card"), removeCardCommand.getField("deck"))) {
                        System.out.println("card with name " + removeCardCommand.getField("card") + " does not exist in side deck");
                    } else {
                        System.out.println("card removed form deck successfully");
                        Deck.removeCardFromDeck(removeCardCommand.getField("card"), removeCardCommand.getField("deck"), "side");
                        User.getCardsThatThereIsNotInAnyDeck().add(removeCardCommand.getField("card"));
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
                    if (Deck.isThereThisCardInMainDeckOfThisDeck(removeCardCommand.getField("card"), removeCardCommand.getField("deck"))) {
                        System.out.println("card with name " + removeCardCommand.getField("card") + " does not exist in main deck");
                    } else {
                        System.out.println("card removed form deck successfully");
                        Deck.removeCardFromDeck(removeCardCommand.getField("card"), removeCardCommand.getField("deck"), "main");
                        User.getCardsThatThereIsNotInAnyDeck().add(removeCardCommand.getField("card"));
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
        } catch (ParseCommandException e) {
            e.printStackTrace();
        }

    }

    public void showAllDecks(){
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
