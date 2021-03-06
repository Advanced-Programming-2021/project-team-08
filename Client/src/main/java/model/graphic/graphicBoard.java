package model.graphic;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import model.animation.RotateCenterTransition;
import model.enums.ZoneType;
import view.menus.GamePlayScene;

import java.util.ArrayList;

public class graphicBoard {
    private final GraphicPlayerBoard player1Board;
    private final GraphicPlayerBoard player2Board;

    public graphicBoard(AnchorPane board) {
        player1Board = new GraphicPlayerBoard(1, (AnchorPane) board.lookup("#player1Board"));
        player2Board = new GraphicPlayerBoard(2, (AnchorPane) board.lookup("#player2Board"));
    }

    public GraphicPlayerBoard getPlayerBoard(int playerNumber) {
        return playerNumber == 1 ? player1Board : player2Board;
    }

    public GraphicCardSlot getCardSlot(boolean forOpponent, ZoneType zone, int number) throws Exception {
        GraphicPlayerBoard board;
        if (forOpponent) {
            board = (GamePlayScene.getInstance().getPlayerNumber() == 2) ? player1Board : player2Board;
        } else {
            board = (GamePlayScene.getInstance().getPlayerNumber() == 1) ? player1Board : player2Board;
        }

        switch (zone) {
            case DECK:
                return board.getDeck();
            case MONSTER:
                if (number < 1 || number > 5) throw new Exception("number out of bounds");
                return board.getMonster(number);
        }
        return null;
    }

    public class GraphicPlayerBoard {
        private final AnchorPane playerBoard;
        private final GraphicCardSlot deck;
        private final GraphicCardSlot hand;
        private final ArrayList<GraphicCardSlot> monster = new ArrayList<>(5);
        private final ArrayList<GraphicCardSlot> spell = new ArrayList<>(5);
        private GraphicCardSlot field;
        private final GraphicCardSlot graveyard;
        private final int playerNumber;

        public GraphicPlayerBoard(int playerNumber, AnchorPane playerBoard) {
            this.playerBoard = playerBoard;
            this.playerNumber = playerNumber;
            deck = new GraphicCardSlot(ZoneType.DECK, playerBoard.lookup("#deck" + playerNumber));
            hand = new GraphicCardSlot(ZoneType.HAND, playerBoard.lookup("#hand" + playerNumber));
            for (int i = 1; i <= 5; i++) {
                monster.add(new GraphicCardSlot(ZoneType.MONSTER, i, playerBoard.lookup("#monster" + playerNumber + "" + i)));
            }
            graveyard = new GraphicCardSlot(ZoneType.GRAVEYARD, playerBoard.lookup("#GY" + playerNumber));
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

            if (!c.isToAttackPosition()) {
                RotateCenterTransition rotateTransition = new RotateCenterTransition(c.getShape(), 800, 90, Rotate.Z_AXIS);
                ParallelTransition parallelTransition = new ParallelTransition();
                parallelTransition.getChildren().add(move);
                parallelTransition.getChildren().add(rotateTransition);
                parallelTransition.play();
            } else {
                move.play();
            }
        }
    }
}


