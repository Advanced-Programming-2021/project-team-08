package model.effectSystem.effects;

import model.cards.MonsterCard;
import model.cards.TrapCard;
import model.cards.data.MonsterCardData;
import model.effectSystem.Effect;

import java.util.ArrayList;

public class DestroyCardOnFlip extends Effect {
    int minAttack;
    MonsterCard summonCard;

    public DestroyCardOnFlip(ArrayList<String> args) {
        super(args);
        try {
            minAttack = Integer.parseInt(args.get(0).trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        gameManager.getOnFlipSummon().addListener((summonCard) -> {
            if (card.getCardOwner().getTrapBanned() > 0) return;
            this.summonCard = (MonsterCard) summonCard;
            if (((MonsterCardData)summonCard.getCardData()).getAttackPoints() >= minAttack) {
                gameManager.temporaryChangeTurn();
                gameManager.getScene().log("now it will be " + card.getCardOwner().getUserData().getUsername() + "'s turn");
                gameManager.getScene().showBoard(gameManager.getGameBoardString());
                if (gameManager.getScene().getActivateTrapCommand()) {
                    ((TrapCard) card).onActivate();
                }
                gameManager.temporaryChangeTurn();
                gameManager.getScene().log("now it will be " + gameManager.getCurrentTurnPlayer().getUserData().getUsername() + "'s turn");
                gameManager.getScene().showBoard(gameManager.getGameBoardString());
            }
        });
    }

    @Override
    public void activate() {
        summonCard.moveToGraveyard();
    }
}
