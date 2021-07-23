package model;

public class GameData {
    private final int id;
    private final String firstPlayerNickname;
    private final String secondPlayerNickname;


    public GameData(int id, String firstPlayerNickname, String secondPlayerNickname) {
        this.id = id;
        this.firstPlayerNickname = firstPlayerNickname;
        this.secondPlayerNickname = secondPlayerNickname;

    }

    @Override
    public String toString() {
        return "GameData{" +
                "id=" + id +
                ", firstPlayerNickname='" + firstPlayerNickname + '\'' +
                ", secondPlayerNickname='" + secondPlayerNickname + '\'' +
                '}';
    }
}
