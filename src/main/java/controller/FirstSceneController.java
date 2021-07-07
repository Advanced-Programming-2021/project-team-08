package controller;
import javafx.event.ActionEvent;
import view.menus.SceneName;

public class FirstSceneController   {

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void register(ActionEvent actionEvent) {
        ApplicationManger.goToScene1(SceneName.REGISTER_MENU,false);
    }

    public void login(ActionEvent actionEvent) {
        ApplicationManger.goToScene1(SceneName.LOGIN_MENU,false);
    }

}
