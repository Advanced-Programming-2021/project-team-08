package model.effectSystem.effects;

import model.effectSystem.ContinuousEffect;

import java.util.ArrayList;

public class IncreaseLPOnSpellActivate extends ContinuousEffect {
    private int amount;

    public IncreaseLPOnSpellActivate(ArrayList<String> args) {
        super(args);
        amount = Integer.parseInt(args.get(0));
    }

    @Override
    public void setup() {
        super.setup();
        gameManager.getOnAnSpellActivated().addListener((card) -> {
            if (isActive) card.getCardOwner().increaseLP(amount);
        });
    }

    @Override
    public void activate() {
        isActive = true;
    }
}
