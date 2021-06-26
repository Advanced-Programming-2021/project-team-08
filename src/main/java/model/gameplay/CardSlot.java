package model.gameplay;

import controller.gameplay.GameManager;
import model.cards.Card;
import model.cards.MonsterCard;
import model.cards.SpellCard;
import model.cards.TrapCard;
import model.cards.data.MonsterCardData;
import model.cards.data.SpellCardData;
import model.cards.data.TrapCardData;
import model.enums.*;

import java.util.ArrayList;

public class CardSlot {
    private ZoneType zoneType;
    private ArrayList<Card> cards = new ArrayList<>();
    private boolean isSingular;

    public CardSlot(ZoneType zoneType) {
        this.zoneType = zoneType;
        switch (zoneType) {
            case GRAVEYARD:
            case DECK:
                isSingular = false;
                break;
            case FIELD:
            case MONSTER:
            case SPELL_AND_TRAP:
                isSingular = true;
                break;
        }
    }

    public ZoneType getZoneType() {
        return zoneType;
    }

    public void setCard(Card card) throws Exception {
        if (isSingular) {
            if (cards.size() > 0) {
                throw new Exception("this zone already had card");
            } else {
                cards.add(card);
                card.setCardSlot(this);
            }
            return;
        }
    }

    public boolean isEmpty() {
        return (cards.size() == 0);
    }

    //for singular zone
    public Card getCard() {
        return cards.get(0);
    }

    public Card getAParticularCard(int number){
        return cards.get(number-1);
    }

    public void removeACard(Card card) {
        this.cards.remove(card);
    }

    public void removeCard() {
        cards.clear();
    }

    //for non-singular zone
    public void addCards(ArrayList<Card> cards) {
        this.cards.addAll(cards);
    }

    public ArrayList<Card> getAllCards() {
        return cards;
    }

    public void appendCard(Card card) {
        cards.add(card);
        card.setCardSlot(this);
    }

    public Card drawTopCard() throws Exception {
        if (cards.size() == 0) throw new Exception("There is no card to draw");
        Card c = cards.get(cards.size() - 1);
        cards.remove(cards.size() - 1);
        return c;
    }

    public Card drawParticularMonster(MonsterAttribute monsterAttribute) {
        ArrayList<Card> deckCards = this.getAllCards();
        if (monsterAttribute == null) {
            for (Card card : deckCards) {
                if (card.getCardType().equals(CardType.MONSTER)) {
                    this.removeACard(card);
                    return card;
                }
            }
        } else {
            for (Card card : deckCards) {
                if (card.getCardType().equals(CardType.MONSTER)) {
                    MonsterCard monsterCard = (MonsterCard) card;
                    if (((MonsterCardData) monsterCard.getCardData()).getAttribute().equals(monsterAttribute)) {
                        this.removeACard(card);
                        return card;
                    }
                }
            }
        }
        return null;
    }

    public Card drawParticularSpellTrap(CardType cardType, SpellTrapProperty spellTrapProperty) {
        ArrayList<Card> deckCards = this.getAllCards();
        if (spellTrapProperty == null) {
            for (Card card : deckCards) {
                if (card.getCardType().equals(cardType)) {
                    this.removeACard(card);
                    return card;
                }
            }
        } else {
            for (Card card : deckCards) {
                if (card.getCardType().equals(cardType)) {
                    if (cardType.equals(CardType.SPELL)) {
                        SpellCard spellCard = (SpellCard) card;
                        if (((SpellCardData) spellCard.getCardData()).getSpellProperty().equals(spellTrapProperty)) {
                            this.removeACard(card);
                            return card;
                        }
                    } else if (cardType.equals(CardType.TRAP)) {
                        TrapCardData trapCardData = (TrapCardData) ((TrapCard) card).getCardData();
                        if (trapCardData.getTrapProperty().equals(spellTrapProperty)) {
                            this.removeACard(card);
                            return card;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        switch (zoneType) {
            case GRAVEYARD:
            case DECK:
                return cards.size() + "";
            case FIELD:
            case SPELL_AND_TRAP:
                if (isEmpty()) {
                    return "E";
                } else if (getCard().getCardStatus() == CardStatus.FACE_UP) {
                    return "O";
                } else {
                    return "H";
                }
            case MONSTER:
                if (isEmpty()) {
                    return "E";
                } else {
                    MonsterCard card = (MonsterCard) cards.get(0);
                    if (card.getCardStatus() == CardStatus.FACE_UP) {
                        if (card.isAttackPosition()) {
                            return "OO";
                        } else {
                            return "DO";
                        }
                    } else {
                        return "DH";
                    }
                }
        }
        return "O";
    }
}
