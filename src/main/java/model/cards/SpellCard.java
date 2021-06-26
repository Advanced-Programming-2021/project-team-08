package model.cards;

import model.cards.data.SpellCardData;
import model.effectSystem.Effect;
import model.enums.CardStatus;
import model.enums.CardType;

public class SpellCard extends Card {
    private boolean activated = false;

    public SpellCard(SpellCardData data) {
        super(data);
        cardType = CardType.SPELL;
    }

    public boolean isActivated() {
        return activated;
    }

    public SpellCardData getData() {
        return (SpellCardData) cardData;
    }

    public void activateEffect(){
        for(Effect effect: cardData.getEffects()){
            effect.activate();
        }
        activated = true;
    }


    public void onActivate() {
        cardStatus = CardStatus.FACE_UP;
        activateEffect();
    }

    @Override
    public void onSet() {
        cardStatus = CardStatus.TO_BACK;
    }
}
