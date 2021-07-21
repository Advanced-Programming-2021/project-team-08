package view.menus;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controller.ApplicationManger;
import controller.ShopController;
import controller.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import model.cards.data.CardData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopScene extends Scene {

    private final User activeUser;
    private final ShopController shopController;
    private Label cardPrice;
    private Label userMoney;
    private Label message;

    public ShopScene() {
        this.activeUser = ApplicationManger.getLoggedInUser();
        shopController = new ShopController();
    }

    public ShopScene(ShopController shopController, User user) {
        this.activeUser = user;
        this.shopController = shopController;
        cardPrice = shopController.getCardPrice();
        userMoney = shopController.getUserMoney();
        message = shopController.getMessageLabel();
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

    public void setCards(AnchorPane anchorPane, ArrayList<CardData> cards, boolean isBuying) {
        int size = cards.size();
        if (size == 0) anchorPane.setPrefHeight(600);
        else if (size % 5 == 0) anchorPane.setPrefHeight((double) (cards.size() / 5) * 445 + 180);
        else anchorPane.setPrefHeight((double) (cards.size() / 5 + 1) * 445 + 180);
        for (int i = 0; i < size; i++) {
            addCardImage(anchorPane, cards.get(i), i, isBuying);
        }
    }

    private void addCardImage(AnchorPane anchorPane, CardData cardData, int index, boolean isBuying) {
        ImageView cardImage = new ImageView(cardData.getCardImage());
        anchorPane.getChildren().add(index, cardImage);
        cardImage.setFitHeight(425);
        cardImage.setFitWidth(240);
        int x = (index % 5) * (260) + 20;
        int y = (index / 5) * (445) + 20;
        cardImage.setX(x);
        cardImage.setY(y);
        HashMap<String, String> data = new HashMap<>();
        data.put("name", cardData.getName());
        String result = ApplicationManger.getServerResponse("shop", "getInventory", data);
        JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
        JsonObject object = JsonParser.parseString(jsonObject.get("returnObject").getAsString()).getAsJsonObject();
        int inventory = object.get("number").getAsInt();
        boolean isBanned = object.get("isBanned").getAsBoolean();

        cardImage.setOnMouseEntered(event -> {
            cardImage.setFitHeight(cardImage.getFitHeight() * 1.4);
            cardImage.setFitWidth(cardImage.getFitWidth() * 1.4);
            cardImage.toFront();
            shopController.updateCardPrice(cardData.getPrice());
        });
        cardImage.setOnMouseExited(event -> {
            cardImage.setFitHeight(cardImage.getFitHeight() / 1.4);
            cardImage.setFitWidth(cardImage.getFitWidth() / 1.4);
            cardImage.toBack();
        });
        cardImage.setOnMouseClicked(event -> {
            if (isBuying) {
                if (isBanned) {
                    message.setText("this card is banned");
                } else if (inventory < 1) {
                    message.setText("there is no card in the inventory");
                } else if (activeUser.getUserData().getMoney() < cardData.getPrice()) {
                    notEnoughMoneyAction();
                } else {
                    buyCard(cardData);
                }
            } else {
                sellCard(cardData);
            }

        });
        cardImage.hoverProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                cardImage.setCursor(Cursor.HAND);
            }
        });
    }

    private void notEnoughMoneyAction() {
        userMoney.setTextFill(Color.RED);
        message.setText("not enough money.");
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        userMoney.setTextFill(Color.WHITE);
                    }
                },
                1000
        );
    }

    private void buyCard(CardData cardData) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + " ?", ButtonType.YES, ButtonType.NO);
        alert.setContentText("Do you really want to buy " + cardData.getCardName() + "?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            shopController.buyACard(cardData);
            message.setText("you bought the card :)");
        }
    }

    private void sellCard(CardData cardData) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + " ?", ButtonType.YES, ButtonType.NO);
        alert.setContentText("Do you really want to sell " + cardData.getCardName() + "?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            shopController.sellACard(cardData);
        }
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    class sortCardsAlphabetically implements Comparator<CardData> {
        public int compare(CardData a, CardData b) {
            return a.getCardName().compareTo(b.getCardName());
        }
    }

}
