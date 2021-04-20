package Controllers;

import Models.UserData;

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

