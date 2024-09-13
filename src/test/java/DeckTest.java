import org.example.Card;
import org.example.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {
    private Deck deck;
    private Deck deck2;

    @BeforeEach
    public void setUp() {
        // initialize deck before each test
        deck = new Deck();
    }

    @Test
    public void testDeckHasCards() {
        // check if Deck is empty. deck2 fails the test, because it hasn't been initialised
        assertFalse(deck.isEmpty(), "Deck should not be empty after initialization");
    }

    @Test
    public void testDrawCard() {
        // drawn card is not null, means Deck has cards and not empty
        Card card = deck.drawCard();
        //Card card2 = deck2.drawCard();
        assertNotNull(card, "Drawn card should not be null");
        // assertNotNull(card2,"Drawn card should not be null"); This test fails

    }

    @Test
    public void testDeckSizeAfterDraw() {
        int initialSizeOfDeck = deck.toString().split(",").length;
        deck.drawCard();
        int finalSizeOfDeck = deck.toString().split(",").length;
        assertTrue(initialSizeOfDeck > finalSizeOfDeck, "Deck size should decrease after draw");
    }
}
