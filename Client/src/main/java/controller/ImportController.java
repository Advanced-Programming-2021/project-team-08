package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import model.cards.data.CardData;
import view.menus.ImportScene;

import java.awt.*;

public class ImportController {

    private ImportScene importScene;

    public ImportController(ImportScene importScene) {
        this.importScene = importScene;
    }

    public void importCard(String cardName) {
        DataManager.importCard(cardName);
    }

    public void exportCard(String cardName) {
        CardData cardData = CardData.getCardByName(cardName);
        if (cardData == null) {
            importScene.printMessage("There is no card with this name.");
            return;
        }
        DataManager.exportCard(cardData);
    }
    public void f(){

    }


}
