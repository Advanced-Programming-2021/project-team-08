package model.effectSystem.effects;


import model.cards.Card;
import model.effectSystem.Effect;
import model.enums.CardType;
import model.enums.SpellTrapProperty;
import model.enums.ZoneType;

import java.util.ArrayList;

public class DrawSpellTrapCardFromDeck extends Effect {
    int number;
    CardType cardType;
    SpellTrapProperty spellProperty;


    public DrawSpellTrapCardFromDeck(ArrayList<String> args) {
        super(args);
        number = Integer.parseInt(args.get(0));
        try {
            cardType = CardType.valueOf(args.get(1));
        }catch (Exception e) {
            e.printStackTrace();
            cardType = null;
        }
        try {
            spellProperty = SpellTrapProperty.valueOf(args.get(2));
        }catch (Exception e) {
            spellProperty = null;
        }
    }

    @Override
    public void activate() {
        if (cardType == null) {
            try {
                Card card = gameManager.getGameBoard().getCardSlot(false, ZoneType.DECK, 1).drawTopCard();
                card.getCardOwner().addCardToHand(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Card card = this.card.getCardOwner().getPlayerBoard().drawParticularSpellTrap(cardType, spellProperty);
        this.card.getCardOwner().addCardToHand(card);
    }
}
