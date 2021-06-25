package model.effectSystem.effects;

import model.cards.Card;
import model.cards.data.MonsterCardData;
import model.effectSystem.Effect;

import java.util.ArrayList;

public class CancelAttackAndCounterAttack extends Effect {

    MonsterCardData attacker;

    public CancelAttackAndCounterAttack(ArrayList<String> args) {
        super(args);
    }

    @Override
    public void setup() {
        super.setup();
        gameManager.getOnAttack().addListener((attacker) -> {
            this.attacker = (MonsterCardData) attacker.getCardData();
            activate();
        });
    }

    @Override
    public void activate() {
        gameManager.setCanAttack(false);
        gameManager.getCurrentTurnPlayer().decreaseLP(attacker.getAttackPoints());
    }
}
