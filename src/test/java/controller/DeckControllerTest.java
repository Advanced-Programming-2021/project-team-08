package controller;

import model.Deck;
import model.cards.data.ReadMonsterCardsData;
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

//    @Test
//    public void deckDelete(){
//
//        String input="deck delete second";
//        String output="deck with name second does not exist"+ System.lineSeparator();
//        deckController.deckDelete(input);
//        assertEquals(output,outputStreamCaptor.toString());
//
//    }

}
