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
            gameManager.temporaryChangeTurn();
            gameManager.getScene().log("now it will be " + card.getCardOwner().getUserData().getUsername() + "'s turn");
            gameManager.getScene().showBoard(gameManager.getGameBoardString());
            if (gameManager.getScene().getActivateTrapCommand()) {
                ((TrapCard) card).onActivate();
            }
            gameManager.temporaryChangeTurn();
            gameManager.getScene().log("now it will be " + getOpponentPlayer().getUserData().getUsername() + "'s turn");
            gameManager.getScene().showBoard(gameManager.getGameBoardString());
            this.attacker = (MonsterCardData) attackResult.getAttacker().getCardData();
            this.attackResult = attackResult;
            activate();
        });
    }

    @Override
    public void activate() {
        attackResult.cancel();
        gameManager.getCurrentTurnPlayer().decreaseLP(attacker.getAttackPoints());
    }

    private Player getOpponentPlayer() {
        if (card.getCardOwner().equals(gameManager.getCurrentTurnPlayer()))
            return gameManager.getCurrentTurnPlayer();
        else return gameManager.getCurrentTurnOpponentPlayer();
    }
}
