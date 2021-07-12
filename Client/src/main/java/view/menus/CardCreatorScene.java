package view.menus;

import controller.ApplicationManger;
import controller.CardCreatorController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.cards.data.CardData;
import model.cards.data.MonsterCardData;
import model.effectSystem.Effect;

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
    public Label cardPrice;
    public ChoiceBox monsterCardType;
    private CardCreatorController controller;
    private HashMap<CardData, Effect> effects = new HashMap<>();

    public CardCreatorScene() {
        this.controller = new CardCreatorController(this);
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
        for (CardData cardData : CardData.getAllCardData()) {
            if (cardData.getEffects().size() > 0 && cardData instanceof MonsterCardData) {
                System.out.println("card name is :" + cardData.getName() + " card type is : " + cardData.getCardType());
                for (Effect effect : cardData.getEffects()) {
                    System.out.println(effect.getClass().getSimpleName());
                    effectPane.getChildren().add(effectPane.getChildren().size(), setEffectLabel(effect, cardData, index));
                    index++;
                }
                System.out.println("---------------------------");
            }
        }
        effectPane.setPrefHeight(index * 60);
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
            updateCost(null);
        });
        return label;
    }

    private void setNumberTextField(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.length() > 4) {
                textField.setText(newValue.substring(0, 4));
                message.setText("maximum number of attack and defence is 4999");
            }
            if (newValue.length() == 4 && !newValue.matches("[01234]\\d\\d\\d")) {
                textField.setText(newValue.substring(0, 3));
                message.setText("maximum number of attack and defence is 4999");
            }
        });
    }


    public void createCard(ActionEvent actionEvent) {
        controller.createCard(defenceField.getText(), attackField.getText(), effects, nameField.getText(), cardDescription.getText(), (String) cardAttribute.getValue(), (String) cardType.getValue(), (String) monsterCardType.getValue());
        try {
            System.out.println("attack is : " + attackField.getText() + "  defence is : " + defenceField.getText() + " card name is: " + nameField.getText());
            System.out.println("attribute is : " + cardAttribute.getValue() + " card type is : " + cardType.getValue());
            System.out.println("description is : " + cardDescription.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    public void updateCost(KeyEvent keyEvent) {
        int cost = controller.getCost(defenceField.getText(), attackField.getText(), effects);
        int level = controller.levelCalculator(defenceField.getText(), attackField.getText());
        cardPrice.setText(String.valueOf(cost));
        this.level.setText(String.valueOf(level));
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

    public void setMessage(String message) {
        this.message.setText(message);
    }
}
