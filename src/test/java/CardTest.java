import org.example.Card;
import org.example.Rank;
import org.example.Suit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {
    @Test
    void testCardRank() {
        Card card = new Card(Rank.ACE, Suit.CLUB);
        Rank expectedRank = Rank.ACE;
        Suit expectedSuit = Suit.CLUB;  // change the value and the test will fail.

        assertEquals(expectedRank, card.getRank(), "The card should have rank ACE");
        assertEquals(expectedSuit, card.getSuit(), "The card should have suit CLUB");
    }

    @Test
    void testCardSuit() {
        Card card = new Card(Rank.JACK, Suit.HEART);
        Suit expectedSuit = Suit.HEART;
        assertEquals(expectedSuit, card.getSuit(), "The card should have suit JACK");
    }
}
