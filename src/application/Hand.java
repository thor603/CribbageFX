package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a hand of cards.  Can be used for computer or player.
 */

public class Hand {
	
	private final List<Card> cards = new ArrayList<Card>();
	private final List<Card> peggedCards = new ArrayList<Card>();
	private boolean isFaceUpHand;
	
	/**
	 * Creates a new instance of hand. 
	 */
	public Hand() {
	}
	
	/**
	 * Add a card to the hand
	 * @return true (as specified by Collection.add(E))
	 */
	boolean addCard(Card card) {
		if (isFaceUpHand()) {
			card.setFaceUp(true);
		}
		
		boolean returnValue = cards.add(card);
		
		//sort the hand
		Collections.sort(cards, Card.getNameComparator());
		
		return returnValue;
	}
	
	/**
	 * Add more than 1 card to the hand.
	 */
	void addCards(List<Card> cards) {
		//using individual addCard method here to ensure faceUp is set
		//appropriately for players
		for (Card card: cards) {
			addCard(card);
		}
	}
	
	/**
	 * Return a reference to a card at a particular index
	 */
	Card peekAtCard(int index) {
		return cards.get(index);
	}
	
	/**
	 * Remove a card from the hand
	 */
	Card remove(int index) {
		return cards.remove(index);
	}
	
	/**
	 * Removes card being pegged from the hand. A reference to the card
	 * is kept internally in the peggedCards list so that card can be returned to
	 * the hand for final scoring.
	 * @return Card that is being pegged
	 */
	Card pegCard(int index) {
		
		Card card = remove(index);
		
		if (card==null) return null;
		
		peggedCards.add(card);
		
		return card;
	}
	
	/**
	 * Clears the hand and the peggedCard references.
	 */
	void clear() {
		cards.clear();
		peggedCards.clear();
	}
	
	/**
	 * Returns the size of the hand (not including peggedCards)
	 */
	int size() {
		return cards.size();
	}
	
	/** 
	 * Returns pegged cards to the hand.  This method used to facilitate scoring
	 * at the end of a pegging round.
	 */
	void returnPeggedCardsToHand() {
		while (peggedCards.size() > 0) {
			cards.add(peggedCards.remove(0));
		}
		
		//sort the cards
		Collections.sort(cards, Card.getNameComparator());
	}
	
	/**
	 * Set the cards to be display faceup
	 */
	void turnCardsFaceUp() {
		for (Card card: cards) {
			card.setFaceUp(true);
		}
	}
	
	/**
	 * Returns reference to underlying list holding the cards in the hand.
	 * @return
	 */
	//TODO: consider whether making a defensive copy is worth it
	List<Card> getHand() {
		return cards;
	}
	
	boolean isFaceUpHand() {
		return isFaceUpHand;
	}
	
	void setFaceUpHand(boolean faceUpHand) {
		isFaceUpHand = faceUpHand;
	}
	
	@Override
	public String toString() {
		return cards.toString();
	}
}
