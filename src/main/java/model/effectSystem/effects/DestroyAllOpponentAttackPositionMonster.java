package model.effectSystem.effects;

import model.cards.MonsterCard;
import model.effectSystem.Effect;
import model.enums.ZoneType;
import model.gameplay.CardSlot;

import java.util.ArrayList;

public class DestroyAllOpponentAttackPositionMonster extends Effect {
    public DestroyAllOpponentAttackPositionMonster(ArrayList<String> args) {
        super(args);
    }

    @Override
    public void activate() {
        CardSlot cardSlot;
        MonsterCard monsterCard;
        for (int i=1;i<=5;i++){
            try {
                cardSlot = gameManager.getGameBoard().getCardSlot(true, ZoneType.MONSTER, i);
                monsterCard = (MonsterCard) cardSlot.getCard();
                if (!cardSlot.isEmpty()&&monsterCard.isAttackPosition()) {
                    CardSlot.moveToGraveyard(cardSlot,card.getCardOwner().getPlayerBoard().getGraveyard());
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
