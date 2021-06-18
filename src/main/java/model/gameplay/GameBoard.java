package model.gameplay;

import controller.gameplay.GameManager;
import model.enums.ZoneType;
import model.Deck;

public class GameBoard {
    PlayerBoard player1Board, player2Board;
    GameManager gameManager;

    public GameBoard(Deck user1Deck, Deck user2Deck, GameManager gameManager) {
        player1Board = new PlayerBoard(user1Deck);
        player2Board = new PlayerBoard(user2Deck);
        this.gameManager = gameManager;
    }

    public PlayerBoard getPlayer1Board() {
        return player1Board;
    }

    public PlayerBoard getPlayer2Board() {
        return player2Board;
    }


    public CardSlot getCardSlot(boolean forOpponent, ZoneType zone, int number) {
        PlayerBoard board;
        if (forOpponent) {
            board = (gameManager.getCurrentPlayerTurn() == 2) ? player1Board : player2Board;
        } else {
            board = (gameManager.getCurrentPlayerTurn() == 1) ? player1Board : player2Board;
        }

        switch (zone) {
            case GRAVEYARD:
                return board.getGraveyard();
            case FIELD:
                break;
            case MONSTER:
                return board.getMonsterZone().get(number - 1);
            case SPELL_AND_TRAP:
                return board.getSpellAndTrapZone().get(number - 1);
        }
        return null;
    }
}
