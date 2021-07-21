package view.menus;

import controller.ApplicationManger;
import controller.DataManager;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.cards.data.CardData;

public class CardOptionsScene extends Scene {

    public Button next;
    public TextField cardName;
    public Label message;
    public Label cardNameLabel;
    String name;
    private static String messageText;

    @Override
    protected int getUserCommand() {
        return 0;
    }

    public void cardCreatorRun(ActionEvent actionEvent) {
        ApplicationManger.goToScene("cardCreatingScene.fxml");
    }

    public void importCardRun(ActionEvent actionEvent) {
        next.setText("import");
        message.setText("");
        cardName.clear();
        cardName.setOpacity(1);
        next.setOpacity(1);
        cardNameLabel.setOpacity(1);
        next.setOnMouseClicked(event -> {
            name = cardName.getText();
            DataManager.importCardGraphic(name);
            showMessage(messageText);
        });
    }

    public void exportCardRun(ActionEvent actionEvent) {
        next.setText("export");
        message.setText("");
        cardName.clear();
        cardName.setOpacity(1);
        next.setOpacity(1);
        cardNameLabel.setOpacity(1);
        next.setOnMouseClicked(event -> {
            name = cardName.getText();
            CardData cardData = CardData.getCardByName(name);
            if (cardData == null) {
                message.setText("There is no card with this name.");
                return;
            }
            DataManager.exportCardGraphic(cardData);
            showMessage(messageText);
        });
    }

    public void back(ActionEvent actionEvent) {
        ApplicationManger.goToScene("mainScene.fxml");

    }

    public static void setMessage(String messageText1) {
        messageText = messageText1;
    }

    public void showMessage(String messageText) {
        message.setText(messageText);
    }
}
