package controller;

import controller.gameplay.GameManager;
import model.Command;
import model.UserData;
import model.cards.Card;
import model.enums.CommandFieldType;
import model.exceptions.ParseCommandException;
import view.menus.ApplicationManger;
import view.menus.GamePlayScene;

import java.util.HashMap;

public class GamePlaySceneController {
    private final static HashMap<String, CommandFieldType> duelPlayerCommand = new HashMap<String, CommandFieldType>() {{
        put("new", CommandFieldType.BOOLEAN);
        put("second-player", CommandFieldType.STRING);
        put("round", CommandFieldType.INT);
    }};
    private final static HashMap<String, CommandFieldType> duelAICommand = new HashMap<String, CommandFieldType>() {{
        put("new", CommandFieldType.BOOLEAN);
        put("ai", CommandFieldType.BOOLEAN);
        put("round", CommandFieldType.INT);
    }};


    private boolean isDuelStarted = false;
    private GamePlayScene scene;
    private GameManager gameManager;

    public GamePlaySceneController(GamePlayScene scene) {
        this.scene = scene;
    }

    public boolean isDuelStarted() {
        return isDuelStarted;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void duel(String input) {
        Command command;
        try {
            command = Command.parseCommand(input, duelPlayerCommand);
            if (command.getField("new").equals("true")) {
                setupDuel(true, Integer.parseInt(command.getField("round")), command.getField("second-player"));
            }
        } catch (ParseCommandException e) {
            try {
                command = Command.parseCommand(input, duelAICommand);
                if (command.getField("ai").equals("true") && command.getField("new").equals("true")) {
                    setupDuel(false, Integer.parseInt(command.getField("round")), null);
                }
            } catch (ParseCommandException ee) {
                scene.ShowError("Invalid Command");
            }
        }
    }

    private void setupDuel(boolean isPlayer, int rounds, String secondPlayer) {
        User secondUser = null;
        if (isPlayer) {
            try {
                secondUser = User.getUserByUsername(secondPlayer);
            } catch (Exception e) {
                scene.ShowError("there is no player with this username");
                return;
            }
            if (secondUser.getActiveDeck() == null) {
                scene.ShowError(secondUser.getUsername() + " has no active deck");
                return;
            }
            // TODO: ۱۵/۰۵/۲۰۲۱ check if deck is valid
        }

        if (ApplicationManger.getLoggedInUser().getActiveDeck() == null) {
            scene.ShowError(ApplicationManger.getLoggedInUser().getUsername() + " has no active deck");
            return;
        }
        // TODO: ۱۵/۰۵/۲۰۲۱ check if deck is valid


        if (rounds == 1 || rounds == 3) {
            startDuel(rounds, isPlayer, secondUser.getUserData());
        } else {
            scene.ShowError("number of rounds is not supported");
        }
    }

    private void startDuel(int rounds, boolean isPlayer, UserData secondPlayer) {
        // TODO: ۱۸/۰۵/۲۰۲۱ play with AI
        isDuelStarted = true;
<<<<<<< HEAD
        for(int i=1; i<=rounds; i++){
            System.out.println("Round "+i);
            gameManager = new GameManager(ApplicationManger.getLoggedInUser().getUserData(), secondPlayer);
=======
        for (int i = 1; i <= rounds; i++) {
            System.out.println("Round " + i);

>>>>>>> e0fa412a280b75541e41038723e2693c8e9f28ae
        }
    }


}
