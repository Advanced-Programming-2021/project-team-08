package controller;

import model.Deck;
import model.UserData;
import model.cards.Card;

import java.util.ArrayList;

public class User {
    private static ArrayList<User> allUser;
    private final UserData userData;

    static {
        allUser = new ArrayList<>();
    }

    public User(String username, String nickname, String password) {
        userData = new UserData(username, nickname, password);
        allUser.add(this);
    }

    public static User getUserByUsername(String username) throws Exception {
        for (User user : allUser) {
            if (user.getUserData().getUsername().equals(username)) return user;
        }
        throw new Exception("No user found with this Username");
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
        User user;
        try {
            user = getUserByUsername(username);
            if (user.getUserData().getPassword().equals(password)) {
                ApplicationManger.setLoggedInUser(user);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static boolean doesUsernameExists(String username) {
        return allUser.stream().filter(c -> c.getUserData().getUsername().equals(username)).count() > 0;
    }

    public static boolean doesNicknameExists(String nickname) {
        return allUser.stream().filter(c -> c.getUserData().getNickname().equals(nickname)).count() > 0;
    }

    public static ArrayList<User> getAllUser() {
        return allUser;
    }

    public static void setAllUser(ArrayList<User> allUser) {
        User.allUser = allUser;
    }

    public static UserData[] getAllUserData() {
        UserData[] scoreboard = new UserData[allUser.size()];
        int counter = 0;
        for (User user : allUser) {
            scoreboard[counter] = user.userData;
            counter++;
        }
        return scoreboard;
    }

    public void addDeck(Deck deck) {
        userData.addDeck(deck);
    }

    public Deck getActiveDeck(){
        return userData.getActiveDeck();
    }

    public ArrayList<Deck> getDecks() {
        return userData.getDecks();
    }

    public void setActiveDeckName(String activeDeckName) {
        userData.setActiveDeckName(activeDeckName);
    }

    public String getUsername() {
        return userData.getUsername();
    }

    public boolean haveThisCardFree(String cardName){
        try {
            return getCardsThatThereIsNotInAnyDeck().contains(Card.getCardIdByName(cardName));
        } catch (Exception e) {
            return false;
        }
    }

    public ArrayList<Integer> getCardsThatThereIsNotInAnyDeck() {
        ArrayList<Integer> result = new ArrayList<>(userData.getMyCardsIds());
        for (Deck deck : getDecks()){
            result.removeAll(deck.getMainDeckIds());
            result.removeAll(deck.getSideDeckIds());
        }

        return result;
    }

    public void setActiveDeck(String deckName) {
        userData.setActiveDeckName(deckName);
    }
}

