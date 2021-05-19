import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.User;
import view.menus.ApplicationManger;
import model.Deck;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws Exception {
        ApplicationManger applicationManger = new ApplicationManger();

        File users = new File("users");
        users.mkdir();
        FileWriter decks = new FileWriter("decks.json");
        ArrayList<Deck> allOfDecks;
        String stringOfDecksFile = new String(Files.readAllBytes(Paths.get("decks.json")));
        allOfDecks = new Gson().fromJson(stringOfDecksFile,
                new TypeToken<List<Deck>>() {
                }.getType());
        Deck.setDecks(allOfDecks);


        File directoryPath = new File("users");
        File[] filesList = directoryPath.listFiles();

        ArrayList<User> allOfUsers = new ArrayList<>();
        for (File file : filesList) {
            String stringOfUserFile = new String(Files.readAllBytes(Paths.get(file.toString())));
            allOfUsers.add(new Gson().fromJson(stringOfUserFile,
                    new TypeToken<User>() {
                    }.getType()));
        }
        User.setAllUser(allOfUsers);

        applicationManger.run();
    }


}
