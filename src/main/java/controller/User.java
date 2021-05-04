package controller;

import model.UserData;
import view.menus.ApplicationManger;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private static ArrayList<User> allUser;
    private final UserData userData;
    private ArrayList<String> decksName;
    private String activeDeck;
    static {
        allUser = new ArrayList<>();
    }

    public User(String username, String nickname, String password) {
        userData = new UserData(username, nickname, password);
        allUser.add(this);
    }

    public static User getUserByUsername(String username) {
        for(User user : allUser) {
            if (user.getUserData().getUsername().equals(username)) return user;
        }
        return null;
    }

    public static User getUserByNickname(String nickname) {
        for (User user : allUser) {
            if (user.getUserData().getNickname().equals(nickname)) return user;
        }
        return null;
    }

    public UserData getUserData() {
        return userData;
    }

    public static boolean loginUser(String username, String password) {
        User user = getUserByUsername(username);
        if (user.getUserData().getPassword().equals(password)) {
            ApplicationManger.setLoggedInUser(user);
            return true;
        }
        return false;
    }

    public static boolean doesUsernameExists(String username) {
        for(User user : allUser) {
            if (user.getUserData().getUsername().equals(username)) return true;
        }
        return false;
    }

    public static ArrayList<User> getAllUser(){
        return allUser;
    }

    public static UserData[] getAllUserData(){
        UserData[] scoreboard = new UserData[allUser.size()];
        int counter=0;
        for (User user: allUser) {
            scoreboard[counter]=user.userData;
            counter++;
        }
        return scoreboard;
    }

    public void addDeck(String deckName){
        this.decksName.add(deckName);
    }

    public ArrayList<String> getDecksName() {
        return decksName;
    }

    public void setActiveDeck(String activeDeck) {
        this.activeDeck = activeDeck;
    }

    public String getActiveDeck() {
        return activeDeck;
    }
/*
    public Deck getDeckByName(String deckName) {
        decks  = userData.getDecks;
        for(User deck : decks) {
            if (deck.getName.equals(deckName)) return deck;
        }
        return null;
    }
     */
}

