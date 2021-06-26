package model.effectSystem.effects;

import model.cards.Card;
import model.effectSystem.ContinuousEffect;
import model.enums.ZoneType;
import model.gameplay.AttackResult;

import java.util.ArrayList;

public class DrawACardFromDeckOnDestroyAtLeastOneMonster extends ContinuousEffect {
    AttackResult attackResult;
    private boolean isDrawACardPlayer1 = false;
    private boolean isDrawACardPlayer2 = false;

    public DrawACardFromDeckOnDestroyAtLeastOneMonster(ArrayList<String> args) {
        super(args);

    }

    @Override
    public void setup() {
        super.setup();
        gameManager.getDestroyAMonster().addListener((attackResult) -> {
            this.attackResult = attackResult;
            if (this.attackResult.getDestroyMonsterCard1() == 1) isDrawACardPlayer1 = true;
            if (this.attackResult.getDestroyMonsterCard2() == 1) isDrawACardPlayer2 = true;
            activate();
        });
    }

    @Override
    public void activate() {
        if (isDrawACardPlayer1) {
            try {
                Card card = gameManager.getGameBoard().getCardSlot(false, ZoneType.DECK, 1).drawTopCard();
                card.getCardOwner().addCardToHand(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (isDrawACardPlayer2) {
            try {
                Card card = gameManager.getGameBoard().getCardSlot(true, ZoneType.DECK, 1).drawTopCard();
                card.getCardOwner().addCardToHand(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
