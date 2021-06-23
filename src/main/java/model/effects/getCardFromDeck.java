package model.effects;

import model.gameplay.Player;

import java.util.ArrayList;

public class getCardFromDeck extends Effect{
    private int n;

    public getCardFromDeck(ArrayList<String> args) {
        super(args);
        this.n = Integer.parseInt(args.get(0));
        System.out.println("getCardFromDeck Con");
    }

    @Override
    public void activate() {
        for (int i=0; i<n; i++){
            card.getCardOwner().drawCard();
        }
        System.out.println("getCardFromDeck " + n);
    }
}

