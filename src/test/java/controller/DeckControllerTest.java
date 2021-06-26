package controller;

import model.cards.data.ReadMonsterCardsData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.menus.DeckMenu;
import view.menus.ImportScene;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeckControllerTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private DeckController deckController;
    User testUser;

    @BeforeEach
    public void setup() {
        System.setOut(new PrintStream(outputStreamCaptor));
        testUser = new User("test", "testing", "test123");
        new User ("alaki", "abool", "abool123");
        ApplicationManger.setLoggedInUser(testUser);
        deckController = new DeckController(new DeckMenu());
    }

    @Test
    public void deckCreate(){
        String input="deck create first";
        String output="deck created successfully!"+ System.lineSeparator();
        deckController.deckCreate(input);
        assertEquals(output,outputStreamCaptor.toString());
    }

}
