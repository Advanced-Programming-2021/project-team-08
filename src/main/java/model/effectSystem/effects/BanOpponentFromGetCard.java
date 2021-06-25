package model.effectSystem.effects;

import controller.User;
import model.cards.TrapCard;
import model.effectSystem.Effect;
import model.gameplay.Player;

import java.util.ArrayList;

public class BanOpponentFromGetCard extends Effect {

    int bannedTurn;
    public BanOpponentFromGetCard(ArrayList<String> args) {
        super(args);
        try {
            bannedTurn = Integer.parseInt(args.get(0).trim());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setup() {
        super.setup();
        gameManager.getOnChangeTurn().addListener(() -> {
            if (card.getCardOwner().getTrapBanned() > 0) return;
            gameManager.temporaryChangeTurn();
            gameManager.getScene().log("now it will be " + card.getCardOwner().getUserData().getUsername() + "'s turn");
            gameManager.getScene().showBoard(gameManager.getGameBoardString());
            if (gameManager.getScene().getActivateTrapCommand()) {
                ((TrapCard) card).onActivate();
            }
            gameManager.temporaryChangeTurn();
            gameManager.getScene().log("now it will be " + gameManager.getCurrentTurnPlayer().getUserData().getUsername() + "'s turn");
            gameManager.getScene().showBoard(gameManager.getGameBoardString());
        });
    }


    @Override
    public void activate() {
        Player player;
        if (gameManager.getCurrentTurnPlayer().equals(card.getCardOwner())) player = gameManager.getCurrentTurnOpponentPlayer();
        else player = gameManager.getCurrentTurnPlayer();
        player.setBannedCardTurn(bannedTurn);
    }
}
