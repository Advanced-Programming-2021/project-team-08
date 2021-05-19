package view.menus;

import controller.GamePlaySceneController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GamePlayScene extends Scene {
    private GamePlaySceneController sceneController;

    @Override
    public void start() {
        sceneController = new GamePlaySceneController(this);
        super.start();
    }

    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();
        Matcher matcher;

        matcher = Pattern.compile("duel ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            sceneController.duel(matcher.group(1));
            return 1;
        }

        if (sceneController.isDuelStarted()) {
            processGamePlayCommand(userInput);
            return 1;
        }

        System.out.println("Invalid Command!");

        return 1;
    }

    private void processGamePlayCommand(String userInput) {
        Matcher matcher;

        matcher = Pattern.compile("select ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            //sceneController.(matcher.group(1));
            return;
        }
    }
}
