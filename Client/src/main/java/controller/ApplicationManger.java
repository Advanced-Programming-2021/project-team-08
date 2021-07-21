package controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.PerspectiveCamera;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.cards.data.ReadMonsterCardsData;
import model.cards.data.ReadSpellTrapCardsData;
import view.menus.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;

public class ApplicationManger extends Application {
    private static Scene currentScene;
    private static User loggedInUser;
    private static Stage mainStage;

    private static Socket socket;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;
    private static String token;

    public static HashMap<SceneName, URL> fxmlAddresses = new HashMap<SceneName, URL>() {{
        String rootPath = "file:" + System.getProperty("user.dir") + "/src/main/resources/FXML/";
        try {
            put(SceneName.FIRST_SCENE, new URL(rootPath + "firstScene.fxml"));
            put(SceneName.REGISTER_MENU, new URL(rootPath + "registerScene.fxml"));
            put(SceneName.LOGIN_MENU, new URL(rootPath + "loginScene.fxml"));
            put(SceneName.MAIN_MENU, new URL(rootPath + "mainScene.fxml"));
            put(SceneName.SHOP_MENU, new URL(rootPath + "shopScene.fxml"));
            put(SceneName.PROFILE_MENU, new URL(rootPath + "profileScene.fxml"));
            put(SceneName.DUEL_SCENE, new URL(rootPath + "duelScene.fxml"));
            put(SceneName.DECK_MENU, new URL(rootPath + "deckScene.fxml"));
            put(SceneName.SCOREBOARD_MENU, new URL(rootPath + "scoreboard.fxml"));
            put(SceneName.LOBBY_SCENE, new URL(rootPath + "Lobby.fxml"));
            put(SceneName.TV_SCENE, new URL(rootPath + "TV.fxml"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }};

    private static SoundManager soundManager;

    public static Stage getMainStage() {
        return mainStage;
    }

    public static DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public static DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public void run(String[] args) {
        //goToScene(SceneName.REGISTER_MENU, false);
        setupServer();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new ReadMonsterCardsData().setGraphic();
        new ReadSpellTrapCardsData().setGraphic();
        mainStage = primaryStage;
        primaryStage.setTitle("Yu-Gi-Oh");
        primaryStage.setOnCloseRequest(event -> {
            System.out.println(getServerResponse("register", "logout", null));
        });
        soundManager = new SoundManager();
        soundManager.playBackgroundSound();
        goToScene("firstScene.fxml");
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
                currentScene = new DeckMenu(null);
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

    public static void setLoggedInUser(User loggedInUser) {
        ApplicationManger.loggedInUser = loggedInUser;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void logoutCurrentUser() {
        loggedInUser = null;
    }

    private void setupServer() {
        try {
            socket = new Socket("localhost", 12345);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        ApplicationManger.token = token;
    }

    public static String getServerResponse(String controller, String method, HashMap<String, String> data) {
        String message;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("controller", controller);
        jsonObject.addProperty("method", method);
        if (!(controller.equals("register") && !method.equals("logout"))) {
            jsonObject.addProperty("token", token);
        }
        if (data != null) {
            for (String key : data.keySet()) {
                jsonObject.addProperty(key, data.get(key));
            }
        }
        message = jsonObject.toString();
        System.out.println(message);
        try {
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
            String serverMessage = dataInputStream.readUTF();
            if (controller.equals("register") && method.equals("login")) {
                JsonObject jsonObject1 = JsonParser.parseString(serverMessage).getAsJsonObject();
                if (jsonObject1.get("type").toString().equals("SUCCESSFUL")) {
                    token = jsonObject1.get("message").getAsString();
                    System.out.println(token);
                }
            }
            return serverMessage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
