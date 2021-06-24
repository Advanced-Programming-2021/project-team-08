package model.effectSystem.effects;

import model.effectSystem.Effect;
import model.enums.ZoneType;
import model.gameplay.CardSlot;

import java.util.ArrayList;

public class destroyAllOpponentMonsters extends Effect {
    public destroyAllOpponentMonsters(ArrayList<String> args) {
        super(args);
    }

    @Override
    public void activate() {
        for (int i = 1; i <= 5; i++) {
            CardSlot cardSlot = null;
            try {
                cardSlot = gameManager.getGameBoard().getCardSlot(true, ZoneType.MONSTER, i);
                if (!cardSlot.isEmpty()) {
                    CardSlot.moveToGraveyard(cardSlot, gameManager.getGameBoard().getCardSlot(true, ZoneType.GRAVEYARD, 0));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("destroyAllOpponentMonsters");
    }
}
