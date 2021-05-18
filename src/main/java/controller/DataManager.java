package controller;

import model.UserData;
import model.cards.data.CardData;

import java.util.ArrayList;

public class DataManager {
    private static ArrayList<UserData> allUserData;
    private static ArrayList<CardData> allCardData;

    public static ArrayList<CardData> getAllCardData() {
        return allCardData;
    }
}
