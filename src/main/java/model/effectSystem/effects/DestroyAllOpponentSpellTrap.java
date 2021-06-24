package model.effectSystem.effects;

import model.effectSystem.Effect;
import model.enums.ZoneType;
import model.gameplay.CardSlot;

import java.util.ArrayList;

public class DestroyAllOpponentSpellTrap extends Effect {

    public DestroyAllOpponentSpellTrap(ArrayList<String> args) {
        super(args);
    }

    @Override
    public void activate() {
        CardSlot cardSlot;
        for (int i = 0; i < 5; i++) {
            try {
                cardSlot = gameManager.getGameBoard().getCardSlot(true, ZoneType.SPELL_AND_TRAP, i);
                if (cardSlot != null) {
                    CardSlot.moveToGraveyard(cardSlot, gameManager.getGameBoard().getCardSlot(true, ZoneType.GRAVEYARD, 0));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("all opponents spell and traps are destroyed");
    }
}
