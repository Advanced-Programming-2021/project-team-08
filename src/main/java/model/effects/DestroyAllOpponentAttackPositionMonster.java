package model.effects;

import model.cards.Card;
import model.cards.MonsterCard;
import model.cards.data.MonsterCardData;
import model.enums.ZoneType;
import model.gameplay.CardSlot;
import model.gameplay.Player;

import java.util.ArrayList;

public class DestroyAllOpponentAttackPositionMonster extends Effect{
    public DestroyAllOpponentAttackPositionMonster(ArrayList<String> args) {
        super(args);
    }

    @Override
    public void activate(Player cardOwner) {
        CardSlot cardSlot;
        MonsterCard monsterCard;
        for (int i=1;i<=5;i++){
            try {
                cardSlot = gameManager.getGameBoard().getCardSlot(true, ZoneType.MONSTER, i);
                monsterCard = (MonsterCard) cardSlot.getCard();
                if (!cardSlot.isEmpty()&&monsterCard.isAttackPosition()) {
                    cardSlot.removeCard();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
