package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.PerspectiveCamera;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.cards.data.ReadMonsterCardsData;
import model.cards.data.ReadSpellTrapCardsData;
import view.menus.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ApplicationManger extends Application {
    public static HashMap<SceneName, URL> fxmlAddresses = new HashMap<SceneName, URL>() {{
        String rootPath = "file:" + System.getProperty("user.dir") + "/src/main/resources/FXML/";
        try {
            put(SceneName.FIRST_SCENE, new URL(rootPath + "firstScene.fxml"));
            put(SceneName.REGISTER_MENU, new URL(rootPath + "registerScene.fxml"));
            put(SceneName.LOGIN_MENU, new URL(rootPath + "loginScene.fxml"));
            put(SceneName.MAIN_MENU, new URL(rootPath + "mainScene.fxml"));
            put(SceneName.SHOP_MENU, new URL(rootPath + "shopScene.fxml"));
            put(SceneName.PROFILE_MENU, new URL(rootPath + "profileScene.fxml"));
            put(SceneName.DUEL_SCENE, new URL(rootPath + "singleDuelScene.fxml"));
            put(SceneName.DECK_MENU, new URL(rootPath + "deckScene.fxml"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }};
    private static Scene currentScene;
    private static User loggedInUser;
    private static Stage mainStage;

    public static Stage getMainStage() {
        return mainStage;
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

    public static void goToScene1(SceneName sceneName, boolean isTest) {
        if (isTest) return;
        try {
            FXMLLoader loader = new FXMLLoader(fxmlAddresses.get(sceneName));
            AnchorPane root = loader.load();
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            //scene.setCamera(new PerspectiveCamera());
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void goToScene(String url) {
        new SoundManager().playSound("musics/tap.wav");
        String rootPath = "file:" + System.getProperty("user.dir") + "/src/main/resources/FXML/";
        try {
            FXMLLoader loader = new FXMLLoader(new URL(rootPath + url));
            AnchorPane root = loader.load();
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            scene.setCamera(new PerspectiveCamera());
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        ApplicationManger.loggedInUser = loggedInUser;
    }

    public static void logoutCurrentUser() {
        loggedInUser = null;
    }

    public void run(String[] args) {
        //goToScene(SceneName.REGISTER_MENU, false);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new ReadMonsterCardsData().setGraphic();
        new ReadSpellTrapCardsData().setGraphic();
        mainStage = primaryStage;
        primaryStage.setTitle("Yu-Gi-Oh");
        new SoundManager().playBackgroundSound();
        goToScene("firstScene.fxml");
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
