import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import view.menus.ApplicationManger;
import view.menus.Deck;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws Exception {
        ApplicationManger applicationManger = new ApplicationManger();

        File users = new File("users");
        users.mkdir();

        FileWriter decks = new FileWriter("decks.json");
        ArrayList<Deck> allOfDecks;
        allOfDecks = new Gson().fromJson(String.valueOf(decks),
                new TypeToken<List<Deck>>() {
                }.getType());
        Deck.setDecks(allOfDecks);


        File directoryPath = new File("users");
        File[] filesList = directoryPath.listFiles();
        ArrayList<User> allOfUsers=null;
        for(File file : filesList) {
            allOfUsers.add(new Gson().fromJson(String.valueOf(file),
                    new TypeToken<User>() {
                     }.getType()));
    }
        User.setAllUser(allOfUsers);



        applicationManger.run();
    }





}
