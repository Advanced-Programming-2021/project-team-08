package model.effectSystem.effects;

import model.cards.Card;
import model.cards.MonsterCard;
import model.effectSystem.ContinuousEffect;
import model.gameplay.CardSlot;

import java.util.ArrayList;

public class SummonACardFromGraveyard extends ContinuousEffect {
    public SummonACardFromGraveyard(ArrayList<String> args) {
        super(args);
    }

    @Override
    public void activate() {
        Card card1=card.getCardOwner().getPlayerBoard().getGraveyard().drawParticularMonster(null);
        MonsterCard monsterCard=(MonsterCard)card1;
        card.getCardOwner().getPlayerBoard().addMonsterCardToZone(card1);
        monsterCard.onSummon();
    }
}
