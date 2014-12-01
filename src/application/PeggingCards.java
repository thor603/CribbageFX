package application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class represents pegging cards.
 *
 */

public class PeggingCards {
	
	public final static int MAX_PEG_VALUE = 31;

	private List<Card> cards = new ArrayList<Card>();
	
	private int pointValue = 0;
	
	public PeggingCards() {
		
	}
	
	
	/**
	 * returns true if a card can be added to the stack (i.e., point value
	 * would be 31 or less), otherwise returns false 
	 */
	public boolean canAdd(Card card) {
		return (card.getPointValue() + getPointValue() <= 
			PeggingCards.MAX_PEG_VALUE);
	}
	
	/**
	 * Adds a card to the pegging stack and returns the number of points for
	 * attributable to adding the card.  Returns -1 if card can't be added to
	 * stack
	 */
	int addCard(Card card) {
		
		//if can't add this card to pegging Cards (i.e., point value would 
		//exceed 31), then return -1
		if (!canAdd(card)) return -1;
		
		//mark card as face up
		card.setFaceUp(true);
		
		//keep track of total point value of pegging cards
		pointValue += card.getPointValue();
		
		//add card to pegging cards
		cards.add(card);
		
		return Scoring.pointsFromPegging(cards);
	}
	
	Card getCard(int index) {
		return cards.get(index);
	}	
	
	List<Card> getList() {
		return cards;
	}
	
	Iterator<Card> iterator() {
		return cards.iterator();
	}
	
	int size() {
		return cards.size();
	}
	
	int getPointValue() {
		return pointValue;		
	}
	
	//clears the cards and resets the pointValue to zero
	void clear() {
		cards.clear();
		pointValue=0;
	}
	
	@Override
	public String toString() {
		return cards.toString();
	}	
}
