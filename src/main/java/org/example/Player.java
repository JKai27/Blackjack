package org.example;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Hand> handList;
    private final String playerName;
    private int money;
    private int bet;
    private boolean doubledDown; // Tracks if the player has doubled down

    public Player(String playerName, int money) {
        handList = new ArrayList<>();
        this.playerName = playerName;
        this.money = money;
        this.doubledDown = false; // Initialize as false
    }

    public void pushBet() {
        money += bet; // Return the bet
    }

    public void stay() {
        System.out.println("Player doesn't want to continue the round and choose to stay");
    }

    // Player places a bet
    public void placeBet(int amount) {
        if (amount > 0 && amount <= money) {
            bet = amount;
            money -= amount;
            doubledDown = false; // Reset doubledDown when placing a new bet
        } else {
            throw new IllegalArgumentException("Invalid bet amount");
        }
    }


    public boolean hasBlackJack() {
        for (Hand hand : handList) {
            return hand.calculateValue() == 21 && handList.size() == 2;
        }
        return false;
    }

    // Player doubles their bet for double down
    public void doubleBet() {
        if (money >= bet) {
            money -= bet; // Deduct the same amount again for doubling
            bet *= 2; // Double the bet
            doubledDown = true; // Mark that the player has doubled down
        } else {
            System.out.println("Not enough money to double the bet.");
        }
    }


    // Check if the player has doubled down
    public boolean hasDoubledDown() {
        return doubledDown;
    }

    // Player wins with the given payout ratio
    public void winBet(double payoutRatio) {
        int originalBet = doubledDown ? bet / 2 : bet;
        int winnings = (int) (originalBet * payoutRatio);
        money += bet + winnings;
        System.out.println("Congratulations! You have won " + winnings + " Euro!");
        System.out.println(getPlayerName() + "'s total money after win: " + getMoney());
    }


    // Player loses the bet
    public void loseBet() {
        System.out.println("Player loses the bet of " + bet + " Euro.");
        // Money has already been deducted when the bet was placed
    }

    // Return the current bet
    public int getBet() {
        return bet;
    }

    public int getMoney() {
        return money;
    }

    public String getPlayerName() {
        return playerName;
    }

    public List<Hand> getHandList() {
        return handList;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }
}
