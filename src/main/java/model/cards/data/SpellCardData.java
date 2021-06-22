package model.cards.data;

import model.enums.CardType;
import model.enums.SpellTrapProperty;

import java.util.ArrayList;

public class SpellCardData extends CardData{
    private static ArrayList<SpellCardData> allSpellCard = new ArrayList<>();
    private SpellTrapProperty spellProperty;
    private boolean isLimited;

    public SpellCardData() {
        CardData.addCardData(this);
        allSpellCard.add(this);
        cardType = CardType.SPELL;
    }

    public SpellTrapProperty getSpellProperty() {
        return spellProperty;
    }

    public boolean isLimited() {
        return isLimited;
    }

    public void setLimited(boolean limited) {
        isLimited = limited;
    }

    public void setTrapProperty(SpellTrapProperty spellProperty) {
        this.spellProperty = spellProperty;
    }

    public static void printAllTraps() {
        for (SpellCardData spellCardData : allSpellCard) {
            System.out.println("spell name is : " + spellCardData.cardName + " spell id is: " + spellCardData.getCardId());
            System.out.println("spell price is : " + spellCardData.getPrice());
            System.out.println("spell property is : " + spellCardData.getSpellProperty());
            System.out.println("spell description is : " + spellCardData.getCardDescription());
            System.out.println();
        }
    }

    @Override
    public String toString() {
        return "Name: " + cardName +
                "\nSpell" +
                "\nType: " + spellProperty +
                "\nDescription: " + cardDescription;
    }
}
