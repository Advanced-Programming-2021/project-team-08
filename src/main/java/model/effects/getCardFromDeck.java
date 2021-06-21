package model.effects;

import model.gameplay.Player;

public class getCardFromDeck extends Effect{
    private int n;

    public getCardFromDeck(Object[] args) {
        super(args);
        this.n = (int)args[0];
    }

    @Override
    public void activate(Player cardOwner) {
        for (int i=0; i<n; i++){
            cardOwner.drawCard();
        }
    }
}
