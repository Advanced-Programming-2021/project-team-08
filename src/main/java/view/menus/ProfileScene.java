package view.menus;

import controller.ProfileController;
import controller.User;
import model.enums.ProfileMessages;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileScene extends Scene{

    private User activeUser;
    private ProfileController profileController;

    public ProfileScene(User user) {
        this.activeUser = user;
        profileController = new ProfileController(user, this);
    }

    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();
        Matcher matcher;
        if ((matcher = Pattern.compile("^profile change (--nickname[^\\n]+)$").matcher(userInput)).find()) {
            profileController.changeNickname(matcher);
        }
        if ((matcher = Pattern.compile("^profile change --password ([^\\n]+)$").matcher(userInput)).find()) {
            profileController.changePassword(matcher);
        }
        if (Pattern.compile("^menu show-current$").matcher(userInput).find()) {
            System.out.println("Profile Menu");
        }
        return 0;
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
