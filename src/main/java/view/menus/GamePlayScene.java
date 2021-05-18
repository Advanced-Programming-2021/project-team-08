package view.menus;

import controller.GamePlaySceneController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GamePlayScene extends Scene{
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

        matcher= Pattern.compile("duel ([^\\n]+)").matcher(userInput);
        if (matcher.matches()){
            sceneController.duel(matcher.group(1));
            return 1;
        }

        return 1;
    }
}
