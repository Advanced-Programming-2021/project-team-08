package controller;

import model.Command;
import model.enums.CommandFieldType;
import model.exceptions.ParseCommandException;
import view.menus.ApplicationManger;
import view.menus.GamePlayScene;

import java.util.HashMap;

public class GamePlaySceneController {
    private static HashMap<String, CommandFieldType> duelPlayerCommand = new HashMap<String, CommandFieldType>() {{
        put("new", CommandFieldType.BOOLEAN);
        put("second-player", CommandFieldType.STRING);
        put("round", CommandFieldType.INT);
    }};
    private static HashMap<String, CommandFieldType> duelAICommand = new HashMap<String, CommandFieldType>() {{
        put("new", CommandFieldType.BOOLEAN);
        put("ai", CommandFieldType.BOOLEAN);
        put("round", CommandFieldType.INT);
    }};

    private GamePlayScene scene;

    public GamePlaySceneController(GamePlayScene scene) {
        this.scene = scene;
    }

    public int duel(String input) {
        Command command;
        try {
            command = Command.parseCommand(input, duelPlayerCommand);
            return duelPlayer(command);
        } catch (ParseCommandException e) {
            try {
                command = Command.parseCommand(input, duelAICommand);
                return duelAI(command);
            } catch (ParseCommandException ee) {
                scene.ShowError("Invalid Command");
            }
        }
        return 0;
    }

    private int duelPlayer(Command command){
        try {
            User secondUser = User.getUserByUsername(command.getField("second-player"));
            if (ApplicationManger.getLoggedInUser().getActiveDeck() == null) {
                scene.ShowError(ApplicationManger.getLoggedInUser().getUsername() + " has no active deck");
                return 0;
            }
            if (secondUser.getActiveDeck() == null) {
                scene.ShowError(secondUser.getUsername() + " has no active deck");
                return 0;
            }
            // TODO: ۱۵/۰۵/۲۰۲۱ check if deck is valid
            if(command.getField("round")!="1" && command.getField("round")!="3"){
                scene.ShowError("number of rounds is not supported");
                return 0;
            }


        } catch (Exception e) {
            scene.ShowError("there is no player with this username");
        }
        return 0;
    }

    private int duelAI(Command command){
        return 0;
    }
}
