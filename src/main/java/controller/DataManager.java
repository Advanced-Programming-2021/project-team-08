package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.UserData;
import model.cards.data.CardData;
import model.cards.data.MonsterCardData;
import model.cards.data.SpellCardData;
import model.cards.data.TrapCardData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DataManager {
    private static ArrayList<UserData> allUserData;
    private static ArrayList<CardData> allCardData;


    public static ArrayList<CardData> getAllCardData() {
        return allCardData;
    }

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
        }catch (IOException e) {
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
                case "MONSTER" :
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
            System.out.println(cardData);
            System.out.println("card imported successfully.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }


}
