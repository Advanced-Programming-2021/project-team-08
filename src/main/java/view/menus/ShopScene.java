package view.menus;

import controller.ProfileController;
import controller.ShopController;
import controller.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopScene extends Scene{

    private User activeUser;
    private ShopController shopController;

    public ShopScene(User user) {
        this.activeUser = user;
        shopController = new ShopController(user, this);
    }

    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();
        Matcher matcher;
        if ((matcher = Pattern.compile("^shop buy (\\w+)$").matcher(userInput)).find()) {
            shopController.buyCard(matcher.group(1));
        }
        if (Pattern.compile("^menu show-current$").matcher(userInput).find()) {
            System.out.println("Shop Menu");
        }
        return 0;
    }
}
