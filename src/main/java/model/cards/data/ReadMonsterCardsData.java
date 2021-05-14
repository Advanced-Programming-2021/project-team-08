package model.cards.data;

import model.enums.MonsterAttribute;
import java.io.BufferedReader;
import java.io.FileReader;


public class ReadMonsterCardsData {

    public void readCardsData() {
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader("Monster.csv"));
            String row;
            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                readACardData(data);
            }
            csvReader.close();
        }catch (Exception e) {
            System.out.println("some thing was wrong in csvReader");
        }
    }

    private void readACardData(String[] rowData) {
        MonsterCardData monsterCardData = new MonsterCardData();
        setMonsterName(rowData[0].trim().replaceAll(" comma",","), monsterCardData);
        setMonsterLevel(rowData[1].trim(), monsterCardData);
        setMonsterAttribute(rowData[2].trim(), monsterCardData);
        setMonsterType(rowData[3].trim(), monsterCardData);
        setMonsterCardType(rowData[4].trim(), monsterCardData);
        setMonsterAttack(rowData[5].trim(), monsterCardData);
        setMonsterDefence(rowData[6].trim(), monsterCardData);
        setMonsterDescription(rowData[7].trim().replaceAll(" comma",","), monsterCardData);
        setMonsterPrice(rowData[8].trim(), monsterCardData);
    }

    private void setMonsterName(String name, MonsterCardData monsterCardData) {
        try {
            monsterCardData.setName(name);
        }catch (Exception NullPointerException) {
            System.out.println("name didn't found");
        }
    }

    private void setMonsterLevel(String level, MonsterCardData monsterCardData) {
        try {
            monsterCardData.setLevel(Integer.parseInt(level));
        }catch (Exception e) {
            System.out.println("level wasn't integer");
        }
    }

    private void setMonsterAttribute(String attribute, MonsterCardData monsterCardData) {
        try {
            monsterCardData.setAttribute(MonsterAttribute.valueOf(attribute));
        }catch (Exception e) {
            System.out.println("attribute enum failed in card " + monsterCardData.getName());
        }
    }

    private void setMonsterType(String monsterType, MonsterCardData monsterCardData) {
        try{
            monsterCardData.setMonsterType(monsterType);
        }catch (Exception e) {
            System.out.println("set monster type was wrong");
        }
    }

    private void setMonsterCardType(String cardType, MonsterCardData monsterCardData) {
        try {
            monsterCardData.setType(cardType);
        }
        catch (Exception e) {
            System.out.println("set type was wrong");
        }
    }

    private void setMonsterAttack(String monsterAttack, MonsterCardData monsterCardData) {
        try {
            monsterCardData.setAttackPoints(Integer.parseInt(monsterAttack));
        }catch (Exception e) {
            System.out.println("set attack point failed");
        }
    }

    private void setMonsterDefence(String defence, MonsterCardData monsterCardData) {
        try {
            monsterCardData.setDefencePoints(Integer.parseInt(defence));
        }catch (Exception e) {
            System.out.println("set defence point failed");
        }
    }

    private void setMonsterDescription(String description, MonsterCardData monsterCardData) {
        try {
            description = description.replace("\"", "");
            monsterCardData.setCardDescription(description);
        }catch (Exception e) {
            System.out.println("set description failed");
        }
    }

    private void setMonsterPrice(String price, MonsterCardData monsterCardData) {
        try {
            monsterCardData.setPrice(Integer.parseInt(price));
        }
        catch (Exception e) {
            System.out.println("failed price: " + price);
        }
    }

    public static void main(String[] args) {
        new ReadMonsterCardsData().readCardsData();
        MonsterCardData.printAllCard();
    }
}
