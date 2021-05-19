package model.gameplay;

import model.cards.Card;
import model.enums.ZoneType;
import view.menus.Deck;

public class GameBoard {
    PlayerBoard player1Board, player2Board;

    public GameBoard(Deck user1Deck, Deck user2Deck) {
        player1Board = new PlayerBoard(user1Deck);
        player2Board = new PlayerBoard(user2Deck);
    }

    public PlayerBoard getPlayer1Board() {
        return player1Board;
    }

    public PlayerBoard getPlayer2Board() {
        return player2Board;
    }

    public CardZone getZone(boolean forOpponent, ZoneType zone, int number) {
        switch (zone) {
            case FIELD:
                break;
            case MONSTER:
                if (!forOpponent) {
                    return player1Board.getMonsterZone().get(number - 1);
                } else {
                    return player2Board.getMonsterZone().get(number - 1);
                }
            case SPELL_AND_TRAP:
                break;
        }
        return null;
    }
}
