package model.graphic;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
        private ArrayList<GraphicCard> deckCards = new ArrayList<>();
        private ArrayList<GraphicCard> handCards = new ArrayList<>();
        private ImageView deck;
        private Group hand;
        private ArrayList<ImageView> monster = new ArrayList<>(5);
        private ArrayList<ImageView> spell = new ArrayList<>(5);
        private ImageView field;
        private ImageView graveyard;
        private int playerNumber;

        public GraphicPlayerBoard(int playerNumber, AnchorPane playerBoard) {
            this.playerBoard = playerBoard;
            this.playerNumber = playerNumber;
            deck = (ImageView) playerBoard.lookup("#deck" + playerNumber);
            hand = (Group) playerBoard.lookup("#hand" + playerNumber);
            for (int i = 0; i < 5; i++) {
                monster.add((ImageView) playerBoard.lookup("#monster" + playerNumber + "" + i));
            }
        }

        public ImageView getDeck() {
            return deck;
        }

        public Group getHand() {
            return hand;
        }

        public ArrayList<GraphicCard> getDeckCards() {
            return deckCards;
        }

        public ArrayList<GraphicCard> getHandCards() {
            return handCards;
        }

        public AnchorPane getPlayerBoard() {
            return playerBoard;
        }
    }
}


