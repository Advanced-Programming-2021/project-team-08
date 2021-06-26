package view.menus;

import controller.RegisterController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterScene extends Scene {

    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();
        Matcher matcher;
        matcher = Pattern.compile("menu enter").matcher(userInput);
        if (matcher.matches()) {
            System.out.println("please login first");
            return 1;
        }
        matcher = Pattern.compile("menu exit").matcher(userInput);
        if (matcher.matches()) {
            return 0;
        }
        matcher = Pattern.compile("menu show-current").matcher(userInput);
        if (matcher.matches()) {
            System.out.println("Login Menu");
            return 1;
        }
        matcher = Pattern.compile("user create ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            return RegisterController.registerUser(matcher.group(1));
        }
        matcher = Pattern.compile("user login ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            return RegisterController.loginUser(matcher.group(1), false);
        }

        System.out.println("Invalid command");

        return 1;
    }

}
