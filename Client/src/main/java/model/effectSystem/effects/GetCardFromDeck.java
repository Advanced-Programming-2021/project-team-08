package model.effectSystem.effects;

import model.effectSystem.Effect;

import java.util.ArrayList;

public class GetCardFromDeck extends Effect {
    private final int n;

    public GetCardFromDeck(ArrayList<String> args) {
        super(args);
        this.n = Integer.parseInt(args.get(0));
        System.out.println("getCardFromDeck Con");
    }

    @Override
    public void activate() {
        card.getCardOwner().drawCard(n, null);

        System.out.println("getCardFromDeck " + n);
    }
}

