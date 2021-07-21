package model.effectSystem.effects;

import model.cards.Card;
import model.effectSystem.Effect;
import model.enums.ZoneType;
import model.gameplay.CardSlot;

import java.util.ArrayList;

public class ControlAMonster extends Effect {
    private CardSlot originalPosition;
    private Card getControlledCard;

    public ControlAMonster(ArrayList<String> args) {
        super(args);
    }

    @Override
    public boolean entryCondition() {
        return !card.getCardOwner().getPlayerBoard().isMonsterZoneFull();
    }

    @Override
    public void activate() {
        int placeOfCardInPlayer2 = gameManager.getScene().getPlaceOfMonsterTransfer();
        try {
            originalPosition = gameManager.getGameBoard().getCardSlot(true, ZoneType.MONSTER, placeOfCardInPlayer2);
            getControlledCard = originalPosition.getCard();
            card.getCardOwner().getPlayerBoard().addMonsterCardToZone(getControlledCard);
            originalPosition.removeCard();
            gameManager.getOnChangeTurn().addListener(() -> {
                try {
                    card.getCardSlot().removeCard();
                    originalPosition.setCard(card);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
