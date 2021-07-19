package controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import model.cards.data.CardData;
import view.menus.ShopScene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ShopController {

    public Label buyMessageLabel;
    public TextField searchedString;
    public AnchorPane scrollPane;
    public Label cardPrice;
    public Label userMoney;
    public ImageView menuName;
    private User activeUser;
    private ShopScene shopScene;
    private boolean isBuyMenu;

    public ShopController() {
        this.activeUser = ApplicationManger.getLoggedInUser();
        if (activeUser == null) {
            this.activeUser = new User("test", "test", "test");
            activeUser.getUserData().decreaseMoney(9900);
        }
    }


    @FXML
    void initialize() {
        updateUserMoney();
        menuName.setViewport(new Rectangle2D(0, 745, 960, 133));
    }

    public void buyCard(String cardName) {

    }

    public void setLabelMessage(String message, boolean isSuccessful) {
        buyMessageLabel.setText(message);
        if (isSuccessful) buyMessageLabel.setTextFill(Color.GREEN);
        else buyMessageLabel.setTextFill(Color.RED);
    }


    public void setSearchedBuyImage() {
        if (shopScene == null) shopScene = new ShopScene(this, activeUser);
        ArrayList<CardData> showingCard = new ArrayList<>();
        for (CardData cardData : CardData.getAllCardData()) {
            if (cardData.getName().toLowerCase(Locale.ROOT).startsWith(searchedString.getText().toLowerCase(Locale.ROOT))) {
                showingCard.add(cardData);
            }
        }
        System.out.println("the size is : " + showingCard.size());
        scrollPane.getChildren().clear();
        shopScene.setCards(scrollPane, showingCard, isBuyMenu);
    }

    public void setSearchedSellImage() {
        if (shopScene == null) shopScene = new ShopScene(this, activeUser);
        ArrayList<CardData> showingCard = new ArrayList<>();
        for (Integer cardId : activeUser.getUserData().getMyCardsIds()) {
            CardData cardData = CardData.getCardById(cardId);
            if (cardData == null) {
                System.out.println("card is null");
                continue;
            }
            if (cardData.getName().toLowerCase(Locale.ROOT).startsWith(searchedString.getText().toLowerCase(Locale.ROOT))) {
                showingCard.add(cardData);
            }
        }
        scrollPane.getChildren().clear();
        shopScene.setCards(scrollPane, showingCard, isBuyMenu);
    }

    public void updateUserMoney() {
        int userMoney = activeUser.getUserData().getMoney();
        if (userMoney < 100000) this.userMoney.setText(String.valueOf(userMoney));
        else this.userMoney.setText(userMoney / 1000 + "K");
    }

    public void updateCardPrice(int price) {
        cardPrice.setText(String.valueOf(price));
    }

    public Label getCardPrice() {
        return cardPrice;
    }

    public Label getUserMoney() {
        return userMoney;
    }

    public void buyACard(CardData cardData) {
        HashMap<String, String> data = new HashMap<>();
        data.put("name", cardData.getName());
        String response = ApplicationManger.getServerResponse("shop", "buy", data);
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        if (type.equals("SUCCESSFUL")) {
            activeUser.getUserData().decreaseMoney(cardData.getPrice());
            activeUser.getUserData().addCard(cardData.getCardId());
            updateUserMoney();
        }
        setLabelMessage(jsonObject.get("message").getAsString(), type.equals("SUCCESSFUL"));
    }

    public Label getMessageLabel() {
        return buyMessageLabel;
    }

    public void back(MouseEvent mouseEvent) {
        ApplicationManger.goToScene("mainScene.fxml");
    }

    public void buyMenu(MouseEvent mouseEvent) {
        isBuyMenu = true;
        setSearchedBuyImage();
    }

    public void sellMenu(MouseEvent mouseEvent) {
        isBuyMenu = false;
        setSearchedSellImage();
    }

    public boolean isBuyMenu() {
        return isBuyMenu;
    }

    public void sellACard(CardData cardData) {
        HashMap<String, String> data = new HashMap<>();
        data.put("name", cardData.getName());
        String response = ApplicationManger.getServerResponse("shop", "sell", data);
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        if (type.equals("SUCCESSFUL")) {
            activeUser.getUserData().addMoney(cardData.getPrice());
            activeUser.getUserData().addCard(cardData.getCardId());
            updateUserMoney();
        }
        setLabelMessage(jsonObject.get("message").getAsString(), type.equals("SUCCESSFUL"));
    }
}
