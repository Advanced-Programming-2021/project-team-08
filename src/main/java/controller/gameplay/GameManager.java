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
        gameBoard = new GameBoard(user1.getActiveDeck(), user2.getActiveDeck());
        this.player1 = new Player(user1, gameBoard.getPlayer1Board());
        this.player2 = new Player(user2, gameBoard.getPlayer2Board());

        turn = 1;
        System.out.println(getGameBoardString());
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public void selectCard(String address) {
        try {
            Command command = Command.parseCommand(address, selectMonsterCardCommand);
            currentSelectedZone = gameBoard.getZone(Boolean.parseBoolean(command.getField("opponent")),
                    ZoneType.MONSTER,
                    Integer.parseInt(command.getField("monster")));
        } catch (ParseCommandException e) {
            e.printStackTrace();
        }
    }

    public String getGameBoardString() {
        String toShow = "";
        if (turn % 2 == 1) {
            toShow += player2.getUserData().getNickname() + ":" + player2.getLP() + "\n";

            for(int i=7; i>player2.getHandCards().size(); i--){toShow += "\t";}
            for(int i=0; i<player2.getHandCards().size(); i++){toShow += "c\t";}
            toShow += "\n";

            toShow += player2.getPlayerBoard().getShowingString(false);

            toShow += "-----------------------------\n";

            toShow += player1.getPlayerBoard().getShowingString(true);

            for(int i=0; i<player1.getHandCards().size(); i++){toShow += "c\t";}
            toShow += "\n";

            toShow += player1.getUserData().getNickname() + ":" + player1.getLP() + "\n";
        }

        return toShow;
    }
}
