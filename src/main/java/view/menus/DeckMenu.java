package view.menus;

import controller.DeckController;
import controller.User;
import controller.ApplicationManger;
import model.cards.data.CardData;

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

        if ((Pattern.compile("^menu enter ([A-Za-z]+)$").matcher(userInput)).find()) {
            System.out.println("menu navigation is not possible");
            return 1;
        }
        if(userInput.equals("menu show-current")){
            System.out.println("Deck Menu");
            return 1;
        }
        if ((matcher = Pattern.compile("^card show ([^\n]+)$").matcher(userInput)).find()) {
            showCard(CardData.getCardByName(matcher.group(1)));
            return 1;
        }
        matcher = Pattern.compile("deck create ([^\\n]+)").matcher(userInput);
        if (matcher.find()) {
            deckController.deckCreate(matcher.group(1));
            return 1;
        }

        matcher = Pattern.compile("deck delete ([^\\n]+)").matcher(userInput);
        if (matcher.find()) {
            deckController.deckDelete(matcher.group(1));
            return 1;
        }

        matcher = Pattern.compile("deck set-activate ([^\\n]+)").matcher(userInput);
        if (matcher.find()) {
            deckController.deckSetActive(matcher.group(1));
            return 1;
        }

        matcher = Pattern.compile("deck add-card --card ([^\\n]+) --deck ([^\\n]+)").matcher(userInput);
        if (matcher.find()) {
            deckController.addCard(userInput);
            return 1;
        }

        matcher = Pattern.compile("deck rm-card --card ([^\\n]+) --deck ([^\\n]+)").matcher(userInput);
        if (matcher.find()) {
            deckController.removeCard(userInput);
            return 1;
        }

        matcher = Pattern.compile("deck show --all").matcher(userInput);
        if (matcher.find()) {
            deckController.showAllDecks();
            return 1;
        }


        matcher = Pattern.compile("deck show --deck-name ([^\\n]+) --side").matcher(userInput);
        if (matcher.find()){
            deckController.showDeck(matcher.group(1),userInput);
            return 1;
        }

        matcher=Pattern.compile("deck show --cards").matcher(userInput);
        if (matcher.find()){
            deckController.deckShowCards();
            return 1;
        }

        if (Pattern.compile("menu exit").matcher(userInput).matches()) {
            ApplicationManger.goToScene(SceneName.MAIN_MENU);
            return 0;
        }

        System.out.println("Invalid command!");
        return 1;
    }
}
