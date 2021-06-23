package model.effectSystem;

import java.util.ArrayList;

public abstract class ContinuousEffect extends Effect {
    protected boolean isActive = false;
    public ContinuousEffect(ArrayList<String> args) {
        super(args);
    }
}
