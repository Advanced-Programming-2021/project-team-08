package model.cards.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.effects.Effect;
import model.enums.CardType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

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
        Gson gson = new Gson();
        JsonObject jsonObject = JsonParser.parseString(effectsJson).getAsJsonObject();
        for (String effectName : jsonObject.keySet()){
            try {
                Object[] args = gson.fromJson(jsonObject.get(effectName), Object[].class);
                effects.add(Effect.getEffectClass(effectName).getConstructor().newInstance(args));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
