package model.effectSystem.effects;

import model.effectSystem.ContinuousEffect;
import model.effectSystem.Effect;

import java.util.ArrayList;

public class increaseLPOnSpellActivate extends ContinuousEffect {
    private int amount;

    public increaseLPOnSpellActivate(ArrayList<String> args) {
        super(args);
        amount = Integer.parseInt(args.get(0));
        gameManager.getOnAnSpellActivated().addListener(()->{
            if(isActive) card.getCardOwner().increaseLP(amount);
        });
    }

    @Override
    public void activate() {
        isActive = true;
    }
}
