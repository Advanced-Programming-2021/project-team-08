package controller;

import model.Command;
import model.enums.CommandFieldType;
import model.exceptions.ParseCommandException;
import view.menus.ProfileScene;

import java.util.HashMap;
import java.util.regex.Matcher;

import model.enums.ProfileMessages;

public class ProfileController {

    private User activeUser;
    private ProfileScene profileScene;

    public ProfileController(ProfileScene profileScene) {
        this.activeUser = ApplicationManger.getLoggedInUser();
        this.profileScene = profileScene;
    }

    public void changeNickname(Matcher command) {
        HashMap<String, CommandFieldType> fieldsOfRegisterUser = new HashMap<>();
        fieldsOfRegisterUser.put("nickname", CommandFieldType.STRING);
        try {
            Command registerCommand = Command.parseCommand(command.group(), fieldsOfRegisterUser);
            if (User.doesNicknameExists(registerCommand.getField("nickname"))) {
                profileScene.errorMessage(ProfileMessages.REPEATED_NICKNAME);
            } else {
                activeUser.getUserData().setNickname(registerCommand.getField("nickname"));
                profileScene.successMessage(ProfileMessages.NICKNAME_CHANGED);
            }
        } catch (ParseCommandException e) {
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
                profileScene.errorMessage(ProfileMessages.WRONG_PASSWORD);
            } else if (registerCommand.getField("new").equals(activeUser.getUserData().getPassword())) {
                profileScene.errorMessage(ProfileMessages.REPEATED_PASSWORD);
            } else {
                activeUser.getUserData().setPassword(registerCommand.getField("new"));
                profileScene.successMessage(ProfileMessages.PASSWORD_CHANGED);
            }
        } catch (ParseCommandException e) {
            System.out.println("Invalid command");
        }
    }
}
