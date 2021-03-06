package model.effectSystem.effects;

import model.cards.MonsterCard;
import model.effectSystem.Effect;
import model.enums.ZoneType;
import model.gameplay.CardSlot;

import java.util.ArrayList;

public class OnAttackDestroyAll extends Effect {
    public OnAttackDestroyAll(ArrayList<String> args) {
        super(args);
    }

    @Override
    public void setup() {
        super.setup();
        gameManager.getOnWantAttack().addListener((attackResult) -> {
            if (!attackResult.getAttackerPlayer().equals(card.getCardOwner())) {
                trapActivateQuestion();
                gameManager.getScene().log("now it will be " + attackResult.getAttackerPlayer().getUserData().getUsername() + "'s turn");
                gameManager.getScene().showBoard(gameManager.getGameBoardString());
            }
        });
    }

    @Override
    public void activate() {
        CardSlot cardSlot;
        for (int i = 1; i <= 5; i++) {
            try {
                cardSlot = gameManager.getGameBoard().getCardSlot(true, ZoneType.MONSTER, i);
                if (!cardSlot.isEmpty()) {
                    MonsterCard monsterCard = (MonsterCard) cardSlot.getCard();
                    if (monsterCard.isAttackPosition()) {
                        monsterCard.moveToGraveyard();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        card.moveToGraveyard();
    }
}
