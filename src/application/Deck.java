package application;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Class represents a deck of playing cards.
 *
 */

public class Deck {

	private List<Card> cards = new ArrayList<Card>();
	
	//iterator for tracking current draw card
	private Iterator<Card> iterator;
	
	/**
	 * Creates a newly shuffled deck of 52 cards.
	 */
	public Deck () {
		
		//create a deck of cards by adding all 52 cards
		for (Card.Suit suit : Card.Suit.values()) {
			for (Card.Name value : Card.Name.values()) {
				cards.add(new Card(suit, value));
			}
		}
		
		//shuffle the deck so its ready for first use
		shuffle();
	}
	
	/**
	 * Shuffles the deck and resets iterator for drawing cards. 
	 * 
	 * NOTE: Drawn cards are never removed from the deck, so no need to return
	 * drawn cards to deck prior to shuffling.
	 */
	public void shuffle() {
		
		//size of deck should always be 52
		assert cards.size() == 52 : "cards.size() should be 52 but instead is " + cards.size();
		
		//reset all of the cards to default values
		for (Card card: cards) {
			card.setFaceUp(false);
		}
		
		Collections.shuffle(cards);
		iterator = cards.iterator();
	}

	/**
	 * Returns the next card from the deck.  Returns null if there are no 
	 * remaining cards
	 */
	public Card drawCard() {
		//Note: iterator is used to simulate drawing cards, but cards are never
		//actually removed from the deck
		if (iterator.hasNext()) {
			return iterator.next();
		} else {
			return null;
		}
	}
	
	/**
	 * Draws 1 or more cards from the deck and returns a newly instantiated list
	 * holding these cards.
	 * @param numCards # of cards to draw; should be between 1 and 6
	 * @return List holding the requested cards, or null if numCards < 1 or there
	 * 		   there are insufficient cards to satisfy the request
	 */
	List<Card> drawCards(int numCards) {
		assert numCards >= 1 && numCards <= 6:"Should never need to draw fewer " +
				"than 1 or more than 6 cards, but " + numCards + " cards have " +
				"been requested.";
		
		if (numCards < 1) 
			return null;
		
		List<Card> tmpList = new ArrayList<Card>();
		
		for (int i=0; i < numCards; i++) {
			Card c = drawCard();
			if (c == null)
				return null;
			tmpList.add(c);
		}
		
		return tmpList;
	}
	
	/**
	 * Draws and returns cut card.
	 */
	//TODO:  make cut card random instead of being drawn from top of deck
	Card getCutCard() {
		return drawCard();
	}
	
	@Override
	public String toString() {
		return cards.toString();
	}
}
