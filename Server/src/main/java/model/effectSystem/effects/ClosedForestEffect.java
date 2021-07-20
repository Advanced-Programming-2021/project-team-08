package model.effectSystem.effects;

import model.cards.data.MonsterCardData;
import model.effectSystem.FieldEffect;
import model.enums.CardType;
import model.enums.ZoneType;
import model.gameplay.CardSlot;

import java.util.ArrayList;

public class ClosedForestEffect extends FieldEffect {

    private int changedAttack;
    private int changedDefence;
    private ArrayList<String> monsterTypes = new ArrayList<>();

    public ClosedForestEffect(ArrayList<String> args) {
        super(args);
        changedAttack = 100;
        changedDefence = 0;
        monsterTypes.add("Beast-Type");
    }

    @Override
    public void setup() {
        super.setup();
        activate();
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

    @Override
    public void activate() {
        card.getOnDestroyEvent().addListener(() -> {
            setAllOwnCardsChangedAttackDefence(false);
        });
        gameManager.getOnSummonACard().addListener((summonerCard) -> {
            if (summonerCard.getCardType().equals(CardType.MONSTER) && summonerCard.getCardOwner().equals(card.getCardOwner())) {
                MonsterCardData monsterCardData = (MonsterCardData) summonerCard.getCardData();
                changeCardAttackDefence(monsterCardData, 1);
            }
        });
        setAllOwnCardsChangedAttackDefence(true);
    }

    private void setAllOwnCardsChangedAttackDefence(boolean set) {
        int sign = -1;
        if (set) sign = 1;
        boolean forOpponent = !gameManager.getCurrentTurnPlayer().equals(card.getCardOwner());
        for (int i = 0; i < 5; i++) {
            CardSlot cardSlot = null;
            try {
                cardSlot = gameManager.getGameBoard().getCardSlot(forOpponent, ZoneType.MONSTER, i);
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
