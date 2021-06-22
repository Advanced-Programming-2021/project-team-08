package model.cards.data;

import model.enums.CardType;
import model.enums.SpellTrapProperty;

import java.util.ArrayList;

public class TrapCardData extends CardData{
    private static ArrayList<TrapCardData> allTraps = new ArrayList<>();
    private SpellTrapProperty trapProperty;
    private boolean isLimited;

    public TrapCardData() {
        CardData.addCardData(this);
        cardType = CardType.TRAP;
        allTraps.add(this);
    }

    public SpellTrapProperty getTrapProperty() {
        return trapProperty;
    }

    public boolean isLimited() {
        return isLimited;
    }

    public void setLimited(boolean limited) {
        isLimited = limited;
    }

    public void setTrapProperty(SpellTrapProperty trapProperty) {
        this.trapProperty = trapProperty;
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

    @Override
    public String toString() {
        return "Name: " + cardName +
                "\nTrap" +
                "\nType: " + trapProperty +
                "\nDescription: " + cardDescription;
    }
}
