package model;

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


    public UserData(String username, String nickname, String password) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.money = 100000;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
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

    public void changePoint(int point) {
        this.point += point;
    }

    public void setActiveDeckName(String activeDeckName) {
        this.activeDeckName = activeDeckName;
    }

    public String getActiveDeckName() {
        return activeDeckName;
    }

    public Deck getActiveDeck(){
        return Deck.getDeckWithName(activeDeckName);
    }

    public void addDeck(Deck deck) {
        decks.add(deck);
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void addCard(int id) {
        myCardsIds.add(id);
    }
}


