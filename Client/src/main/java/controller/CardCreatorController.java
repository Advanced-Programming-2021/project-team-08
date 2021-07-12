package controller;

import model.cards.data.CardData;
import model.cards.data.MonsterCardData;
import model.effectSystem.Effect;
import model.enums.MonsterAttribute;
import view.menus.CardCreatorScene;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class CardCreatorController {

    private User activeUser;
    private CardCreatorScene scene;

    public CardCreatorController(CardCreatorScene cardCreatorScene) {
        activeUser = ApplicationManger.getLoggedInUser();
        scene = cardCreatorScene;
    }

    public int getCost(String defenceString, String attackString, HashMap<CardData, Effect> effects) {
        try {
            int cost = 0;
            int defence = 0;
            int attack = 0;
            if (!defenceString.equals("")) defence = Integer.parseInt(defenceString);
            if (!attackString.equals("")) attack = Integer.parseInt(attackString);
            cost += (attack + defence) * (attack + defence) / 2400;
            for (CardData cardData : effects.keySet()) {
                cost += effects.get(cardData).getPrice();
            }
            return cost;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void createCard(String defenceString, String attackString, HashMap<CardData, Effect> effects, String name, String description, String attribute, String type, String monsterType) {
        if (name.equals("")) {
            scene.setMessage("you should set a name for this card");
            return;
        }
        if (defenceString.equals("")) {
            scene.setMessage("you should fill the defence field");
            return;
        }
        if (attackString.equals("")) {
            scene.setMessage("you should fill the attack field");
            return;
        }
        if (description.equals("")) {
            scene.setMessage("you should write a description for this card");
            return;
        }
        if (attribute == null) {
            scene.setMessage("you should set card attribute");
            return;
        }
        if (type == null) {
            scene.setMessage("you should set card type");
            return;
        }
        if (monsterType == null) {
            scene.setMessage("you should set monster card type");
            return;
        }
        if (getCost(defenceString, attackString, effects) / 10 > activeUser.getUserData().getMoney()) {
            scene.setMessage("you don't have enough money to create the card");
            return;
        }
        MonsterCardData monsterCardData = setMonsterData(attackString, defenceString, name, effects, description, attribute, type, monsterType);
        saveToCsv(monsterCardData, effects);
        activeUser.getUserData().decreaseMoney(monsterCardData.getPrice());
        scene.setMessage("card created successfully.");
    }

    private MonsterCardData setMonsterData(String attackString, String defenceString, String name, HashMap<CardData, Effect> effects, String description, String attribute, String type, String monsterType) {
        MonsterCardData monsterCardData = new MonsterCardData();
        monsterCardData.setName(name);
        monsterCardData.setAttackPoints(Integer.parseInt(attackString));
        monsterCardData.setDefencePoints(Integer.parseInt(defenceString));
        monsterCardData.setCardDescription(description);
        monsterCardData.setMonsterType(monsterType);
        monsterCardData.setType(type);
        monsterCardData.setAttribute(MonsterAttribute.valueOf(attribute));
        monsterCardData.setId(MonsterCardData.getAllMonsterCardData().size());
        monsterCardData.setLevel(levelCalculator(defenceString, attackString));
        monsterCardData.setPrice(getCost(defenceString, attackString, effects));
        System.out.println(monsterCardData);
        if (effects.size() > 0) monsterCardData.setEffect(createEffectJson(effects));
        return monsterCardData;
    }

    private String createEffectJson(HashMap<CardData, Effect> effects) {
        StringBuilder json = new StringBuilder();
        for (CardData cardData : effects.keySet()) {
            try {
                BufferedReader csvReader = new BufferedReader(new FileReader("Monster.csv"));
                String row;
                for (int i = 0; i < cardData.getCardId(); i++) {
                    csvReader.readLine();
                }
                row = csvReader.readLine();
                String[] rowData = row.split(",");
                json.append(rowData[10]);
                json.append(" comma");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return json.substring(0, json.length() - 6);
    }

    private void saveToCsv(MonsterCardData monsterCardData, HashMap<CardData, Effect> effects) {
        try {
            FileWriter csvWriter = new FileWriter("Monster.csv", true);
            StringBuilder cardData = new StringBuilder();
            cardData.append("\n");
            cardData.append(monsterCardData.getName()).append(",");
            cardData.append(monsterCardData.getLevel()).append(",");
            cardData.append(monsterCardData.getAttribute()).append(",");
            cardData.append(monsterCardData.getMonsterType()).append(",");
            cardData.append(monsterCardData.getType()).append(",");
            cardData.append(monsterCardData.getAttackPoints()).append(",");
            cardData.append(monsterCardData.getDefencePoints()).append(",");
            cardData.append(monsterCardData.getCardDescription()).append(",");
            cardData.append(monsterCardData.getPrice()).append(",");
            cardData.append(monsterCardData.getCardId());
            if (effects.size() > 0) {
                String json = createEffectJson(effects);
                cardData.append(",");
                cardData.append(json);
                monsterCardData.setEffect(json);
            }
            csvWriter.write(cardData.toString());
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int levelCalculator(String defenceString, String attackString) {
        int attack = 0;
        int defence = 0;
        if (attackString.length() > 0) attack = Integer.parseInt(attackString);
        if (defenceString.length() > 0) defence = Integer.parseInt(defenceString);
        int mid = (defence + attack) / 2;
        return mid / 300;
    }


}


