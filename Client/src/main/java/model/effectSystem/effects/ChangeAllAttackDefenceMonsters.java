package model.effectSystem.effects;

import model.cards.data.MonsterCardData;
import model.effectSystem.FieldEffect;
import model.enums.CardType;
import model.enums.ZoneType;
import model.gameplay.CardSlot;

import java.util.ArrayList;

public class ChangeAllAttackDefenceMonsters extends FieldEffect {

    private int changedAttack;
    private int changedDefence;

    private final ArrayList<String> monsterTypes = new ArrayList<>();

    public ChangeAllAttackDefenceMonsters(ArrayList<String> args) {
        super(args);
        try {
            changedAttack = Integer.parseInt(args.get(0).trim());
            changedDefence = Integer.parseInt(args.get(1).trim());
            for (int i = 2; i < args.size(); i++) {
                monsterTypes.add(args.get(i).trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setup() {
        super.setup();
        activate();
    }

    @Override
    public void activate() {
        card.getOnDestroyEvent().addListener(() -> {
            setAllCardsChangedAttackDefence(false);
        });
        gameManager.getOnSummonACard().addListener((summonerCard) -> {
            if (summonerCard.getCardType().equals(CardType.MONSTER)) {
                MonsterCardData monsterCardData = (MonsterCardData) summonerCard.getCardData();
                changeCardAttackDefence(monsterCardData, 1);
            }
        });
        setAllCardsChangedAttackDefence(true);
    }

    private boolean isSameAttribute(String type) {
        if (monsterTypes.size() == 0) return true;
        for (String monsterType : monsterTypes) {
            if (monsterType.equals(type)) return true;
        }
        return false;
    }

    private void changeCardAttackDefence(MonsterCardData monsterCardData, int sign) {
        if (isSameAttribute(monsterCardData.getMonsterType())) {
            monsterCardData.setChangedAttack(monsterCardData.getChangedAttack() + sign * changedAttack);
            monsterCardData.setChangedDefence(monsterCardData.getChangedDefence() + sign * changedDefence);
        }
    }

    private void setAllCardsChangedAttackDefence(boolean set) {
        int sign = -1;
        if (set) sign = 1;
        for (int i = 0; i < 5; i++) {
            CardSlot cardSlot = null;
            try {
                cardSlot = gameManager.getGameBoard().getCardSlot(true, ZoneType.MONSTER, i);
                if (!cardSlot.isEmpty()) {
                    MonsterCardData monsterCardData = (MonsterCardData) cardSlot.getCard().getCardData();
                    changeCardAttackDefence(monsterCardData, sign);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 5; i++) {
            CardSlot cardSlot = null;
            try {
                cardSlot = gameManager.getGameBoard().getCardSlot(false, ZoneType.MONSTER, i);
                if (!cardSlot.isEmpty()) {
                    MonsterCardData monsterCardData = (MonsterCardData) cardSlot.getCard().getCardData();
                    changeCardAttackDefence(monsterCardData, sign);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
