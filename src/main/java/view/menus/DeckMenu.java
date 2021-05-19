package view.menus;

import controller.DeckController;
import controller.ShopController;
import controller.User;
import view.menus.ApplicationManger;
import model.Deck;
import view.menus.Scene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeckMenu extends Scene {

    private User activeUser;
    private DeckController deckController;

    public DeckMenu(){
        this.activeUser=ApplicationManger.getLoggedInUser();
        deckController = new DeckController(this);
    }

    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();
        Matcher matcher;
        Matcher matcher1;

        matcher = Pattern.compile("deck create ([A-Za-z]+)").matcher(userInput);
        if (matcher.find()) {
            deckController.deckCreate(userInput);
        }

        matcher = Pattern.compile("deck delete ([A-Za-z]+)").matcher(userInput);
        if (matcher.find()) {
            deckController.deckDelete(userInput);
        }

        matcher = Pattern.compile("deck set-activate ([A-Za-z]+)").matcher(userInput);
        if (matcher.find()) {
            deckController.deckSetActive(userInput);
        }

        matcher = Pattern.compile("deck add-card --card ([A-Za-z]+) --deck ([A-Za-z]+)").matcher(userInput);
        if (matcher.find()) {
            deckController.addCard(userInput);
        }

        matcher = Pattern.compile("deck rm-card --card ([A-Za-z]+) --deck ([A-Za-z]+)").matcher(userInput);
        if (matcher.find()) {
            deckController.removeCard(userInput);
        }

        matcher = Pattern.compile("deck show --all").matcher(userInput);
        if (matcher.find()) {
            deckController.showAllDecks();
        }

        //show deck
        return 1;
    }
}
