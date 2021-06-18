package controller;

import com.google.gson.Gson;
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

    public static void importCard(CardData cardData) {
        FileWriter fileWriter;
        String fileLocation = "cards/" + cardData.getCardName() + ".json";
        try {
            fileWriter = new FileWriter(fileLocation);
            fileWriter.write(new Gson().toJson(cardData));
            fileWriter.close();
            System.out.println("file imported successfully.");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportCard(String cardName) {
        FileReader fileReader;
        String fileLocation = "cards/" + cardName + ".json";
        File file = new File(fileLocation);
        try {
            fileReader = new FileReader(fileLocation);
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
        } catch (IOException e) {
            System.out.println("the file isn't saved yet.");
        }

    }


}
