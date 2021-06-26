import com.google.gson.Gson;
import controller.ApplicationManger;
import controller.GamePlaySceneController;
import controller.User;
import controller.gameplay.GameManager;
import model.Deck;
import model.cards.Card;
import model.cards.data.ReadMonsterCardsData;
import model.cards.data.ReadSpellTrapCardsData;
import model.enums.Phase;
import model.gameplay.PlayerBoard;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import view.menus.GamePlayScene;

import java.io.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EffectsTest {
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

        Integer[] cards = {31, 31, 31, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 7, 8, 8, 8, 9, 9, 9, 10, 10, 10, 11, 11, 11, 12, 12, 12, 13};

        Deck deck1 = new Deck("deckTest1", "test1");
        Deck deck2 = new Deck("deckTest2", "test2");
        testUser1.getUserData().addDeck(deck1);
        testUser2.getUserData().addDeck(deck2);
        testUser1.getUserData().setActiveDeckName("deckTest1");
        testUser2.getUserData().setActiveDeckName("deckTest2");
        deck1.getMainDeckIds().addAll(Arrays.asList(cards));
        deck2.getMainDeckIds().addAll(Arrays.asList(cards));
        sceneController.duel("--new --second-player test2 --rounds 1");
    }

    @Test
    public void checkEffect() throws Exception {
        GameManager gameManager = sceneController.getGameManager();
        for (int i = 0; i < 2; i++) {
            Card card = Card.createCardByName("Fireyarou");
            card.setup(gameManager.getCurrentTurnPlayer());
            gameManager.getGameBoard().getPlayer1Board().addMonsterCardToZone(card);
        }
        for (int i = 0; i < 2; i++) {
            Card card = Card.createCardByName("Fireyarou");
            card.setup(gameManager.getCurrentTurnOpponentPlayer());
            gameManager.getGameBoard().getPlayer2Board().addMonsterCardToZone(card);
        }

        gameManager.setCurrentPhase(Phase.MAIN);
        PlayerBoard playerBoard = gameManager.getCurrentTurnPlayer().getPlayerBoard();
        gameManager.addCard_C("Raigeki");
        gameManager.selectCard("select --hand " + playerBoard.getHand().getAllCards().size());
        gameManager.activateCard();

        assertEquals(0, gameManager.getCurrentTurnOpponentPlayer().getPlayerBoard().numberOfMonstersInZone());
    }

    @AfterAll
    static void afterAll() {
        File userFile = new File("users/" + "test1" + ".json");
        userFile.delete();
        userFile = new File("users/" + "test2" + ".json");
        userFile.delete();
    }
}
