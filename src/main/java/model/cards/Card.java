package model.cards;

import controller.ApplicationManger;
import controller.gameplay.GameManager;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import model.cards.data.CardData;
import model.cards.data.MonsterCardData;
import model.cards.data.SpellCardData;
import model.cards.data.TrapCardData;
import model.effectSystem.Effect;
import model.enums.CardStatus;
import model.enums.CardType;
import model.event.EventNoParam;
import model.gameplay.CardSlot;
import model.gameplay.Player;

public abstract class Card {
    protected CardType cardType;
    protected CardData cardData;

    protected CardSlot cardSlot;

    protected CardStatus cardStatus;

    protected Player cardOwner;

    protected EventNoParam onDestroy = new EventNoParam();

    private static Image cardBackImage = new Image("file:" + System.getProperty("user.dir") + "/src/main/resources/asset/gameplay/cardBack.png");

    private ImageView shape;

    public static Card getCardByCardData(CardData data) {
        Card card = null;

        switch (data.getCardType()) {
            case MONSTER:
                card = new MonsterCard((MonsterCardData) data);
                break;
            case SPELL:
                card = new SpellCard((SpellCardData) data);
                break;
            case TRAP:
                card = new TrapCard((TrapCardData) data);
                break;
        }

        return card;
    }

    public static Card createCardByName(String cardName) throws Exception {
        CardData data = CardData.getAllCardData().stream().filter(c -> c.getCardName().equals(cardName)).findFirst().orElse(null);
        if (data == null) {
            throw new Exception("No card exist with this name!");
        }

        return createCardByCardData(data);
    }

    public static Card createCardByCardData(CardData data) {
        Card card = null;

        switch (data.getCardType()) {
            case MONSTER:
                card = new MonsterCard((MonsterCardData) data);
                break;
            case SPELL:
                card = new SpellCard((SpellCardData) data);
                break;
            case TRAP:
                card = new TrapCard((TrapCardData) data);
                break;
        }

        return card;
    }

    public static int getCardIdByName(String cardName) throws Exception {
        CardData data = CardData.getAllCardData().stream().filter(c -> c.getCardName().equals(cardName)).findFirst().orElse(null);

        if (data == null) {
            throw new Exception("No card exist with this name!");
        } else {
            return data.getCardId();
        }
    }

    public Card(CardData cardData) {
        this.cardData = cardData;
        for (Effect effect : cardData.getEffects()) {
            effect.setCard(this);
        }
        shape = new ImageView(cardBackImage);
        shape.setFitWidth(80);
        shape.setFitHeight(120);
        shape.setScaleX(-1);

        shape.setOnMouseEntered(event -> onMouseEnter());
        shape.setOnMouseExited(event -> onMouseExit());
        shape.setOnMouseClicked(event -> onClick(event));
    }

    public Player getCardOwner() {
        return cardOwner;
    }

    public CardStatus getCardStatus() {
        return cardStatus;
    }

    public CardData getCardData() {
        return cardData;
    }

    public CardType getCardType() {
        return cardType;
    }

    public ImageView getShape() {
        return shape;
    }

    public void setup(Player owner) {
        cardOwner = owner;
        for (Effect effect : cardData.getEffects()) {
            effect.setup();
        }
    }

    public abstract void onSet();

    public CardSlot getCardSlot() {
        return cardSlot;
    }

    public void setCardSlot(CardSlot cardSlot) {
        this.cardSlot = cardSlot;
    }

    public EventNoParam getOnDestroyEvent() {
        return onDestroy;
    }

    public void moveToGraveyard() {
        CardSlot graveyard = cardOwner.getPlayerBoard().getGraveyard();
        onDestroy.invoke();
        cardSlot.removeCard();
        graveyard.appendCard(this);
    }

    public void onMouseEnter() {
        switch (cardSlot.getZoneType()) {
            case HAND:
                if (GameManager.getInstance().getCurrentTurnPlayer() != cardOwner) return;
                shape.setLayoutY(shape.getLayoutY() - 30);
                shape.toFront();
                break;
        }
    }

    public void onMouseExit() {
        switch (cardSlot.getZoneType()) {
            case HAND:
                if (GameManager.getInstance().getCurrentTurnPlayer() != cardOwner) return;
                shape.setLayoutY(shape.getLayoutY() + 30);
                shape.toBack();
                break;
        }
    }

    private void onClick(MouseEvent event) {
        switch (cardSlot.getZoneType()) {
            case GRAVEYARD:
                break;
            case DECK:
                break;
            case FIELD:
                break;
            case MONSTER:
                break;
            case SPELL_AND_TRAP:
                break;
            case HAND:
                ContextMenu contextMenu = new ContextMenu();
                MenuItem menuItem1;
                MenuItem menuItem2;
                switch (cardType){
                    case MONSTER:
                        menuItem1 = new MenuItem("Summon");
                        menuItem1.setOnAction(e -> System.out.println("summon"));
                        menuItem2 = new MenuItem("Set");
                        menuItem2.setOnAction(e -> System.out.println("set"));
                        contextMenu.getItems().addAll(menuItem1,menuItem2);
                        break;
                    case SPELL:
                        menuItem1 = new MenuItem("Activate");
                        menuItem1.setOnAction(e -> System.out.println("activate"));
                        menuItem2 = new MenuItem("Set");
                        menuItem2.setOnAction(e -> System.out.println("set"));
                        contextMenu.getItems().addAll(menuItem1,menuItem2);
                        break;
                    case TRAP:
                        menuItem1 = new MenuItem("Set");
                        menuItem1.setOnAction(e -> System.out.println("set"));
                        contextMenu.getItems().addAll(menuItem1);
                        break;
                }

                contextMenu.setX(event.getScreenX());
                contextMenu.setY(event.getScreenY());

                contextMenu.show(ApplicationManger.getMainStage());
                break;
        }
    }

    @Override
    public String toString() {
        return cardData.getCardName() + ":" + cardData.getCardDescription();
    }
}
