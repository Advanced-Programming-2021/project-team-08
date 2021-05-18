package model.gameplay;

import view.menus.Deck;

public class GameBoard {
    PlayerBoard player1Board, Player2Board;

    public GameBoard(Deck user1Deck, Deck user2Deck) {
        player1Board = new PlayerBoard(user1Deck);
        Player2Board = new PlayerBoard(user2Deck);
    }
}
