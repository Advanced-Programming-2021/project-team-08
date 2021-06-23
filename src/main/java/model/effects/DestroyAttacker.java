package model.effects;

import model.cards.MonsterCard;
import model.gameplay.AttackResult;
import model.gameplay.Player;

import java.util.ArrayList;

public class DestroyAttacker extends Effect{
    private AttackResult attackResult;

    public DestroyAttacker(ArrayList<String> args) {
        super(args);
        ((MonsterCard)card).getOnAttacked().addListener((attackResult)->{
            this.attackResult = attackResult;
            activate();
        });
        /*class myEvent implements Event<AttackResult>{
            @Override
            public void invoke(AttackResult arg) {
                attackResult = arg;
                activate(null);
            }
        }
        ((MonsterCard)card).getOnAttacked().addListener(new myEvent());*/
    }

    @Override
    public void activate() {
        if(attackResult.isDestroyCard2()){
            attackResult.setDestroyCard1(true);
        }
    }
}
