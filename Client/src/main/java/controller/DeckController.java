package controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Command;
import model.Deck;
import model.cards.data.CardData;
import model.cards.data.SpellCardData;
import model.cards.data.TrapCardData;
import model.enums.CardType;
import model.enums.CommandFieldType;
import model.exceptions.ParseCommandException;
import view.menus.DeckMenu;
import view.menus.SceneName;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeckController {
    private DeckMenu deckMenu;
    public TextField deckNameTextField;
    public Label deckNameLabel;
    public VBox scrollPane;
    public AnchorPane listOfDecks;
    public AnchorPane deckSetting;
    public Button nextDeckCreate;
    public Label message;
    public AnchorPane scrollMainDeck;
    public AnchorPane scrollSideDeck;
    public AnchorPane scrollHandCards;
    public Label goToSideOrMainDeck;
    public Button goToMainDeckButton;
    public Button goToSideDeckButton;
    public Button backToListOfDecks;
    public Button backToMainScene;
    public Label addOrDeleteMessage;
    public Rectangle mainDeckDrop;
    public Rectangle sideDeckDrop;


    @FXML
    void initialize() {
        if (deckMenu == null) deckMenu = new DeckMenu(this);
        ArrayList<Deck> showingDeck = ApplicationManger.getLoggedInUser().getDecks();
        listOfDecks.setLayoutX(0);
        deckSetting.setLayoutX(1600);
        scrollPane.getChildren().clear();
        setDecks(scrollPane, showingDeck);
    }

    public void showMessage(String message) {
        addOrDeleteMessage.setOpacity(1);
        addOrDeleteMessage.setText(message);
        if (message.equals("card removed from main deck successfully") ||
                message.equals("card removed from side deck successfully") ||
                message.equals("card added to side deck successfully") ||
                message.equals("card added to main deck successfully"))
            addOrDeleteMessage.setTextFill(Color.GREEN);
        else addOrDeleteMessage.setTextFill(Color.RED);
    }

    public Button getGoToMainDeckButton() {
        return goToMainDeckButton;
    }

    public Button getGoToSideDeckButton() {
        return goToSideDeckButton;
    }

    public void setDecks(VBox scrollPane, ArrayList<Deck> decks) {
        for (int i = 0; i < decks.size(); i++) {
            Deck deck = decks.get(i);
            HBox hBox = new HBox();
            hBox.setPrefWidth(1000);
            hBox.setMinHeight(70);
            Button deckButton = new Button();
            Button deleteButton = new Button();
//            Button editDeck=new Button();
//            editDeck.setText("edit");
            deleteButton.setText("Delete");
            Button setActiveButton = new Button();
            hBox.getChildren().add(0, deckButton);
            hBox.getChildren().add(1, deleteButton);
            System.out.println(ApplicationManger.getLoggedInUser().getUserData().getActiveDeck());
            if (ApplicationManger.getLoggedInUser().getUserData().isThereADeckThatActivated() &&
                    ApplicationManger.getLoggedInUser().getUserData().getActiveDeck().getName().equals(deck.getName())) {
                Label setActiveLabel = new Label();
                setActiveLabel.setText("active");
                setActiveLabel.setAlignment(Pos.CENTER);
                setActiveLabel.setStyle("-fx-background-color: rgb(140, 225, 0)");
                hBox.getChildren().add(2, setActiveLabel);
                setActiveLabel.setPrefHeight(100);
                setActiveLabel.setPrefWidth(500);
            } else {
                setActiveButton.setText("set active");
                hBox.getChildren().add(2, setActiveButton);
                setActiveButton.setPrefHeight(100);
                setActiveButton.setPrefWidth(500);

            }
            deckButton.setPrefWidth(5000);
//            hBox.getChildren().add(3, editDeck);
//            System.out.println(deck.getName());
            if (Deck.isThisDeckValid(deck))
                deckButton.setText(deck.getName() + " / valid \nnumber of cards of main deck: " + deck.getMainDeck().size() + "\n number of cards of side deck: " + deck.getSideDeck().size());
            else
                deckButton.setText(deck.getName() + " / invalid \nnumber of cards of main deck: " + deck.getMainDeck().size() + "\n number of cards of side deck: " + deck.getSideDeck().size());
            scrollPane.getChildren().add(i, hBox);
            deckButton.setPrefHeight(100);
            deleteButton.setPrefHeight(100);
//            editDeck.setPrefHeight(100);

            deckButton.setPrefWidth(700);
            deleteButton.setPrefWidth(500);
//            editDeck.setPrefWidth(500);
            double x = (i % 5) * (260) + 20;
            double y = (i / 5) * (445) + 20;
            deckButton.setLayoutX(x);
            deckButton.setLayoutY(y);
            hBox.setSpacing(20);
            deckButton.setOnMouseClicked(event -> {
                deckSetting.setLayoutX(0.0);
                listOfDecks.setLayoutX(1600.0);
                addOrDeleteMessage.setOpacity(0);
                goToSideOrMainDeck.setOpacity(0);
                goToMainDeckButton.setOpacity(0);
                goToSideDeckButton.setOpacity(0);
                addOrDeleteCard(deck);

            });
            setActiveButton.setOnMouseClicked(event -> {
                ApplicationManger.getLoggedInUser().setActiveDeck(deck.getName());
                message.setTextFill(Color.GREEN);
                message.setText("deck activated successful");
                message.setOpacity(1);
                scrollPane.getChildren().clear();
                setDecks(scrollPane, decks);
            });
            deleteButton.setOnMouseClicked(event -> {
                Deck.removeADeck(deck.getName());
                message.setTextFill(Color.GREEN);
                message.setText("deck deleted successfully");
                message.setOpacity(1);
                scrollPane.getChildren().clear();
                setDecks(scrollPane, decks);
            });
//            editDeck.setOnMouseClicked(event -> {
//
//            });
            backToMainScene.setOnMouseClicked(event -> {
                ApplicationManger.goToScene1(SceneName.MAIN_MENU, false);
            });
        }
    }

    public void addOrDeleteCard(Deck deck) {

        ArrayList<CardData> mainDeckCards = deck.getMainDeck();
        ArrayList<CardData> sideDeckCards = deck.getSideDeck();
        ArrayList<CardData> handCards = Deck.getCardDataArrayFromIdArray(
                ApplicationManger.getLoggedInUser().getCardsThatThereIsNotInAnyDeck());
        scrollMainDeck.getChildren().clear();
        scrollSideDeck.getChildren().clear();
        scrollHandCards.getChildren().clear();
        deckMenu.setCards(scrollMainDeck, mainDeckCards, "main", deck.getName());
        deckMenu.setCards(scrollSideDeck, sideDeckCards, "side", deck.getName());
        deckMenu.setCards(scrollHandCards, handCards, "hand", deck.getName());
        backToListOfDecks.setOnMouseClicked(event -> {
            nextDeckCreate.setOpacity(0);
            message.setOpacity(0);
            deckNameLabel.setOpacity(0);
            deckNameTextField.setOpacity(0);
            listOfDecks.setLayoutX(0.0);
            deckSetting.setLayoutX(1600.0);
            scrollPane.getChildren().clear();
            setDecks(scrollPane, ApplicationManger.getLoggedInUser().getDecks());
        });

    }

    public void deckCreate() {
        deckNameTextField.clear();
        deckNameLabel.setOpacity(1);
        deckNameTextField.setOpacity(1);
        nextDeckCreate.setOpacity(1);

    }

    public Label getMessage() {
        return message;
    }

    public void nextDeckCreate() {
        String deckName = deckNameTextField.getText();
        if (deckName.equals("")) {
            message.setTextFill(Color.RED);
            message.setOpacity(1);
            message.setText("You have not entered any names");
        } else {
            if (Deck.isThereADeckWithThisName(deckName)) {
                message.setTextFill(Color.RED);
                message.setOpacity(1);
                message.setText("deck with name " + deckName + " already exists");
            } else {
                String username = ApplicationManger.getLoggedInUser().getUsername();
                Deck deck = new Deck(deckName, username);
                ApplicationManger.getLoggedInUser().addDeck(deck);
                message.setTextFill(Color.GREEN);
                message.setOpacity(1);
                message.setText("deck created successfully!");
                ArrayList<Deck> showingDeck = ApplicationManger.getLoggedInUser().getDecks();
                scrollPane.getChildren().clear();
                setDecks(scrollPane, showingDeck);

            }
        }
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

    public void setOpacity() {
        goToSideOrMainDeck.setOpacity(1);
        goToMainDeckButton.setOpacity(1);
        goToSideDeckButton.setOpacity(1);
    }


}
