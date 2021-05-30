package controller;

import model.cards.data.CardData;
import view.menus.ApplicationManger;
import view.menus.ShopScene;

public class ShopController {

    private User activeUser;
    private ShopScene shopScene;

    public ShopController(ShopScene shopScene) {
        this.activeUser = ApplicationManger.getLoggedInUser();
        this.shopScene = shopScene;
    }

    public void buyCard(String cardName) {
        CardData cardData = CardData.getCardByName(cardName);
        if (cardData == null) {
            System.out.println("there is no card with this name");
        }
        else if (activeUser.getUserData().getMoney() < cardData.getPrice()) {
            System.out.println("you have not enough money");
        }
        else {
            activeUser.getUserData().addCard(cardData.getId());
        }
    }
}
