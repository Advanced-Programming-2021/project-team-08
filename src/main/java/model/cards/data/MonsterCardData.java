package model.cards.data;

import model.enums.CardType;
import model.enums.MonsterAttribute;

import java.util.ArrayList;
import java.util.function.Supplier;

public class MonsterCardData extends CardData {

    private int level;

    private MonsterAttribute attribute;
    private String type;
    private String monsterType;

    private int attackPoints, defencePoints;
    private int changedAttack = 0;
    private int changedDefence = 0;

    private static final ArrayList<MonsterCardData> allMonsterCardData = new ArrayList<>();

    private boolean calculatedAttackPoint = false;
    private Supplier<Integer> calculateAttackMethod;

    public MonsterCardData() {
        cardType = CardType.MONSTER;
        CardData.addCardData(this);
        allMonsterCardData.add(this);
    }

    public void setCalculatedAttackPoint(boolean calculatedAttackPoint) {
        this.calculatedAttackPoint = calculatedAttackPoint;
    }

    public void setCalculateAttackMethod(Supplier<Integer> calculateAttackMethod) {
        this.calculateAttackMethod = calculateAttackMethod;
    }

    public void setAttackPoints(int attackPoints) {
        this.attackPoints = attackPoints;
    }

    public void setAttribute(MonsterAttribute attribute) {
        this.attribute = attribute;
    }

    public void setDefencePoints(int defencePoints) {
        this.defencePoints = defencePoints;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMonsterType(String monsterType) {
        this.monsterType = monsterType;
    }

    public int getLevel() {
        return level;
    }

    public MonsterAttribute getAttribute() {
        return attribute;
    }

    public int getAttackPoints() {
        if(calculatedAttackPoint){
            return calculateAttackMethod.get();
        }
        return attackPoints + changedAttack;
    }

    public String getMonsterType() {
        return monsterType;
    }

    public int getDefencePoints() {
        return defencePoints + changedDefence;
    }

    public String getType() {
        return type;
    }

    public void setChangedAttack(int changedAttack) {
        this.changedAttack = changedAttack;
    }

    public void setChangedDefence(int changedDefence) {
        this.changedDefence = changedDefence;
    }

    public int getChangedAttack() {
        return changedAttack;
    }

    public int getChangedDefence() {
        return changedDefence;
    }

    public static MonsterCardData getCardByName(String cardName) {
        for (MonsterCardData monsterCardData : allMonsterCardData) {
            if (cardName.equals(monsterCardData.getName())) return monsterCardData;
        }
        return null;
    }

    public static void printAllMonsterCard() {
        for (MonsterCardData monsterCardData : allMonsterCardData) {
            try {
                System.out.println("name: " + monsterCardData.getName() + "  level: " + monsterCardData.getLevel() + " id: " + monsterCardData.getCardId());
                System.out.println("monster attribute: " + monsterCardData.getAttribute());
                System.out.println("monster type: " + monsterCardData.getMonsterType());
                System.out.println("monster card type:" + monsterCardData.getType());
                System.out.println("monster attack: " + monsterCardData.getAttackPoints() + "  monster defence point: " + monsterCardData.getDefencePoints());
                System.out.println(monsterCardData.getCardDescription());
                System.out.println("monster price: " + monsterCardData.getPrice());
                System.out.println();
            } catch (NullPointerException e) {
                System.out.println("null pointer in printing cards");
            }
        }
    }

    public static void setAllIncreasedAttackDefenceZero() {
        for (MonsterCardData monsterCardData : allMonsterCardData) {
            monsterCardData.setChangedAttack(0);
            monsterCardData.setChangedDefence(0);
        }
    }


    @Override
    public String toString() {
        return "Name: " + cardName +
                "\nLevel: " + level +
                "\nType: " + type +
                "\nATK: " + attackPoints +
                "\nDEF: " + defencePoints +
                "\nDescription: " + cardDescription;
    }
}
