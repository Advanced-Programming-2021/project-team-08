package model.effectSystem.effects;

import model.cards.Card;
import model.cards.TrapCard;
import model.cards.data.MonsterCardData;
import model.effectSystem.Effect;
import model.gameplay.AttackResult;
import model.gameplay.Player;

import java.util.ArrayList;

public class CancelAttackAndCounterAttack extends Effect {

    private MonsterCardData attacker;
    private AttackResult attackResult;

    public CancelAttackAndCounterAttack(ArrayList<String> args) {
        super(args);
    }

    @Override
    public void setup() {
        super.setup();
        gameManager.getOnWantAttack().addListener((attackResult) -> {
            trapActivateQuestion();
            gameManager.getScene().log("now it will be " + attackResult.getAttackerPlayer().getUserData().getUsername() + "'s turn");
            gameManager.getScene().showBoard(gameManager.getGameBoardString());
            this.attacker = (MonsterCardData) attackResult.getAttacker().getCardData();
            this.attackResult = attackResult;
        });
    }

    @Override
    public void activate() {
        attackResult.cancel();
        gameManager.getCurrentTurnPlayer().decreaseLP(attacker.getAttackPoints());
    }

}
