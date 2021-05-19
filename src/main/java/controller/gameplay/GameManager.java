package controller.gameplay;

import model.Command;
import model.UserData;
import model.exceptions.ParseCommandException;
import model.gameplay.Player;
import model.gameplay.*;
import model.enums.*;

import java.util.HashMap;

public class GameManager {
    private final static HashMap<String, CommandFieldType> selectMonsterCardCommand = new HashMap<String, CommandFieldType>() {{
        put("opponent", CommandFieldType.BOOLEAN);
        put("monster", CommandFieldType.INT);
    }};
    private final static HashMap<String, CommandFieldType> selectHandCardCommand = new HashMap<String, CommandFieldType>() {{
        put("hand", CommandFieldType.INT);
    }};

    private Player player1, player2;
    private int turn;
    private Phase currentPhase;
    private GameBoard gameBoard;

    private CardZone currentSelectedZone;

    public GameManager(UserData user1, UserData user2) {
        this.player1 = new Player(user1);
        this.player2 = new Player(user2);
        gameBoard = new GameBoard(user1.getActiveDeck(), user2.getActiveDeck());
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public void selectCard(String address) {
        try{
            Command command = Command.parseCommand(address, selectMonsterCardCommand);
            currentSelectedZone = gameBoard.getZone(Boolean.parseBoolean(command.getField("opponent")),
                    ZoneType.MONSTER,
                    Integer.parseInt(command.getField("monster")));
        } catch (ParseCommandException e) {
            e.printStackTrace();
        }
    }
}
