package model.effectSystem.effects;

import model.cards.TrapCard;
import model.effectSystem.CounterEffect;
import model.gameplay.AttackResult;

import java.util.ArrayList;

public class CancelAttackAndEndTurn extends CounterEffect {

    AttackResult attackResult;

    public CancelAttackAndEndTurn(ArrayList<String> args) {
        super(args);
    }

    @Override
    public void setup() {
        super.setup();
        gameManager.getOnWantAttack().addListener((attackResult -> {
            if (card.getCardOwner().getTrapBanned() > 0) return;
            this.attackResult = attackResult;
            gameManager.temporaryChangeTurn();
            gameManager.getScene().log("now it will be " + card.getCardOwner().getUserData().getUsername() + "'s turn");
            gameManager.getScene().showBoard(gameManager.getGameBoardString());
            if (gameManager.getScene().getActivateTrapCommand()) {
                ((TrapCard) card).onActivate();
            }
            gameManager.temporaryChangeTurn();
            gameManager.getScene().log("now it will be " + attackResult.getAttackerPlayer().getUserData().getUsername() + "'s turn");
            gameManager.getScene().showBoard(gameManager.getGameBoardString());
        }));
    }

    @Override
    public void activate() {
        attackResult.cancel();
        gameManager.goToNextPhase();
    }
}
