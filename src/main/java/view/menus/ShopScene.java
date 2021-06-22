package view.menus;

import controller.ApplicationManger;
import controller.MainMenuController;
import controller.ShopController;
import controller.User;
import model.cards.data.CardData;

import java.util.ArrayList;
import java.util.Comparator;
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
        if ((Pattern.compile("^menu enter ([A-Za-z]+)$").matcher(userInput)).find()) {
            System.out.println("menu navigation is not possible");
            return 1;
        }
        if ((matcher = Pattern.compile("^card show ([^\n]+)$").matcher(userInput)).find()) {
            showCard(CardData.getCardByName(matcher.group(1)));
            return 1;
        }
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
        ArrayList<CardData> allCards = CardData.getAllCardData();
        allCards.sort(new sortCardsAlphabetically());
        for (CardData cardData : allCards) {
            System.out.println(cardData.getName() + " : "  + cardData.getPrice());
        }
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    class sortCardsAlphabetically implements Comparator<CardData> {
        public int compare(CardData a, CardData b)
        {
            return a.getCardName().compareTo(b.getCardName());
        }
    }
}
