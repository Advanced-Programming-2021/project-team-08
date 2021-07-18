package view;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.cards.data.CardData;

public class CardView {
    public AnchorPane anchorPane;
    public ImageView cardImage;
    public Label description;


    private static CardView instance;

    public static CardView getInstance() {
        return instance;
    }

    public void initialize(){
        instance = this;
    }

    public void showCard(CardData cardData){
        cardImage.setImage(cardData.getCardImage());
        description.setText(cardData.toString());
    }

    public void init(CardData cardData, double height, double width) {
        anchorPane.setPrefHeight(height * 3 / 2);
        anchorPane.setPrefWidth(width);
        ImageView imageView = new ImageView();
        imageView.setImage(cardData.getCardImage());
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        anchorPane.getChildren().add(0, imageView);
        anchorPane.getChildren().add(1, createScrollPane(cardData, height, width));
    }

    private ScrollPane createScrollPane(CardData cardData, double height, double width) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(createDescriptionLabel(cardData, height, width));
        scrollPane.setPrefHeight(height / 2);
        scrollPane.setPrefWidth(width);
        scrollPane.setLayoutY(height);
        return scrollPane;
    }

    private Label createDescriptionLabel(CardData cardData, double height, double width) {
        Label label = new Label();
        label.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-font-size: 20; -fx-text-fill: darkred;");
        label.setWrapText(true);
        label.setPrefWidth(width);
        label.setText(cardData.getCardDescription());
        return label;
    }


    public AnchorPane getAnchorPane() {
        return anchorPane;
    }
}


