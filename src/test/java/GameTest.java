import org.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Mockito.*;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;


public class GameTest {
    private Player player;
    private Game game;
    private Deck deck;

    @BeforeEach
    void setUp() {
        player = new Player("Test Player", 1000);
        game = new Game(player);
        deck = Mockito.spy(new Deck());
    }

    @Test
    void testStartGameWithValidBet() {
        // Mocking Scanner input for the Game
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextInt()).thenReturn(100); // Valid bet of 100
        game.start();

        assertEquals(900, player.getMoney(), "Player's money should be reduced by bet amount");
    }

}
