package view.menus;

import controller.GamePlaySceneController;
import model.enums.Phase;

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
            if(userInput.equals("next phase")){
                sceneController.getGameManager().goToNextPhase();
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

    }

    private void standbyPhaseCommand(String userInput) {
        Matcher matcher;

        if(userInput.equals("select -d")){
            sceneController.getGameManager().deselect();
        }

        matcher = Pattern.compile("select ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            sceneController.getGameManager().selectZone(matcher.group(1));
            return;
        }
    }

    private void mainPhaseCommand(String userInput) {
        Matcher matcher;

        matcher = Pattern.compile("select ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            sceneController.getGameManager().selectZone(matcher.group(1));
            return;
        }
    }

    private void battlePhaseCommand(String userInput) {
        Matcher matcher;

        matcher = Pattern.compile("select ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            //sceneController.(matcher.group(1));
            return;
        }
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
