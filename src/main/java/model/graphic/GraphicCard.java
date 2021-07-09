package model.graphic;

import controller.ApplicationManger;
import controller.gameplay.GameManager;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.cards.Card;
import model.cards.data.CardData;
import model.enums.CardStatus;
import model.enums.ZoneType;
import model.gameplay.CardSlot;

public class GraphicCard {
    private static Image back = new Image("file:" + System.getProperty("user.dir") + "/src/main/resources/asset/gameplay/cardBack.png");
    private Image face;
    private ImageView shape;
    private CardData data;
    private GraphicCardSlot slot;
    private CardStatus status = CardStatus.TO_BACK;

    public GraphicCard(Card card) {
        this.data = card.getCardData();
        this.face = card.getCardData().getCardImage();
        shape = new ImageView(back);
        shape.setFitWidth(80);
        shape.setFitHeight(110);
        shape.setScaleX(-1);

        shape.setOnMouseEntered(event -> onMouseEnter());
        shape.setOnMouseExited(event -> onMouseExit());

        shape.setOnMouseClicked(event -> {
            switch (GameManager.getInstance().getCurrentPhase()) {
                case DRAW:
                    break;
                case STANDBY:
                    break;
                case MAIN:
                    onClickMain(event);
                    break;
                case BATTLE:
                    onClickBattle(event);
                    break;
                case END:
                    break;
            }
        });
    }


    public static Image getBack() {
        return back;
    }

    public void setSlot(GraphicCardSlot slot) {
        this.slot = slot;
    }

    public CardStatus getStatus() {
        return status;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    private void onClickMain(MouseEvent event) {
        ContextMenu contextMenu;
        MenuItem menuItem1;
        MenuItem menuItem2;
        switch (slot.getType()) {
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
                contextMenu = new ContextMenu();
                switch (data.getCardType()) {
                    case MONSTER:
                        menuItem1 = new MenuItem("Summon");
                        menuItem1.setOnAction(e -> {
                            System.out.println("summon");
                            try {
                                GameManager.getInstance().selectCard("--hand " + (slot.getAllCards().indexOf(this) + 1));
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            GameManager.getInstance().summonCard();
                        });
                        menuItem2 = new MenuItem("Set");
                        menuItem2.setOnAction(e -> {
                            System.out.println("set");
                            try {
                                GameManager.getInstance().selectCard("--hand " + (slot.getAllCards().indexOf(this) + 1));
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            GameManager.getInstance().setCard();
                        });
                        contextMenu.getItems().addAll(menuItem1, menuItem2);
                        break;
                    case SPELL:
                        menuItem1 = new MenuItem("Activate");
                        menuItem1.setOnAction(e -> System.out.println("activate"));
                        menuItem2 = new MenuItem("Set");
                        menuItem2.setOnAction(e -> System.out.println("set"));
                        contextMenu.getItems().addAll(menuItem1, menuItem2);
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


    private void onClickBattle(MouseEvent event) {
        ContextMenu contextMenu = new ContextMenu();
        switch (slot.getType()) {
            case GRAVEYARD:
                break;
            case DECK:
                break;
            case FIELD:
                break;
            case MONSTER:
                contextMenu = new ContextMenu();
                for (int i = 1; i <= 5; i++) {
                    try {
                        CardSlot cs = GameManager.getInstance().getGameBoard().getCardSlot(true, ZoneType.MONSTER, i);
                        if (!cs.isEmpty()) {
                            MenuItem m = new MenuItem("Attack " + i);
                            int finalI = i;
                            m.setOnAction(e -> {
                                System.out.println("attack ");
                                try {
                                    GameManager.getInstance().selectCard("--monster " + slot.getNumber());
                                    GameManager.getInstance().attack(finalI);
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                            });
                            contextMenu.getItems().add(m);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case SPELL_AND_TRAP:
                break;
            case HAND:
                break;
        }
        contextMenu.setX(event.getScreenX());
        contextMenu.setY(event.getScreenY());

        contextMenu.show(ApplicationManger.getMainStage());
    }

    public void onMouseEnter() {
        switch (slot.getType()) {
            case HAND:
                shape.setLayoutY(shape.getLayoutY() - 30);
                break;
        }
    }

    public void onMouseExit() {
        switch (slot.getType()) {
            case HAND:
                shape.setLayoutY(shape.getLayoutY() + 30);
                break;
        }
    }

    public ImageView getShape() {
        return shape;
    }

    public Image getFace() {
        return face;
    }
}
