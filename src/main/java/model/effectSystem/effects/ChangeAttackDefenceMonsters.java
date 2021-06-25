package model.effectSystem.effects;

import model.effectSystem.FieldEffect;
import model.enums.MonsterAttribute;

import java.util.ArrayList;
import java.util.Locale;

public class ChangeAttackDefenceMonsters extends FieldEffect{

    private int increasedAttack;
    private int increasedDefence;

    private ArrayList<MonsterAttribute> attributes = new ArrayList<>();
    public ChangeAttackDefenceMonsters(ArrayList<String> args) {
        super(args);
        try {
            increasedAttack = Integer.parseInt(args.get(0).trim());
            increasedDefence = Integer.parseInt(args.get(1).trim());
            int i = 2;
            while (args.get(i) != null) {
                attributes.add(MonsterAttribute.valueOf(args.get(i).toUpperCase(Locale.ROOT).trim()));
                i++;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setup() {
        super.setup();
        gameManager.getOnSummonACard().addListener((summoner) -> {

        });
    }

    @Override
    public void activate() {

    }
}
