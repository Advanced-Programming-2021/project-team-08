package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import view.menus.SceneName;

public class FirstSceneController {


    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void register(ActionEvent actionEvent) {
        RegisterController.setButton("register");
        ApplicationManger.goToScene1(SceneName.REGISTER_MENU, false);
    }

    public void login(ActionEvent actionEvent) {
        RegisterController.setButton("login");
        ApplicationManger.goToScene1(SceneName.LOGIN_MENU, false);
    }

}
