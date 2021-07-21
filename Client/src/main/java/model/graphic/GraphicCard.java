package model.graphic;

import controller.ApplicationManger;
import controller.gameplay.GameManager;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.cards.data.CardData;
import model.enums.CardStatus;
import model.enums.ZoneType;
import view.CardView;
import view.menus.GamePlayScene;

public class GraphicCard {
    private static final Image back = new Image("file:" + System.getProperty("user.dir") + "/src/main/resources/asset/gameplay/cardBack.png");
    private final Image face;
    private final ImageView shape;
    private final CardData data;
    private GraphicCardSlot slot;
    private CardStatus status = CardStatus.TO_BACK;
    private boolean toAttackPosition = true;
    private final int cardOwnerPlayerNumber;

    public GraphicCard(CardData cardData, int cardOwnerNumber) {
        cardOwnerPlayerNumber = cardOwnerNumber;
        this.data = cardData;
        this.face = cardData.getCardImage();
        shape = new ImageView(back);
        shape.setFitWidth(80);
        shape.setFitHeight(110);
        shape.setScaleX(-1);

        shape.setOnMouseEntered(event -> onMouseEnter());
        shape.setOnMouseExited(event -> onMouseExit());

        shape.setOnMouseClicked(event -> {
            if (cardOwnerPlayerNumber != GamePlayScene.getInstance().getPlayerNumber()) return;
            switch (GamePlayScene.getInstance().getCurrentPhase()) {
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

    public boolean isToAttackPosition() {
        return toAttackPosition;
    }

    public void setToAttackPosition(boolean toAttackPosition) {
        this.toAttackPosition = toAttackPosition;
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
        boolean isAI = GamePlayScene.getInstance().isAI();
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
                            if (isAI) {
                                try {
                                    GameManager.getInstance().selectCard("--hand " + (slot.getAllCards().indexOf(this) + 1));
                                    GameManager.getInstance().summonCard();
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                            } else {
                                GamePlayScene.getInstance().sendMessageToServer("select --hand " + (slot.getAllCards().indexOf(this) + 1) + ",summon");
                            }
                        });
                        menuItem2 = new MenuItem("Set");
                        menuItem2.setOnAction(e -> {
                            System.out.println("set");
                            if (isAI) {
                                try {
                                    GameManager.getInstance().selectCard("--hand " + (slot.getAllCards().indexOf(this) + 1));
                                    GameManager.getInstance().setCard();
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                            } else {
                                GamePlayScene.getInstance().sendMessageToServer("select --hand " + (slot.getAllCards().indexOf(this) + 1) + ",set");
                            }

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
        boolean isAI = GamePlayScene.getInstance().isAI();
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
                        GraphicCardSlot gcs = GamePlayScene.getInstance().getgBoard().getCardSlot(true, ZoneType.MONSTER, i);
                        if (!gcs.isEmpty()) {
                            MenuItem m = new MenuItem("Attack " + i);
                            int finalI = i;
                            m.setOnAction(e -> {
                                System.out.println("attack " + finalI);
                                if (isAI) {
                                    try {
                                        GameManager.getInstance().selectCard("--monster " + slot.getNumber());
                                        GameManager.getInstance().attack(finalI);
                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                    }
                                } else {
                                    GamePlayScene.getInstance().sendMessageToServer("select --monster " + slot.getNumber() + ",attack " + finalI);
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
        if (cardOwnerPlayerNumber == GamePlayScene.getInstance().getPlayerNumber()) {
            switch (slot.getType()) {
                case FIELD:
                case MONSTER:
                case SPELL_AND_TRAP:
                case HAND:
                    CardView.getInstance().showCard(data);
                    shape.getStyleClass().add("onHoverCard");
                    break;
            }
            if (slot.getType() == ZoneType.HAND) {
                shape.setLayoutY(shape.getLayoutY() - 30);
            }
        } else {
            switch (slot.getType()) {
                case FIELD:
                case SPELL_AND_TRAP:
                case MONSTER:
                    if (status == CardStatus.FACE_UP) {
                        CardView.getInstance().showCard(data);
                        shape.getStyleClass().add("onHoverCard");
                    }
                    break;
            }
        }
    }

    public void onMouseExit() {
        shape.getStyleClass().remove("onHoverCard");
        if (cardOwnerPlayerNumber == GamePlayScene.getInstance().getPlayerNumber()) {
            if (slot.getType() == ZoneType.HAND) {
                shape.setLayoutY(shape.getLayoutY() + 30);
            }
        }
    }

    public ImageView getShape() {
        return shape;
    }

    public Image getFace() {
        return face;
    }
}
