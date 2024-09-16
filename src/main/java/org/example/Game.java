package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {
    private final Deck deck;
    private final Player player;
    private final Dealer dealer;
    private final int MIN_STAKE = 10;
    private final int MAX_STAKE = 500;

    public Game(Player player) {
        this.player = player;
        this.dealer = new Dealer("Casino Bamberg");
        this.deck = new Deck();
    }

    public void start() {
        System.out.println("Starting Blackjack...");

        // Initialize or reset the game state
        player.getHandList().clear();
        dealer.getHand().cards.clear();
        deck.shuffle();

        if (!checkIfPlayerCanPlay()) return;

        int betAmount = getValidBetAmount();
        player.placeBet(betAmount);
        System.out.println("\nBet placed: " + betAmount);

        // Deal initial cards
        dealInitialCards();

        // Display initial hands
        displayInitialHands();

        // Check for immediate Blackjack
        if (checkBlackJack(player.getHandList().get(0))) {
            handleBlackJack();
            return;
        }

        // Provide option to split if possible
        if (player.getHandList().get(0).canSplit()) {
            System.out.println("You can split your hand. Do you want to split? (y/n)");
            Scanner scanner = new Scanner(System.in);
            if (scanner.next().equalsIgnoreCase("y")) {
                split(); // update player's hand list with split hands and then call payersTurn()
            }
        }

        // Player's turn for each hand (including split hands)
        playersTurn();

        // Dealer's turn (if player has not busted)
        boolean noHandsBusted = true;

        for (Hand hand : player.getHandList()) {
            if (hand.isBust()) {
                noHandsBusted = false; // If any hand is busted, set the flag to false
                break; // No need to check further, exit the loop
            }
        }

        if (noHandsBusted) {
            dealer.play(deck); // Dealer plays if none of the player's hands have busted
        }


        // Final hands and determine the outcome
        showFinalHands();
        determineWinner();
    }

    private boolean checkIfPlayerCanPlay() {
        if (player.getMoney() < MIN_STAKE) {
            System.out.println("\nInsufficient funds to play! Minimum stake required: " + MIN_STAKE);
            return false;
        }
        return true;
    }

    private void playersTurn() {
        Scanner scanner = new Scanner(System.in);

        for (Hand hand : player.getHandList()) {
            System.out.println("Your turn for this hand:");
            hand.showHand();

            while (hand.calculateValue() < 21) {
                System.out.println("Your hand value: " + hand.calculateValue());

                // Double down option
                if (hand.isDoubleDownPossible() && player.getMoney() >= player.getBet() * 2) {
                    System.out.println("You can double down. Do you want to double your bet? (y/n)");
                    if (scanner.next().equalsIgnoreCase("y")) {
                        doubleDown(hand);
                        break; // After doubling down, the player's turn for this hand ends
                    }
                }

                // Hit or stay
                System.out.println("Hit or Stay? (h/s)");
                String playerInput = scanner.next().toLowerCase();

                if (playerInput.equals("h")) {
                    hand.hit(deck);
                    System.out.println("You drew: " + hand.getCards().get(hand.getCards().size() - 1));

                    if (hand.isBust()) {
                        System.out.println("Bust! This hand is over.");
                        break; // End turn for this hand if busted
                    }
                } else if (playerInput.equals("s")) {
                    player.stay();
                    break;
                } else {
                    System.out.println("Invalid input! Please enter 'h' for hit or 's' for stay.");
                }
            }
        }
    }


    public void split() {
        // Get the original hand from the player's hand list

        Hand originalHand = null;
        for (Hand hand : player.getHandList()) {
            if (hand.canSplit()) {
                originalHand = hand;
                break;
            }
        }

        if (originalHand == null) {
            System.out.println("No valid hand to split.");
            return;
        }

        // Create new hands for the split
        Hand hand1 = new Hand();
        Hand hand2 = new Hand();

        // Move first and second card to the split hands
        hand1.addCard(originalHand.getCard(0));
        hand2.addCard(originalHand.getCard(1));

        // Deal one card to each new hand
        hand1.hit(deck);
        hand2.hit(deck);

        // Duplicate bet for both new hands
        int originalBet = player.getBet();
        player.setBet(originalBet); // Set the bet for the current hand
        player.placeBet(originalBet); // Place the bet for the new hands

        // Remove the original hand from the hand list
        player.getHandList().remove(originalHand);

        // Add the new split hands to the hand list
        player.getHandList().add(hand1);
        player.getHandList().add(hand2);

        System.out.println("Hands after split:");
        System.out.println("Hand 1:");
        hand1.showHand();
        System.out.println("Hand 2:");
        hand2.showHand();
    }


    private void dealInitialCards() {
        Hand playerHand = new Hand();
        playerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());
        player.getHandList().add(playerHand);

        dealer.getHand().addCard(deck.drawCard());
        dealer.getHand().addCard(deck.drawCard());
    }

    private void displayInitialHands() {
        System.out.println(player.getPlayerName() + "'s Hand: ");
        player.getHandList().get(0).showHand();
        System.out.println("Dealer's Hand: ");
        System.out.println(dealer.getHand().getCard(0)); // Dealer shows only one card
        System.out.println("Hidden card");
    }

    private void handleBlackJack() {
        Hand playerHand = player.getHandList().get(0);
        System.out.println(player.getPlayerName() + " has a Blackjack!");
        if (!checkBlackJack(dealer.getHand())) {
            player.winBet(1.5); // 3/2 payout for Blackjack
            System.out.println(player.getPlayerName() + " wins with a Blackjack!");
        } else {
            System.out.println("Dealer also has a Blackjack! It's a draw.");
            player.pushBet();
        }
    }

    private void showFinalHands() {
        System.out.println(player.getPlayerName() + "'s Hands: ");
        for (Hand hand : player.getHandList()) {
            hand.showHand();
            System.out.println("Hand value: " + hand.calculateValue());
        }
        System.out.println("Dealer's Hand: ");
        dealer.getHand().showHand();
        System.out.println("Dealer's Hand value: " + dealer.getHand().calculateValue());
    }

    private void determineWinner() {
        int dealerValue = dealer.getHand().calculateValue();

        for (int i = 0; i < player.getHandList().size(); i++) {
            Hand hand = player.getHandList().get(i);
            int playerValue = hand.calculateValue();

            System.out.println("Results for Hand " + (i + 1) + ":");
            hand.showHand();
            System.out.println("Your hand value: " + playerValue);
            System.out.println("Dealer's hand value: " + dealerValue);

            if (playerValue > 21) {
                System.out.println("You busted with this hand.");
            } else if (dealerValue > 21 || playerValue > dealerValue) {
                double payoutRatio = player.hasDoubledDown() ? 2.0 : 1.0; // Double the bet payout if doubled down
                player.winBet(payoutRatio);
                System.out.println("You win with Hand " + (i + 1) + "!");
                System.out.println("Your hand is " + (playerValue - dealerValue) + " points higher than the dealer's.");
            } else if (playerValue == dealerValue) {
                player.pushBet();
                System.out.println("It's a tie for Hand " + (i + 1) + "!");
            } else {
                player.loseBet();
                System.out.println("Dealer wins Hand " + (i + 1) + ".");
                System.out.println("Dealer's hand is " + (dealerValue - playerValue) + " points higher than yours.");
            }
            System.out.println(); // Separate hands for readability
        }

        System.out.println(player.getPlayerName() + "'s total money: " + player.getMoney());
    }


    private boolean checkBlackJack(Hand hand) {
        return hand.calculateValue() == 21 && hand.getCards().size() == 2;
    }

    public void doubleDown(Hand hand) {
        if (hand.isDoubleDownPossible() && player.getMoney() >= player.getBet()) {
            player.doubleBet(); // double the player's bet
            hand.hit(deck); // Player receives only one more card
            System.out.println("You drew: " + hand.getCards().get(hand.getCards().size() - 1));

            if (hand.isBust()) {
                System.out.println("Bust! You exceeded 21. You lose the doubled bet.");
                player.loseBet();
            }
        } else {
            System.out.println("Double down is not possible.");
        }
    }

    private int getValidBetAmount() {
        Scanner scanner = new Scanner(System.in);
        int betAmount = 0;

        while (true) {
            System.out.println("\nEnter your bet amount (between " + MIN_STAKE + " and " + MAX_STAKE + "):");
            try {
                betAmount = scanner.nextInt();
                if (betAmount >= MIN_STAKE && betAmount <= MAX_STAKE && betAmount <= player.getMoney()) {
                    break;
                } else if (betAmount < MIN_STAKE) {
                    System.out.println("Bet amount cannot be less than " + MIN_STAKE);
                } else if (betAmount > MAX_STAKE) {
                    System.out.println("Bet amount cannot exceed " + MAX_STAKE);
                } else {
                    System.out.println("Insufficient funds. You only have " + player.getMoney());
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // Clear the invalid input
            }
        }
        return betAmount;
    }
}
/*

        Hand originalHand = player.getHandList().stream()
                .filter(Hand::canSplit)
                .findFirst()
                .orElse(null);


                  if (player.getHandList().stream().noneMatch(Hand::isBust)) {
            dealer.play(deck);
        }
 */