package Models;

public class UserData {
    private String username;
    private String nickname;
    private String password;
    private int point;
    //private ArrayList<Deck> decks;
    {
        point = 0;
        // decks = new ArrayList<>();
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

    /*
    public Deck getDeckByName(String deckName) {
        for(Deck deck : decks) {
            if (deck.getName.equals(deckName)) return deck;
        }
        return null;
    }
     */

    public void changePoint(int point) {
        this.point += point;
    }

    /*
    public void addDeck(Deck deck) {
        decks.add(deck);
    }
     */
}

