package model.effects;

import controller.gameplay.GameManager;
import model.gameplay.Player;
import org.reflections.Reflections;

import java.util.Set;

public abstract class Effect {
    protected static GameManager gameManager;
    private static Set<Class<? extends Effect>> allEffects;
    static {
        Reflections r = new Reflections("model.effects");
        allEffects = r.getSubTypesOf(Effect.class);
    }

    public static void setGameManager(GameManager gameManager) {
        Effect.gameManager = gameManager;
    }

    public static Class<? extends Effect> getEffectClass(String className){
        return allEffects.stream().filter(c -> c.getName().equals(className)).findFirst().orElse(null);
    }

    public Effect(Object[] args) {
    }

    public abstract void activate(Player cardOwner);
}
