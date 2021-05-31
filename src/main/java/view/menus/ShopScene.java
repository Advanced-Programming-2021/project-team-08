package view.menus;

import controller.ApplicationManger;
import controller.ShopController;
import controller.User;
import model.cards.data.CardData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopScene extends Scene {

    private User activeUser;
    private ShopController shopController;

    public ShopScene() {
        this.activeUser = ApplicationManger.getLoggedInUser();
        shopController = new ShopController(this);
    }

    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();
        Matcher matcher;
        if ((matcher = Pattern.compile("^shop buy ([\\w ]+)$").matcher(userInput)).find()) {
            shopController.buyCard(matcher.group(1));
        }
        else if (Pattern.compile("^menu show-current$").matcher(userInput).find()) {
            System.out.println("Shop Menu");
        }
        else if (Pattern.compile("shop show --all").matcher(userInput).find()) {
            showAllCard();
        }
        else if (Pattern.compile("menu exit").matcher(userInput).matches()) {
            ApplicationManger.goToScene(SceneName.MAIN_MENU);
            return 0;
        }
        else {
            System.out.println("invalid command");

        }
        return 1;
    }

    private void showAllCard() {
        System.out.println("show all card entered " + CardData.getAllCardData().size());
        for (CardData cardData : CardData.getAllCardData()) {
            System.out.println(cardData.getName() + " : "  + cardData.getCardDescription());
        }
    }

    public void printMessage(String message) {
        System.out.println(message);
    }
}
