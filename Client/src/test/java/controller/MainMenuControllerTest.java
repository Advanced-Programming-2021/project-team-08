package controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainMenuControllerTest {

    private static final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();


    @BeforeAll
    static void setup() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @BeforeEach
    void set() {
        outputStreamCaptor.reset();
    }

    @Test
    void entryMenuTest() {
        MainMenuController.enterMenu("scoreboard", true);
        assertEquals("<<SCOREBOARD MENU>>\r\n", outputStreamCaptor.toString());
        outputStreamCaptor.reset();
        MainMenuController.enterMenu("shop", true);
        assertEquals("<<SHOP MENU>>\r\n", outputStreamCaptor.toString());
        outputStreamCaptor.reset();
        MainMenuController.enterMenu("profile", true);
        assertEquals("<<PROFILE MENU>>\r\n", outputStreamCaptor.toString());
        outputStreamCaptor.reset();
        MainMenuController.enterMenu("deck", true);
        assertEquals("<<DECK MENU>>\r\n", outputStreamCaptor.toString());
        outputStreamCaptor.reset();
        MainMenuController.enterMenu("duel", true);
        assertEquals("<<GAMEPLAY SCENE>>\r\n", outputStreamCaptor.toString());
        outputStreamCaptor.reset();
        MainMenuController.enterMenu("import", true);
        assertEquals("<<IMPORT EXPORT MENU>>\r\n", outputStreamCaptor.toString());
        outputStreamCaptor.reset();
        MainMenuController.enterMenu("ya ha ya ha", true);
        assertEquals("invalid menu name\r\n", outputStreamCaptor.toString());
        outputStreamCaptor.reset();
    }
}
