package model.gameplay;

import model.UserData;
import model.cards.Card;

import java.util.ArrayList;

public class Player {
    UserData userData;
    int LP = 4000;
    ArrayList<Card> handCards = new ArrayList<>();

    public Player(UserData userData) {
        this.userData = userData;
    }
}
