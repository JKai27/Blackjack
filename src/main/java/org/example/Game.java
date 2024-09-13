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
        this.dealer = new Dealer();
        this.deck = new Deck();
    }

    public void start() {
        System.out.println("Starting Blackjack...");

        // Initialize or reset the game state
        player.cards.clear();
        dealer.cards.clear();
        deck.shuffle();

        // Check if player can participate
        if (player.getMoney() < MIN_STAKE) {
            System.out.println("\nInsufficient funds to play! Minimum stake required: " + MIN_STAKE);
            return;
        }

        // Ask player to place a bet
        int betAmount = getValidBetAmount();
        player.placeBet(betAmount);
        System.out.println("\nBet placed: " + betAmount);

        // Deal initial cards
        dealInitialCards();

        // Show initial hands
        displayInitialHands();

        // Check for Blackjack
        if (checkBlackJack(player)) {
            handleBlackJack();
            return;
        }

        // Player's turn
        playersTurn();
        if (player.isBust()) {
            return;
        }

        // Dealer's turn
        dealer.play(deck);

        // Show final hands
        showFinalHands();

        // Determine winner
        determineWinner();
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

    private void dealInitialCards() {
        player.addCard(deck.drawCard());
        dealer.addCard(deck.drawCard());
        player.addCard(deck.drawCard());
        dealer.addCard(deck.drawCard());
    }

    private void displayInitialHands() {
        System.out.println(player.getPlayerName() + "'s Hand: ");
        player.showHand();
        System.out.println("Dealer's Hand: ");
        System.out.println(dealer.getCardByIndex(0)); // Dealer shows only one card
        System.out.println("Hidden card");
    }

    private void handleBlackJack() {
        System.out.println(player.getPlayerName() + " has a Blackjack!");
        if (!checkBlackJack(dealer)) {
            player.winBet(1.5); // 3/2 payout for Blackjack
            System.out.println(player.getPlayerName() + " wins with a Blackjack!");
        } else {
            System.out.println("Dealer also has a Blackjack! It's a draw.");
            player.pushBet();
        }
    }

    private void playersTurn() {
        Scanner scanner = new Scanner(System.in);
        while (player.calculateValue() < 21) {
            System.out.println("Your hand value: " + player.calculateValue());

            // Double down option
            if (player.isDoubleDownPossible() && player.getBet() * 2 <= player.getMoney()) {
                System.out.println("You can double down. Do you want to double your bet? (y/n)");
                String input = scanner.next().toLowerCase();
                if (input.equals("y")) {
                    doubleDown();
                    return; // After doubling down, the player's turn ends
                }
            }

            // Hit or stay
            System.out.println("Hit or Stay? (h/s)");
            String playerInput = scanner.next().toLowerCase();

            if (playerInput.equals("h")) {
                player.hit(deck);
                System.out.println("You drew: " + player.getCardByIndex(player.cards.size() - 1));

                if (player.isBust()) {
                    System.out.println("Bust! You exceeded 21. Dealer wins.");
                    player.loseBet();
                    return;
                }
            } else if (playerInput.equals("s")) {
                player.stay();
                break;
            } else {
                System.out.println("Invalid input! Please enter 'h' for hit or 's' for stay.");
            }
        }
    }

    private void showFinalHands() {
        System.out.println(player.getPlayerName() + "'s Hand: ");
        player.showHand();
        System.out.println("Player's Hand value: " + player.calculateValue());
        System.out.println("Dealer's Hand: ");
        dealer.showHand();
        System.out.println("Dealer's Hand value: " + dealer.calculateValue());
    }

    private void determineWinner() {
        int playerValue = player.calculateValue();
        int dealerValue = dealer.calculateValue();

        if (dealerValue > 21 || playerValue > dealerValue) {
            double payoutRatio = player.hasDoubledDown() ? 2.0 : 1.0; // Double the bet payout if doubled down
            player.winBet(payoutRatio);
            System.out.println(player.getPlayerName() + " wins!");
        } else if (playerValue == dealerValue) {
            player.pushBet();
            System.out.println("It's a tie!");
        } else {
            player.loseBet();
            System.out.println(player.getPlayerName() + " loses.");
        }
        System.out.println(player.getPlayerName() + "'s total money: " + player.getMoney());
    }

    private boolean checkBlackJack(Hand hand) {
        return hand.calculateValue() == 21 && hand.cards.size() == 2;
    }

    // Updated doubleDown method
    public void doubleDown() {
        if (player.isDoubleDownPossible() && player.getMoney() >= player.getBet()) {
            player.doubleBet(); // double the player's bet
            player.hit(deck); // Player receives only one more card
            System.out.println("You drew: " + player.getCardByIndex(player.cards.size() - 1));

            if (player.isBust()) {
                System.out.println("Bust! You exceeded 21. You lose the doubled bet.");
                player.loseBet();
            }
        } else {
            System.out.println("Double down is not possible.");
        }
    }
}
