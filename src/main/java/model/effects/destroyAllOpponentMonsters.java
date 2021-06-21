package model.effects;

import model.enums.ZoneType;
import model.gameplay.CardSlot;
import model.gameplay.Player;

public class destroyAllOpponentMonsters extends Effect {
    public destroyAllOpponentMonsters(Object[] args) {
        super(args);
    }

    @Override
    public void activate(Player cardOwner) {
        for (int i = 1; i <= 5; i++) {
            CardSlot cardSlot = gameManager.getGameBoard().getCardSlot(true, ZoneType.MONSTER, i);
            if (!cardSlot.isEmpty()) {
                cardSlot.removeCard();
            }
        }
    }
}
