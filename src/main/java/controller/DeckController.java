package controller;

import com.google.gson.Gson;
import model.Command;
import model.Deck;
import model.cards.Card;
import model.cards.SpellCard;
import model.cards.TrapCard;
import model.cards.data.CardData;
import model.cards.data.MonsterCardData;
import model.enums.CardType;
import model.enums.CommandFieldType;
import model.exceptions.ParseCommandException;
import org.json.simple.parser.JSONParser;
import view.menus.DeckMenu;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeckController {
    private DeckMenu deckMenu;

    public DeckController(DeckMenu deckMenu) {
        this.deckMenu = deckMenu;
    }

    public void deckCreate(String deckName) {
        if (Deck.isThereADeckWithThisName(deckName)) {
            System.out.println("deck with name " + deckName + "already exists");
        } else {
            System.out.println("deck created successfully!");
            String username = ApplicationManger.getLoggedInUser().getUsername();
            Deck deck = new Deck(deckName, username);
            ApplicationManger.getLoggedInUser().addDeck(deck);
        }
    }

    public void deckDelete(String deckName) {
        if (!Deck.isThereADeckWithThisName(deckName)) {
            System.out.println("deck with name " + deckName + " does not exist");
        } else {
            System.out.println("deck deleted successfully");
            Deck.removeADeck(deckName);
        }

    }

    public void deckSetActive(String deckName) {
        if (!Deck.isThereADeckWithThisName(deckName)) {
            System.out.println("deck with name " + deckName + " does not exist");
        } else {
            ApplicationManger.getLoggedInUser().setActiveDeck(deckName);
            System.out.println("deck activated successfully");
        }
    }

    public void addCard(String userInput) {
        HashMap<String, CommandFieldType> fieldsOfAddCard = new HashMap<>();
        fieldsOfAddCard.put("card", CommandFieldType.STRING);
        fieldsOfAddCard.put("deck", CommandFieldType.STRING);
        fieldsOfAddCard.put("side", CommandFieldType.BOOLEAN);
        try {
            Command addCardCommand = Command.parseCommand(userInput, fieldsOfAddCard);
            if (!ApplicationManger.getLoggedInUser().haveThisCardFree(addCardCommand.getField("card"))) {
                System.out.println("card with name " + addCardCommand.getField("card") + " does not exist");
            } else if (!Deck.isThereADeckWithThisName(addCardCommand.getField("deck"))) {
                System.out.println("deck with name " + addCardCommand.getField("deck") + " does not exist");
            } else {
                CardData cardData = CardData.getCardByName(addCardCommand.getField("card"));
                Card card = Card.getCardByCardData(cardData);
                if (Boolean.parseBoolean(addCardCommand.getField("side"))) {
                    if (Deck.isSideDeckFull(addCardCommand.getField("deck"))) {
                        System.out.println("side deck is full");
                    } else if (Deck.isThereAreThreeCardsOfThisCardInDeck(addCardCommand.getField("card"), addCardCommand.getField("deck"))) {
                        System.out.println("there are already three cards with name " + addCardCommand.getField("card") + " in deck " + addCardCommand.getField("deck"));
                    } else if (card.getCardType().equals(CardType.SPELL) && ((SpellCard) card).getData().isLimited() &&
                            Deck.isThereAreOneCardsOfThisCardInDeck(addCardCommand.getField("card"), addCardCommand.getField("deck"))) {
                        System.out.println("there are already one card with name " + addCardCommand.getField("card") + " in deck " + addCardCommand.getField("deck"));
                    } else if (card.getCardType().equals(CardType.TRAP) && ((TrapCard) card).getData().isLimited() &&
                            Deck.isThereAreOneCardsOfThisCardInDeck(addCardCommand.getField("card"), addCardCommand.getField("deck"))) {
                        System.out.println("there are already one card with name " + addCardCommand.getField("card") + " in deck " + addCardCommand.getField("deck"));
                    } else {
                        System.out.println("card added to deck successfully");
                        Deck.addCard(addCardCommand.getField("card"), addCardCommand.getField("deck"), "side");
                        ApplicationManger.getLoggedInUser().getUserData().save();
                    }
                } else {
                    if (Deck.isMainDeckFull(addCardCommand.getField("deck"))) {
                        System.out.println("main deck is full");
                    } else if (Deck.isThereAreThreeCardsOfThisCardInDeck(addCardCommand.getField("card"), addCardCommand.getField("deck"))) {
                        System.out.println("there are already three cards with name " + addCardCommand.getField("card") + " in deck " + addCardCommand.getField("deck"));
                    } else if (card.getCardType().equals(CardType.SPELL) && ((SpellCard) card).getData().isLimited() &&
                            Deck.isThereAreOneCardsOfThisCardInDeck(addCardCommand.getField("card"), addCardCommand.getField("deck"))) {
                        System.out.println("there are already one card with name " + addCardCommand.getField("card") + " in deck " + addCardCommand.getField("deck"));
                    } else if (card.getCardType().equals(CardType.TRAP) && ((TrapCard) card).getData().isLimited() &&
                            Deck.isThereAreOneCardsOfThisCardInDeck(addCardCommand.getField("card"), addCardCommand.getField("deck"))) {
                        System.out.println("there are already one card with name " + addCardCommand.getField("card") + " in deck " + addCardCommand.getField("deck"));
                    } else {
                        System.out.println("card added to deck successfully");
                        Deck.addCard(addCardCommand.getField("card"), addCardCommand.getField("deck"), "main");;
                        ApplicationManger.getLoggedInUser().getUserData().save();
                    }
                }
            }
        } catch (ParseCommandException e) {
            e.printStackTrace();
        }
    }

    public void removeCard(String userInput) {
        HashMap<String, CommandFieldType> fieldsOfRemoveCard = new HashMap<>();
        fieldsOfRemoveCard.put("card", CommandFieldType.STRING);
        fieldsOfRemoveCard.put("deck", CommandFieldType.STRING);
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
                        ApplicationManger.getLoggedInUser().getUserData().save();
                    }
                } else {
                    if (Deck.isThereThisCardInMainDeckOfThisDeck(removeCardCommand.getField("card"), removeCardCommand.getField("deck"))) {
                        System.out.println("card with name " + removeCardCommand.getField("card") + " does not exist in main deck");
                    } else {
                        System.out.println("card removed form deck successfully");
                        Deck.removeCardFromDeck(removeCardCommand.getField("card"), removeCardCommand.getField("deck"), "main");
                        ApplicationManger.getLoggedInUser().getUserData().save();
                    }
                }
            }
        } catch (ParseCommandException e) {
            e.printStackTrace();
        }

    }

    public void showAllDecks() {
        ArrayList<Deck> decks = ApplicationManger.getLoggedInUser().getDecks();
        System.out.println("Decks:");
        System.out.println("Active deck:");
        Deck activeDeck = ApplicationManger.getLoggedInUser().getActiveDeck();
        if (activeDeck != null) {
            if (Deck.isThisDeckValid(activeDeck)) {
                System.out.println(activeDeck.getName() + ": main deck " + activeDeck.getMainDeck().size() + ", side deck " + activeDeck.getSideDeck().size() + ", valid");
            } else {
                System.out.println(activeDeck.getName() + ": main deck " + activeDeck.getMainDeck().size() + ", side deck " + activeDeck.getSideDeck().size() + ", invalid");
            }
        }
        System.out.println("Other decks:");
        decks.sort(Comparator.comparing(Deck::getName));
        for (Deck deck : decks) {
            if (Deck.isThisDeckValid(deck)) {
                System.out.println(deck.getName() + ": main deck " + deck.getMainDeck().size() + ", side deck " + deck.getSideDeck().size() + ", valid");
            } else {
                System.out.println(deck.getName() + ": main deck " + deck.getMainDeck().size() + ", side deck " + deck.getSideDeck().size() + ", invalid");
            }
        }
    }

    public void showDeck(String deckName, String userInput) {
        ArrayList<CardData> cards = new ArrayList<>();
        ArrayList<String> monstersCardName = new ArrayList<>();
        ArrayList<String> spellsOrTrapsCardName = new ArrayList<>();
        if (!Deck.isThereADeckWithThisName(deckName))
            System.out.println("deck with name " + deckName + " does not exist");
        else {
            System.out.println("Deck: " + deckName);
            if (userInput.contains("side")) {
                System.out.println("Side deck:");
                cards = Deck.getDeckWithName(deckName).getSideDeck();
            } else if (userInput.contains("main")) {
                System.out.println("Main deck:");
                cards = Deck.getDeckWithName(deckName).getMainDeck();
            }
            for (CardData card : cards) {
                if (card.getCardType().equals(CardType.MONSTER))
                    monstersCardName.add(card.getCardName());
                else spellsOrTrapsCardName.add(card.getCardName());
            }
            monstersCardName.sort(Comparator.naturalOrder());
            spellsOrTrapsCardName.sort(Comparator.naturalOrder());
            System.out.println("Monsters:");
            for (String cardName : monstersCardName) {
                System.out.println(cardName + ": " + MonsterCardData.getCardByName(cardName).getCardDescription());
            }
            System.out.println("Spell and Traps:");
            for (String cardName:spellsOrTrapsCardName){
                CardData cardData = CardData.getCardByName(cardName);
                Card card = Card.getCardByCardData(cardData);
                if (card.getCardType().equals(CardType.SPELL)){
                    String description=((SpellCard)card).getData().getCardDescription();
                    System.out.println(cardName + ": " + description);
                }
                else if (card.getCardType().equals(CardType.TRAP)){
                    String description=((TrapCard)card).getData().getCardDescription();
                    System.out.println(cardName + ": " + description);
                }
            }
        }
    }

    public void deckShowCards() {
        ArrayList<CardData> cards = Deck.getCardDataArrayFromIdArray(ApplicationManger.getLoggedInUser().getUserData().getMyCardsIds());
        cards.sort(Comparator.comparing(CardData::getName));
        for (CardData card : cards) {
            System.out.println(card.getCardName() + ": " + card.getCardDescription());
        }

    }
}
