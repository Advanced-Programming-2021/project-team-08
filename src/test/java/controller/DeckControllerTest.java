package controller;

import model.Deck;
import model.cards.data.ReadSpellTrapCardsData;
import model.exceptions.ParseCommandException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.menus.DeckMenu;
import view.menus.ShopScene;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeckControllerTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private DeckController deckController;
    private ShopController shopController;
    private User testUser;

    @BeforeEach
    void setup() {
        System.setOut(new PrintStream(outputStreamCaptor));
        testUser = new User("test", "testing", "test123");
        new User("alaki", "abool", "abool123");
        ApplicationManger.setLoggedInUser(testUser);
        deckController = new DeckController(new DeckMenu());
        shopController = new ShopController(new ShopScene());
        new ReadSpellTrapCardsData().readSpellTrapData();
    }

    @Test
    void deckCreate() {
        String input = "deck create first";
        Matcher matcher = Pattern.compile("deck create ([^\\n]+)").matcher(input);
        if (matcher.find()) {
            String output = "deck created successfully!" + System.lineSeparator();
            deckController.deckCreate(matcher.group(1));
            assertEquals(output, outputStreamCaptor.toString());
        }
        outputStreamCaptor.reset();
        String username = ApplicationManger.getLoggedInUser().getUsername();
        Deck deck = new Deck("first", username);
        ApplicationManger.getLoggedInUser().addDeck(deck);
        if (matcher.find()) {
            String output = "deck with name first already exists" + System.lineSeparator();
            deckController.deckCreate(matcher.group(1));
            assertEquals(output, outputStreamCaptor.toString());
        }

    }

    @Test
    void deckDelete() {
        String input = "deck delete first";
        Matcher matcher = Pattern.compile("deck delete ([^\\n]+)").matcher(input);
        if (matcher.find()) {
            String output = "deck with name first does not exist" + System.lineSeparator();
            deckController.deckDelete(matcher.group(1));
            assertEquals(output, outputStreamCaptor.toString());
        }
        outputStreamCaptor.reset();
        String username = ApplicationManger.getLoggedInUser().getUsername();
        Deck deck = new Deck("first", username);
        ApplicationManger.getLoggedInUser().addDeck(deck);

        if (matcher.find()) {
            String output = "deck deleted successfully" + System.lineSeparator();
            deckController.deckDelete(matcher.group(1));
            assertEquals(output, outputStreamCaptor.toString());
        }
    }

    @Test
    void deckSetActive() {
        String input = "deck set-activate first";
        Matcher matcher = Pattern.compile("deck set-activate ([^\\n]+)").matcher(input);
        if (matcher.find()) {
            String output = "deck with name first does not exist" + System.lineSeparator();
            deckController.deckSetActive(matcher.group(1));
            assertEquals(output, outputStreamCaptor.toString());
        }
        outputStreamCaptor.reset();
        String username = ApplicationManger.getLoggedInUser().getUsername();
        Deck deck = new Deck("first", username);
        ApplicationManger.getLoggedInUser().addDeck(deck);
        if (matcher.find()) {
            String output = "deck activated successfully" + System.lineSeparator();
            deckController.deckSetActive(matcher.group(1));
            assertEquals(output, outputStreamCaptor.toString());
        }
    }

    @Test
    void addCard() throws ParseCommandException {
        String username = ApplicationManger.getLoggedInUser().getUsername();
        Deck deck = new Deck("first", username);
        ApplicationManger.getLoggedInUser().addDeck(deck);
        shopController.buyCard("Monster Reborn");
        String input = "--card cardName --deck first";
        String output = "you bought Monster Reborn successfully." + System.lineSeparator() + "card with name cardName does not exist" + System.lineSeparator();
        deckController.addCard(input);
        assertEquals(output, outputStreamCaptor.toString());
        outputStreamCaptor.reset();
        input = "--card Monster Reborn --deck second";
        output = "deck with name second does not exist" + System.lineSeparator();
        deckController.addCard(input);
        assertEquals(output, outputStreamCaptor.toString());
        outputStreamCaptor.reset();
        input = "--card Monster Reborn --deck first";
        output = "card added to deck successfully" + System.lineSeparator();
        deckController.addCard(input);
        assertEquals(output, outputStreamCaptor.toString());
    }

    @Test
    void removeCard() throws ParseCommandException {
        String username = ApplicationManger.getLoggedInUser().getUsername();
        Deck deck = new Deck("first", username);
        ApplicationManger.getLoggedInUser().addDeck(deck);
        String input = "--card Monster Reborn --deck first";
        shopController.buyCard("Monster Reborn");
        deckController.addCard(input);
        input = "--card Monster Reborn --deck second";
        String output = "you bought Monster Reborn successfully." + System.lineSeparator() + "card added to deck successfully" + System.lineSeparator() + "deck with name second does not exist" + System.lineSeparator();
        deckController.removeCard(input);
        assertEquals(output, outputStreamCaptor.toString());
        outputStreamCaptor.reset();
        input = "--card Name --deck first";
        output = "card with name Name does not exist in main deck" + System.lineSeparator();
        deckController.removeCard(input);
        assertEquals(output, outputStreamCaptor.toString());
        outputStreamCaptor.reset();
        input = "--card Monster Reborn --deck first";
        output = "card removed form deck successfully" + System.lineSeparator();
        deckController.removeCard(input);
        assertEquals(output, outputStreamCaptor.toString());
    }
}
