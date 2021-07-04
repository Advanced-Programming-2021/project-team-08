package controller;

import model.Command;
import model.enums.CommandFieldType;
import model.enums.ProfileMessages;
import model.exceptions.ParseCommandException;
import view.menus.ProfileScene;

import java.util.HashMap;

public class ProfileController {

    private User activeUser;
    private ProfileScene profileScene;

    public ProfileController(ProfileScene profileScene) {
        this.activeUser = ApplicationManger.getLoggedInUser();
        if (activeUser == null) activeUser = new User("test", "test", "test");
        this.profileScene = profileScene;
    }

    public void changeNickname(String command) {
        HashMap<String, CommandFieldType> fieldsOfRegisterUser = new HashMap<>();
        fieldsOfRegisterUser.put("nickname", CommandFieldType.STRING);
        try {
            Command registerCommand = Command.parseCommand(command, fieldsOfRegisterUser);
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

    public void changeNickname1(String newNickname) {
        if (User.doesNicknameExists(newNickname)) {
            profileScene.setNicknameChangeMessage("this nickname is already used.", true);
        }else {
            activeUser.getUserData().setNickname(newNickname);
            profileScene.setNicknameChangeMessage("nickname changed successfully", false);
        }
    }

    public void changePassword1(String oldPass, String newPass) {
        if (!oldPass.equals(activeUser.getUserData().getPassword())) {
            profileScene.setPasswordChangeMessage("old message is wrong", true);
        }else if (newPass.equals(activeUser.getUserData().getPassword())) {
            profileScene.setPasswordChangeMessage("please enter a new password", true);
        }else {
            activeUser.getUserData().setPassword(newPass);
            profileScene.setPasswordChangeMessage("password changed successfully", false);
        }
    }

    public void changePassword(String command) {
        HashMap<String, CommandFieldType> fieldsOfRegisterUser = new HashMap<>();
        fieldsOfRegisterUser.put("current", CommandFieldType.STRING);
        fieldsOfRegisterUser.put("new", CommandFieldType.STRING);
        try {
            Command registerCommand = Command.parseCommand(command, fieldsOfRegisterUser);
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
