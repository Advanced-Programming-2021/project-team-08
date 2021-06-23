package model.cards;

import model.cards.data.SpellCardData;
import model.effects.Effect;
import model.enums.CardStatus;
import model.enums.CardType;
import model.gameplay.CardSlot;
import model.gameplay.Player;

public class SpellCard extends Card {
    private boolean activated = false;

    public SpellCard(SpellCardData data) {
        cardData = data;
        cardType = CardType.SPELL;
    }

    public boolean isActivated() {
        return activated;
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
        activated = true;
    }

    public void onActivate(Player cardOwner) {
        cardStatus = CardStatus.FACE_UP;
        activateEffect(cardOwner);
    }

    public void onSet() {
        cardStatus = CardStatus.TO_BACK;
    }
}
