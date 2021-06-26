package model.effectSystem.effects;

import model.effectSystem.Effect;
import model.gameplay.Player;

import java.util.ArrayList;

public class BanOpponentTrapUse extends Effect {
    public BanOpponentTrapUse(ArrayList<String> args) {
        super(args);
    }

    @Override
    public void setup() {
        super.setup();
        card.getOnDestroyEvent().addListener(() -> {
            Player opponentPlayer = getOpponentPlayer();
            opponentPlayer.setTrapBanned(opponentPlayer.getTrapBanned() - 1);
        });
    }

    @Override
    public void activate() {
        Player opponentPlayer = getOpponentPlayer();
        opponentPlayer.setTrapBanned(opponentPlayer.getTrapBanned() + 1);
    }

    private Player getOpponentPlayer() {
        if (card.getCardOwner().equals(gameManager.getCurrentTurnPlayer()))
            return gameManager.getCurrentTurnOpponentPlayer();
        return gameManager.getCurrentTurnPlayer();
    }
}
