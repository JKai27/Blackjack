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

    public boolean isFaceUpCardAce() {
        return hand.cards.get(0).getRank() == Rank.ACE;
    }



        // Method for insurance scenario
        public boolean drawForInsurance(Deck deck) {
            // Draw one card
            Card drawnCard = deck.drawCard();
            hand.addCard(drawnCard);
            System.out.println("Dealer draws for insurance: " + drawnCard);

            // Check if dealer has Blackjack
            if (hand.calculateValue() == 21) {
                System.out.println("Dealer has a Blackjack!");
                // Here you can signal to the game that the player wins insurance and loses their original bet
                return true; // Indicating dealer has blackjack
            } else {
                System.out.println("Dealer does not have a Blackjack.");
                return false; // Indicating dealer does not have blackjack
            }
        }


    public Hand getHand() {
        return hand;
    }
}
