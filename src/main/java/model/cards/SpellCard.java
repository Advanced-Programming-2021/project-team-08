package model.cards;

import model.effects.Effect;
import model.gameplay.Player;

public class SpellCard extends Card {
    @Override
    public void setup() {

    }

    public void activateEffect(Player cardOwner){
        for(Effect effect: cardData.getEffects()){
            effect.activate(cardOwner);
        }
    }
}
