package controller;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import model.cards.data.CardData;
import view.menus.ShopScene;

public class ShopController {

    public Label buyMessageLabel;
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


}
