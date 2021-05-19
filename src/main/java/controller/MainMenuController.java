package controller;

import view.menus.ApplicationManger;
import view.menus.SceneName;

public class MainMenuController {
    public static int enterMenu(String menuName) {
        switch (menuName) {
            case "scoreboard":
                ApplicationManger.goToScene(SceneName.SCOREBOARD_MENU);
                return 0;
            case "shop":
                ApplicationManger.goToScene(SceneName.SHOP_MENU);
                return 0;
            case "profile":
                ApplicationManger.goToScene(SceneName.PROFILE_MENU);
                return 0;
            case "deck":
                ApplicationManger.goToScene(SceneName.DECK_MENU);
                return 0;
            case "duel":
                System.out.println("Duel Menu");
                ApplicationManger.goToScene(SceneName.GAMEPLAY_SCENE);
                return 0;
            default:
                System.out.println("invalid menu name");
                return 1;
        }
    }
}
