package model.effectSystem.effects;

import model.cards.MonsterCard;
import model.effectSystem.Effect;
import model.gameplay.AttackResult;

import java.util.ArrayList;

public class DestroyAttacker extends Effect {
    private AttackResult attackResult;

    public DestroyAttacker(ArrayList<String> args) {
        super(args);
    }

    @Override
    public void setup() {
        super.setup();
        ((MonsterCard) card).getOnAttacked().addListener((attackResult) -> {
            this.attackResult = attackResult;
            activate();
        });
    }

    @Override
    public void activate() {
        if (attackResult.isDestroyCard2()) {
            attackResult.setDestroyCard1(true);
        }
    }
}
