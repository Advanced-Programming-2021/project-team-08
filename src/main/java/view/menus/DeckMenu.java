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

        matcher = Pattern.compile("deck create ([^\\n]+)").matcher(userInput);
        if (matcher.find()) {
            deckController.deckCreate(matcher.group(1));
            return 1;
        }

        matcher = Pattern.compile("deck delete ([^\\n]+)").matcher(userInput);
        if (matcher.find()) {
            deckController.deckDelete(matcher.group(1));
        }

        matcher = Pattern.compile("deck set-activate ([^\\n]+)").matcher(userInput);
        if (matcher.find()) {
            deckController.deckSetActive(matcher.group(1));
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


        matcher = Pattern.compile("deck show --deck-name ([A-Za-z]+) --side").matcher(userInput);
        if (matcher.find()){
            deckController.showDeck(matcher.group(1),userInput);
        }

        matcher=Pattern.compile("deck show --cards").matcher(userInput);
        if (matcher.find()){
            deckController.deckShowCards();
        }

        if (Pattern.compile("menu exit").matcher(userInput).matches()) {
            ApplicationManger.goToScene(SceneName.MAIN_MENU);
            return 0;
        }

        System.out.println("Invalid command!");
        return 1;
    }
}
