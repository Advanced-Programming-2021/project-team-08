package model.effectSystem;

import controller.gameplay.GameManager;
import model.cards.Card;
import model.cards.TrapCard;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Set;

public abstract class Effect {
    protected static GameManager gameManager;
    protected Card card;
    private static Set<Class<? extends Effect>> allEffects;
    static {
        Reflections r = new Reflections("model.effectSystem.effects");
        allEffects = r.getSubTypesOf(Effect.class);
    }

    public static Class<? extends Effect> getEffectClass(String className){
        return allEffects.stream().filter(c -> c.getSimpleName().equals(className)).findFirst().orElse(null);
    }

    public static void setGameManager(GameManager gameManager) {
        Effect.gameManager = gameManager;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setup(){

    }

    protected void trapActivateQuestion(){
        if (card.getCardOwner().getTrapBanned() > 0) return;
        gameManager.temporaryChangeTurn();
        gameManager.getScene().log("now it will be " + card.getCardOwner().getUserData().getUsername() + "'s turn");
        gameManager.getScene().showBoard(gameManager.getGameBoardString());
        if (gameManager.getScene().getActivateTrapCommand()) {
            ((TrapCard) card).onActivate();
        }
        gameManager.temporaryChangeTurn();
    }

    public Effect(ArrayList<String> args) {
    }

    public abstract void activate();
}
