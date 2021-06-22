package model.cards.data;

import model.enums.SpellTrapProperty;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Locale;

public class ReadSpellTrapCardsData {

    public void readSpellTrapData() {
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader("SpellTrap.csv"));
            String row;
            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                if (data.length == 7 || data.length == 8) {
                    readACardData(data);
                } else {
                    System.out.println("couldn't parse a row from SpellTrap.csv file in row ");
                }
            }
            csvReader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("some thing was wrong in csvReader in spell trap reader");
        }
    }

    public void readACardData(String[] data) {
        if (data[1].equals("Trap")) {
            TrapCardData trapCardData = new TrapCardData();
            trapCardData = (TrapCardData) setSpellTrapData(data, trapCardData);
            trapCardData.setLimited(data[4].trim().equals("Limited"));
            trapCardData.setTrapProperty(SpellTrapProperty.valueOf(data[2].trim().toUpperCase(Locale.ROOT).replace("-","_")));
        }else if (data[1].equals("Spell")) {
            SpellCardData spellCardData = new SpellCardData();
            spellCardData = (SpellCardData) setSpellTrapData(data, spellCardData);
            spellCardData.setLimited(data[4].trim().equals("Limited"));
            spellCardData.setTrapProperty(SpellTrapProperty.valueOf(data[2].trim().toUpperCase(Locale.ROOT).replace("-","_")));
        }else {
            System.out.println("the card type is wrong");
        }
    }

    public CardData setSpellTrapData(String[] data, CardData cardData) {
        try {
            cardData.setName(data[0].trim());
            cardData.setCardDescription(data[3].replace(" comma", ",").trim());
            cardData.setPrice(Integer.parseInt(data[5].trim()));
            cardData.setId(Integer.parseInt(data[6].trim()));
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return cardData;
    }

}
