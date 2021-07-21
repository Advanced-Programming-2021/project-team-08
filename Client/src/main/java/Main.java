import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import controller.ApplicationManger;
import controller.User;
import model.UserData;
import model.cards.data.ReadMonsterCardsData;
import model.cards.data.ReadSpellTrapCardsData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ApplicationManger applicationManger = new ApplicationManger();
        try {
            firstSetup();
        } catch (IOException e) {
            System.out.println("ERROR: Couldn't load game!");
            System.exit(-1);
        }
        applicationManger.run(args);
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
            allOfUsers.add(new User(new Gson().fromJson(stringOfUserFile,
                    new TypeToken<UserData>() {
                    }.getType())));
        }
        User.setAllUser(allOfUsers);
    }


}
