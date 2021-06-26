package controller;

import model.cards.data.ReadSpellTrapCardsData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.menus.ShopScene;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShopControllerTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private ShopController shopController;
    private ShopScene shopScene;
    private User testUser;


    @BeforeEach
    private void setUser() {
        System.setOut(new PrintStream(outputStreamCaptor));
        testUser = new User("test", "testing", "test123");
        ApplicationManger applicationManger = new ApplicationManger();
        ApplicationManger.setLoggedInUser(testUser);
        shopScene = new ShopScene();
        shopController = new ShopController(shopScene);
        new ReadSpellTrapCardsData().readSpellTrapData();
    }

    @Test
    public void wrongNameBuyTest() {
        shopController.buyCard("wrong name");
        assertEquals("there is no card with this name" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    public void notEnoughMoney() {
        testUser.getUserData().changeMoneyWithoutSave(-100000);
        shopController.buyCard("Monster Reborn");
        testUser.getUserData().changeMoneyWithoutSave(100000);
        String output = "you have not enough money" + System.lineSeparator() + "your money is " + testUser.getUserData().getMoney()+ " card Price is " + "2500" + System.lineSeparator();
        assertEquals(output, outputStreamCaptor.toString());
    }

    @Test
    public void buySuccessfully() {
        shopController.buyCard("Monster Reborn");
        String output = "you bought " + "Monster Reborn" + " successfully." + System.lineSeparator();
        assertEquals(output, outputStreamCaptor.toString());
        assertEquals(54, testUser.getCardsThatThereIsNotInAnyDeck().get(0));
        assertEquals(97500, testUser.getUserData().getMoney());
    }


}
