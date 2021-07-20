package view.menus;

import controller.ApplicationManger;
import controller.MainMenuController;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import model.animation.SpriteAnimation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static controller.ApplicationManger.getServerResponse;
import static controller.ApplicationManger.goToScene;

public class MainScene extends Scene {


    public ImageView duel;
    public ImageView deck;
    public ImageView shop;
    public ImageView profile;
    public ImageView exit;
    public ImageView menuName;
    public Label duelLabel;
    public Label deckLabel;
    public Label shopLabel;
    public Label profileLabel;
    public Label exitLabel;
    public ImageView card;
    public Label cardLabel;
    private Image menuHoverImage;
    private Image menuImage;


    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();
        Matcher matcher;
        if ((matcher = Pattern.compile("^menu enter ([A-Za-z]+)$").matcher(userInput)).find()) {
            return MainMenuController.enterMenu(matcher.group(1), false);
        } else if (Pattern.compile("^menu exit$").matcher(userInput).find()) {
            goToScene(SceneName.REGISTER_MENU, false);
            return 0;
        } else if (Pattern.compile("^menu show-current$").matcher(userInput).find()) {
            System.out.println("Main Menu");
        } else if (Pattern.compile("^user logout$").matcher(userInput).find()) {
            return logout();
        } else {
            System.out.println("invalid command");
        }
        return 1;
    }

    @FXML
    void initialize() {
        String hoverPath = "/src/main/resources/asset/mainMenu/ds_btn_active.png";
        String path = "/src/main/resources/asset/mainMenu/ds_btn_inactive.png";
        try {
            menuHoverImage = new Image(new URL("file:" + System.getProperty("user.dir") + hoverPath).toExternalForm());
            menuImage = new Image(new URL("file:" + System.getProperty("user.dir") + path).toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        setMenuName();
        setMenuItemAnimation(duel, duelLabel);
        setMenuItemAnimation(deck, deckLabel);
        setMenuItemAnimation(shop, shopLabel);
        setMenuItemAnimation(card, cardLabel);
        setMenuItemAnimation(profile, profileLabel);
        setMenuItemAnimation(exit, exitLabel);
    }

    private void setMenuName() {
        menuName.setViewport(new Rectangle2D(0, 745, 960, 133));
    }

    private void setMenuItemAnimation(ImageView menuItem, Label label) {
        SequentialTransition menuItemActiveTransition = setupActiveAnimation(menuItem);
        SequentialTransition menuItemInactiveTransition = setupInactiveAnimation(menuItem);
        label.setDisable(true);
        menuItemInactiveTransition.play();
        menuItem.setOnMouseEntered(event -> {
            menuItem.setImage(menuHoverImage);
            menuItemInactiveTransition.stop();
            menuItemActiveTransition.play();
            menuItem.setFitWidth(758.4);
            menuItem.setFitHeight(121.2);
            menuItem.setLayoutX(420.8);
            menuItem.setLayoutY(menuItem.getLayoutY() - 10.1);
            //label.setFont(new Font(55));
            label.setTextFill(Color.YELLOW);
        });
        menuItem.setOnMouseExited(event -> {
            menuItem.setImage(menuImage);
            menuItemActiveTransition.stop();
            menuItemInactiveTransition.play();
            menuItem.setFitWidth(632);
            menuItem.setFitHeight(101);
            menuItem.setLayoutX(484);
            menuItem.setLayoutY(menuItem.getLayoutY() + 10.1);
            //label.setFont(new Font(45));
            label.setTextFill(Color.WHITE);
        });
    }


    public int logout() {
        System.out.println("user logged out successfully!");
        ApplicationManger.logoutCurrentUser();
        goToScene(SceneName.REGISTER_MENU, false);
        return 0;
    }


    private SequentialTransition setupInactiveAnimation(ImageView imageView) {
        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.setCycleCount(-1);
        sequentialTransition.getChildren().add(sequentialTransition.getChildren().size(), setSpriteAnimation(1, 10, imageView));
        sequentialTransition.getChildren().add(sequentialTransition.getChildren().size(), setSpriteAnimation(2, 10, imageView));
        sequentialTransition.getChildren().add(sequentialTransition.getChildren().size(), setSpriteAnimation(3, 10, imageView));
        sequentialTransition.getChildren().add(sequentialTransition.getChildren().size(), setSpriteAnimation(4, 10, imageView));
        sequentialTransition.getChildren().add(sequentialTransition.getChildren().size(), setSpriteAnimation(5, 9, imageView));
        sequentialTransition.getChildren().add(sequentialTransition.getChildren().size(), setSpriteAnimation(6, 8, imageView));
        return sequentialTransition;
    }

    private SequentialTransition setupActiveAnimation(ImageView imageView) {
        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.setCycleCount(-1);
        sequentialTransition.getChildren().add(sequentialTransition.getChildren().size(), setSpriteAnimation(1, 5, imageView));
        sequentialTransition.getChildren().add(sequentialTransition.getChildren().size(), setSpriteAnimation(2, 5, imageView));
        sequentialTransition.getChildren().add(sequentialTransition.getChildren().size(), setSpriteAnimation(3, 5, imageView));
        sequentialTransition.getChildren().add(sequentialTransition.getChildren().size(), setSpriteAnimation(4, 4, imageView));
        return sequentialTransition;
    }


    private SpriteAnimation setSpriteAnimation(int column, int count, ImageView imageView) {
        SpriteAnimation animation = new SpriteAnimation(imageView, Duration.millis(1000), count, 1, 0, 0, 632, 101);
        animation.setOffsetX((column - 1) * 632);
        animation.setLastIndex(count - 1);
        return animation;
    }

    public void duelRun(MouseEvent mouseEvent) {
        goToScene("duelScene.fxml");
    }

    public void deckRun(MouseEvent mouseEvent) {
        ApplicationManger.goToScene1(SceneName.DECK_MENU, false);
    }

    public void shopRun(MouseEvent mouseEvent) {
        goToScene("shopScene.fxml");
    }

    public void profileRun(MouseEvent mouseEvent) {
        goToScene("profileScene.fxml");
    }

    public void exitGame(MouseEvent mouseEvent) {
        System.out.println(getServerResponse("register", "logout", null));
        System.exit(0);
    }

    public void card(MouseEvent mouseEvent) {
        goToScene("cardCreatingOptions.fxml");
    }
}
