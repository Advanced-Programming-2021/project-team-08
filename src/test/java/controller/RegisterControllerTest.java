package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class RegisterControllerTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
     void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        User testUser = new User("test", "testing", "test123");
    }

    @Test
    void registerUser() {
        String input = "user create --password 123 --nickname ali12 --username Ali";
        RegisterController.registerUser(input);
        assertEquals("user created successfully!" + System.lineSeparator(), outputStreamCaptor.toString());
    }

    @Test
    void registerTest() {
        String input = "user create --password 123 --nickname ali --username test";
        RegisterController.registerUser(input);
        assertEquals("user with username test already exists" + System.lineSeparator(), outputStreamCaptor.toString());
        outputStreamCaptor.reset();
        input = "user create --password 123 --nickname testing --username ali";
        RegisterController.registerUser(input);
        assertEquals("user with nickname testing already exists" + System.lineSeparator(), outputStreamCaptor.toString());
        outputStreamCaptor.reset();
        input = "user create la la la la";
        RegisterController.registerUser(input);
        assertEquals("Invalid command" + System.lineSeparator(), outputStreamCaptor.toString());
        outputStreamCaptor.reset();
    }

    @Test
    void loginTest() {
        String input = "user login --password test --username test";
        RegisterController.loginUser(input);
        String output = "username and password didn't match\r\n";
        assertEquals(output, outputStreamCaptor.toString());
        outputStreamCaptor.reset();
        input = "user login --password test --username wrong";
        RegisterController.loginUser(input);
        output = "username and password didn't match\r\n";
        assertEquals(output, outputStreamCaptor.toString());
        outputStreamCaptor.reset();
        input = "user login --password test123 la la la";
        RegisterController.loginUser(input);
        output = "Invalid command\r\n";
        assertEquals(output, outputStreamCaptor.toString());
    }
}