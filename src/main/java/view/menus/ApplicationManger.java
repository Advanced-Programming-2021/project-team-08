package view.menus;

import controller.DeckController;
import controller.User;

public class ApplicationManger {
    private static Scene currentScene;
    private static User loggedInUser;
        public void run() {
            goToScene(SceneName.REGISTER_MENU);
        }


    public static void goToScene(SceneName sceneName){
        switch (sceneName){
            case REGISTER_MENU:
                currentScene = new RegisterScene();
                currentScene.start();
                break;
            // TODO: ۱۸/۰۴/۲۰۲۱ other cases
            case MAIN_MENU:
                currentScene=new MainScene();
                currentScene.start();
                break;
            //TODO: 19/04/2021 other cases
            case DECK_MENU:
                currentScene=new DeckController();
                currentScene.start();
                break;
            //TODO: 04/05/2021 other cases
        }

    }
    public static void setLoggedInUser(User loggedInUser) {
        ApplicationManger.loggedInUser = loggedInUser;
    }
    public static User getLoggedInUser() {
        return loggedInUser;
    }
    public static void logoutCurrentUser() {
            loggedInUser=null;
    }
}
