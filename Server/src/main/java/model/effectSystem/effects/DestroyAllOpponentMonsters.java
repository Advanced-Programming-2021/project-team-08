package model.effectSystem.effects;

import model.effectSystem.Effect;
import model.enums.ZoneType;
import model.gameplay.CardSlot;

import java.util.ArrayList;

public class DestroyAllOpponentMonsters extends Effect {
    public DestroyAllOpponentMonsters(ArrayList<String> args) {
        super(args);
    }

    @Override
    public void activate() {
        for (int i = 1; i <= 5; i++) {
            CardSlot cardSlot = null;
            try {
                cardSlot = gameManager.getGameBoard().getCardSlot(true, ZoneType.MONSTER, i);
                if (!cardSlot.isEmpty()) {
                    cardSlot.getCard().moveToGraveyard();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("destroyAllOpponentMonsters");
    }
}
