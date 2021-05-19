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

<<<<<<< HEAD
<<<<<<< HEAD
        if(sceneController.isDuelStarted()){
            matcher= Pattern.compile("change phase ([^\\n]+)").matcher(userInput);
            if (matcher.matches()){
                //sceneController.(matcher.group(1));
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
=======
=======
>>>>>>> e0fa412a280b75541e41038723e2693c8e9f28ae
        if (sceneController.isDuelStarted()) {
            processGamePlayCommand(userInput);
>>>>>>> e0fa412a280b75541e41038723e2693c8e9f28ae
            return 1;
        }

        System.out.println("Invalid Command!");

        return 1;
    }

<<<<<<< HEAD
<<<<<<< HEAD
    private void drawPhaseCommand(String userInput){

    }

    private void standbyPhaseCommand(String userInput){
        Matcher matcher;

        matcher= Pattern.compile("select ([^\\n]+)").matcher(userInput);
        if (matcher.matches()){
            sceneController.getGameManager().selectCard(matcher.group(1));
            return;
        }
    }

    private void mainPhaseCommand(String userInput){
        Matcher matcher;

        matcher= Pattern.compile("select ([^\\n]+)").matcher(userInput);
        if (matcher.matches()){
            //sceneController.(matcher.group(1));
            return;
        }
    }

    private void battlePhaseCommand(String userInput){
        Matcher matcher;

        matcher= Pattern.compile("select ([^\\n]+)").matcher(userInput);
        if (matcher.matches()){
            //sceneController.(matcher.group(1));
            return;
        }
    }

    private void endPhaseCommand(String userInput){
=======
    private void processGamePlayCommand(String userInput) {
>>>>>>> e0fa412a280b75541e41038723e2693c8e9f28ae
        Matcher matcher;

=======
    private void processGamePlayCommand(String userInput) {
        Matcher matcher;

>>>>>>> e0fa412a280b75541e41038723e2693c8e9f28ae
        matcher = Pattern.compile("select ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            //sceneController.(matcher.group(1));
            return;
        }
    }
}
