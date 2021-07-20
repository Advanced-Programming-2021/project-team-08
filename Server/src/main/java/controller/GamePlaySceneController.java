package controller;

import controller.gameplay.GameManager;
import model.Command;
import model.Deck;
import model.User;
import model.UserData;
import model.enums.CommandFieldType;
import model.exceptions.ParseCommandException;
import view.menus.GamePlayScene;

import java.util.HashMap;

public class GamePlaySceneController {
    private final static HashMap<String, CommandFieldType> duelPlayerCommand = new HashMap<String, CommandFieldType>() {{
        put("new", CommandFieldType.BOOLEAN);
        put("second-player", CommandFieldType.STRING);
        put("rounds", CommandFieldType.INT);
    }};
    private final static HashMap<String, CommandFieldType> duelAICommand = new HashMap<String, CommandFieldType>() {{
        put("new", CommandFieldType.BOOLEAN);
        put("ai", CommandFieldType.BOOLEAN);
        put("rounds", CommandFieldType.INT);
    }};
    private static int currentRound;
    private boolean isDuelStarted = false;
    private GamePlayScene scene;
    private GameManager gameManager;
    private DuelData currentDuelData;

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
                setupDuel(true, Integer.parseInt(command.getField("rounds")), command.getField("second-player"));
            }
        } catch (ParseCommandException e) {
            try {
                command = Command.parseCommand(input, duelAICommand);
                if (command.getField("ai").equals("true") && command.getField("new").equals("true")) {
                    setupDuel(false, Integer.parseInt(command.getField("rounds")), null);
                }
            } catch (ParseCommandException ee) {
                scene.showError("Invalid Command +" + ee);
            }
        }
    }

    private void setupDuel(boolean isPlayer, int rounds, String secondPlayer) {
        User secondUser = null;
        if (isPlayer) {
            try {
                secondUser = User.getUserByUsername(secondPlayer);
            } catch (Exception e) {
                scene.showError("there is no player with this username");
                return;
            }
        }
        //if (ApplicationManger.getLoggedInUser().getActiveDeck() == null) {
        //    scene.showError(ApplicationManger.getLoggedInUser().getUsername() + " has no active deck");
        //    return;
        //}
        if (isPlayer) {
            if (secondUser.getActiveDeck() == null) {
                scene.showError(secondUser.getUsername() + " has no active deck");
                return;
            }
        }
//        if (!Deck.isThisDeckValid(ApplicationManger.getLoggedInUser().getActiveDeck())) {
//            scene.showError(ApplicationManger.getLoggedInUser().getUsername() + "'s deck is invalid");
//            return;
//        }
        if (isPlayer) {
            if (!Deck.isThisDeckValid(secondUser.getActiveDeck())) {
                scene.showError(secondUser.getUsername() + "'s deck is invalid");
                return;
            }
        }

        if (rounds == 1 || rounds == 3) {
            if (isPlayer) {
                startDuel(rounds, true, secondUser.getUserData());
            } else {
                startDuel(rounds, false, null);
            }
        } else {
            scene.showError("number of rounds is not supported");
        }
    }

    public void startDuel(int rounds, boolean isPlayer, UserData secondPlayer) {
        //currentDuelData = new DuelData(rounds, isPlayer, ApplicationManger.getLoggedInUser().getUserData(), secondPlayer);
        currentRound = 1;
        System.out.println("Round " + currentRound);
        //gameManager = new GameManager(currentDuelData.firstPlayer, currentDuelData.secondPlayer, scene, this);
        isDuelStarted = true;
    }

    public void gameFinished(int winnerNumber, int player1LP, int player2LP) {
        isDuelStarted = false;
        scene.log(currentDuelData.setRoundResult(winnerNumber, player1LP, player2LP));

        if (!currentDuelData.isFinished()) {
            currentRound++;
            System.out.println("Round " + currentRound);
            //gameManager = new GameManager(currentDuelData.firstPlayer, currentDuelData.secondPlayer, scene, this);

            isDuelStarted = true;
            return;
        }

        currentDuelData.applyDuelResult();
        scene.log(currentDuelData.getResultString());
    }

    public static class DuelData {
        private int rounds;
        private boolean isPlayer;
        private UserData firstPlayer, secondPlayer;

        private int firstPlayerWins, secondPlayerWins;
        private int maxLP1, maxLP2;

        public DuelData(int rounds, boolean isPlayer, UserData firstPlayer, UserData secondPlayer) {
            this.rounds = rounds;
            this.isPlayer = isPlayer;
            this.firstPlayer = firstPlayer;
            this.secondPlayer = secondPlayer;
            this.firstPlayerWins = 0;
            this.secondPlayerWins = 0;
            this.maxLP1 = 0;
            this.maxLP2 = 0;
        }

        public UserData getFirstPlayer() {
            return firstPlayer;
        }

        public UserData getSecondPlayer() {
            return secondPlayer;
        }

        public boolean isPlayer() {
            return isPlayer;
        }

        public String setRoundResult(int winnerNumber, int player1LP, int player2LP) {
            if (player1LP > maxLP1) maxLP1 = player1LP;
            if (player2LP > maxLP2) maxLP2 = player2LP;

            if (winnerNumber == 1) {
                firstPlayerWins++;
                return firstPlayer.getUsername() + " won the game and the score is: " + firstPlayerWins + "-" + secondPlayerWins;
            } else {
                secondPlayerWins++;
                if (isPlayer)
                    return secondPlayer.getUsername() + " won the game and the score is: " + firstPlayerWins + "-" + secondPlayerWins;
                else
                    return "AI won the game and the score is: " + firstPlayerWins + "-" + secondPlayerWins;
            }
        }

        public String getResultString() {
            if (firstPlayerWins > secondPlayerWins) {
                return firstPlayer.getUsername() + " won the whole match with score: " + firstPlayerWins + "-" + secondPlayerWins;
            } else {
                if (isPlayer)
                    return secondPlayer.getUsername() + " won the whole match with score: " + firstPlayerWins + "-" + secondPlayerWins;
                else
                    return "AI won the whole match with score: " + firstPlayerWins + "-" + secondPlayerWins;
            }
        }

        public void applyDuelResult() {
            if (firstPlayerWins > secondPlayerWins) {
                firstPlayer.addPoint(rounds * 1000);
                firstPlayer.addMoney(rounds * (1000 + maxLP1));
                if (isPlayer) secondPlayer.addMoney(rounds * (100));
            } else {
                if (isPlayer) secondPlayer.addPoint(rounds * 1000);
                if (isPlayer) secondPlayer.addMoney(rounds * (1000 + maxLP2));
                firstPlayer.addMoney(rounds * (100));
            }
        }

        public boolean isFinished() {
            if (firstPlayerWins == 2 || secondPlayerWins == 2) return true;
            return currentRound >= rounds;
        }
    }
}
