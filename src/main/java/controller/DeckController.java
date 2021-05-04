package controller;

import view.menus.ApplicationManger;
import view.menus.Deck;
import view.menus.Scene;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeckController extends Scene {
    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();
        Matcher matcher;
        Matcher matcher1;
        matcher = Pattern.compile("deck create ([A-Za-z]+)").matcher(userInput);
        if (matcher.find()) {
            if (Deck.isThereADeckWithThisName(matcher.group(1))) {
                System.out.println("deck with name " + matcher.group(1) + "already exists");
            } else {
                System.out.println("deck created successfully!");
                Deck deck = new Deck(matcher.group(1));
                ApplicationManger.getLoggedInUser().addDeck(matcher.group(1));
            }
        }
        matcher = Pattern.compile("deck delete ([A-Za-z]+)").matcher(userInput);
        if (matcher.find()) {
            if (!Deck.isThereADeckWithThisName(matcher.group(1))) {
                System.out.println("deck with name " + matcher.group(1) + " does not exist");
            } else {
                System.out.println("deck deleted successfully");
                //Transfer deck cards to player cards
                Deck.removeADeck(matcher.group(1));
            }
        }
        matcher = Pattern.compile("deck set-activate ([A-Za-z]+)").matcher(userInput);
        if (matcher.find()) {
            if (!Deck.isThereADeckWithThisName(matcher.group(1))) {
                System.out.println("deck with name " + matcher.group(1) + " does not exist");
            } else {
                System.out.println("deck activated successfully");
                ApplicationManger.getLoggedInUser().setActiveDeck(matcher.group(1));
            }
        }
        matcher = Pattern.compile("deck add-card --card ([A-Za-z]+) --deck ([A-Za-z]+)").matcher(userInput);
        if (matcher.find()) {
//            No cards between player cards
            if (!Deck.isThereADeckWithThisName(matcher.group(2))) {
                System.out.println("deck with name " + matcher.group(2) + " does not exist");
            } else {
                matcher1 = Pattern.compile("--side").matcher(userInput);
                if (matcher1.find()) {
                    if (Deck.isSideDeckFull(matcher.group(2))) {
                        System.out.println("side deck is full");
                    } else if (Deck.isThereAreThreeCardsOfThisCardInDeck(matcher.group(1), matcher.group(2))) {
                        System.out.println("there are already three cards with name " + matcher.group(1) + " in deck " + matcher.group(2));
                    }
                    else {
                        System.out.println("card added to deck successfully");
                        Deck.addCard(matcher.group(1),matcher.group(2),"side");
                    }
                } else {
                    if (Deck.isMainDeckFull(matcher.group(2))) {
                        System.out.println("main deck is full");
                    } else if (Deck.isThereAreThreeCardsOfThisCardInDeck(matcher.group(1), matcher.group(2))) {
                        System.out.println("there are already three cards with name " + matcher.group(1) + " in deck " + matcher.group(2));
                    }
                    else {
                        System.out.println("card added to deck successfully");
                        Deck.addCard(matcher.group(1),matcher.group(2),"main");
                    }
                }
            }
        }
        matcher = Pattern.compile("deck rm-card --card ([A-Za-z]+) --deck ([A-Za-z]+)").matcher(userInput);
        if (matcher.find()){
            if (!Deck.isThereADeckWithThisName(matcher.group(2))) {
                System.out.println("deck with name " + matcher.group(2) + " does not exist");
            }
            else {
                matcher1=Pattern.compile("--side").matcher(userInput);
                if (matcher1.find()){
                    if (Deck.isThereThisCardInSideDeckOfThisDeck(matcher.group(1),matcher.group(2))){
                        System.out.println("card with name " + matcher.group(1) + " does not exist in side deck");
                    }
                    else {
                        System.out.println("card removed form deck successfully");
                        //Transfer deck cards to player cards
                        Deck.removeCardFromDeck(matcher.group(1),matcher.group(2),"side");
                    }
                }
                else {
                    if (Deck.isThereThisCardInMainDeckOfThisDeck(matcher.group(1),matcher.group(2))){
                        System.out.println("card with name " + matcher.group(1) + " does not exist in main deck");
                    }
                    else {
                        System.out.println("card removed form deck successfully");
                        //Transfer deck cards to player cards
                        Deck.removeCardFromDeck(matcher.group(1),matcher.group(2),"main");
                    }
                }
            }
        }
        matcher =Pattern.compile("deck show --all").matcher(userInput);
        if (matcher.find()){
            ArrayList<String> decksName = ApplicationManger.getLoggedInUser().getDecksName();
            System.out.println("Decks:");
            System.out.println("Active deck:");
            if(ApplicationManger.getLoggedInUser().getActiveDeck()!=null){
                System.out.println(ApplicationManger.getLoggedInUser().getActiveDeck());
            }
            System.out.println("Other decks:");
            decksName.sort(Comparator.naturalOrder());
            for (String item:decksName){
                if (Deck.isThisDeckValid(item)){
                    System.out.println(item+": main deck " + Deck.numberOfMainDeckCards(item) + ", side deck " + Deck.numberOfSideDeckCards(item) + ", valid");
                }
                else {
                    System.out.println(item+": main deck " + Deck.numberOfMainDeckCards(item) + ", side deck " + Deck.numberOfSideDeckCards(item) + ", invalid");
                }
            }
        }
        //show deck
        return 1;
    }
}
