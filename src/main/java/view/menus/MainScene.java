package view.menus;

import controller.MainMenuController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainScene extends Scene {

    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();
        Matcher matcher;
        if ((matcher = Pattern.compile("^menu enter ([A-Za-z]+)$").matcher(userInput)).find()) {
            MainMenuController.enterMenu(matcher.group(1));
        } else if (Pattern.compile("^menu exit$").matcher(userInput).find()) {
            ApplicationManger.goToScene(SceneName.REGISTER_MENU);
            return 0;
        } else if (Pattern.compile("^menu show-current$").matcher(userInput).find()) {
            System.out.println("Main Menu");
        } else if (Pattern.compile("^user logout$").matcher(userInput).find()) {
            return logout();
        } else {
            System.out.println("invalid command");
        }
        return 1;
    }

    public int logout() {
        System.out.println("user logged out successfully!");
        ApplicationManger.logoutCurrentUser();
        ApplicationManger.goToScene(SceneName.REGISTER_MENU);
        return 0;
    }

}
