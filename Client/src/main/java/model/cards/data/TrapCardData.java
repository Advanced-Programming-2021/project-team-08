package model.cards.data;

import com.google.gson.annotations.Expose;
import model.enums.CardType;
import model.enums.SpellTrapProperty;

import java.util.ArrayList;

public class TrapCardData extends CardData {
    private static final ArrayList<TrapCardData> allTraps = new ArrayList<>();
    @Expose
    private SpellTrapProperty trapProperty;
    @Expose
    private boolean isLimited;

    public TrapCardData() {
        CardData.addCardData(this);
        cardType = CardType.TRAP;
        allTraps.add(this);
    }

    public static void printAllTraps() {
        for (TrapCardData trapCardData : allTraps) {
            System.out.println("trap name is : " + trapCardData.cardName + " trap id is: " + trapCardData.getCardId());
            System.out.println("trap price is : " + trapCardData.getPrice());
            System.out.println("trap property is : " + trapCardData.getTrapProperty());
            System.out.println("trap description is : " + trapCardData.getCardDescription());
            System.out.println();
        }
    }

    public SpellTrapProperty getTrapProperty() {
        return trapProperty;
    }

    public void setTrapProperty(SpellTrapProperty trapProperty) {
        this.trapProperty = trapProperty;
    }

    public boolean isLimited() {
        return isLimited;
    }

    public void setLimited(boolean limited) {
        isLimited = limited;
    }

    @Override
    public String toString() {
        return "Name: " + cardName +
                "\nTrap" +
                "\nType: " + trapProperty +
                "\nDescription: " + cardDescription;
    }
}
