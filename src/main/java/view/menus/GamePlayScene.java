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
            if (userInput.equals("next phase")) {
                sceneController.getGameManager().goToNextPhase();
                return 1;
            }

            switch (sceneController.getGameManager().getCurrentPhase()) {
                case DRAW:
                    drawPhaseCommand(userInput);
                    break;
                case STANDBY:
                    standbyPhaseCommand(userInput);
                    break;
                case MAIN:
                    mainPhaseCommand(userInput);
                    break;
                case BATTLE:
                    battlePhaseCommand(userInput);
                    break;
                case END:
                    endPhaseCommand(userInput);
                    break;
            }
            return 1;
        }

        System.out.println("Invalid Command!");

        return 1;
    }

    private void drawPhaseCommand(String userInput) {
        System.out.println("Invalid Command!");
    }

    private void standbyPhaseCommand(String userInput) {
        Matcher matcher;

        if (userInput.equals("select -d")) {
            sceneController.getGameManager().deselect();
            return;
        }

        matcher = Pattern.compile("select ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            sceneController.getGameManager().selectCard(matcher.group(1));
            return;
        }

        System.out.println("Invalid Command!");
    }

    private void mainPhaseCommand(String userInput) {
        Matcher matcher;

        if (userInput.equals("select -d")) {
            sceneController.getGameManager().deselect();
            return;
        }
        matcher = Pattern.compile("select ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            sceneController.getGameManager().selectCard(matcher.group(1));
            return;
        }
        if (userInput.equals("summon")) {
            sceneController.getGameManager().summonCard();
            return;
        }
        if (userInput.equals("set")) {
            sceneController.getGameManager().setCard();
            return;
        }

        System.out.println("Invalid Command!");
    }

    private void battlePhaseCommand(String userInput) {
        Matcher matcher;

        if (userInput.equals("select -d")) {
            sceneController.getGameManager().deselect();
            return;
        }
        matcher = Pattern.compile("select ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            sceneController.getGameManager().selectCard(matcher.group(1));
            return;
        }

        System.out.println("Invalid Command!");
    }

    private void endPhaseCommand(String userInput) {

    }

    public void showPhase(String phaseName) {
        System.out.println("Phase: " + phaseName);
    }

    public void showBoard(String gameBoard) {
        System.out.println(gameBoard);
    }
}
