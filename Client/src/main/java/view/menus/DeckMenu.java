package view.menus;

import controller.ApplicationManger;
import controller.DeckController;
import controller.User;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import model.Deck;
import model.cards.data.CardData;
import model.exceptions.ParseCommandException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeckMenu extends Scene {

    private final User activeUser;
    private final DeckController deckController;


    public void setCards(AnchorPane anchorPane, ArrayList<CardData> cards, String scrollPaneType, String deckName) {
        int size = cards.size();
        if (size == 0) anchorPane.setPrefHeight(600);
        else if (size % 3 == 0) anchorPane.setPrefHeight((double) (cards.size() / 3) * 212.5 + 180);
        else anchorPane.setPrefHeight((double) (cards.size() / 3 + 1) * 212.5 + 180);
        for (int i = 0; i < cards.size(); i++) {
            addCardImage(anchorPane, cards.get(i), i, scrollPaneType, deckName);
        }
    }

    private void addCardImage(AnchorPane anchorPane, CardData cardData, int index, String scrollPaneType, String deckName) {
        ImageView cardImage = new ImageView(cardData.getCardImage());
        anchorPane.getChildren().add(index, cardImage);
        cardImage.setFitHeight(212.5);
        cardImage.setFitWidth(120);
        int x = (index % 3) * (130) + 20;
        int y = (index / 3) * (222) + 20;
        cardImage.setX(x);
        cardImage.setY(y);
        cardImage.setOnMouseEntered(event -> {
            cardImage.setOpacity(0.5);
        });
        cardImage.setOnMouseClicked(event -> {
            if (!scrollPaneType.equals("hand")) {
                Deck.removeCardFromDeck(cardData.getCardName(), deckName, scrollPaneType);
                if (scrollPaneType.equals("main"))
                    deckController.showMessage("card removed from main deck successfully");
                else if (scrollPaneType.equals("side"))
                    deckController.showMessage("card removed from side deck successfully");
                deckController.addOrDeleteCard(Deck.getDeckWithName(deckName));
            }
        });
        cardImage.setOnMouseExited(event -> cardImage.setOpacity(1));
        cardImage.hoverProperty().addListener((observable, oldValue, newValue) -> cardImage.setCursor(Cursor.HAND));

        cardImage.setOnDragDetected(event -> {
            if (scrollPaneType.equals("hand")) {
                System.out.println("onDragDetected");
                cardImage.setVisible(false);
                Dragboard db = cardImage.startDragAndDrop(TransferMode.ANY);
                HashMap<DataFormat, Object> content = new HashMap<>();
                content.put(DataFormat.PLAIN_TEXT, cardData.getCardName());
                content.put(DataFormat.IMAGE, cardData.getCardImage());
                db.setContent(content);
                event.consume();
            }
        });

        deckController.mainDeckDrop.setOnDragEntered(event -> deckController.mainDeckDrop.setOpacity(1));
        deckController.mainDeckDrop.setOnDragExited(event -> deckController.mainDeckDrop.setOpacity(0));
        deckController.sideDeckDrop.setOnDragEntered(event -> deckController.sideDeckDrop.setOpacity(1));
        deckController.sideDeckDrop.setOnDragExited(event -> deckController.sideDeckDrop.setOpacity(0));

        deckController.mainDeckDrop.setOnDragOver(event -> event.acceptTransferModes(TransferMode.ANY));
        deckController.sideDeckDrop.setOnDragOver(event -> event.acceptTransferModes(TransferMode.ANY));

        deckController.mainDeckDrop.setOnDragDropped(event -> {
            System.out.println("MAIN");
            Dragboard db = event.getDragboard();
            try {
                Deck.addCardGraphic(db.getContent(DataFormat.PLAIN_TEXT).toString(), "main", deckName);
                deckController.showMessage("card added to main deck successfully");
                deckController.addOrDeleteCard(Deck.getDeckWithName(deckName));
            } catch (Exception e) {
                deckController.addOrDeleteMessage.setTextFill(Color.RED);
                deckController.addOrDeleteMessage.setOpacity(1);
                deckController.addOrDeleteMessage.setText(e.getMessage());
            }
            event.consume();
        });
        deckController.sideDeckDrop.setOnDragDropped(event -> {
            System.out.println("SIDE");
            Dragboard db = event.getDragboard();
            try {
                Deck.addCardGraphic(db.getContent(DataFormat.PLAIN_TEXT).toString(), "side", deckName);
                deckController.showMessage("card added to side deck successfully");
                deckController.addOrDeleteCard(Deck.getDeckWithName(deckName));
            } catch (Exception e) {
                deckController.addOrDeleteMessage.setTextFill(Color.RED);
                deckController.addOrDeleteMessage.setOpacity(1);
                deckController.addOrDeleteMessage.setText(e.getMessage());
            }
            event.consume();
        });
        cardImage.setOnDragDone(event -> {
            cardImage.setVisible(true);
        });
    }


    public DeckMenu(DeckController deckController) {
        this.activeUser = ApplicationManger.getLoggedInUser();
        this.deckController = deckController;
    }

    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();
        Matcher matcher;

        if ((Pattern.compile("^menu enter ([A-Za-z]+)$").matcher(userInput)).find()) {
            System.out.println("menu navigation is not possible");
            return 1;
        }
        if (userInput.equals("menu show-current")) {
            System.out.println("Deck Menu");
            return 1;
        }
        if ((matcher = Pattern.compile("^card show ([^\n]+)$").matcher(userInput)).find()) {
            showCard(CardData.getCardByName(matcher.group(1)));
            return 1;
        }
        matcher = Pattern.compile("deck create ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            deckController.deckCreate(matcher.group(1));
            return 1;
        }

        matcher = Pattern.compile("deck delete ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            deckController.deckDelete(matcher.group(1));
            return 1;
        }

        matcher = Pattern.compile("deck set-activate ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            deckController.deckSetActive(matcher.group(1));
            return 1;
        }

        matcher = Pattern.compile("deck add-card ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            try {
                deckController.addCard(matcher.group(1));
            } catch (ParseCommandException e) {
                System.out.println("invalid command");
            }
            return 1;
        }

        matcher = Pattern.compile("deck rm-card ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            deckController.removeCard(matcher.group(1));
            return 1;
        }

        matcher = Pattern.compile("deck show --all").matcher(userInput);
        if (matcher.matches()) {
            deckController.showAllDecks();
            return 1;
        }

        matcher = Pattern.compile("deck show --cards").matcher(userInput);
        if (matcher.matches()) {
            deckController.deckShowCards();
            return 1;
        }

        matcher = Pattern.compile("deck show ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            try {
                deckController.showDeck(matcher.group(1));
            } catch (ParseCommandException e) {
                System.out.println("invalid command");
            }
            return 1;
        }

        if (Pattern.compile("menu exit").matcher(userInput).matches()) {
            ApplicationManger.goToScene(SceneName.MAIN_MENU, false);
            return 0;
        }

        System.out.println("Invalid command!");
        return 1;
    }
}
