package controller.gameplay;

import model.UserData;
import model.gameplay.Player;
import model.gameplay.*;
import model.enums.*;

public class GameManager {
    private Player player1, player2;
    private int turn;
    private Phase currentPhase;
    private GameBoard gameBoard;

    public GameManager(UserData user1, UserData user2) {
        this.player1 = new Player(user1);
        this.player2 = new Player(user2);
        gameBoard = new GameBoard(user1.getActiveDeck(), user2.getActiveDeck());
    }
}
