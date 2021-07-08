package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.Command;
import model.Deck;
import model.cards.data.CardData;
import model.cards.data.SpellCardData;
import model.cards.data.TrapCardData;
import model.enums.CardType;
import model.enums.CommandFieldType;
import model.exceptions.ParseCommandException;
import view.menus.DeckMenu;
import view.menus.ShopScene;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeckController {
    private DeckMenu deckMenu;
    public TextField deckNameTextField;
    public TextField cardNameTextField;
    public Label deckNameLabel;
    public Label cardNameLabel;
    public AnchorPane scrollPane;
    public AnchorPane listOfDecks;
    public AnchorPane deckSetting;

    @FXML
    void initialize(){
        if (deckMenu==null) deckMenu = new DeckMenu();
        ArrayList<Deck> showingDeck = ApplicationManger.getLoggedInUser().getDecks();
        listOfDecks.setLayoutX(0);
        deckSetting.setLayoutX(1600);
        scrollPane.getChildren().clear();
        deckMenu.setDecks(scrollPane, showingDeck);
    }


    public AnchorPane getListOfDecks() {
        return listOfDecks;
    }

    public AnchorPane getDeckSetting() {
        return deckSetting;
    }

    public DeckController() {

    }

    public void deckCreate(String deckName) {
        if (Deck.isThereADeckWithThisName(deckName)) {
            System.out.println("deck with name " + deckName + " already exists");
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

    public void addCard(String userInput) throws ParseCommandException {
        HashMap<String, CommandFieldType> fieldsOfAddCard = new HashMap<>();
        fieldsOfAddCard.put("card", CommandFieldType.STRING);
        fieldsOfAddCard.put("deck", CommandFieldType.STRING);
        fieldsOfAddCard.put("side", CommandFieldType.BOOLEAN);

        Command addCardCommand = Command.parseCommand(userInput, fieldsOfAddCard);
        String cardName = addCardCommand.getField("card");
        String deckName = addCardCommand.getField("deck");
        boolean isSide = Boolean.parseBoolean(addCardCommand.getField("side"));

        if (!ApplicationManger.getLoggedInUser().haveThisCardFree(cardName)) {
            System.out.println("card with name " + cardName + " does not exist");
        } else if (!Deck.isThereADeckWithThisName(deckName)) {
            System.out.println("deck with name " + deckName + " does not exist");
        } else {
            CardData cardData = CardData.getCardByName(cardName);
            if (isSide) {
                if (Deck.isSideDeckFull(deckName)) {
                    System.out.println("side deck is full");
                } else if (Deck.isThereAreThreeCardsOfThisCardInDeck(cardName, deckName)) {
                    System.out.println("there are already three cards with name " + cardName + " in deck " + deckName);
                } else if (cardData.getCardType().equals(CardType.SPELL) && ((SpellCardData) cardData).isLimited() &&
                        Deck.isThereAreOneCardsOfThisCardInDeck(cardName, deckName)) {
                    System.out.println("there are already one card with name " + cardName + " in deck " + deckName);
                } else if (cardData.getCardType().equals(CardType.TRAP) && ((TrapCardData) cardData).isLimited() &&
                        Deck.isThereAreOneCardsOfThisCardInDeck(cardName, deckName)) {
                    System.out.println("there are already one card with name " + cardName + " in deck " + deckName);
                } else {
                    System.out.println("card added to deck successfully");
                    Deck.addCard(cardName, deckName, "side");
                    ApplicationManger.getLoggedInUser().getUserData().save();
                }
            } else {
                if (Deck.isMainDeckFull(deckName)) {
                    System.out.println("main deck is full");
                } else if (Deck.isThereAreThreeCardsOfThisCardInDeck(cardName, deckName)) {
                    System.out.println("there are already three cards with name " + cardName + " in deck " + deckName);
                } else if (cardData.getCardType().equals(CardType.SPELL) && ((SpellCardData) cardData).isLimited() &&
                        Deck.isThereAreOneCardsOfThisCardInDeck(cardName, deckName)) {
                    System.out.println("there are already one card with name " + cardName + " in deck " + deckName);
                } else if (cardData.getCardType().equals(CardType.TRAP) && ((TrapCardData) cardData).isLimited() &&
                        Deck.isThereAreOneCardsOfThisCardInDeck(cardName, deckName)) {
                    System.out.println("there are already one card with name " + cardName + " in deck " + deckName);
                } else {
                    System.out.println("card added to deck successfully");
                    Deck.addCard(cardName, deckName, "main");
                    ApplicationManger.getLoggedInUser().getUserData().save();
                }
            }
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
                    if (!Deck.isThereThisCardInMainDeckOfThisDeck(removeCardCommand.getField("card"), removeCardCommand.getField("deck"))) {
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

    public void showDeck(String userCommand) throws ParseCommandException {
        HashMap<String, CommandFieldType> showADeck = new HashMap<>();
        showADeck.put("deck-name", CommandFieldType.STRING);
        showADeck.put("side", CommandFieldType.BOOLEAN);

        Command command = Command.parseCommand(userCommand, showADeck);
        String deckName = command.getField("deck-name");
        Boolean isSide = Boolean.parseBoolean(command.getField("side"));

        ArrayList<CardData> cards;
        ArrayList<CardData> monstersCardName = new ArrayList<>();
        ArrayList<CardData> spellsOrTrapsCardName = new ArrayList<>();

        if (!Deck.isThereADeckWithThisName(deckName))
            System.out.println("deck with name " + deckName + " does not exist");
        else {
            System.out.println("Deck: " + deckName);
            if (isSide) {
                System.out.println("Side deck:");
                cards = Deck.getDeckWithName(deckName).getSideDeck();
            } else {
                System.out.println("Main deck:");
                cards = Deck.getDeckWithName(deckName).getMainDeck();
            }
            for (CardData card : cards) {
                if (card.getCardType().equals(CardType.MONSTER))
                    monstersCardName.add(card);
                else spellsOrTrapsCardName.add(card);
            }
            monstersCardName.sort(Comparator.comparing(CardData::getCardName));
            spellsOrTrapsCardName.sort(Comparator.comparing(CardData::getCardName));
            System.out.println("Monsters:");
            for (CardData card : monstersCardName) {
                System.out.println(card.getCardName() + ": " + card.getCardDescription());
            }
            System.out.println("Spell and Traps:");
            for (CardData card : spellsOrTrapsCardName) {
                System.out.println(card.getCardName() + ": " + card.getCardDescription());
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

    public void deckCreateGraphic(ActionEvent actionEvent) {
            deckNameTextField.setOpacity(1);
            deckNameLabel.setOpacity(1);
    }

    public void removeCardGraphic(ActionEvent actionEvent) {
        deckNameTextField.setOpacity(1);
        deckNameLabel.setOpacity(1);

        cardNameLabel.setOpacity(1);
        cardNameTextField.setOpacity(1);
    }

    public void addCardGraphic(ActionEvent actionEvent) {
        deckNameTextField.setOpacity(1);
        deckNameLabel.setOpacity(1);

        cardNameLabel.setOpacity(1);
        cardNameTextField.setOpacity(1);
    }

    public void deckSetActiveGraphic(ActionEvent actionEvent) {
        deckNameTextField.setOpacity(1);
        deckNameLabel.setOpacity(1);
    }

    public void deckDeleteGraphic(ActionEvent actionEvent) {
        deckNameTextField.setOpacity(1);
        deckNameLabel.setOpacity(1);
    }

    public void showDeckGraphic(ActionEvent actionEvent) {
        deckNameTextField.setOpacity(1);
        deckNameLabel.setOpacity(1);
    }

    public void deckShowCardsGraphic(ActionEvent actionEvent) {
        deckNameTextField.setOpacity(1);
        deckNameLabel.setOpacity(1);
    }
}
