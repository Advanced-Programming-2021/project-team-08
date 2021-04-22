package Controllers;

import Controllers.User;
import Models.Command;
import Models.Enums.CommandFieldType;
import Views.Menus.ApplicationManger;
import Views.Menus.SceneName;

import java.util.HashMap;

public class RegisterController {
    public static int registerUser(String userInput){
        HashMap<String , CommandFieldType> fieldsOfRegisterUser=new HashMap<>();
        fieldsOfRegisterUser.put("username",CommandFieldType.STRING);
        fieldsOfRegisterUser.put("password",CommandFieldType.STRING);
        fieldsOfRegisterUser.put("nickname",CommandFieldType.STRING);
//        Command registerCommand = Command.parseCommand(userInput,fieldsOfRegisterUser);
//        if (User.doesUsernameExists(registerCommand.getField("username")))
//            System.out.printf("user with username" + registerCommand.getField("username") + "already exists");
//        else if (User.doesUsernameExists(registerCommand.getField("nickname")))
//            System.out.printf("user with nickname <nickname> already exists");
//        else {
//            new User(registerCommand.getField("username"),registerCommand.getField("nickname"),registerCommand.getField("password"));
//            System.out.printf("user created successfully!");
//        }
        return 1;
    }

    public static int loginUser(String userInput){
        HashMap<String , CommandFieldType> fieldsOfLoginUser=new HashMap<>();
        fieldsOfLoginUser.put("username",CommandFieldType.STRING);
        fieldsOfLoginUser.put("nickname",CommandFieldType.STRING);
//        Command loginCommand = Command.parseCommand(userInput,fieldsOfLoginUser);
//        if (!User.doesUsernameExists(loginCommand.getField("username")))
//            System.out.printf("Username and password didn’t match!");
//        else {
//            if (User.loginUser(loginCommand.getField("username"),loginCommand.getField("password"))){
//                System.out.printf("user logged in successfully!");
//                ApplicationManger.goToScene(SceneName.MAIN_MENU);
//                return 0;
//            }
//            else {
//                System.out.printf("Username and password didn’t match!");
//            }

//        }
        return 1;
    }
}
