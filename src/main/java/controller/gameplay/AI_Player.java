package controller.gameplay;

import model.Deck;
import model.UserData;
import model.cards.Card;
import model.cards.MonsterCard;
import model.cards.SpellCard;
import model.enums.CardType;
import model.enums.SpellTrapProperty;
import model.enums.ZoneType;
import model.gameplay.CardSlot;
import model.gameplay.Player;
import model.gameplay.PlayerBoard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public class AI_Player {
    private Deck deck = new Deck("myDeck", "AI");
    private static UserData data = new UserData("AI", "AI", "@A@I@");
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

    public static UserData getAIUserData() {
        return data;
    }

    public void setup(Player playerObject, Player opponent) {
        this.playerObject = playerObject;
        this.opponent = opponent;
        board = playerObject.getPlayerBoard();
    }

    public void playATurn() {
        try {
            Thread.sleep(1000);
            gameManager.goToNextPhase();
            Thread.sleep(1000);
            gameManager.goToNextPhase();
            setTraps();
            activateSpells();
            summonMonster();
            Thread.sleep(1000);
            gameManager.goToNextPhase();
            doAttack();
            Thread.sleep(1000);
            gameManager.goToNextPhase();
            Thread.sleep(1000);
            gameManager.goToNextPhase();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setTraps() {
        if (board.isSpellTrapZoneFull()) return;
        ArrayList<Card> trapCards = new ArrayList<>();
        trapCards.addAll(board.getHand().getAllCards().stream().filter(c -> c.getCardType() == CardType.TRAP).collect(Collectors.toList()));
        while (!board.isSpellTrapZoneFull() && trapCards.size() > 0) {
            try {
                gameManager.selectCard("--hand " + (board.getHand().getAllCards().indexOf(trapCards.get(0)) + 1));
                gameManager.setCard();
                trapCards.remove(0);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void activateSpells() {
        if (board.isSpellTrapZoneFull()) return;
        ArrayList<Card> spellCards = new ArrayList<>();
        spellCards.addAll(board.getHand().getAllCards().stream().filter(c -> c.getCardType() == CardType.SPELL).collect(Collectors.toList()));
        ArrayList<Card> fieldCards = new ArrayList<>();
        fieldCards.addAll(spellCards.stream().filter(c -> ((SpellCard) c).getData().getSpellProperty() == SpellTrapProperty.FIELD).collect(Collectors.toList()));
        spellCards.removeAll(fieldCards);

        while (!board.isSpellTrapZoneFull() && spellCards.size() > 0) {
            try {
                gameManager.selectCard("--hand " + (board.getHand().getAllCards().indexOf(spellCards.get(0)) + 1));
                gameManager.activateCard();
                spellCards.remove(0);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        if (fieldCards.size() > 0) {
            try {
                gameManager.selectCard("--hand " + (board.getHand().getAllCards().indexOf(fieldCards.get(0)) + 1));
                gameManager.activateCard();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void summonMonster() {
        if (board.isMonsterZoneFull()) return;
        ArrayList<Card> monsterCards = new ArrayList<>();
        monsterCards.addAll(board.getHand().getAllCards().stream().filter(c -> c.getCardType() == CardType.MONSTER).collect(Collectors.toList()));
        monsterCards.sort(Comparator.comparing(m -> ((MonsterCard) m).getData().getLevel()).reversed());
        for (int i = 0; i < monsterCards.size(); i++) {
            if (((MonsterCard) monsterCards.get(i)).getTributeNumber() > board.numberOfMonstersInZone()) continue;

            try {
                gameManager.selectCard("--hand " + (board.getHand().getAllCards().indexOf(monsterCards.get(i)) + 1));
                gameManager.summonCard(chooseTribute(((MonsterCard) monsterCards.get(i)).getTributeNumber()).toArray(new Integer[0]));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<Integer> chooseTribute(int n) {
        ArrayList<CardSlot> myMonsters = new ArrayList<>(board.getMonsterZone().stream().filter(c -> !c.isEmpty()).collect(Collectors.toList()));
        myMonsters.sort(Comparator.comparing(c -> ((MonsterCard) c.getCard()).getData().getLevel()));

        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add(board.getMonsterZone().indexOf(myMonsters.get(i)) + 1);
        }

        return result;
    }

    private void doAttack() {
        ArrayList<AttackResultCalculated> attacks = new ArrayList<>();
        ArrayList<CardSlot> myMonsters = new ArrayList<>(board.getMonsterZone().stream().filter(c -> !c.isEmpty() && ((MonsterCard) c.getCard()).isAttackPosition()).collect(Collectors.toList()));
        ArrayList<CardSlot> opponentMonsters = new ArrayList<>(opponent.getPlayerBoard().getMonsterZone().stream().filter(c -> !c.isEmpty()).collect(Collectors.toList()));

        for (CardSlot i : myMonsters) {
            for (CardSlot j : opponentMonsters) {
                attacks.add(new AttackResultCalculated((MonsterCard) i.getCard(), (MonsterCard) j.getCard()));
            }
        }

        attacks.sort(Comparator.comparing(AttackResultCalculated::attackValue).reversed());
        /*for (AttackResultCalculated a:attacks){
            System.out.println(a);
        }*/

        for (int i = 0; i < attacks.size(); i++) {
            AttackResultCalculated attack = attacks.get(i);
            if (attack.attacker.isAttackedThisTurn()) continue;
            if (attack.attackValue() <= 0) break;
            if (attack.attacked.getCardSlot().getZoneType() != ZoneType.MONSTER) continue;

            try {
                gameManager.selectCard("--monster " + (board.getMonsterZone().indexOf(attack.attacker.getCardSlot()) + 1));
                gameManager.attack(opponent.getPlayerBoard().getMonsterZone().indexOf(attack.attacked.getCardSlot()) + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (opponent.getPlayerBoard().numberOfMonstersInZone() == 0) {
            for (CardSlot cardSlot : myMonsters) {
                if (!cardSlot.isEmpty()) {
                    if (!((MonsterCard) cardSlot.getCard()).isAttackedThisTurn()) {
                        try {
                            gameManager.selectCard("--monster " + (board.getMonsterZone().indexOf(cardSlot) + 1));
                            gameManager.attackDirect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private class AttackResultCalculated {
        //1 for attacker
        //2 for attacked

        private int player1LPDecrease = 0;
        private int player2LPDecrease = 0;
        private boolean destroyCard1 = false;
        private boolean destroyCard2 = false;
        private MonsterCard attacker, attacked;

        public AttackResultCalculated(MonsterCard attacker, MonsterCard attacked) {
            this.attacker = attacker;
            this.attacked = attacked;
            int point1, point2;
            if (attacked.isAttackPosition()) {
                point1 = attacker.getData().getAttackPoints();
                point2 = attacked.getData().getAttackPoints();

                if (point1 > point2) {
                    player2LPDecrease = point1 - point2;
                    destroyCard2 = true;
                } else if (point1 == point2) {
                    destroyCard1 = true;
                    destroyCard2 = true;
                } else {
                    player1LPDecrease = point2 - point1;
                    destroyCard1 = true;
                }
            } else {
                point1 = attacker.getData().getAttackPoints();
                point2 = attacked.getData().getDefencePoints();

                if (point1 > point2) {
                    destroyCard2 = true;
                } else if (point1 == point2) {
                } else {
                    player1LPDecrease = point2 - point1;
                }
            }
        }

        public int attackValue() {
            return player2LPDecrease - player1LPDecrease + (destroyCard2 ? 1 : 0) - (destroyCard1 ? 1 : 0);
        }
    }
}
