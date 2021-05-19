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

    }

}
