package model.effectSystem.effects;

import model.effectSystem.Effect;
import model.enums.ZoneType;
import model.gameplay.CardSlot;

import java.util.ArrayList;

public class DestroyAllMonsters extends Effect {
    public DestroyAllMonsters(ArrayList<String> args) {
        super(args);
    }

    @Override
    public void activate() {
        CardSlot cardSlot = null;
        for (int i = 1; i <= 5; i++) {
            try {
                cardSlot = gameManager.getGameBoard().getCardSlot(true, ZoneType.MONSTER, i);
                if (!cardSlot.isEmpty()) {
                    cardSlot.getCard().moveToGraveyard();
                }
                cardSlot = gameManager.getGameBoard().getCardSlot(false, ZoneType.MONSTER, i);
                if (!cardSlot.isEmpty()) {
                    cardSlot.getCard().moveToGraveyard();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("destroyAllMonsters");
    }
}
