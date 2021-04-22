package Views.Menus;

import Controllers.MainMenuController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainScene extends Scene{

    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();
        Matcher matcher;
        matcher=Pattern.compile("menu enter ([A-Za-z])").matcher(userInput);
        if (matcher.matches()){
            return MainMenuController.enterMenu(matcher.group(1));
        }
        matcher=Pattern.compile("menu exit").matcher(userInput);
        if (matcher.matches()){
            ApplicationManger.goToScene(SceneName.REGISTER_MENU);
            return 0;
        }
        matcher=Pattern.compile("menu show-current").matcher(userInput);
        if (matcher.matches()){
            System.out.printf("Main Menu");
        }
        matcher=Pattern.compile("user logout").matcher(userInput);
        if (matcher.matches()){
            return logout();
        }
        return 1;
    }

    public int logout(){
        System.out.printf("user logged out successfully!");
        ApplicationManger.logoutCurrentUser();
        ApplicationManger.goToScene(SceneName.REGISTER_MENU);
        return 0;
    }

}
