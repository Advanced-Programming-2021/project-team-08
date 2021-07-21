package model.gameplay;

import model.cards.Card;
import model.cards.MonsterCard;
import model.enums.CardStatus;
import model.enums.CardType;

public class AttackResult {
    //1 for attacker
    //2 for attacked

    private final Player attackerPlayer;
    private Player attackedPlayer;
    private int player1LPDecrease = 0;
    private int player2LPDecrease = 0;
    private boolean destroyCard1 = false;
    private boolean destroyCard2 = false;
    private int destroyMonsterCard1 = 0;
    private int destroyMonsterCard2 = 0;
    private boolean attackedFlip = false;

    private String resultMessage = "";
    private Card attacker;
    private boolean canceled = false;

    public AttackResult(MonsterCard attacker, MonsterCard attacked) {
        attackerPlayer = attacker.getCardOwner();
        attackedPlayer = attacked.getCardOwner();
        this.attacker = attacker;
        int point1, point2;
        if (attacked.isAttackPosition()) {
            point1 = attacker.getData().getAttackPoints();
            point2 = attacked.getData().getAttackPoints();

            if (point1 > point2) {
                player2LPDecrease = point1 - point2;
                destroyCard2 = true;
                if (attacked.getCardType().equals(CardType.MONSTER))
                    ++destroyMonsterCard2;
                resultMessage = "your opponent's monster is destroyed and your opponent received " + player2LPDecrease + " battle damage";
            } else if (point1 == point2) {
                destroyCard1 = true;
                if (attacker.getCardType().equals(CardType.MONSTER))
                    ++destroyMonsterCard1;
                destroyCard2 = true;
                if (attacked.getCardType().equals(CardType.MONSTER))
                    ++destroyMonsterCard2;
                resultMessage = "both your and your opponent's monster cards are destroyed and no one received damage";
            } else {
                player1LPDecrease = point2 - point1;
                destroyCard1 = true;
                if (attacker.getCardType().equals(CardType.MONSTER))
                    ++destroyMonsterCard1;
                resultMessage = "your monster card is destroyed and you received " + player1LPDecrease + " battle damage";
            }
        } else {
            point1 = attacker.getData().getAttackPoints();
            point2 = attacked.getData().getDefencePoints();

            if (point1 > point2) {
                destroyCard2 = true;
                if (attacked.getCardType().equals(CardType.MONSTER))
                    ++destroyMonsterCard2;
                resultMessage = "the defence position monster is destroyed";
            } else if (point1 == point2) {
                resultMessage = "no card is destroyed";
            } else {
                player1LPDecrease = point2 - point1;
                resultMessage = "no card is destroyed and you received " + player1LPDecrease + " battle damage";
            }
        }

        if (attacked.getCardStatus() == CardStatus.TO_BACK) {
            resultMessage = "opponent's monster card was \"" + attacked.getData().getCardName() + "\" and " + resultMessage;
            attackedFlip = true;
        }
        attacked.onAttacked(this);
    }


    public AttackResult(MonsterCard attacker) {
        attackerPlayer = attacker.getCardOwner();
        player2LPDecrease = attacker.getData().getAttackPoints();
        resultMessage = "your opponent received " + attacker.getData().getAttackPoints() + " battle damage";
    }

    public int getDestroyMonsterCard1() {
        return destroyMonsterCard1;
    }

    public int getDestroyMonsterCard2() {
        return destroyMonsterCard2;
    }

    public int getPlayer1LPDecrease() {
        return player1LPDecrease;
    }

    public int getPlayer2LPDecrease() {
        return player2LPDecrease;
    }

    public void setPlayer2LPDecrease(int player2LPDecrease) {
        this.player2LPDecrease = player2LPDecrease;
    }

    public boolean isDestroyCard1() {
        return destroyCard1;
    }

    public void setDestroyCard1(boolean destroyCard1) {
        this.destroyCard1 = destroyCard1;
    }

    public boolean isDestroyCard2() {
        return destroyCard2;
    }

    public void setDestroyCard2(boolean destroyCard2) {
        this.destroyCard2 = destroyCard2;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public Player getAttackerPlayer() {
        return attackerPlayer;
    }

    public Player getAttackedPlayer() {
        return attackedPlayer;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public Card getAttacker() {
        return attacker;
    }

    public void cancel() {
        this.canceled = true;
    }

    public boolean isAttackedFlip() {
        return attackedFlip;
    }
}
