package org.example;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Hand> handList;
    private final String playerName;
    private double money;
    private double bet;
    private double insuranceBet;
    private boolean doubledDown; // Tracks if the player has doubled down
    private boolean tookInsurance;

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
    public void placeBet(double amount) {
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

    // Player wins with the given payout ratio
    public void winBet(double payoutRatio) {
        double originalBet = doubledDown ? bet / 2 : bet;
        double winnings = (originalBet * payoutRatio);
        money += bet + winnings;
        System.out.println("Congratulations! You have won " + winnings + " Euro!");
        System.out.println(getPlayerName() + "'s total money after win: " + getMoney());
    }

    public void manageInsuranceBet(boolean wonInsurance) {
        // Display the player's last balance
        System.out.println("Your balance before insurance: " + money + " Euro");

        if (wonInsurance) {
            // Player wins the insurance
            double insuranceWinnings = getInsuranceBet() * 2; // 2:1 payout for insurance
            money += insuranceWinnings;
            System.out.println("Your insurance bet paid out: " + insuranceWinnings + " Euro!");
        } else {
            // Player loses the insurance
            money -= getInsuranceBet(); // Deduct the insurance bet
            System.out.println("You lost the insurance bet.");
        }

        // Display the player's new balance
        System.out.println("Your balance after insurance: " + money + " Euro");
    }

    // Check if the player has doubled down
    public boolean hasDoubledDown() {
        return doubledDown;
    }


    // Player loses the bet
    public void loseBet() {
        System.out.println("Player loses the bet of " + bet + " Euro.");
        // Money has already been deducted when the bet was placed
    }

    // Return the current bet
    public double getBet() {
        return bet;
    }

    public double getMoney() {
        return money;
    }

    public String getPlayerName() {
        return playerName;
    }

    public List<Hand> getHandList() {
        return handList;
    }

    public void setBet(double bet) {
        this.bet = bet;
    }

    public void setInsuranceBet(double insuranceBet) {
        this.insuranceBet = insuranceBet;
    }

    public double getInsuranceBet() {
        return insuranceBet;
    }

    // Player takes Insurance against dealers Blackjack
    public void takeInsurance() {
        double insuranceBet = bet / 2;
        tookInsurance = true;
        setInsuranceBet(insuranceBet);
    }

    public void loseInsuranceBet() {
        System.out.println("Player loses the bet of " + insuranceBet + " Euro.");
    }
}
