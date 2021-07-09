package controller;

import controller.gameplay.AI_Player;
import model.Deck;
import view.menus.GamePlayScene;

public class DuelController {

    private User activeUser;
    private static GamePlaySceneController.DuelData currentDuelData;

    public static GamePlaySceneController.DuelData getCurrentDuelData() {
        return currentDuelData;
    }

    public DuelController() {
        activeUser = ApplicationManger.getLoggedInUser();
        if (activeUser == null) ApplicationManger.setLoggedInUser(new User("text", "test", "test"));
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

        if (isPlayer) {
            currentDuelData = new GamePlaySceneController.DuelData(rounds, true, activeUser.getUserData(), secondUser.getUserData());
        } else {
            currentDuelData = new GamePlaySceneController.DuelData(rounds, false, activeUser.getUserData(), AI_Player.getAIUserData());
        }
        ApplicationManger.goToScene("gameplayScene.fxml");
        return "";
    }
}
