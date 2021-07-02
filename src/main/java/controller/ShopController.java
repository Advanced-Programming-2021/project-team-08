package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import model.cards.Card;
import model.cards.data.CardData;
import view.menus.ShopScene;

import java.util.ArrayList;
import java.util.Locale;

public class ShopController {

    public Label buyMessageLabel;
    public TextField searchedString;
    public AnchorPane scrollPane;
    private User activeUser;
    private ShopScene shopScene;

    public ShopController() {
        this.activeUser = ApplicationManger.getLoggedInUser();
    }

    public void buyCard(String cardName) {
        CardData cardData = CardData.getCardByName(cardName);
        if (cardData == null) {
            shopScene.printMessage("there is no card with this name");
        } else if (activeUser.getUserData().getMoney() < cardData.getPrice()) {
            shopScene.printMessage("you have not enough money");
            shopScene.printMessage("your money is " + activeUser.getUserData().getMoney() + " card Price is " + cardData.getPrice());
        } else {
            activeUser.getUserData().decreaseMoney(cardData.getPrice());
            activeUser.getUserData().addCard(cardData.getCardId());
            shopScene.printMessage("you bought " + cardName + " successfully.");
        }
    }

    public void setLabelMessage(String message, boolean isSuccessful) {
        buyMessageLabel.setText(message);
        if (isSuccessful) buyMessageLabel.setTextFill(Color.GREEN);
        else buyMessageLabel.setTextFill(Color.RED);
    }


    public void setSearchedImage(ActionEvent actionEvent) {
        shopScene = new ShopScene();
        ArrayList<CardData> showingCard = new ArrayList<>();
        for (CardData cardData : CardData.getAllCardData()) {
            if (cardData.getName().toLowerCase(Locale.ROOT).startsWith(searchedString.getText())) {
                showingCard.add(cardData);
            }
        }
        System.out.println("the size is : " + showingCard.size());
        scrollPane.getChildren().clear();
        shopScene.setCards(scrollPane, showingCard);
    }
}
