package model.effectSystem.effects;

import model.cards.MonsterCard;
import model.effectSystem.Effect;
import model.enums.CardStatus;
import model.enums.ZoneType;
import model.gameplay.AttackResult;
import model.gameplay.CardSlot;

import java.util.ArrayList;

public class DecreaseOwnerLPOnAttackAnOpponentMonster extends Effect {
    private AttackResult attackResult;
    int amount;

    public DecreaseOwnerLPOnAttackAnOpponentMonster(ArrayList<String> args) {
        super(args);
        amount = Integer.parseInt(args.get(0));
    }
    @Override
    public void setup(){
        super.setup();
        ((MonsterCard) card).getOnAttacked().addListener((attackResult) -> {
            this.attackResult = attackResult;
            activate();
        });
    }
    @Override
    public void activate() {
        if (attackResult.isDestroyCard2()) {
            attackResult.setDestroyCard2(false);
        }
        if (card.getCardStatus().equals(CardStatus.TO_BACK))
            gameManager.getCurrentTurnPlayer().decreaseLP(amount);
    }
}
