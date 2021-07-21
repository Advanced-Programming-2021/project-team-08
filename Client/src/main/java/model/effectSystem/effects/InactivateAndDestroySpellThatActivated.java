package model.effectSystem.effects;

import model.cards.Card;
import model.cards.SpellCard;
import model.effectSystem.CounterEffect;

import java.util.ArrayList;

public class InactivateAndDestroySpellThatActivated extends CounterEffect {
    Card card1;

    public InactivateAndDestroySpellThatActivated(ArrayList<String> args) {
        super(args);
    }

    @Override
    public void setup() {
        super.setup();
        int size = card.getCardOwner().getPlayerBoard().getHand().getAllCards().size();
        gameManager.getOnAnSpellActivated().addListener((card1) -> {
            card.getCardOwner().getPlayerBoard().getHand().getAParticularCard(
                    gameManager.getScene().throwAwayACardFromHand(size)).moveToGraveyard();
            this.card1 = card1;
            activate();
        });
    }

    @Override
    public void activate() {
        SpellCard spellCard = (SpellCard) card1;
        // not activate
        if (!spellCard.isActivated())
            card1.moveToGraveyard();
    }


}
