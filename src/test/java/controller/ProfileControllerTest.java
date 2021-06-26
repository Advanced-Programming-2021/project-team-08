package controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.menus.ProfileScene;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfileControllerTest {

    private  final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private User testUser;
    private ProfileController profileController;

    @BeforeEach
    public void setup() {
        System.setOut(new PrintStream(outputStreamCaptor));
        testUser = new User("test", "testing", "test123");
        ApplicationManger.setLoggedInUser(testUser);
        new User ("alaki", "abool", "abool123");
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

    @Test
    public void changeNicknameTest() {
        String input  = "profile change --nickname abool";
        Matcher matcher = Pattern.compile("^profile change (--nickname[^\\n]+)$").matcher(input);
        if (matcher.find()) {
            profileController.changeNickname(matcher.group());
            String output = "this nickname is already used" + System.lineSeparator();
            assertEquals(output, outputStreamCaptor.toString());
        }
        outputStreamCaptor.reset();
        input = "profile change --nickname salam";
        matcher = Pattern.compile("^profile change (--nickname[^\\n]+)$").matcher(input);
        if (matcher.find()) {
            profileController.changeNickname(matcher.group());
            String output = "nickname changed successfully!" + System.lineSeparator();
            assertEquals(output, outputStreamCaptor.toString());
        }
    }

    @Test
    public void changePasswordTest() {
        String input = "profile change --password --current test12 --new test";
        Matcher matcher = Pattern.compile("^profile change --password ([^\\n]+)$").matcher(input);
        if (matcher.find()) {
            profileController.changePassword(matcher.group());
            String output = "current password is invalid" + System.lineSeparator();
            assertEquals(output, outputStreamCaptor.toString());
        }
        outputStreamCaptor.reset();
        input = "profile change --password --current test123 --new test123";
        matcher = Pattern.compile("^profile change --password ([^\\n]+)$").matcher(input);
        if (matcher.find()) {
            profileController.changePassword(matcher.group());
            String output = "please enter a new password" + System.lineSeparator();
            assertEquals(output, outputStreamCaptor.toString());
        }
        outputStreamCaptor.reset();
        input = "profile change --password --current test123 --new test";
        matcher = Pattern.compile("^profile change --password ([^\\n]+)$").matcher(input);
        if (matcher.find()) {
            profileController.changePassword(matcher.group());
            String output = "password changed successfully!" + System.lineSeparator();
            assertEquals(output, outputStreamCaptor.toString());
        }
    }



    @AfterAll
    public static void endWorks() {
        File userFile = new File("users/" + "test" + ".json");
        userFile.delete();
    }
}
