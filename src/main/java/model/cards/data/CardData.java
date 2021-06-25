package model.cards.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.effectSystem.Effect;
import model.enums.CardType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CardData {
    protected CardType cardType;
    protected String cardName;
    protected int cardId;
    protected int price;
    protected String cardDescription;
    private ArrayList<Effect> effects = new ArrayList<>();
    private static ArrayList<CardData> allCardData = new ArrayList<>();

    public String getCardName() {
        return cardName;
    }

    public CardType getCardType() {
        return cardType;
    }

    public static CardData getCardByName(String cardName) {
        for (CardData cardData : allCardData) {
            if (cardData.getName().equals(cardName)) return cardData;
        }
        return null;
    }

    public int getPrice(){
        return  price;
    }

    public int getCardId(){
        return cardId;
    }

    public ArrayList<Effect> getEffects() {
        return effects;
    }

    public static void addCardData(CardData cardData) {
        allCardData.add(cardData);
    }

    public static ArrayList<CardData> getAllCardData() {
        return allCardData;
    }

    public  String getName() {
        return cardName;
    }

    public void setName(String name) {
        this.cardName = name;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setId(int id) {
        this.cardId = id;
    }

    public void setEffect(String effectsJson){
        Matcher matcher = Pattern.compile("\\{[^\\n]+?\\}").matcher(effectsJson);
        while (matcher.find()){
            Gson gson = new Gson();
            JsonObject jsonObject = JsonParser.parseString(matcher.group()).getAsJsonObject();
            for (String effectName : jsonObject.keySet()){
                try {
                    ArrayList<String> args = new ArrayList<>(Arrays.asList(gson.fromJson(jsonObject.get(effectName), String[].class)));
                    effects.add(Effect.getEffectClass(effectName).getConstructor(ArrayList.class).newInstance(args));
                } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
