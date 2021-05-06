package controller;

import model.Command;
import model.enums.CommandFieldType;
import model.exceptions.ParseCommandException;
import view.menus.ProfileScene;

import java.util.HashMap;
import java.util.regex.Matcher;

import model.enums.ProfileErrors;

public class ProfileController {

    private User activeUser;
    private ProfileScene profileScene;

    public ProfileController(User user, ProfileScene profileScene) {
        this.activeUser = user;
        this.profileScene = profileScene;
    }

    public void changeNickname(Matcher command) {
        HashMap<String, CommandFieldType> fieldsOfRegisterUser = new HashMap<>();
        fieldsOfRegisterUser.put("nickname", CommandFieldType.STRING);
        try {
            Command registerCommand = Command.parseCommand(command.group(), fieldsOfRegisterUser);
            if (registerCommand.getField("nickname").equals(activeUser.getUserData().getNickname())) {
                profileScene.printMessage(ProfileErrors.REPEATED_NICKNAME);
            }
            else {
                activeUser.getUserData().setUsername(registerCommand.getField("nickname"));
                profileScene.printMessage(ProfileErrors.NICKNAME_CHANGED);
            }
        }catch (ParseCommandException e) {
            System.out.println("Invalid command");
        }
    }

    public void changePassword(Matcher command) {
        HashMap<String, CommandFieldType> fieldsOfRegisterUser = new HashMap<>();
        fieldsOfRegisterUser.put("current", CommandFieldType.STRING);
        fieldsOfRegisterUser.put("new", CommandFieldType.STRING);
        try {
            Command registerCommand = Command.parseCommand(command.group(), fieldsOfRegisterUser);
            if (!registerCommand.getField("current").equals(activeUser.getUserData().getPassword())) {
                profileScene.printMessage(ProfileErrors.WRONG_PASSWORD);
            }
            else if (registerCommand.getField("new").equals(activeUser.getUserData().getPassword())) {
                profileScene.printMessage(ProfileErrors.REPEATED_PASSWORD);
            }
            else {
                activeUser.getUserData().setPassword(registerCommand.getField("new"));
                profileScene.printMessage(ProfileErrors.PASSWORD_CHANGED);
            }
        }catch (ParseCommandException e) {
            System.out.println("Invalid command");
        }
    }
}
