package controller;

import view.menus.*;

public class ApplicationManger {
    private static Scene currentScene;
    private static User loggedInUser;

    public void run() {
        goToScene(SceneName.REGISTER_MENU, false);
    }


    public static void goToScene(SceneName sceneName, boolean isTest) {
        System.out.println("<<" + sceneName.name().replace("_", " ") + ">>");
        if (isTest) return;
        switch (sceneName) {
            case REGISTER_MENU:
                currentScene = new RegisterScene();
                currentScene.start();
                break;
            case MAIN_MENU:
                currentScene = new MainScene();
                currentScene.start();
                break;
            case DECK_MENU:
                currentScene = new DeckMenu();
                currentScene.start();
                break;
            case SHOP_MENU:
                currentScene = new ShopScene();
                currentScene.start();
                break;
            case PROFILE_MENU:
                currentScene = new ProfileScene();
                currentScene.start();
                break;
            case SCOREBOARD_MENU:
                currentScene = new ScoreboardMenu();
                currentScene.start();
                break;
            case IMPORT_EXPORT_MENU:
                currentScene = new ImportScene();
                currentScene.start();
                break;
            case GAMEPLAY_SCENE:
                currentScene = new GamePlayScene();
                currentScene.start();
                break;
        }
    }

    public static void setLoggedInUser(User loggedInUser) {
        ApplicationManger.loggedInUser = loggedInUser;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void logoutCurrentUser() {
        loggedInUser = null;
    }

/*    public static void modifyFile(String filePath, String oldString, String newString) {
        File fileToBeModified = new File(filePath);
        String oldContent = "";
        BufferedReader reader = null;
        FileWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(fileToBeModified));
            String line = reader.readLine();
            while (line != null) {
                oldContent = oldContent + line + System.lineSeparator();
                line = reader.readLine();
            }
            String newContent = oldContent.replaceAll(oldString, newString);
            writer = new FileWriter(fileToBeModified);
            writer.write(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}
