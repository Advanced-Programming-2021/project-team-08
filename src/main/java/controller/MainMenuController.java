package controller;

import view.menus.ApplicationManger;
import view.menus.SceneName;

public class MainMenuController {
    public static int enterMenu(String menuName){
        switch (menuName) {
            case "Scoreboard":
                ApplicationManger.goToScene(SceneName.SCOREBOARD_MENU);
                break;
            case "Shop":
                ApplicationManger.goToScene(SceneName.SHOP_MENU);
                break;
            case "Profile":
                ApplicationManger.goToScene(SceneName.PROFILE_MENU);
                break;
            case "Deck":
                ApplicationManger.goToScene(SceneName.DECK_MENU);
                break;
        }
        return 0;
    }
}
