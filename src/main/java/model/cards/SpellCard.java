package model.cards;

import model.cards.data.SpellCardData;
import model.effects.Effect;
import model.enums.CardType;
import model.gameplay.Player;

public class SpellCard extends Card {
    public SpellCard(SpellCardData data) {
        cardData = data;
        cardType = CardType.SPELL;
    }

    public SpellCardData getData() {
        return (SpellCardData) cardData;
    }

    @Override
    public void setup() {

    }

    public void activateEffect(Player cardOwner){
        for(Effect effect: cardData.getEffects()){
            effect.activate(cardOwner);
        }
    }
}
