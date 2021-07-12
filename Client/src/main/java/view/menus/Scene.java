package view.menus;

import model.cards.Card;
import model.cards.data.CardData;

import java.util.Scanner;

public abstract class Scene {
    protected static Scanner scanner = new Scanner(System.in);

    public void start() {
        while (getUserCommand() != 0) ;
    }

    protected abstract int getUserCommand();

    public void showError(String errorMessage) {
        System.out.println(errorMessage);
    }

    public void log(String message) {
        System.out.println(message);
    }

    public void showCard(Card card) {
        if (card == null) {
            showError("card with this name doesn't exist");
            return;
        }
        System.out.println(card.getCardData().toString());
    }

    public void showCard(CardData cardData) {
        if (cardData == null) {
            showError("card with this name doesn't exist");
            return;
        }
        System.out.println(cardData);
    }
}
