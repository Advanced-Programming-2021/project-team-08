package model.cards.data;

import model.enums.MonsterAttribute;

import java.util.ArrayList;

public class MonsterCardData extends CardData{

    private int level;
    private String name;
    private MonsterAttribute attribute;
    private String type;
    private String monsterType;

    private int attackPoints, defencePoints;
    int price;
    private String cardDescription;

    private static ArrayList<MonsterCardData> allMonsterCardData = new ArrayList<>();

    public MonsterCardData() {
        allMonsterCardData.add(this);
    }

    public void setAttackPoints(int attackPoints) {
        this.attackPoints = attackPoints;
    }

    public void setAttribute(MonsterAttribute attribute) {
        this.attribute = attribute;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setMonsterType(String monsterType) {
        this.monsterType = monsterType;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public MonsterAttribute getAttribute() {
        return attribute;
    }

    public int getPrice() {
        return price;
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

    public String getCardDescription() {
        return cardDescription;
    }

    public static void printAllCard() {
        for(MonsterCardData monsterCardData: allMonsterCardData) {
            try{
                System.out.println("name: " + monsterCardData.getName() + "  level: " + monsterCardData.getLevel());
                System.out.println("monster attribute: " + monsterCardData.getAttribute());
                System.out.println("monster type: " + monsterCardData.getMonsterType());
                System.out.println("monster card type:" + monsterCardData.getType());
                System.out.println("monster attack: " + monsterCardData.getAttackPoints() + "  monster defence point: " + monsterCardData.getDefencePoints());
                System.out.println(monsterCardData.getCardDescription());
                System.out.println("monster price: " + monsterCardData.getPrice());
                System.out.println();
            }catch (NullPointerException e) {
                System.out.println("null pointer in printing cards");
            }
        }
    }
}
