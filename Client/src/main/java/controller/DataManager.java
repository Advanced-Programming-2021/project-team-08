package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.cards.data.*;
import view.menus.CardOptionsScene;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataManager {

    public static void exportCard(CardData cardData) {
        File exportDir = new File("exportedCards");
        exportDir.mkdir();
        String fileLocation = "exportedCards/" + cardData.getCardName() + ".json";
        try {
            File file = new File(fileLocation);
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(fileLocation);
            fileWriter.write(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(cardData));
            fileWriter.close();
            System.out.println("card exported successfully.");
        } catch (IOException e) {
            System.out.println("the file isn't saved yet.");
        }
    }

    public static void importCard(String cardName) {
        File importDir = new File("importCards");
        importDir.mkdir();
        String fileLocation = "importCards/" + cardName + ".json";
        File file = new File(fileLocation);
        try {
            FileReader fileReader = new FileReader(fileLocation);
            String fileData = new String(Files.readAllBytes(Paths.get(file.toString())));
            JsonObject jsonObject = JsonParser.parseReader(fileReader).getAsJsonObject();
            CardData cardData;
            switch (jsonObject.get("cardType").toString().replaceAll("\"", "")) {
                case "MONSTER":
                    cardData = new Gson().fromJson(fileData, MonsterCardData.class);
                    break;
                case "SPELL":
                    cardData = new Gson().fromJson(fileData, SpellCardData.class);
                    break;
                case "TRAP":
                    cardData = new Gson().fromJson(fileData, TrapCardData.class);
                    break;
                default:
                    System.out.println("couldn't find card type");
                    return;
            }
            cardData.readEffectFromEffectString();
            System.out.println(cardData);
            System.out.println("card imported successfully.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void importCardGraphic(String cardName) {
        File importDir = new File("importCards");
        importDir.mkdir();
        String fileLocation = "importCards/" + cardName + ".json";
        File file = new File(fileLocation);
        try {
            FileReader fileReader = new FileReader(fileLocation);
            String fileData = new String(Files.readAllBytes(Paths.get(file.toString())));
            System.out.println(fileData);
            JsonObject jsonObject = JsonParser.parseReader(fileReader).getAsJsonObject();
            CardData cardData;
            switch (jsonObject.get("cardType").toString().replaceAll("\"", "")) {
                case "MONSTER":
                    cardData = new ReadMonsterCardsData().readACardData(fileData.split(","));
                    break;
                case "SPELL":
                case "TRAP":
                    cardData = new ReadSpellTrapCardsData().readACardData(fileData.split(","));
                    break;
                default:
                    CardOptionsScene.setMessage("couldn't find card type");
                    return;
            }
            CardOptionsScene.setMessage("card imported successfully.");
        } catch (IOException e) {
            CardOptionsScene.setMessage(e.getMessage());
        }
    }

    public static void exportCardGraphic(CardData cardData) {
        File exportDir = new File("exportedCards");
        exportDir.mkdir();
        String fileLocation = "exportedCards/" + cardData.getCardName() + ".json";
        try {
            File file = new File(fileLocation);
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(fileLocation);
            fileWriter.write(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(cardData));
            fileWriter.close();
            CardOptionsScene.setMessage("card exported successfully.");
        } catch (IOException e) {
            CardOptionsScene.setMessage("the file isn't saved yet.");
        }
    }
}
