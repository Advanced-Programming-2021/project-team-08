package model.effects;

import java.util.ArrayList;

public class increaseLPOnSpellActivate extends Effect{
    public increaseLPOnSpellActivate(ArrayList<String> args) {
        super(args);
        gameManager.getOnAnSpellActivated().addListener(this::activate);
    }

    @Override
    public void activate() {
        card.getCardOwner().increaseLP(500);
    }
}
