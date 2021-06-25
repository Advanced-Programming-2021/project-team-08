package model.effectSystem.effects;

import model.cards.TrapCard;
import model.effectSystem.Effect;
import model.enums.ZoneType;
import model.gameplay.CardSlot;

import java.util.ArrayList;

public class DestroyAllMonstersOnSummonAMonster extends Effect {
    public DestroyAllMonstersOnSummonAMonster(ArrayList<String> args) {
        super(args);
    }

    @Override
    public void setup(){
        super.setup();
        gameManager.getOnSummonACard().addListener((summonCard)->{
            if (gameManager.getScene().getActivateTrapCommand()) {
                ((TrapCard) card).onActivate();
            }
            activate();
        });
    }


    @Override
    public void activate() {
        for (int i = 1; i <= 5; i++) {
            try {
                CardSlot cardSlot = gameManager.getGameBoard().getCardSlot(true, ZoneType.MONSTER, i);
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
