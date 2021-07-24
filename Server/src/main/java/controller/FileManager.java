package controller;

import com.google.gson.Gson;
import model.User;
import model.enums.MessageType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
    private static final FileManager fileManager = new FileManager();

    public static FileManager getInstance() {
        return fileManager;
    }

    private FileManager() {

    }

    public MessageType createUser(User user) {
        FileWriter userFile;
        try {
            File file = new File("users");
            if (!file.exists()) {
                if (!file.mkdir()) throw new IOException("could not make users folder");
            }
            userFile = new FileWriter("users/" + user.getUserData().getUsername() + ".json");
            userFile.write(new Gson().toJson(user.getUserData()));
            userFile.close();
            return MessageType.SUCCESSFUL;
        } catch (IOException e) {
            e.printStackTrace();
            return MessageType.ERROR;
        }
    }
}
