package view.menus;

import controller.ApplicationManger;
import controller.DeckController;
import controller.User;
import model.cards.data.CardData;
import model.exceptions.ParseCommandException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeckMenu extends Scene {

    private User activeUser;
    private DeckController deckController;

    public DeckMenu() {
        this.activeUser = ApplicationManger.getLoggedInUser();
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
        if (userInput.equals("menu show-current")) {
            System.out.println("Deck Menu");
            return 1;
        }
        if ((matcher = Pattern.compile("^card show ([^\n]+)$").matcher(userInput)).find()) {
            showCard(CardData.getCardByName(matcher.group(1)));
            return 1;
        }
        matcher = Pattern.compile("deck create ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            deckController.deckCreate(matcher.group(1));
            return 1;
        }

        matcher = Pattern.compile("deck delete ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            deckController.deckDelete(matcher.group(1));
            return 1;
        }

        matcher = Pattern.compile("deck set-activate ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            deckController.deckSetActive(matcher.group(1));
            return 1;
        }

        matcher = Pattern.compile("deck add-card ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            try {
                deckController.addCard(matcher.group(1));
            } catch (ParseCommandException e) {
                System.out.println("invalid command");
            }
            return 1;
        }

        matcher = Pattern.compile("deck rm-card ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            deckController.removeCard(matcher.group(1));
            return 1;
        }

        matcher = Pattern.compile("deck show --all").matcher(userInput);
        if (matcher.matches()) {
            deckController.showAllDecks();
            return 1;
        }

        matcher = Pattern.compile("deck show --cards").matcher(userInput);
        if (matcher.matches()) {
            deckController.deckShowCards();
            return 1;
        }

        matcher = Pattern.compile("deck show ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            try {
                deckController.showDeck(matcher.group(1));
            } catch (ParseCommandException e) {
                System.out.println("invalid command");
            }
            return 1;
        }

        if (Pattern.compile("menu exit").matcher(userInput).matches()) {
            ApplicationManger.goToScene(SceneName.MAIN_MENU, false);
            return 0;
        }

        System.out.println("Invalid command!");
        return 1;
    }
}
