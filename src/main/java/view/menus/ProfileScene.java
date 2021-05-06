package view.menus;

import controller.ProfileController;
import controller.User;
import model.enums.ProfileErrors;
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
        if ((matcher = Pattern.compile("profile change (--nickname[^\\n]+)").matcher(userInput)).matches()) {
            profileController.changeNickname(matcher);
        }
        if ((matcher = Pattern.compile("profile change --password ([^\\n]+)").matcher(userInput)).matches()) {
            profileController.changePassword(matcher);
        }
        if (Pattern.compile("menu show-current").matcher(userInput).matches()) {
            System.out.println("Profile Menu");
        }
        return 0;
    }

    public void printMessage(ProfileErrors error) {
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
            case NICKNAME_CHANGED:
                System.out.println("nickname changed successfully!");
                break;
            case PASSWORD_CHANGED:
                System.out.println("password changed successfully!");
                break;
        }
    }
}
