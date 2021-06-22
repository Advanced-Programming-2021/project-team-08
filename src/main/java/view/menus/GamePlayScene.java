package view.menus;

import controller.GamePlaySceneController;
import model.cards.Card;

import java.util.ArrayList;
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

        if (cheatCommand(userInput) == 1) return 1;

        if ((Pattern.compile("^menu enter ([A-Za-z]+)$").matcher(userInput)).find()) {
            System.out.println("menu navigation is not possible");
            return 1;
        }
        if (userInput.equals("menu show-current")) {
            System.out.println("Duel Menu");
            return 1;
        }
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
            if (userInput.equals("select -d")) {
                sceneController.getGameManager().deselect();
                return 1;
            }
            if (userInput.equals("card show --selected")) {
                sceneController.getGameManager().showSelectedCard();
                return 1;
            }
            if (userInput.equals("show graveyard")) {
                sceneController.getGameManager().showGraveyard();
                return 1;
            }
            matcher = Pattern.compile("select ([^\\n]+)").matcher(userInput);
            if (matcher.matches()) {
                sceneController.getGameManager().selectCard(matcher.group(1));
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

    private int cheatCommand(String userInput) {
        Matcher matcher = Pattern.compile("add card to hand ([^\\n]+)").matcher(userInput);
        if (matcher.matches()) {
            sceneController.getGameManager().addCard_C(matcher.group(1));
            return 1;
        }
        matcher = Pattern.compile("increase --LP ([0-9]+)").matcher(userInput);
        if (matcher.matches()) {
            sceneController.getGameManager().increaseLP_C(Integer.parseInt(matcher.group(1)));
            return 1;
        }
        return 0;
    }

    private void drawPhaseCommand(String userInput) {
        System.out.println("Invalid Command!");
    }

    private void standbyPhaseCommand(String userInput) {
        Matcher matcher;

        System.out.println("Invalid Command!");
    }

    private void mainPhaseCommand(String userInput) {
        Matcher matcher;

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

        matcher = Pattern.compile("attack ([1-5])").matcher(userInput);
        if (matcher.matches()) {
            sceneController.getGameManager().attack(Integer.parseInt(matcher.group(1)));
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

    public void showGraveyard(ArrayList<Card> cards) {
        if (cards.size() == 0) {
            System.out.println("graveyard empty");
        } else {
            for (int i = 1; i <= cards.size(); i++) {
                System.out.println(i + ". " + cards.get(i - 1).toString());
            }
        }
    }

    public int getTributeCommand() throws Exception {
        System.out.println("enter tribute monster number:");
        String input = scanner.nextLine();
        if (input.equals("cancel")) {
            throw new Exception("operation canceled");
        }
        return Integer.parseInt(input);
    }
}
