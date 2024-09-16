package org.example;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    protected final List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void hit(Deck deck) {
        addCard(deck.drawCard());
    }

    // Check if the player can double down based on the hand value
    public boolean isDoubleDownPossible() {
        return calculateValue() >= 9 && calculateValue() <= 11 && cards.size() == 2;
    }

    public boolean hasBlackJack() {
        return calculateValue() == 21 && cards.size() == 1;
    }

    public int calculateValue() {
        int totalValue = 0;
        int numberOfAces = 0;
        for (Card card : cards) {
            totalValue += card.getValue();

            if (card.getRank() == Rank.ACE) {
                numberOfAces++;
            }
        }
        while (totalValue > 21 && numberOfAces > 0) {
            totalValue -= 10;
            numberOfAces--;
        }
        return totalValue;
    }

    public List<Card> getCards() {
        return cards;
    }


    public void showHand() {
        for (Card card : cards) {
            System.out.println(card);
        }
    }

    // conditions to split hand
    public boolean canSplit() {
        return cards.size() == 2 && cards.get(0).getRank().getValue() == cards.get(1).getRank().getValue();
    }

    public Card getCard(int index) {
        return cards.get(index);
    }

    @Override
    public String toString() {
        return "Hand: " + cards.toString();
    }

    public boolean isBust() {
        return calculateValue() > 21;
    }

    public String getCardByIndex(int i) {
        return cards.get(i).toString();
    }
}