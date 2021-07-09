package view.menus;

import controller.ApplicationManger;
import controller.CardCreatorController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.cards.Card;
import model.cards.data.CardData;
import model.cards.data.MonsterCardData;
import model.effectSystem.Effect;
import model.enums.CardType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class CardCreatorScene extends Scene {
    public TextField nameField;
    public TextField attackField;
    public TextField defenceField;
    public ChoiceBox cardAttribute;
    public ChoiceBox cardType;
    public TextArea cardDescription;
    public Label level;
    public AnchorPane effectPane;
    public Label effectLabel;
    public ScrollPane effectScroll;
    public Label message;
    private CardCreatorController controller;
    private HashMap<CardData, Effect> effects = new HashMap<>();

    public CardCreatorScene() {
        this.controller = new CardCreatorController();
    }

    @Override
    protected int getUserCommand() {
        return 0;
    }

    @FXML
    public void initialize() {
        setNumberTextField(attackField);
        setNumberTextField(defenceField);
        effectLabel.setPrefHeight(200);
        setEffects();
    }

    private void setEffects() {
        int index = 0;
        for (CardData cardData :  CardData.getAllCardData()) {
            if (cardData.getEffects().size() > 0 && cardData instanceof MonsterCardData) {
                System.out.println("card name is :" + cardData.getName() + " card type is : " + cardData.getCardType());
                for (Effect effect : cardData.getEffects()) {
                    System.out.println(effect.getClass().getSimpleName());
                    effectPane.getChildren().add(effectPane.getChildren().size(), setEffectLabel(effect, cardData,index));
                    index++;
                }
                System.out.println("---------------------------");
            }
        }
        effectPane.setPrefHeight(index * 60 );
    }

    private Label setEffectLabel(Effect effect, CardData cardData, int index) {
        Label label = new Label(effect.getClass().getSimpleName());
        label.setPrefHeight(50);
        label.setPrefWidth(500);
        label.setLayoutY(index * 60);
        label.setLayoutX(20);
        label.setFont(new Font(20));
        label.setTextFill(Color.BLUE);
        label.setCursor(Cursor.HAND);
        label.setOnMouseEntered(event -> {
            label.setTextFill(Color.DARKBLUE);
        });
        label.setOnMouseExited(event -> {
            label.setTextFill(Color.BLUE);
        });
        label.setOnMouseClicked(event -> {
            label.setDisable(true);
            effects.put(cardData, effect);
            effectLabel.setText(effectLabel.getText() + "\n " + effect.getClass().getSimpleName());
        });
        return label;
    }

    private void setNumberTextField(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }


    public void createCard(ActionEvent actionEvent) {
    }

    public void updateCost(KeyEvent keyEvent) {
        //int cost = controller.getCost()
    }

    public void back(ActionEvent actionEvent) {
        ApplicationManger.goToScene("cardCreatingOptions.fxml");
    }

    public void addEffect(ActionEvent actionEvent) {
        effectScroll.setVisible(true);
    }

    public void cancelEffect(ActionEvent actionEvent) {
        effectScroll.setVisible(false);
    }
}
