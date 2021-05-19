package model.gameplay;

import model.UserData;
import model.cards.Card;

import java.util.ArrayList;

public class Player {
    private UserData userData;
    private int LP = 8000;
    private ArrayList<Card> handCards = new ArrayList<>();
    private PlayerBoard playerBoard;

    public Player(UserData userData, PlayerBoard playerBoard) {
        this.userData = userData;
        this.playerBoard = playerBoard;
    }

    public UserData getUserData() {
        return userData;
    }

    public int getLP() {
        return LP;
    }

    public ArrayList<Card> getHandCards() {
        return handCards;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public void drawCard(){

    }
}
