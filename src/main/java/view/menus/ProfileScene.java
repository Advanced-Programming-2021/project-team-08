package view.menus;

import controller.ProfileController;
import controller.User;
import model.enums.ProfileMessages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileScene extends Scene {

    //private User activeUser;
    private ProfileController profileController;

    public ProfileScene() {
<<<<<<< HEAD
=======
        this.activeUser = ApplicationManger.getLoggedInUser();
>>>>>>> e0fa412a280b75541e41038723e2693c8e9f28ae
        profileController = new ProfileController(this);
    }

    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();
        Matcher matcher;
        if ((matcher = Pattern.compile("^profile change (--nickname[^\\n]+)$").matcher(userInput)).find()) {
            profileController.changeNickname(matcher);
        } else if ((matcher = Pattern.compile("^profile change --password ([^\\n]+)$").matcher(userInput)).find()) {
            profileController.changePassword(matcher);
        } else if (Pattern.compile("^menu show-current$").matcher(userInput).find()) {
            System.out.println("Profile Menu");
        } else if (Pattern.compile("^menu exit$").matcher(userInput).find()) {
            return 0;
        } else System.out.println("invalid commands");
        return 1;
    }

    public void errorMessage(ProfileMessages error) {
        switch (error) {
            case REPEATED_NICKNAME:
                System.out.println("this nickname is already used");
                break;
            case WRONG_PASSWORD:
                System.out.println("current password is invalid");
                break;
            case REPEATED_PASSWORD:
                System.out.println("please enter a new password");
                break;
        }
        getUserCommand();
    }

    public void successMessage(ProfileMessages message) {
        switch (message) {
            case NICKNAME_CHANGED:
                System.out.println("nickname changed successfully!");
                break;
            case PASSWORD_CHANGED:
                System.out.println("password changed successfully!");
                break;
        }
    }
}
