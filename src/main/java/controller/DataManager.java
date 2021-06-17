package controller;

import com.google.gson.Gson;
import model.UserData;
import model.cards.data.CardData;
import model.cards.data.MonsterCardData;

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
        File fileReader;
        String fileLocation = "cards/" + cardName + ".json";
        fileReader = new File(fileLocation);
        try {
            String fileData = new String(Files.readAllBytes(Paths.get(fileReader.toString())));
            CardData cardData = new Gson().fromJson(fileData, MonsterCardData.class);
            System.out.println(cardData);
        } catch (IOException e) {
            System.out.println("the file isn't saved yet.");
        }

    }

}
