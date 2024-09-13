package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your name:");
        String name = isValidName(scanner);

        int money = 0;
        while (true) {
            System.out.println("Enter your initial money (minimum " + 10 + "):");
            try {
                money = scanner.nextInt();
                if (money >= 10) {
                    break;
                } else {
                    System.out.println("Initial money must be at least 10.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // Clear the invalid input
            }
        }

        Player player = new Player(name, money);
        Game game = new Game(player);

        while (true) {
            game.start();
            String answer = "";

            // Ensure only 'y' or 'n' is accepted
            while (!answer.equals("y") && !answer.equals("n")) {
                System.out.println("Do you want to play another round? (y/n)");
                answer = scanner.next().toLowerCase();

                if (!answer.equals("y") && !answer.equals("n")) {
                    System.out.println("Invalid input. Please enter 'y' or 'n'.");
                }
            }

            if (answer.equals("n")) {
                break; // Exit the loop if 'n' is entered
            }
        }



        System.out.println("Thank you for playing! Your final balance is: " + player.getMoney());
        scanner.close();
    }

    private static String isValidName(Scanner scanner) {
        String name;
        while (true) {
            if (scanner.hasNext("[A-Za-z]+")) {
                name = scanner.next(); // reads the input
                break;
            } else {
                System.out.println("Invalid name. The name must contain alphabets only.");
                scanner.nextLine();
            }
        }
        return name;
    }
}
