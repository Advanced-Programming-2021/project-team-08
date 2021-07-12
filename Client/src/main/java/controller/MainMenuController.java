package controller;

import view.menus.SceneName;

public class MainMenuController {
    public static int enterMenu(String menuName, boolean isTest) {
        switch (menuName) {
            case "scoreboard":
                ApplicationManger.goToScene(SceneName.SCOREBOARD_MENU, isTest);
                return 0;
            case "shop":
                ApplicationManger.goToScene(SceneName.SHOP_MENU, isTest);
                return 0;
            case "profile":
                ApplicationManger.goToScene(SceneName.PROFILE_MENU, isTest);
                return 0;
            case "deck":
                ApplicationManger.goToScene(SceneName.DECK_MENU, isTest);
                return 0;
            case "duel":
                ApplicationManger.goToScene(SceneName.GAMEPLAY_SCENE, isTest);
                return 0;
            case "import":
                ApplicationManger.goToScene(SceneName.IMPORT_EXPORT_MENU, isTest);
                return 0;
            default:
                System.out.println("invalid menu name");
                return 1;
        }
    }
}
