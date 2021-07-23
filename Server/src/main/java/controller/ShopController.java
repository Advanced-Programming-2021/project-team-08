package controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.User;
import model.cards.data.CardData;
import model.enums.MessageType;

public class ShopController extends ServerController {

    private static final ShopController shopController = new ShopController();

    public static ShopController getInstance() {
        return shopController;
    }

    @Override
    public String getServerMessage(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();

        String methodName = jsonObject.get("method").getAsString();
        switch (methodName) {
            case "buy":
                return buyCard(input);
            case "getInventory":
                return getInventory(input);
            case "sell":
                return sellCard(input);
            default:
                return serverMessage(MessageType.ERROR, "invalid method name", null);
        }
    }

    private String sellCard(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        CardData cardData = CardData.getCardByName(jsonObject.get("name").getAsString());
        User user = getUserByToken(jsonObject.get("token").getAsString());
        if (cardData == null) return serverMessage(MessageType.ERROR, "invalid card name", null);
        user.getUserData().addMoney(cardData.getPrice());
        user.getUserData().removeCard(cardData.getCardId());
        cardData.setNumber(cardData.getNumber() + 1);
        return serverMessage(MessageType.SUCCESSFUL, "you sold the card successfully", null);
        //TODO save to csv
    }

    public void freeCard(String name) {
        CardData cardData = CardData.getCardByName(name);
        if (cardData == null) {
            System.out.println("invalid card name");
            return;
        }
        cardData.setBanned(false);
        System.out.println("card freed successfully");
        //TODO save to csv
    }

    public void banCard(String name) {
        CardData cardData = CardData.getCardByName(name);
        if (cardData == null) {
            System.out.println("invalid card name");
            return;
        }
        cardData.setBanned(true);
        System.out.println("card banned successfully");
    }

    public void changeInventory(String name, int newValue) {
        CardData cardData = CardData.getCardByName(name);
        if (cardData == null) {
            System.out.println("invalid card name");
            return;
        }
        cardData.changeInventory(newValue);
    }


    private String buyCard(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        CardData cardData = CardData.getCardByName(jsonObject.get("name").getAsString());
        User user = getUserByToken(jsonObject.get("token").getAsString());
        if (cardData == null) return serverMessage(MessageType.ERROR, "invalid card name", null);
        else if (cardData.getNumber() < 1)
            return serverMessage(MessageType.ERROR, "this card is finished in inventory", null);
        else if (cardData.isBanned()) return serverMessage(MessageType.ERROR, "the card is banned", null);
        else if (user.getUserData().getMoney() < cardData.getPrice())
            return serverMessage(MessageType.ERROR, "not enough money", null);
        else {
            cardData.setNumber(cardData.getNumber() - 1);
            user.getUserData().decreaseMoney(cardData.getPrice());
            user.getUserData().addCard(cardData.getCardId());
            return serverMessage(MessageType.SUCCESSFUL, "you bought the card successfully", null);
        }
        //TODO save to csv
    }

    private String getInventory(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        CardData cardData = CardData.getCardByName(jsonObject.get("name").getAsString());
        if (cardData == null) return serverMessage(MessageType.ERROR, "invalid card name", null);
        JsonObject object = new JsonObject();
        object.addProperty("number", cardData.getNumber());
        object.addProperty("isBanned", cardData.isBanned());
        return serverMessage(MessageType.SUCCESSFUL, "inventory successfully sent", object.toString());
    }
}
