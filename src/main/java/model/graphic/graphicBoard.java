package model.graphic;

import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import model.enums.ZoneType;

import java.util.ArrayList;

public class graphicBoard {
    private GraphicPlayerBoard player1Board;
    private GraphicPlayerBoard player2Board;

    public graphicBoard(AnchorPane board) {
        player1Board = new GraphicPlayerBoard(1, (AnchorPane) board.lookup("#player1Board"));
        player2Board = new GraphicPlayerBoard(2, (AnchorPane) board.lookup("#player2Board"));
    }

    public GraphicPlayerBoard getPlayerBoard(int playerNumber) {
        return playerNumber == 1 ? player1Board : player2Board;
    }

    public class GraphicPlayerBoard {
        private AnchorPane playerBoard;
        private GraphicCardSlot deck;
        private GraphicCardSlot hand;
        private ArrayList<GraphicCardSlot> monster = new ArrayList<>(5);
        private ArrayList<GraphicCardSlot> spell = new ArrayList<>(5);
        private GraphicCardSlot field;
        private GraphicCardSlot graveyard;
        private int playerNumber;

        public GraphicPlayerBoard(int playerNumber, AnchorPane playerBoard) {
            this.playerBoard = playerBoard;
            this.playerNumber = playerNumber;
            deck = new GraphicCardSlot(ZoneType.DECK, playerBoard.lookup("#deck" + playerNumber));
            hand = new GraphicCardSlot(ZoneType.HAND, playerBoard.lookup("#hand" + playerNumber));
            for (int i = 1; i <= 5; i++) {
                monster.add(new GraphicCardSlot(ZoneType.MONSTER, i, playerBoard.lookup("#monster" + playerNumber + "" + i)));
            }
            graveyard = new GraphicCardSlot(ZoneType.GRAVEYARD, playerBoard.lookup("#GY" + playerNumber));;
        }

        public GraphicCardSlot getDeck() {
            return deck;
        }

        public GraphicCardSlot getHand() {
            return hand;
        }

        public GraphicCardSlot getMonster(int number) {
            return monster.get(number - 1);
        }

        public AnchorPane getPlayerBoard() {
            return playerBoard;
        }

        public void moveToGraveyard(int attacker) {
            GraphicCard c = getMonster(attacker).getCard();
            getMonster(attacker).removeCard(c);
            graveyard.appendCard(c);

            TranslateTransition move = new TranslateTransition();
            move.setDuration(Duration.millis(800));
            move.setNode(c.getShape());
            move.setToX(graveyard.getImageView().getLayoutX() + 5);
            move.setToY(graveyard.getImageView().getLayoutY() + 25);
            move.play();
        }
    }
}


