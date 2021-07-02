package view.menus;

import controller.ApplicationManger;
import controller.ShopController;
import controller.User;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import model.cards.data.CardData;
import model.cards.data.MonsterCardData;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopScene extends Scene {

    private User activeUser;
    private ShopController shopController;

    public ShopScene() {
        this.activeUser = ApplicationManger.getLoggedInUser();
        shopController = new ShopController();
    }

    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();

        if (cheatCommand(userInput) == 1) return 1;

        Matcher matcher;
        if ((Pattern.compile("^menu enter ([A-Za-z]+)$").matcher(userInput)).find()) {
            System.out.println("menu navigation is not possible");
            return 1;
        }
        if (userInput.equals("show my balance")) {
            System.out.println("your balance is: " + activeUser.getUserData().getMoney());
            return 1;
        }
        if ((matcher = Pattern.compile("^card show ([^\n]+)$").matcher(userInput)).find()) {
            showCard(CardData.getCardByName(matcher.group(1)));
            return 1;
        }
        if ((matcher = Pattern.compile("^shop buy ([\\w ]+)$").matcher(userInput)).find()) {
            shopController.buyCard(matcher.group(1));
        } else if (Pattern.compile("^menu show-current$").matcher(userInput).find()) {
            System.out.println("Shop Menu");
        } else if (Pattern.compile("shop show --all").matcher(userInput).find()) {
            showAllCard();
        } else if (Pattern.compile("menu exit").matcher(userInput).matches()) {
            ApplicationManger.goToScene(SceneName.MAIN_MENU, false);
            return 0;
        } else {
            System.out.println("invalid command");

        }
        return 1;
    }

    private int cheatCommand(String userInput) {
        Matcher matcher = Pattern.compile("increse --money ([0-9]+)").matcher(userInput);
        if (matcher.matches()) {
            ApplicationManger.getLoggedInUser().getUserData().addMoney(Integer.parseInt(matcher.group(1)));
            return 1;
        }
        return 0;
    }

    private void showAllCard() {
        System.out.println("show all card entered " + CardData.getAllCardData().size());
        ArrayList<CardData> allCards = CardData.getAllCardData();
        allCards.sort(new sortCardsAlphabetically());
        for (CardData cardData : allCards) {
            System.out.println(cardData.getName() + " : " + cardData.getPrice());
        }
    }

    public void setCards(AnchorPane anchorPane, ArrayList<CardData> cards) {
        for (int i = 0; i < cards.size(); i++) {
            anchorPane.getChildren().add(i, cardImage(cards.get(i), i));
        }
    }

    private ImageView cardImage(CardData cardData, int index) {
        String path;
        String cardName = cardData.getCardName().replaceAll(" ", "");
        if (cardData instanceof MonsterCardData) {
            path = "/src/main/resources/asset/Cards/Monsters/" + cardName + ".jpg";
            if (new File(path).exists()) System.out.println("the fucking file is exist");
        }else {
            path = "/src/main/resources/asset/Cards/SpellTrap/" + cardName + ".jpg";
        }
        ImageView cardImage = new ImageView();
        try {
            cardImage.setImage(new Image(new URL("file:" + System.getProperty("user.dir") + path).toExternalForm()));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        cardImage.setFitHeight(425);
        cardImage.setFitWidth(240);
        int x = (index % 5) * (260) + 20;
        int y = (index / 5) * (445) + 20;
        cardImage.setX(x);
        cardImage.setY(y);
        System.out.println("card x is : " + x + "card y is : " + y);
        cardImage.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cardImage.setFitHeight(425 * 2);
                cardImage.setFitWidth(240 * 2);
            }
        });
        cardImage.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cardImage.setFitHeight(425);
                cardImage.setFitWidth(240);
            }
        });
        return cardImage;
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    class sortCardsAlphabetically implements Comparator<CardData> {
        public int compare(CardData a, CardData b) {
            return a.getCardName().compareTo(b.getCardName());
        }
    }
}
