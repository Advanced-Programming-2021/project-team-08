package controller;

import view.menus.ApplicationManger;
import view.menus.SceneName;

public class MainMenuController {
    public static void enterMenu(String menuName) {
        switch (menuName) {
            case "scoreboard":
                ApplicationManger.goToScene(SceneName.SCOREBOARD_MENU);
                break;
            case "shop":
                ApplicationManger.goToScene(SceneName.SHOP_MENU);
                break;
            case "profile":
                ApplicationManger.goToScene(SceneName.PROFILE_MENU);
                break;
            case "deck":
                ApplicationManger.goToScene(SceneName.DECK_MENU);
                break;
        }
    }
}
