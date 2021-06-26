package model.effectSystem.effects;

import model.cards.MonsterCard;
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

    }

    @Override
    public void setup() {
        super.setup();
        gameManager.getOnFlipSummon().addListener((summonCard) -> {
            if (card.getCardOwner().getTrapBanned() > 0) return;
            this.summonCard = (MonsterCard) summonCard;
            if (((MonsterCardData) summonCard.getCardData()).getAttackPoints() >= minAttack) {
                trapActivateQuestion();
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
