package model.cards;

import model.cards.data.TrapCardData;
import model.effectSystem.Effect;
import model.enums.CardStatus;
import model.enums.CardType;

public class TrapCard extends Card {
    private boolean activated = false;

    public TrapCard(TrapCardData data) {
        super(data);
        cardType = CardType.TRAP;
    }

    public TrapCardData getData() {
        return (TrapCardData) cardData;
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
