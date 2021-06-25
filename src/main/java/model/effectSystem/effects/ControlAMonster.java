package model.effectSystem.effects;

import model.effectSystem.Effect;
import model.enums.ZoneType;
import model.gameplay.CardSlot;

import java.util.ArrayList;

public class ControlAMonster extends Effect {

    public ControlAMonster(ArrayList<String> args) {
        super(args);
    }

    @Override
    public void activate() {
        int placeOfCardInPlayer2 = gameManager.getScene().getPlaceOfMonsterTransfer();
        try {
            CardSlot cardSlot = gameManager.getGameBoard().getCardSlot(true, ZoneType.MONSTER, placeOfCardInPlayer2);
            card.getCardOwner().getPlayerBoard().addMonsterCardToZone(cardSlot.getCard());
            cardSlot.removeCard();
            int placeOfCardInPlayer1 = card.getCardOwner().getPlayerBoard().getPlaceOfCardThatTransfer();


        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
}
