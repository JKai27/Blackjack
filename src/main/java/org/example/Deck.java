package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;
    private static final int NUMBER_OF_CARDS_IN_A_DECK = 52;


    // The Deck-Constructor makes a Single deck out of 6 decks with 52 cards each deck
    public Deck() {
        cards = new ArrayList<>(NUMBER_OF_CARDS_IN_A_DECK);
        int numberOfDecks = 6;
        for (int i = 0; i < numberOfDecks; i++) {

            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    cards.add(new Card(rank, suit));
                }
            }
        }
        shuffle();
    }

    // Shuffles the deck
    public void shuffle() {
        Collections.shuffle(cards);
    }

    // draws the last card out of deck.
    public Card drawCard() {
        return cards.isEmpty() ? null : cards.remove(0);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    @Override
    public String toString() {
        return "Deck: " + cards.toString();
    }
}
