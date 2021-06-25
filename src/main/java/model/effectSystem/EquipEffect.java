package model.effectSystem;

import model.cards.Card;
import model.cards.MonsterCard;

import java.util.ArrayList;

public abstract class EquipEffect extends Effect {
    protected MonsterCard selectedMonster;

    public void setSelectedMonster(MonsterCard selectedMonster) {
        this.selectedMonster = selectedMonster;
    }

    public EquipEffect(ArrayList<String> args) {
        super(args);
    }

    public boolean hasCardCondition(Card card) {
        return true;
    }
}
