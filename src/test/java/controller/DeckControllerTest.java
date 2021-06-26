package controller;

import model.Deck;
import model.cards.data.ReadMonsterCardsData;
import model.exceptions.ParseCommandException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.menus.DeckMenu;
import view.menus.ImportScene;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

 public class DeckControllerTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private DeckController deckController;
    private User testUser;

    @BeforeEach
     void setup() {
        System.setOut(new PrintStream(outputStreamCaptor));
        testUser = new User("test", "testing", "test123");
        new User ("alaki", "abool", "abool123");
        ApplicationManger.setLoggedInUser(testUser);
        deckController = new DeckController(new DeckMenu());
    }

    @Test
    void deckCreate(){

        String input="deck create first";
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
     void deckDelete(){

        String input="deck delete first";
        Matcher matcher = Pattern.compile("deck delete ([^\\n]+)").matcher(input);
        if(matcher.find()) {
            String output = "deck with name first does not exist" + System.lineSeparator();
            deckController.deckDelete(matcher.group(1));
            assertEquals(output, outputStreamCaptor.toString());
        }
        outputStreamCaptor.reset();

        String username = ApplicationManger.getLoggedInUser().getUsername();
        Deck deck = new Deck("first", username);
        ApplicationManger.getLoggedInUser().addDeck(deck);

        if(matcher.find()) {
            String output = "deck deleted successfully" + System.lineSeparator();
            deckController.deckDelete(matcher.group(1));
            assertEquals(output, outputStreamCaptor.toString());
        }
    }

    @Test
     void deckSetActive(){
        String input="deck set-activate first";
        Matcher matcher = Pattern.compile("deck set-activate ([^\\n]+)").matcher(input);
        if (matcher.find()){
            String output="deck with name first does not exist"+System.lineSeparator();
            deckController.deckSetActive(matcher.group(1));
            assertEquals(output, outputStreamCaptor.toString());
        }
        outputStreamCaptor.reset();

        String username = ApplicationManger.getLoggedInUser().getUsername();
        Deck deck = new Deck("first", username);
        ApplicationManger.getLoggedInUser().addDeck(deck);

        if (matcher.find()){
            String output="deck activated successfully"+System.lineSeparator();
            deckController.deckSetActive(matcher.group(1));
            assertEquals(output, outputStreamCaptor.toString());
        }
    }

    @Test
     void addCard() throws ParseCommandException {
        String username = ApplicationManger.getLoggedInUser().getUsername();
        Deck deck = new Deck("first", username);
        ApplicationManger.getLoggedInUser().addDeck(deck);

        String input="deck add-card --card cardName --deck first";
        Matcher matcher = Pattern.compile("deck add-card ([^\\n]+)").matcher(input);
        if (matcher.find()){
            String output="card with name cardName does not exist"+System.lineSeparator();
            deckController.addCard(matcher.group(1));
            assertEquals(output, outputStreamCaptor.toString());
        }
        outputStreamCaptor.reset();

        input="deck add-card --card Yomi Ship --deck second";
        if (matcher.find()){
            String output="deck with name second does not exist"+System.lineSeparator();
            deckController.addCard(matcher.group(1));
            assertEquals(output, outputStreamCaptor.toString());
        }
        outputStreamCaptor.reset();

        input="deck add-card --card Yomi Ship --deck first";
        if (matcher.find()){
            String output="card added to deck successfully"+System.lineSeparator();
            deckController.addCard(matcher.group(1));
            assertEquals(output, outputStreamCaptor.toString());
        }

    }
}
