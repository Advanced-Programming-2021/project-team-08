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

        shape.setOnMouseClicked(event -> onClick(event));
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

    private void onClick(MouseEvent event) {
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
                ContextMenu contextMenu = new ContextMenu();
                MenuItem menuItem1;
                MenuItem menuItem2;
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

    public ImageView getShape() {
        return shape;
    }

    public Image getFace() {
        return face;
    }
}
