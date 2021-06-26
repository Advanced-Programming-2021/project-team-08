package model.effectSystem.effects;

import model.cards.Card;
import model.cards.MonsterCard;
import model.cards.data.MonsterCardData;
import model.effectSystem.EquipEffect;
import model.gameplay.Player;

import java.util.ArrayList;

public class EquipAMonsterAttackDefence extends EquipEffect {
    private int changeAttack;
    private int changeDefence;
    ArrayList<String> monsterTypes = new ArrayList<>();
    public EquipAMonsterAttackDefence(ArrayList<String> args) {
        super(args);
        changeAttack = Integer.parseInt(args.get(0));
        changeDefence = Integer.parseInt(args.get(1));
        int i = 2;
        while (args.get(i) != null) {
            monsterTypes.add(args.get(i));
            i++;
        }
    }

    @Override
    public boolean hasCardCondition(MonsterCardData monsterCardData) {
        if (monsterTypes.size() == 0) return true;
        return hasMonsterWithTheType(monsterCardData);
    }

    @Override
    public boolean entryCondition(Player player) {
        if (monsterTypes.size() == 0) return true;
        for (int i = 0; i < 5; i++) {
            MonsterCardData monsterCardData = (MonsterCardData)player.getPlayerBoard().getMonsterZone().get(i).getCard().getCardData();
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
        ((MonsterCardData)selectedMonster.getCardData()).setChangedAttack(sign * changeAttack);
        ((MonsterCardData)selectedMonster.getCardData()).setChangedDefence(sign * changeDefence);
    }
}
