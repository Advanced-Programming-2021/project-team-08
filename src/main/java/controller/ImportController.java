package controller;

import model.cards.data.CardData;
import view.menus.ImportScene;

public class ImportController {

    private ImportScene importScene;

    public ImportController(ImportScene importScene) {
        this.importScene = importScene;
    }

    public void importCard(String cardName) {
        CardData cardData = CardData.getCardByName(cardName);
        if (cardData == null) {
            importScene.printMessage("There is no card with this name.");
        }
        else {
            DataManager.importCard(cardData);
        }
    }

    public void exportCard(String cardName) {
        CardData cardData = CardData.getCardByName(cardName);
        if (cardData == null) {
            importScene.printMessage("There is no card with this name.");
        }
        else {
            DataManager.exportCard(cardName);
        }
    }

}
