package model.cards.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import javafx.scene.image.Image;
import model.effectSystem.Effect;
import model.enums.CardType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CardData {
    private static ArrayList<CardData> allCardData = new ArrayList<>();
    @Expose
    protected CardType cardType;
    @Expose
    protected String cardName;
    @Expose
    protected int cardId;
    @Expose
    protected int price;
    @Expose
    protected String cardDescription;
    protected ArrayList<Effect> effects = new ArrayList<>();
    @Expose
    private String effectString;
    private Image cardImage;
    private int number = 5;
    private boolean isBanned = false;

    public static CardData getCardByName(String cardName) {
        for (CardData cardData : allCardData) {
            if (cardData.getName().equals(cardName)) return cardData;
        }
        return null;
    }

    public static void addCardData(CardData cardData) {
        allCardData.add(cardData);
    }

    public static ArrayList<CardData> getAllCardData() {
        return allCardData;
    }

    public String getCardName() {
        return cardName;
    }

    public CardType getCardType() {
        return cardType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCardId() {
        return cardId;
    }

    public ArrayList<Effect> getEffects() {
        return effects;
    }

    public String getName() {
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

    public void setId(int id) {
        this.cardId = id;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public Image getCardImage() {
        return cardImage;
    }

    public void setCardImage(Image cardImage) {
        this.cardImage = cardImage;
    }

    public void setEffect(String effectsJson) {
        effectString = effectsJson;
        Matcher matcher = Pattern.compile("\\{[^\\n]+?}").matcher(effectsJson);
        while (matcher.find()) {
            Gson gson = new Gson();
            JsonObject jsonObject = JsonParser.parseString(matcher.group()).getAsJsonObject();
            for (String effectName : jsonObject.keySet()) {
                try {
                    ArrayList<String> args = new ArrayList<>(Arrays.asList(gson.fromJson(jsonObject.get(effectName), String[].class)));
                    effects.add(Effect.getEffectClass(effectName).getConstructor(ArrayList.class).newInstance(args));
                } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            //TODO: set effect must be completed
        }
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void readEffectFromEffectString() {
        setEffect(effectString);
    }


    public  void changeInventory() {

    }
}
