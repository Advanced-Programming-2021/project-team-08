package model.cards.data;

import model.enums.CardType;
import model.enums.MonsterAttribute;

import java.util.ArrayList;

public class MonsterCardData extends CardData {

    private int level;

    private MonsterAttribute attribute;
    private String type;
    private String monsterType;

    private int attackPoints, defencePoints;
    private static ArrayList<MonsterCardData> allMonsterCardData = new ArrayList<>();

    public MonsterCardData() {
        cardType = CardType.MONSTER;
        CardData.addCardData(this);
        allMonsterCardData.add(this);
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
        return attackPoints;
    }

    public String getMonsterType() {
        return monsterType;
    }

    public int getDefencePoints() {
        return defencePoints;
    }

    public String getType() {
        return type;
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
