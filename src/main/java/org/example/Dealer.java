package org.example;

public class Dealer extends Hand {
    public void play(Deck deck) {
        while (true) {
            addCard(deck.drawCard());
            if (calculateValue() >= 17) {
                break;
            }
        }
    }
}
