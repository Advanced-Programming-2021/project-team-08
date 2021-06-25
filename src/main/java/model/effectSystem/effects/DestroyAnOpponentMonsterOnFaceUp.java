package model.effectSystem.effects;

import model.cards.TrapCard;
import model.effectSystem.Effect;
import model.enums.ZoneType;
import model.gameplay.CardSlot;

import java.util.ArrayList;

public class DestroyAnOpponentMonsterOnFaceUp extends Effect {
    public int placeOfMonster;
    public DestroyAnOpponentMonsterOnFaceUp(ArrayList<String> args) {
        super(args);
    }

    @Override
    public void setup(){
        super.setup();
        gameManager.getFaceUp().addListener((rotateCard)->{
            if(gameManager.getScene().getDestroyingAMonsterCommand()) {
                placeOfMonster = gameManager.getScene().getPlaceOfMonster();
                activate();
            }
        });
    }

    @Override
    public void activate() {
        CardSlot cardSlot;
        try {
            cardSlot = gameManager.getGameBoard().getCardSlot(true, ZoneType.MONSTER, placeOfMonster);
            CardSlot graveyard = cardSlot.getCard().getCardOwner().getPlayerBoard().getGraveyard();
            graveyard.appendCard(cardSlot.getCard());
            cardSlot.removeCard();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
