package view.menus;

import controller.DeckController;
import controller.User;

import java.io.*;

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
    public static void modifyFile(String filePath, String oldString, String newString) {
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
    }
}
