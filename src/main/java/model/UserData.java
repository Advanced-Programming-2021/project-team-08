package model;

import com.google.gson.Gson;
import javafx.scene.image.Image;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class UserData {
    private String username;
    private String nickname;
    private String password;
    private int point = 0;
    private int money;
    private ArrayList<Integer> myCardsIds = new ArrayList<>();
    private ArrayList<Deck> decks = new ArrayList<>();
    private String activeDeckName;
    private String profileImageUrl;


    public UserData(String username, String nickname, String password) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.money = 100000;
        try {
            profileImageUrl = new URL("file:" + System.getProperty("user.dir") + "/src/main/resources/asset/profileMenu/Chara001.dds6.png").toExternalForm();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        save();
    }

    public void setPassword(String password) {
        this.password = password;
        save();
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public void removeDeck(Deck deck) {
        decks.remove(deck);
        save();
    }

    public void addDeck(Deck deck) {
        decks.add(deck);
        save();
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public int getPoint() {
        return point;
    }

    public void addPoint(int point) {
        this.point += point;
        save();
    }

    public void setActiveDeckName(String activeDeckName) {
        this.activeDeckName = activeDeckName;
        save();
    }

    public Deck getActiveDeck() {
        return decks.stream().filter(e -> e.getName().equals(activeDeckName)).findFirst().orElse(null);
    }

    public int getMoney() {
        return money;
    }

    public void addMoney(int amount) {
        this.money += amount;
        save();
    }

    public void decreaseMoney(int amount) {
        this.money -= amount;
        save();
    }

    public void changeMoneyWithoutSave(int money) {
        this.money += money;
    }

    public void addCard(int id) {
        myCardsIds.add(id);
        save();
    }

    public ArrayList<Integer> getMyCardsIds() {
        return myCardsIds;
    }

    public void save() {
        try {
            FileWriter userFile = new FileWriter("users/" + username + ".json");
            userFile.write(new Gson().toJson(this));
            userFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setProfileImageUrl(String url) {
        this.profileImageUrl = url;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}


