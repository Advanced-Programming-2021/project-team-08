package model.graphic;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
        private ArrayList<ImageView> spell = new ArrayList<>(5);
        private ImageView field;
        private ImageView graveyard;
        private int playerNumber;

        public GraphicPlayerBoard(int playerNumber, AnchorPane playerBoard) {
            this.playerBoard = playerBoard;
            this.playerNumber = playerNumber;
            deck = new GraphicCardSlot(ZoneType.DECK, playerBoard.lookup("#deck" + playerNumber));
            hand = new GraphicCardSlot(ZoneType.HAND, playerBoard.lookup("#hand" + playerNumber));
            for (int i = 1; i <= 5; i++) {
                monster.add(new GraphicCardSlot(ZoneType.MONSTER, i, playerBoard.lookup("#monster" + playerNumber + "" + i)));
            }
        }

        public GraphicCardSlot getDeck() {
            return deck;
        }

        public GraphicCardSlot getHand() {
            return hand;
        }

        public ArrayList<GraphicCardSlot> getMonster() {
            return monster;
        }

        public AnchorPane getPlayerBoard() {
            return playerBoard;
        }
    }
}


