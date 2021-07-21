package model.gameplay;

import controller.gameplay.GameManager;
import model.Deck;
import model.enums.ZoneType;

public class GameBoard {
    PlayerBoard player1Board, player2Board;
    GameManager gameManager;

    public GameBoard(Deck user1Deck, Deck user2Deck, GameManager gameManager) {
        player1Board = new PlayerBoard(user1Deck, 1, gameManager);
        player2Board = new PlayerBoard(user2Deck, 2, gameManager);
        this.gameManager = gameManager;
    }

    public PlayerBoard getPlayer1Board() {
        return player1Board;
    }

    public PlayerBoard getPlayer2Board() {
        return player2Board;
    }


    public CardSlot getCardSlot(GameManager.CardSlotAddress address) throws Exception {
        return getCardSlot(address.isForOpponent(), address.getZone(), address.getNumber());
    }

    public CardSlot getCardSlot(boolean forOpponent, ZoneType zone, int number) throws Exception {
        PlayerBoard board;
        if (forOpponent) {
            board = (gameManager.getCurrentPlayerTurn() == 2) ? player1Board : player2Board;
        } else {
            board = (gameManager.getCurrentPlayerTurn() == 1) ? player1Board : player2Board;
        }

        switch (zone) {
            case GRAVEYARD:
                return board.getGraveyard();
            case DECK:
                return board.getDeckZone();
            case FIELD:
                return board.getFieldZone();
            case MONSTER:
                if (number < 1 || number > 5) throw new Exception("number out of bounds");
                return board.getMonsterZone().get(number - 1);
            case SPELL_AND_TRAP:
                if (number < 1 || number > 5) throw new Exception("number out of bounds");
                return board.getSpellAndTrapZone().get(number - 1);
        }
        return null;
    }
}
