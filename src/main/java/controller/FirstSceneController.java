package controller;
import javafx.event.ActionEvent;

public class FirstSceneController   {

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void register(ActionEvent actionEvent) {
        ApplicationManger.goToScene("registerScene.fxml");
    }

    public void login(ActionEvent actionEvent) {
        ApplicationManger.goToScene("loginScene.fxml");
    }

}
