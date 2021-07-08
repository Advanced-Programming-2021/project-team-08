package controller;

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

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class ShopController {

    public Label buyMessageLabel;
    public TextField searchedString;
    public AnchorPane scrollPane;
    public Label cardPrice;
    public Label userMoney;
    public ImageView menuName;
    private User activeUser;
    private ShopScene shopScene;

    private static int i = 0;

    public ShopController() {
        i++;
        this.activeUser = ApplicationManger.getLoggedInUser();
        if (activeUser == null) {
            this.activeUser = new User("test", "test", "test");
            activeUser.getUserData().decreaseMoney(9900);
        }
        System.out.println("user money is : " + activeUser.getUserData().getMoney() + "in the time : " + i);
    }


    @FXML
    void initialize() {
        updateUserMoney();
        setSearchedImage(null);
        menuName.setViewport(new Rectangle2D(0, 745, 960, 133));
    }

    public void buyCard(String cardName) {

    }

    public void setLabelMessage(String message, boolean isSuccessful) {
        buyMessageLabel.setText(message);
        if (isSuccessful) buyMessageLabel.setTextFill(Color.GREEN);
        else buyMessageLabel.setTextFill(Color.RED);
    }


    public void setSearchedImage(KeyEvent actionEvent) {
        if (shopScene == null) shopScene = new ShopScene(this, activeUser);
        ArrayList<CardData> showingCard = new ArrayList<>();
        for (CardData cardData : CardData.getAllCardData()) {
            if (cardData.getName().toLowerCase(Locale.ROOT).startsWith(searchedString.getText().toLowerCase(Locale.ROOT))) {
                showingCard.add(cardData);
            }
        }
        System.out.println("the size is : " + showingCard.size());
        scrollPane.getChildren().clear();
        shopScene.setCards(scrollPane, showingCard);
    }

    public void updateUserMoney() {
        int userMoney = activeUser.getUserData().getMoney();
        if (userMoney < 100000) this.userMoney.setText(String.valueOf(userMoney));
        else this.userMoney.setText(userMoney/1000 + "K");
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
        activeUser.getUserData().decreaseMoney(cardData.getPrice());
        activeUser.getUserData().addCard(cardData.getCardId());
        updateUserMoney();
    }

    public Label getMessageLabel() {
        return buyMessageLabel;
    }

    public void back(MouseEvent mouseEvent) {
        ApplicationManger.goToScene("mainScene.fxml");
    }
}
