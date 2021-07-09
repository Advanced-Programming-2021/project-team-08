package model.cards.data;

import javafx.scene.image.Image;
import model.enums.MonsterAttribute;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;


public class ReadMonsterCardsData {

    public void readCardsData() {
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader("Monster.csv"));
            String row;
            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                if (data.length == 10 || data.length == 11) {
                    readACardData(data);
                } else System.out.println("the size of data was " + data.length + " so couldn't parse it");
            }
            csvReader.close();
        } catch (Exception e) {
            System.out.println("some thing was wrong in monsterData csvReader");
        }
    }

    private void readACardData(String[] rowData) {
        MonsterCardData monsterCardData = new MonsterCardData();
        setMonsterName(rowData[0].trim().replaceAll(" comma", ","), monsterCardData);
        setMonsterLevel(rowData[1].trim(), monsterCardData);
        setMonsterAttribute(rowData[2].trim(), monsterCardData);
        setMonsterType(rowData[3].trim(), monsterCardData);
        setMonsterCardType(rowData[4].trim(), monsterCardData);
        setMonsterAttack(rowData[5].trim(), monsterCardData);
        setMonsterDefence(rowData[6].trim(), monsterCardData);
        setMonsterDescription(rowData[7].trim().replaceAll(" comma", ",").replaceAll("\\(nextline\\)", "\n"), monsterCardData);
        setMonsterPrice(rowData[8].trim(), monsterCardData);
        setMonsterId(rowData[9].trim(), monsterCardData);
        if (rowData.length == 11) {
            monsterCardData.setEffect(rowData[10].replace(" comma", ",").trim());
        }
    }

    private void setMonsterName(String name, MonsterCardData monsterCardData) {
        try {
            monsterCardData.setName(name);
        } catch (Exception NullPointerException) {
            System.out.println("name didn't found");
        }
    }

    private void setMonsterLevel(String level, MonsterCardData monsterCardData) {
        try {
            monsterCardData.setLevel(Integer.parseInt(level));
        } catch (Exception e) {
            System.out.println("level wasn't integer");
        }
    }

    private void setMonsterAttribute(String attribute, MonsterCardData monsterCardData) {
        try {
            monsterCardData.setAttribute(MonsterAttribute.valueOf(attribute));
        } catch (Exception e) {
            System.out.println("attribute enum failed in card " + monsterCardData.getName());
        }
    }

    private void setMonsterType(String monsterType, MonsterCardData monsterCardData) {
        try {
            monsterCardData.setMonsterType(monsterType);
        } catch (Exception e) {
            System.out.println("set monster type was wrong");
        }
    }

    private void setMonsterCardType(String cardType, MonsterCardData monsterCardData) {
        try {
            monsterCardData.setType(cardType);
        } catch (Exception e) {
            System.out.println("set type was wrong");
        }
    }

    private void setMonsterAttack(String monsterAttack, MonsterCardData monsterCardData) {
        try {
            monsterCardData.setAttackPoints(Integer.parseInt(monsterAttack));
        } catch (Exception e) {
            System.out.println("set attack point failed");
        }
    }

    private void setMonsterDefence(String defence, MonsterCardData monsterCardData) {
        try {
            monsterCardData.setDefencePoints(Integer.parseInt(defence));
        } catch (Exception e) {
            System.out.println("set defence point failed");
        }
    }

    private void setMonsterDescription(String description, MonsterCardData monsterCardData) {
        try {
            description = description.replace("\"", "");
            monsterCardData.setCardDescription(description);
        } catch (Exception e) {
            System.out.println("set description failed");
        }
    }

    private void setMonsterPrice(String price, MonsterCardData monsterCardData) {
        try {
            monsterCardData.setPrice(Integer.parseInt(price));
        } catch (Exception e) {
            System.out.println("failed price: " + price);
        }
    }

    private void setMonsterId(String id, MonsterCardData monsterCardData) {
        try {
            monsterCardData.setId(Integer.parseInt(id));
        } catch (Exception e) {
            System.out.println("setting id failed");
        }
    }

    private void setImage(CardData monsterCardData) {
        String path;
        String cardName = monsterCardData.getCardName().replaceAll(" ", "");
        if (monsterCardData.getCardId() < 42) {
            path = "/src/main/resources/asset/Cards/Monsters/" + cardName + ".jpg";
        }else {
            path = "/src/main/resources/asset/cardCreating/card.png";
        }
        if (!new File(path.substring(1)).exists()) {
            System.out.println("the file with this path didn't load: " + path);
        }
        try {
           monsterCardData.setCardImage(new Image(new URL("file:" + System.getProperty("user.dir") + path).toExternalForm()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setGraphic() {
        try {
            for (CardData monsterCardData : MonsterCardData.getAllCardData()) {
                if (monsterCardData instanceof MonsterCardData) {
                    setImage(monsterCardData);
                }
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ReadMonsterCardsData().readCardsData();
        new ReadMonsterCardsData().setGraphic();
        new ReadSpellTrapCardsData().readSpellTrapData();
        MonsterCardData.printAllMonsterCard();
        SpellCardData.printAllTraps();
        TrapCardData.printAllTraps();
    }
}
