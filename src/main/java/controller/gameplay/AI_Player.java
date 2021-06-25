package controller.gameplay;

import model.Deck;
import model.UserData;
import model.cards.MonsterCard;
import model.enums.CardStatus;
import model.enums.Phase;
import model.gameplay.CardSlot;
import model.gameplay.Player;
import model.gameplay.PlayerBoard;

import java.util.ArrayList;
import java.util.Arrays;

public class AI_Player {
    private Deck deck = new Deck("myDeck", "AI");
    private UserData data = new UserData("AI", "AI", "@A@I@");
    private Player playerObject;
    private Player opponent;
    private PlayerBoard board;
    private GameManager gameManager;

    {
        Integer[] cards = {31, 31, 31, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 7, 8, 8, 8, 9, 9, 9, 10, 10, 10, 11, 11, 11, 12, 12, 12, 13};
        deck.getMainDeckIds().addAll(Arrays.asList(cards));
        data.addDeck(deck);
        data.setActiveDeckName("myDeck");
    }

    public AI_Player(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public UserData getAIUserData() {
        return data;
    }

    public void setup(Player playerObject, Player opponent) {
        this.playerObject = playerObject;
        this.opponent = opponent;
        board = playerObject.getPlayerBoard();
    }

    public void playATurn() {
        gameManager.goToNextPhase();
        gameManager.goToNextPhase();
        try {
            playerObject.summonCard(playerObject.getCardFromHand(1));

            gameManager.goToNextPhase();
            ArrayList<CardSlot> monsterZone = board.getMonsterZone();
            for (int i = 0, monsterZoneSize = monsterZone.size(); i < monsterZoneSize; i++) {
                CardSlot cardSlot = monsterZone.get(i);
                if (!cardSlot.isEmpty()) {
                    MonsterCard card = (MonsterCard) cardSlot.getCard();
                    if (card.getCardStatus() == CardStatus.FACE_UP && card.isAttackPosition()) {
                        gameManager.selectCard("--monster " + (i + 1));
                        doAttack();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        gameManager.goToNextPhase();
    }

    private void doAttack() {
        ArrayList<CardSlot> monsterZone = opponent.getPlayerBoard().getMonsterZone();
        for (int i = 0, monsterZoneSize = monsterZone.size(); i < monsterZoneSize; i++) {
            CardSlot enemyCardSlot = monsterZone.get(i);
            if (!enemyCardSlot.isEmpty()) {
                gameManager.attack(i + 1);
                return;
            }
        }
        gameManager.attackDirect();
    }
}
