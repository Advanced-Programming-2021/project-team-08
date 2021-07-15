package view.menus;

import controller.ApplicationManger;
import controller.ProfileController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import model.enums.ProfileMessages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileScene extends Scene {
    private final ProfileController profileController;
    public Label nicknameChangeMessage;
    public Label passwordChangeMessage;
    public Pane changeNicknamePane;
    public Pane changePasswordPane;
    public TextField oldPassField;
    public TextField newPassField;
    public TextField newNicknameField;
    public Pane specsPane;
    public Label usernameLabel;
    public Label nicknameLabel;

    public ProfileScene() {
        profileController = new ProfileController(this);
    }

    @Override
    public int getUserCommand() {
        String userInput = scanner.nextLine().trim();
        Matcher matcher;
        if ((Pattern.compile("^menu enter ([A-Za-z]+)$").matcher(userInput)).find()) {
            System.out.println("menu navigation is not possible");
            return 1;
        }
        if ((matcher = Pattern.compile("^profile change (--nickname[^\\n]+)$").matcher(userInput)).find()) {
            profileController.changeNickname(matcher.group());
        } else if ((matcher = Pattern.compile("^profile change --password ([^\\n]+)$").matcher(userInput)).find()) {
            profileController.changePassword(matcher.group());
        } else if (Pattern.compile("^menu show-current$").matcher(userInput).find()) {
            System.out.println("Profile Menu");
        } else if (Pattern.compile("^menu exit$").matcher(userInput).find()) {
            ApplicationManger.goToScene(SceneName.MAIN_MENU, false);
            return 0;
        } else System.out.println("invalid commands");
        return 1;
    }

    @FXML
    void initialize() {
        updateLabels();
        Image image = new Image(ApplicationManger.getLoggedInUser().getUserData().getProfileImageUrl());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        specsPane.getChildren().add(specsPane.getChildren().size(), imageView);
    }

    private void updateLabels() {
        usernameLabel.setText("username: " + ApplicationManger.getLoggedInUser().getUsername());
        nicknameLabel.setText("nickname: " + ApplicationManger.getLoggedInUser().getUserData().getNickname());
    }

    public void errorMessage(ProfileMessages error) {
        switch (error) {
            case REPEATED_NICKNAME:
                System.out.println("this nickname is already used");
                break;
            case WRONG_PASSWORD:
                System.out.println("current password is invalid");
                break;
            case REPEATED_PASSWORD:
                System.out.println("please enter a new password");
                break;
        }
    }

    public void successMessage(ProfileMessages message) {
        switch (message) {
            case NICKNAME_CHANGED:
                System.out.println("nickname changed successfully!");
                break;
            case PASSWORD_CHANGED:
                System.out.println("password changed successfully!");
                break;
        }
    }

    public void setNicknameChangeMenu(ActionEvent actionEvent) {
        changeNicknamePane.setVisible(!changeNicknamePane.isVisible());
        resetNicknameChangeMenu();
    }

    public void setPasswordChangeMenu(ActionEvent actionEvent) {
        changePasswordPane.setVisible(!changePasswordPane.isVisible());
        resetPasswordChangeMenu();
    }

    public void changeNickname(ActionEvent actionEvent) {
        profileController.changeNickname1(newNicknameField.getText());
        updateLabels();
    }

    public void changePassword(ActionEvent actionEvent) {
        profileController.changePassword1(oldPassField.getText(), newPassField.getText());
    }

    public void setNicknameChangeMessage(String message, boolean hasError) {
        nicknameChangeMessage.setText(message);
        if (hasError) nicknameChangeMessage.setTextFill(Color.RED);
        else nicknameChangeMessage.setTextFill(Color.GREEN);
    }

    public void setPasswordChangeMessage(String message, boolean hasError) {
        passwordChangeMessage.setText(message);
        if (hasError) passwordChangeMessage.setTextFill(Color.RED);
        else passwordChangeMessage.setTextFill(Color.GREEN);
    }

    private void resetPasswordChangeMenu() {
        oldPassField.setText("");
        newPassField.setText("");
        passwordChangeMessage.setText("");
    }

    private void resetNicknameChangeMenu() {
        nicknameChangeMessage.setText("");
        newNicknameField.setText("");
    }

    public void back(ActionEvent actionEvent) {
        ApplicationManger.goToScene("mainScene.fxml");
    }

    public void showScoreboard(ActionEvent actionEvent) {
        ApplicationManger.goToScene1(SceneName.SCOREBOARD_MENU,false);
    }
}
