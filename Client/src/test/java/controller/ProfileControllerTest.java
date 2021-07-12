package controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.menus.ProfileScene;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfileControllerTest {

    private static final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private static User testUser;
    private static ProfileController profileController;

    @BeforeAll
    public static void set() {
        System.setOut(new PrintStream(outputStreamCaptor));
        testUser = new User("test", "testing", "test123");
        ApplicationManger.setLoggedInUser(testUser);
        new User("alaki", "abool", "abool123");
        FileWriter userFile = null;
        try {
            userFile = new FileWriter("users/" + "test" + ".json");
            userFile.write(new Gson().toJson(testUser));
            userFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        profileController = new ProfileController(new ProfileScene());
    }

    @AfterAll
    public static void endWorks() {
        File userFile = new File("users/" + "test" + ".json");
        userFile.delete();
        User.deleteAccount(testUser);
    }

    @BeforeEach
    public void setup() {
        outputStreamCaptor.reset();
    }

    @Test
    public void changeNicknameTest() {
        String input = "profile change --nickname abool";
        profileController.changeNickname(input);
        String output = "this nickname is already used" + System.lineSeparator();
        assertEquals(output, outputStreamCaptor.toString());
        outputStreamCaptor.reset();
        input = "profile change --nickname salam";
        profileController.changeNickname(input);
        output = "nickname changed successfully!" + System.lineSeparator();
        assertEquals(output, outputStreamCaptor.toString());
    }

    @Test
    public void changePasswordTest() {
        String input = "profile change --password --current test12 --new test";
        profileController.changePassword(input);
        String output = "current password is invalid" + System.lineSeparator();
        assertEquals(output, outputStreamCaptor.toString());
        outputStreamCaptor.reset();
        input = "profile change --password --current test123 --new test123";
        profileController.changePassword(input);
        output = "please enter a new password" + System.lineSeparator();
        assertEquals(output, outputStreamCaptor.toString());
        outputStreamCaptor.reset();
        input = "profile change --password --current test123 --new test";
        profileController.changePassword(input);
        output = "password changed successfully!" + System.lineSeparator();
        assertEquals(output, outputStreamCaptor.toString());
    }
}
