package controller;

import java.net.Socket;

public class GameController {
    private Socket player1Socket, player2Socket;

    public GameController(WaitingGame gameData) {
        player1Socket = gameData.getUser1Socket();
        player2Socket = gameData.getUser2Socket();

    }

    public void processClientMessage(String input){

    }
}
