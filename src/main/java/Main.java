import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.User;
import controller.ApplicationManger;
import model.cards.data.ReadMonsterCardsData;
import model.cards.data.ReadSpellTrapCardsData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ApplicationManger applicationManger = new ApplicationManger();
        try {
            firstSetup();
        }catch (IOException e){
            System.out.println("ERROR: Couldn't load game!");
            System.exit(-1);
        }
        applicationManger.run();
    }

    public static void firstSetup() throws IOException {
        new ReadMonsterCardsData().readCardsData();
        new ReadSpellTrapCardsData().readSpellTrapData();
        File users = new File("users");
        users.mkdir();

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
    }
}
