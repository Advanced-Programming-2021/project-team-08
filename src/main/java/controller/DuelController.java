package controller;

import model.Deck;
import view.menus.GamePlayScene;

public class DuelController {

    private User activeUser;
    private GamePlaySceneController gamePlaySceneController;

    public DuelController() {
        activeUser = ApplicationManger.getLoggedInUser();
        if (activeUser == null) activeUser = new User("test", "test", "test");
    }

    public String duelMultiplayer(int rounds, String secondPlayerName) {
        return setupDuel(true, rounds, secondPlayerName);
    }

    public String duelSinglePlayer(int rounds) {
        return setupDuel(false, rounds, null);
    }

    private String setupDuel(boolean isPlayer, int rounds, String secondPlayer) {
        User secondUser = null;
        if (isPlayer) {
            try {
                secondUser = User.getUserByUsername(secondPlayer);
            } catch (Exception e) {
                return "there is no player with this username";
            }
        }
        if (ApplicationManger.getLoggedInUser().getActiveDeck() == null) {
            return ApplicationManger.getLoggedInUser().getUsername() + " has no active deck";
        }
        if (isPlayer) {
            if (secondUser.getActiveDeck() == null) {
                return secondUser.getUsername() + " has no active deck";
            }
        }
        if (!Deck.isThisDeckValid(ApplicationManger.getLoggedInUser().getActiveDeck())) {
            return ApplicationManger.getLoggedInUser().getUsername() + "'s deck is invalid";
        }
        if (isPlayer) {
            if (!Deck.isThisDeckValid(secondUser.getActiveDeck())) {
                return secondUser.getUsername() + "'s deck is invalid";
            }
        }
        gamePlaySceneController = new GamePlaySceneController(new GamePlayScene());
        if (isPlayer) {
            gamePlaySceneController.startDuel(rounds, true, secondUser.getUserData());
        } else {
            gamePlaySceneController.startDuel(rounds, false, null);
        }
        return "";
    }
}
