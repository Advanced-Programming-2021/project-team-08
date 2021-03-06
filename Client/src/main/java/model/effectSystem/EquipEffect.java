package model.effectSystem;

import model.cards.MonsterCard;
import model.cards.data.MonsterCardData;

import java.util.ArrayList;

public abstract class EquipEffect extends Effect {
    protected MonsterCard selectedMonster;

    public EquipEffect(ArrayList<String> args) {
        super(args);
    }

    public void setSelectedMonster(MonsterCard selectedMonster) {
        this.selectedMonster = selectedMonster;
    }

    public boolean hasCardCondition(MonsterCardData monsterCardData) {
        return true;
    }
}
