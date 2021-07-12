package view.menus;

import controller.ApplicationManger;
import javafx.event.ActionEvent;

public class CardOptionsScene extends Scene {
    @Override
    protected int getUserCommand() {
        return 0;
    }

    public void cardCreatorRun(ActionEvent actionEvent) {
        ApplicationManger.goToScene("cardCreatingScene.fxml");
    }

    public void importCardRun(ActionEvent actionEvent) {
        //TODO: importScene write
    }

    public void exportCardRun(ActionEvent actionEvent) {
        //TODO: exportScene write
    }

    public void back(ActionEvent actionEvent) {
        ApplicationManger.goToScene("mainScene.fxml");

    }
}
