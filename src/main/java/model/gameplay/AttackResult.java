package model.gameplay;

import model.cards.MonsterCard;
import model.enums.CardStatus;

public class AttackResult {
    //1 for attacker
    //2 for attacked

    private Player attackerPlayer;
    private int player1LPDecrease = 0;
    private int player2LPDecrease = 0;
    private boolean destroyCard1 = false;
    private boolean destroyCard2 = false;
    private String resultMessage = "";

    private boolean canceled = false;

    public AttackResult(MonsterCard attacker, MonsterCard attacked) {
        attackerPlayer = attacker.getCardOwner();
        int point1, point2;
        if (attacked.isAttackPosition()) {
            point1 = attacker.getData().getAttackPoints();
            point2 = attacked.getData().getAttackPoints();

            if (point1 > point2) {
                player2LPDecrease = point1 - point2;
                destroyCard2 = true;
                resultMessage = "your opponent's monster is destroyed and your opponent received " + player2LPDecrease + " battle damage";
            } else if (point1 == point2) {
                destroyCard1 = true;
                destroyCard2 = true;
                resultMessage = "both your and your opponent's monster cards are destroyed and no one received damage";
            } else {
                player1LPDecrease = point2 - point1;
                destroyCard1 = true;
                resultMessage = "your monster card is destroyed and you received " + player1LPDecrease + " battle damage";
            }
        } else {
            point1 = attacker.getData().getAttackPoints();
            point2 = attacked.getData().getDefencePoints();

            if (point1 > point2) {
                destroyCard2 = true;
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
        }
        attacked.onAttacked(this);
    }


    public AttackResult(MonsterCard attacker) {
        attackerPlayer = attacker.getCardOwner();
        player1LPDecrease = attacker.getData().getAttackPoints();
        resultMessage = "your opponent received " + attacker.getData().getAttackPoints() + " battle damage";
    }

    public void setPlayer2LPDecrease(int player2LPDecrease) {
        this.player2LPDecrease = player2LPDecrease;
    }

    public int getPlayer1LPDecrease() {
        return player1LPDecrease;
    }

    public int getPlayer2LPDecrease() {
        return player2LPDecrease;
    }

    public boolean isDestroyCard1() {
        return destroyCard1;
    }

    public boolean isDestroyCard2() {
        return destroyCard2;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setDestroyCard1(boolean destroyCard1) {
        this.destroyCard1 = destroyCard1;
    }

    public Player getAttackerPlayer() {
        return attackerPlayer;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void cancel() {
        this.canceled = true;
    }
}
