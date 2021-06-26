package controller;

import com.google.gson.Gson;
import model.Command;
import model.enums.CommandFieldType;
import model.exceptions.ParseCommandException;
import view.menus.SceneName;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class RegisterController {


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
}
