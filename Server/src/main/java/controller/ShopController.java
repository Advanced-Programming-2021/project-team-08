package controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.User;
import model.cards.Card;
import model.cards.data.CardData;
import model.enums.MessageType;

public class ShopController extends ServerController{
    @Override
    public String getServerMessage(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();

        String methodName = jsonObject.get("method").getAsString();
        switch (methodName) {
            case "buy":
                return buyCard(input);
            case "getInventory":
                return getInventory(input);
            case "banCard" :
                return banCard(input);
            case "freeCard" :
                return freeCard(input);
            default:
                return serverMessage(MessageType.ERROR, "invalid method name", null);
        }
    }

    private String freeCard(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        CardData cardData = CardData.getCardByName(jsonObject.get("name").getAsString());
        User user = getUserByToken(jsonObject.get("token").getAsString());
        if (!user.getUsername().equals("admin")) return serverMessage(MessageType.ERROR, "you don't have access to do that", null);
        if (cardData == null) return serverMessage(MessageType.ERROR, "invalid card name", null);
        cardData.setBanned(false);
        return serverMessage(MessageType.SUCCESSFUL, "you successfully freed the card", null);
    }

    private String banCard(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        CardData cardData = CardData.getCardByName(jsonObject.get("name").getAsString());
        User user = getUserByToken(jsonObject.get("token").getAsString());
        if (!user.getUsername().equals("admin")) return serverMessage(MessageType.ERROR, "you don't have access to do that", null);
        if (cardData == null) return serverMessage(MessageType.ERROR, "invalid card name", null);
        cardData.setBanned(true);
        return serverMessage(MessageType.SUCCESSFUL, "you successfully banned the card", null);
    }

    private String buyCard(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        CardData cardData = CardData.getCardByName(jsonObject.get("name").getAsString());
        User user = getUserByToken(jsonObject.get("token").getAsString());
        if (cardData == null) return serverMessage(MessageType.ERROR, "invalid card name", null);
        else if (cardData.getNumber() < 1) return serverMessage(MessageType.ERROR, "this card is finished in inventory", null);
        else if (cardData.isBanned()) return serverMessage(MessageType.ERROR, "the card is banned", null);
        else if (user.getUserData().getMoney() < cardData.getPrice()) return serverMessage(MessageType.ERROR, "not enough money", null);
        else {
            cardData.setNumber(cardData.getNumber() - 1);
            user.getUserData().decreaseMoney(cardData.getPrice());
            user.getUserData().addCard(cardData.getCardId());
            return serverMessage(MessageType.SUCCESSFUL, "you bought the card successfully", null);
        }
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
