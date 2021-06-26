import com.google.gson.Gson;
import controller.ApplicationManger;
import controller.GamePlaySceneController;
import controller.User;
import model.Deck;
import model.cards.data.ReadMonsterCardsData;
import model.cards.data.ReadSpellTrapCardsData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import view.menus.GamePlayScene;

import java.io.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GamePlayTest {
    private static final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private static User testUser1;
    private static User testUser2;
    private static GamePlayScene scene;
    private static GamePlaySceneController sceneController;

    @BeforeAll
    static void beforeAll() {
        System.setOut(new PrintStream(outputStreamCaptor));
        testUser1 = new User("test1", "testing1", "test123");
        testUser2 = new User("test2", "testing2", "test123");
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter("users/" + "test1" + ".json");
            fileWriter.write(new Gson().toJson(testUser1));
            fileWriter.close();
            fileWriter = new FileWriter("users/" + "test2" + ".json");
            fileWriter.write(new Gson().toJson(testUser2));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new ReadMonsterCardsData().readCardsData();
        new ReadSpellTrapCardsData().readSpellTrapData();
        ApplicationManger.setLoggedInUser(testUser1);
        scene = new GamePlayScene();
        sceneController = scene.getSceneController();
    }

    @Test
    public void newDuel() {
        Integer[] cards1 = {1, 1, 1};
        Integer[] cards2 = {31, 31, 31, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 7, 8, 8, 8, 9, 9, 9, 10, 10, 10, 11, 11, 11, 12, 12, 12, 13};

        sceneController.duel("--new --second-player wrong --rounds 1");
        assertEquals("there is no player with this username" + System.lineSeparator(), outputStreamCaptor.toString());
        outputStreamCaptor.reset();

        Deck deck1 = new Deck("deckTest1", "test1");
        deck1.getMainDeckIds().addAll(Arrays.asList(cards1));
        testUser1.getUserData().addDeck(deck1);
        Deck deck2 = new Deck("deckTest2", "test2");
        deck2.getMainDeckIds().addAll(Arrays.asList(cards1));
        testUser2.getUserData().addDeck(deck2);

        sceneController.duel("--new --second-player test2 --rounds 1");
        assertEquals("test1 has no active deck" + System.lineSeparator(), outputStreamCaptor.toString());
        outputStreamCaptor.reset();

        testUser1.getUserData().setActiveDeckName("deckTest1");
        sceneController.duel("--new --second-player test2 --rounds 1");
        assertEquals("test2 has no active deck" + System.lineSeparator(), outputStreamCaptor.toString());
        outputStreamCaptor.reset();

        testUser2.getUserData().setActiveDeckName("deckTest2");
        sceneController.duel("--new --second-player test2 --rounds 1");
        assertEquals("test1's deck is invalid" + System.lineSeparator(), outputStreamCaptor.toString());
        outputStreamCaptor.reset();

        deck1.getMainDeckIds().addAll(Arrays.asList(cards2));
        sceneController.duel("--new --second-player test2 --rounds 1");
        assertEquals("test2's deck is invalid" + System.lineSeparator(), outputStreamCaptor.toString());
        outputStreamCaptor.reset();

        deck2.getMainDeckIds().addAll(Arrays.asList(cards2));
        sceneController.duel("--new --second-player test2 --rounds 1");
        assertEquals("Round 1", outputStreamCaptor.toString().split(System.lineSeparator())[0]);
        outputStreamCaptor.reset();
    }

    @AfterAll
    static void afterAll() {
        File userFile = new File("users/" + "test1" + ".json");
        userFile.delete();
        userFile = new File("users/" + "test2" + ".json");
        userFile.delete();
    }
}
