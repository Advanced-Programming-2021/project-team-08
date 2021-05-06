package controller;

import view.menus.ShopScene;

public class ShopController {

    private User activeUser;
    private ShopScene shopScene;

    public ShopController(User user, ShopScene shopScene) {
        this.activeUser = user;
        this.shopScene = shopScene;
    }

    public void buyCard(String cardName) {

    }
}
