package model;

import model.enums.CommandFieldType;
import model.exceptions.ParseCommandException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandParserTest {

    @Test
    void parseCommand() throws ParseCommandException {
        HashMap<String, CommandFieldType> loginCommandFields = new HashMap<String, CommandFieldType>() {
            {
                put("username", CommandFieldType.STRING);
                put("password", CommandFieldType.STRING);
            }
        };

        Command command = Command.parseCommand("login --username ali --password abc123", loginCommandFields);
        Assertions.assertAll(
                () -> assertEquals("ali", command.getField("username")),
                () -> assertEquals("abc123", command.getField("password"))
        );
    }

    @Test
    @DisplayName("couldn't parse command exception")
    void parseCommandException() {
        HashMap<String, CommandFieldType> loginCommandFields = new HashMap<String, CommandFieldType>() {
            {
                put("username", CommandFieldType.STRING);
                put("password", CommandFieldType.STRING);
            }
        };
        assertThrows(ParseCommandException.class,
                () -> Command.parseCommand("login --username ali abc123", loginCommandFields)
        );
    }
}