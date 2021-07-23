package controller.gameplay;

import java.util.ArrayList;

public class StreamController {
    public static StreamController streamController = new StreamController();

    ArrayList<String> gameOrders;

    public static StreamController getInstance() {
        return streamController;
    }

    public void setGameOrders(ArrayList<String> gameOrders) {
        this.gameOrders = gameOrders;
    }

    public ArrayList<String> getGameOrders() {
        return gameOrders;
    }
}
