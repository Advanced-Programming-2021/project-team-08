package controller;

import com.google.gson.Gson;
import model.User;
import model.enums.MessageType;

import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
    private static FileManager fileManager = new FileManager();

    public static FileManager getInstance() {
        return fileManager;
    }

    private FileManager() {

    }

    public MessageType createUser(User user) {
        FileWriter userFile;
        try {
            userFile = new FileWriter("users/" + user.getUserData().getUsername() +".json");
            userFile.write(new Gson().toJson(user));
            userFile.close();
            return MessageType.SUCCESSFUL;
        } catch (IOException e) {
            e.printStackTrace();
            return MessageType.ERROR;
        }
    }
}