package model.effectSystem.effects;

import model.cards.Card;
import model.cards.MonsterCard;
import model.cards.data.MonsterCardData;
import model.effectSystem.Effect;
import model.enums.CardStatus;
import model.enums.ZoneType;
import model.gameplay.CardSlot;
import model.gameplay.Player;

import java.util.ArrayList;

public class DetermineTheAttackPower extends Effect {
    int amount;
    public DetermineTheAttackPower(ArrayList<String> args) {
        super(args);
        amount = Integer.parseInt(args.get(0));
    }

    @Override
    public void activate() {
        int totalLevel=0;
        CardSlot cardSlot = null;
        MonsterCard monsterCard=null;
        MonsterCardData monsterCardData=null;
        for (int i = 1; i <= 5; i++) {
            try {
                cardSlot =gameManager.getGameBoard().getCardSlot(true, ZoneType.MONSTER, i);
                if (!cardSlot.isEmpty()) {
                    if (cardSlot.getCard().getCardStatus().equals(CardStatus.FACE_UP)) {
                        monsterCard = (MonsterCard) cardSlot.getCard();;
                        monsterCardData = (MonsterCardData) monsterCard.getCardData();
                        totalLevel += monsterCardData.getLevel();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ((MonsterCardData)((MonsterCard) card).getCardData()).setAttackPoints(totalLevel*amount);
    }
}
