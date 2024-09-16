package org.example;

public class Dealer {
    private final Hand hand;

    public Dealer(String name) {
        this.hand = new Hand();
    }

    public void play(Deck deck) {
        // Dealer keeps drawing until hand value is at least 17
        while (hand.calculateValue() < 17) {
            hand.addCard(deck.drawCard());
            System.out.println("Dealer draws: " + hand.getCards().get(hand.getCards().size() - 1));
        }
        System.out.println("Dealer stands with a value of: " + hand.calculateValue());
    }


    public Hand getHand() {
        return hand;
    }
}
