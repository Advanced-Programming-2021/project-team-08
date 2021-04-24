package Controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class RegisterControllerTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void registerUser() {
        String input = "create user --password 123 --nickname ali12 --username Ali";
        RegisterController.registerUser(input);
        assertEquals("user created successfully!", outputStreamCaptor.toString());
    }
}