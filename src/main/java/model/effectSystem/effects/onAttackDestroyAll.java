package model.effectSystem.effects;

import model.cards.MonsterCard;
import model.effectSystem.Effect;
import model.enums.CardStatus;
import model.enums.ZoneType;
import model.gameplay.CardSlot;

import java.util.ArrayList;

public class onAttackDestroyAll extends Effect {
    public onAttackDestroyAll(ArrayList<String> args) {
        super(args);
    }

    @Override
    public void setup() {
        super.setup();
        gameManager.getOnWantAttack().addListener((attacker) -> {
            if (!attacker.equals(card.getCardOwner())) {
                gameManager.temporaryChangeTurn();
                gameManager.getScene().log("now it will be " + card.getCardOwner().getUserData().getUsername() + "'s turn");
                gameManager.getScene().showBoard(gameManager.getGameBoardString());
                if (gameManager.getScene().getActivateTrapCommand()) {
                    activate();
                }
                gameManager.temporaryChangeTurn();
                gameManager.getScene().log("now it will be " + attacker.getUserData().getUsername() + "'s turn");
                gameManager.getScene().showBoard(gameManager.getGameBoardString());
            }
        });
    }

    @Override
    public void activate() {
        card.setCardStatus(CardStatus.FACE_UP);
        CardSlot cardSlot;
        for (int i = 1; i <= 5; i++) {
            try {
                cardSlot = gameManager.getGameBoard().getCardSlot(true, ZoneType.MONSTER, i);
                if (!cardSlot.isEmpty()) {
                    MonsterCard monsterCard = (MonsterCard) cardSlot.getCard();
                    if (monsterCard.isAttackPosition()) {
                        CardSlot.moveToGraveyard(cardSlot, gameManager.getGameBoard().getCardSlot(true, ZoneType.GRAVEYARD, 0));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        CardSlot.moveToGraveyard(card.getCardSlot(), card.getCardOwner().getPlayerBoard().getGraveyard());
    }
}
