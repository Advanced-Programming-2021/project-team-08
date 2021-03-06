package model.effectSystem.effects;

import model.cards.data.MonsterCardData;
import model.effectSystem.EquipEffect;

import java.util.ArrayList;

public class EquipAMonsterAttackDefence extends EquipEffect {
    ArrayList<String> monsterTypes = new ArrayList<>();
    private final int changeAttack;
    private final int changeDefence;

    public EquipAMonsterAttackDefence(ArrayList<String> args) {
        super(args);
        changeAttack = Integer.parseInt(args.get(0));
        changeDefence = Integer.parseInt(args.get(1));
        for (int i = 2; i < args.size(); i++) {
            monsterTypes.add(args.get(i));
        }
    }

    @Override
    public boolean hasCardCondition(MonsterCardData monsterCardData) {
        if (monsterTypes.size() == 0) return true;
        return hasMonsterWithTheType(monsterCardData);
    }

    @Override
    public boolean entryCondition() {
        if (monsterTypes.size() == 0) return true;
        for (int i = 0; i < 5; i++) {
            MonsterCardData monsterCardData = (MonsterCardData) card.getCardOwner().getPlayerBoard().getMonsterZone().get(i).getCard().getCardData();
            if (monsterCardData != null) if (hasMonsterWithTheType(monsterCardData)) return true;
        }
        return false;
    }

    @Override
    public void setup() {
        super.setup();
        card.getOnDestroyEvent().addListener(() -> {
            setChangeAttackDefence(-1);
        });
        selectedMonster.getOnDestroyEvent().addListener(() -> {
            card.moveToGraveyard();
        });
    }

    @Override
    public void activate() {
        setChangeAttackDefence(1);
    }

    private boolean hasMonsterWithTheType(MonsterCardData monsterCardData) {
        for (String monsterType : monsterTypes) {
            if (monsterType.equals(monsterCardData.getMonsterType())) return true;
        }
        return false;
    }

    private void setChangeAttackDefence(int sign) {
        ((MonsterCardData) selectedMonster.getCardData()).setChangedAttack(sign * changeAttack);
        ((MonsterCardData) selectedMonster.getCardData()).setChangedDefence(sign * changeDefence);
    }
}
