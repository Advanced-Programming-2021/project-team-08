package controller;

import com.google.gson.Gson;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import model.Command;
import model.cards.data.CardData;
import model.enums.CommandFieldType;
import model.exceptions.ParseCommandException;
import view.menus.SceneName;
import view.menus.ShopScene;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class RegisterController {
    public TextField usernameOfSignup;
    public TextField passwordOfSignup;
    public TextField nicknameOfSignup;
    public TextField usernameOfLogin;
    public TextField passwordOfLogin;
    public Label errorOfSignup;
    public Label successOfSignup;
    public Label errorOfLogin;
    public Label successOfLogin;
    public AnchorPane scrollPane;
    public AnchorPane registerAnchorPane;
    public AnchorPane avatarAnchorPane;


    public static int registerUser(String userInput) {
        HashMap<String, CommandFieldType> fieldsOfRegisterUser = new HashMap<>();
        fieldsOfRegisterUser.put("username", CommandFieldType.STRING);
        fieldsOfRegisterUser.put("password", CommandFieldType.STRING);
        fieldsOfRegisterUser.put("nickname", CommandFieldType.STRING);
        try {
            Command registerCommand = Command.parseCommand(userInput, fieldsOfRegisterUser);
            if (User.doesUsernameExists(registerCommand.getField("username")))
                System.out.println("user with username " + registerCommand.getField("username") + " already exists");
            else if (User.doesNicknameExists(registerCommand.getField("nickname")))
                System.out.println("user with nickname " + registerCommand.getField("nickname") + " already exists");
            else {
                User user = new User(registerCommand.getField("username"), registerCommand.getField("nickname"), registerCommand.getField("password"));
                System.out.println("user created successfully!");
                FileWriter userFile = new FileWriter("users/" + registerCommand.getField("username") + ".json");
                userFile.write(new Gson().toJson(user));
                userFile.close();
            }
        } catch (ParseCommandException | IOException e) {
            System.out.println("Invalid command");
        }
        return 1;
    }

    public static int loginUser(String userInput, boolean isTest) {
        HashMap<String, CommandFieldType> fieldsOfLoginUser = new HashMap<>();
        fieldsOfLoginUser.put("username", CommandFieldType.STRING);
        fieldsOfLoginUser.put("password", CommandFieldType.STRING);
        try {
            Command loginCommand = Command.parseCommand(userInput, fieldsOfLoginUser);
            if (!User.doesUsernameExists(loginCommand.getField("username")))
                System.out.println("username and password didn't match");
            else {
                if (User.loginUser(loginCommand.getField("username"), loginCommand.getField("password"))) {
                    System.out.println("user logged in successfully!");
                    if (!isTest) ApplicationManger.goToScene(SceneName.MAIN_MENU, false);
                    return 0;
                } else {
                    System.out.println("username and password didn't match");
                }

            }
        } catch (ParseCommandException e) {
            System.out.println("Invalid command");
        }
        return 1;
    }

    public void backToFirstScene(ActionEvent actionEvent) {
        ApplicationManger.goToScene1(SceneName.FIRST_SCENE, false);
    }

    public void nextOfSignup(ActionEvent actionEvent) {
        try {
            if (User.doesUsernameExists(usernameOfSignup.getText())) {
                errorOfSignup.setText("user with username " + usernameOfSignup.getText() + " already exists");
                errorOfSignup.setTextFill(Color.RED);
            } else if (User.doesNicknameExists(nicknameOfSignup.getText())) {
                errorOfSignup.setText("user with nickname " + nicknameOfSignup.getText() + " already exists");
                errorOfSignup.setTextFill(Color.RED);
            } else {
                User user = new User(usernameOfSignup.getText(), nicknameOfSignup.getText(), passwordOfSignup.getText());
                successOfSignup.setText("user created successfully!");
                registerAnchorPane.setLayoutX(1600);
                avatarAnchorPane.setLayoutX(0);
                setCards();
                successOfSignup.setTextFill(Color.GREEN);
                FileWriter userFile = new FileWriter("users/" + usernameOfSignup.getText() + ".json");
                userFile.write(new Gson().toJson(user));
                userFile.close();
            }
        } catch (IOException e) {
            errorOfSignup.setText("Invalid command");
            errorOfSignup.setTextFill(Color.RED);
        }
    }

    public void nextOfLogin(ActionEvent actionEvent) {
        try {
            if (!User.doesUsernameExists(usernameOfLogin.getText())) {
                errorOfLogin.setText("user with username " + usernameOfLogin.getText() + " doesn't exists");
                errorOfLogin.setTextFill(Color.RED);
            } else {
                if (User.loginUser(usernameOfLogin.getText(), passwordOfLogin.getText())) {
                    ApplicationManger.goToScene1(SceneName.MAIN_MENU, false);
//                    if (!isTest) ApplicationManger.goToScene(SceneName.MAIN_MENU, false);
                } else {
                    errorOfLogin.setText("username and password didn't match");
                    errorOfLogin.setTextFill(Color.RED);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorOfLogin.setText("Invalid command");
            errorOfLogin.setTextFill(Color.RED);
        }
    }


    public void setCards() throws MalformedURLException {
        scrollPane.setPrefHeight((double) (140 / 5) * 300 + 180);

        File[] files=new File("src/main/resources/asset/busts").listFiles();
        assert  files != null;
        for (int i = 0; i < 130; i++) {
            addCardImage(files[i].getName() , i);
        }
    }

    private void addCardImage(String imageName,int index) {
        String path="src/main/resources/asset/busts/" + imageName;
        File file= new File(path);
        Image image = new Image(file.toURI().toString());
        ImageView cardImage = new ImageView(image);
        scrollPane.getChildren().add(index, cardImage);
        cardImage.setFitHeight(280);
        cardImage.setFitWidth(200);
        int x = (index % 5) * (220) + 20;
        int y = (index / 5) * (300) + 20;
        cardImage.setX(x);
        cardImage.setY(y);
        cardImage.setOnMouseEntered(event -> {
            cardImage.setFitHeight(cardImage.getFitHeight() * 1.4);
            cardImage.setFitWidth(cardImage.getFitWidth() * 1.4);
            cardImage.toFront();
        });
        cardImage.setOnMouseExited(event -> {
            cardImage.setFitHeight(cardImage.getFitHeight() / 1.4);
            cardImage.setFitWidth(cardImage.getFitWidth() / 1.4);
            cardImage.toBack();
        });
        cardImage.setOnMouseClicked(event -> {
            ApplicationManger.getLoggedInUser().getUserData().setProfileImageUrl(file.toURI().toString());
        });
        cardImage.hoverProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                cardImage.setCursor(Cursor.HAND);
            }
        });
    }
}