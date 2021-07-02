package view.menus;

import controller.ApplicationManger;
import controller.ShopController;
import controller.User;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.cards.data.CardData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopScene extends Scene {

    private User activeUser;
    private ShopController shopController;

    public ShopScene() {
        this.activeUser = ApplicationManger.getLoggedInUser();
        shopController = new ShopController();
    }

    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();

        if (cheatCommand(userInput) == 1) return 1;

        Matcher matcher;
        if ((Pattern.compile("^menu enter ([A-Za-z]+)$").matcher(userInput)).find()) {
            System.out.println("menu navigation is not possible");
            return 1;
        }
        if (userInput.equals("show my balance")) {
            System.out.println("your balance is: " + activeUser.getUserData().getMoney());
            return 1;
        }
        if ((matcher = Pattern.compile("^card show ([^\n]+)$").matcher(userInput)).find()) {
            showCard(CardData.getCardByName(matcher.group(1)));
            return 1;
        }
        if ((matcher = Pattern.compile("^shop buy ([\\w ]+)$").matcher(userInput)).find()) {
            shopController.buyCard(matcher.group(1));
        } else if (Pattern.compile("^menu show-current$").matcher(userInput).find()) {
            System.out.println("Shop Menu");
        } else if (Pattern.compile("shop show --all").matcher(userInput).find()) {
            showAllCard();
        } else if (Pattern.compile("menu exit").matcher(userInput).matches()) {
            ApplicationManger.goToScene(SceneName.MAIN_MENU, false);
            return 0;
        } else {
            System.out.println("invalid command");

        }
        return 1;
    }

    private int cheatCommand(String userInput) {
        Matcher matcher = Pattern.compile("increse --money ([0-9]+)").matcher(userInput);
        if (matcher.matches()) {
            ApplicationManger.getLoggedInUser().getUserData().addMoney(Integer.parseInt(matcher.group(1)));
            return 1;
        }
        return 0;
    }

    private void showAllCard() {
        System.out.println("show all card entered " + CardData.getAllCardData().size());
        ArrayList<CardData> allCards = CardData.getAllCardData();
        allCards.sort(new sortCardsAlphabetically());
        for (CardData cardData : allCards) {
            System.out.println(cardData.getName() + " : " + cardData.getPrice());
        }
    }

    public void setCards(AnchorPane anchorPane, ArrayList<CardData> cards) {

        int size = cards.size();
        if (size == 0) anchorPane.setPrefHeight(600);
        else if (size % 5 == 0) anchorPane.setPrefHeight((double) (cards.size() / 5) * 445 + 180);
        else anchorPane.setPrefHeight((double) (cards.size() / 5 + 1) * 445 + 180);
        for (int i = 0; i < size; i++) {
            addCardImage(anchorPane, cards.get(i), i);
        }
    }

    private void  addCardImage(AnchorPane anchorPane, CardData cardData, int index) {
        System.out.println("the real index is : " + index + " but must be " + anchorPane.getChildren().size());
        ImageView cardImage = new ImageView(cardData.getCardImage());
        anchorPane.getChildren().add(index, cardImage);
        cardImage.setFitHeight(425);
        cardImage.setFitWidth(240);
        int x = (index % 5) * (260) + 20;
        int y = (index / 5) * (445) + 20;
        cardImage.setX(x);
        cardImage.setY(y);
        cardImage.setOnMouseEntered(event -> {
            cardImage.setFitHeight(425 * 1.4);
            cardImage.setFitWidth(240 * 1.4);
            cardImage.toFront();
        });
        cardImage.setOnMouseExited(event -> {
            cardImage.setFitHeight(425);
            cardImage.setFitWidth(240);
            cardImage.toBack();
        });
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    class sortCardsAlphabetically implements Comparator<CardData> {
        public int compare(CardData a, CardData b) {
            return a.getCardName().compareTo(b.getCardName());
        }
    }

    private void setOnBack(AnchorPane anchorPane, int index) {
        try {
            anchorPane.getChildren().get(index).toBack();
        }catch (Exception e) {

        }
    }
}
