package controller.gameplay;

import model.Deck;
import model.UserData;
import model.cards.Card;
import model.cards.MonsterCard;
import model.enums.CardStatus;
import model.enums.CardType;
import model.gameplay.CardSlot;
import model.gameplay.Player;
import model.gameplay.PlayerBoard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

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
        setTraps();
        summonMonster();
        gameManager.goToNextPhase();

        try {
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

    private void setTraps() {
        if (board.isSpellTrapZoneFull()) return;
        ArrayList<Card> trapCards = new ArrayList<>();
        trapCards.addAll(board.getHand().getAllCards().stream().filter(c -> c.getCardType() == CardType.TRAP).collect(Collectors.toList()));
        while (board.isSpellTrapZoneFull() && trapCards.size() > 0){
            try {
                gameManager.selectCard("--hand " + (board.getHand().getAllCards().indexOf(trapCards.get(0)) + 1));
                gameManager.setCard();
                setTraps();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void summonMonster() {
        if (board.isMonsterZoneFull()) return;
        ArrayList<Card> monsterCards = new ArrayList<>();
        monsterCards.addAll(board.getHand().getAllCards().stream().filter(c -> c.getCardType() == CardType.MONSTER).collect(Collectors.toList()));
        monsterCards.sort(Comparator.comparing(m -> ((MonsterCard)m).getData().getLevel()).reversed());
        try {
            gameManager.selectCard("--hand " + (board.getHand().getAllCards().indexOf(monsterCards.get(0)) + 1));
            gameManager.summonCard();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
