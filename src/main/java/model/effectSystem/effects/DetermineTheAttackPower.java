package model.effectSystem.effects;

import model.cards.Card;
import model.cards.MonsterCard;
import model.cards.data.MonsterCardData;
import model.effectSystem.Effect;
import model.enums.CardStatus;
import model.enums.ZoneType;
import model.gameplay.CardSlot;
import model.gameplay.Player;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class DetermineTheAttackPower extends Effect {
    int amount;

    public DetermineTheAttackPower(ArrayList<String> args) {
        super(args);
        amount = Integer.parseInt(args.get(0));
    }

    @Override
    public void setup() {
        super.setup();
        activate();
    }

    @Override
    public void activate() {
        ((MonsterCard) card).getData().setCalculatedAttackPoint(true);
        ((MonsterCard) card).getData().setCalculateAttackMethod(() -> {
            int totalLevel = 0;
            CardSlot cardSlot;
            MonsterCard monsterCard;
            MonsterCardData monsterCardData;
            for (int i = 1; i <= 5; i++) {
                try {
                    cardSlot = gameManager.getGameBoard().getCardSlot(true, ZoneType.MONSTER, i);
                    if (!cardSlot.isEmpty()) {
                        if (cardSlot.getCard().getCardStatus().equals(CardStatus.FACE_UP)) {
                            monsterCard = (MonsterCard) cardSlot.getCard();
                            monsterCardData = (MonsterCardData) monsterCard.getCardData();
                            totalLevel += monsterCardData.getLevel();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return totalLevel * amount;
        });
    }
}
