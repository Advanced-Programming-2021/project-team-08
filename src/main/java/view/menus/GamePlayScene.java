package view.menus;

import controller.GamePlaySceneController;
import model.cards.Card;
import model.effectSystem.EquipEffect;
import model.gameplay.Player;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GamePlayScene extends Scene {
    private GamePlaySceneController sceneController;
    private boolean waitForAI = false;

    public void setWaitForAI(boolean waitForAI) {
        this.waitForAI = waitForAI;
    }

    @Override
    public void start() {
        sceneController = new GamePlaySceneController(this);
        super.start();
    }

    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();
        Matcher matcher;

        if(waitForAI) return 1;

        if (cheatCommand(userInput) == 1) return 1;

        if ((Pattern.compile("^menu enter ([^\n]+)$").matcher(userInput)).find()) {
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
                try {
                    sceneController.getGameManager().selectCard(matcher.group(1));
                } catch (Exception e) {
                    showError(e.getMessage());
                }
                return 1;
            }
            if (userInput.equals("surrender")) {
                sceneController.getGameManager().surrender();
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
        if (sceneController.isDuelStarted()) {
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
            matcher = Pattern.compile("duel set-winner ([^\\n]+)").matcher(userInput);
            if (matcher.matches()) {
                sceneController.getGameManager().setWinner_C(matcher.group(1));
                return 1;
            }
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
        if (userInput.equals("flip-summon")) {
            sceneController.getGameManager().flipSummonCard();
            return;
        }
        if (userInput.equals("set")) {
            sceneController.getGameManager().setCard();
            return;
        }
        matcher = Pattern.compile("set --position (attack|defence)").matcher(userInput);
        if (matcher.matches()) {
            sceneController.getGameManager().setPosition(matcher.group(1));
            return;
        }
        if (userInput.equals("activate effect")) {
            sceneController.getGameManager().activateCard();
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
        if (userInput.equals("attack direct")) {
            sceneController.getGameManager().attackDirect();
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


    public boolean getActivateTrapCommand() {
        System.out.println("do you want to activate your trap and spell?");
        String input = scanner.nextLine();
        while (!input.equals("yes") && !input.equals("no")) {
            System.out.println("you should enter yes/no");
            input = scanner.nextLine();
        }
        return input.equals("yes");
    }

    public Card getSelectedCard(EquipEffect equipEffect, Player player) {
        System.out.println("enter the card that you want to equip");
        String input = scanner.nextLine();
        int number;
        try {
            number = Integer.parseInt(input);
        }catch (Exception e) {
            System.out.println("you should enter a number");
            return getSelectedCard(equipEffect, player);
        }
        try {
            if (equipEffect.hasCardCondition(player.getPlayerBoard().getMonsterZone().get(number).getCard())) {
                return player.getPlayerBoard().getMonsterZone().get(number).getCard();
            }
            else {
                System.out.println("selected card has not the condition");
                return getSelectedCard(equipEffect, player);
            }
        }catch (Exception e) {
            return getSelectedCard(equipEffect, player);
        }
    }


    public boolean getDestroyingAMonsterCommand() {
        System.out.println("do you want to destroy an opponent monster?");
        String input = scanner.nextLine();
        while (!input.equals("yes") && !input.equals("no")) {
            System.out.println("you should enter yes/no");
            input = scanner.nextLine();
        }
        return input.equals("yes");
    }

    public int getPlaceOfMonster(){
        System.out.println("insert number of place of monster that you want to destroy.");
        String input = scanner.nextLine();
        while (!input.equals("1")&&!input.equals("2")&&!input.equals("3")&&!input.equals("4")&&!input.equals("5")){
            System.out.println("you should enter a number between 1 to 5.");
            input = scanner.nextLine();
        }
        return Integer.parseInt(input);
    }

    public int getPlaceOfMonsterTransfer(){
        System.out.println("insert number of place of monster that you want to transfer.");
        String input = scanner.nextLine();
        while (!input.equals("1")&&!input.equals("2")&&!input.equals("3")&&!input.equals("4")&&!input.equals("5")){
            System.out.println("you should enter a number between 1 to 5.");
            input = scanner.nextLine();
        }
        return Integer.parseInt(input);
    }
}
