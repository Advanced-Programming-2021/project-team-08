package view.menus;

import controller.ApplicationManger;
import controller.MainMenuController;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import model.animation.SpriteAnimation;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainScene extends Scene {

    public ImageView imageView;

    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();
        Matcher matcher;
        if ((matcher = Pattern.compile("^menu enter ([A-Za-z]+)$").matcher(userInput)).find()) {
            return MainMenuController.enterMenu(matcher.group(1), false);
        } else if (Pattern.compile("^menu exit$").matcher(userInput).find()) {
            ApplicationManger.goToScene(SceneName.REGISTER_MENU, false);
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
        Image image = null;
        try {
            image =  new Image((new URL("file:" + System.getProperty("user.dir") + "/src/main/resources/asset/mainMenu/ds_btn_inactive.png").toExternalForm()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        imageView.setImage(image);
        SpriteAnimation animation = new SpriteAnimation(imageView, Duration.millis(1000), 57, 6,0,0,632,100);
        animation.play();
    }

    public int logout() {
        System.out.println("user logged out successfully!");
        ApplicationManger.logoutCurrentUser();
        ApplicationManger.goToScene(SceneName.REGISTER_MENU, false);
        return 0;
    }

}
