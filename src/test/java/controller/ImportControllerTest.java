package controller;

import model.cards.data.ReadMonsterCardsData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.menus.ImportScene;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImportControllerTest {

    private ImportController importController;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setup() {
        importController = new ImportController(new ImportScene());
        System.setOut(new PrintStream(outputStreamCaptor));
        new ReadMonsterCardsData().readCardsData();
    }

    @Test
    void exportTest() {
        String cardName = "wrong name";
        importController.exportCard(cardName);
        String output = "There is no card with this name.\r\n";
        assertEquals(output, outputStreamCaptor.toString());
        outputStreamCaptor.reset();
        cardName = "Battle OX";
        File file = new File("exportedCards/Battle OX.json");
        boolean shouldDelete = true;
        if (file.exists()) {
            shouldDelete = false;
        }
        importController.exportCard(cardName);
        output = "card exported successfully.\r\n";
        assertEquals(output, outputStreamCaptor.toString());
        if (shouldDelete) file.delete();
    }

    @Test
    void importTest() {
        String cardName = "wrong name";
        importController.importCard(cardName);
        String output = "importCards\\wrong name.json (The system cannot find the file specified)\r\n";
        assertEquals(output, outputStreamCaptor.toString());
        outputStreamCaptor.reset();
        cardName = "test";
        importController.importCard(cardName);
        output = "Name: Test\n" +
                "Spell\n" +
                "Type: FIELD\n" +
                "Description: All Fiend and Spellcaster monsters on the field gain 200 ATK/DEF, also all Fairy monsters on the field lose 200 ATK/DEF.\r\n" +
                "card imported successfully.\r\n";
        assertEquals(output, outputStreamCaptor.toString());
    }
}
