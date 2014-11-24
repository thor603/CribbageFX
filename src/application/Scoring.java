package application;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Class includes static methods for scoring.
 *
 */

public final class Scoring {
	
	//private constructor so class can't be instantiated
	private Scoring() {
		throw new RuntimeException("Should NOT instantiate this class.");
	};
	
	/**
	 * Returns an integer of 0 or greater representing the points scored by the 
	 * playing of the last card in the list of cards
	 * 
	 * @param cards to score, with the last card in list being the most recently 
	 *        played card
	 * @return points pegged by last card in list
	 */
	public static int pointsFromPegging(List<Card> cards) {
		
		final int cardsSize = cards.size();
		
		//number of cards should be between zero and 8
		assert cardsSize > 0 && cardsSize <= 8;
		
		int points = 0;
		
		Card playedCard = cards.get(cardsSize - 1);
		
		int cardsSum = sumOfCards(cards);
		
		//check for 15 or 31 total
		
		if (cardsSum == 15 || cardsSum == 31) {
			points += 2;
			//System.out.println ("Pegging +2 from a 15 or 31");
		}
			
		//** check for pairs
		
		int numPairs = 0;

		//iterate backwards over the the list of cards check for adjacent pairs
		for (int index=cardsSize-2, counter = 0; index >= 0; index--, counter++) {
			
			//should never check more than the 3 most recent cards
			if (counter > 3) { break;}
			
			//if a pair is found, then increment numPairs, otherwise break
			if (playedCard.getName() == cards.get(index).getName()) {
				numPairs++;
			} else { 
				break;	
			}
		}
		
		//** add points for pairs
		if (numPairs == 1) { 
			points += 2; 
			//System.out.println ("Pegging +2 from pairs");
		} else if (numPairs == 2 ) {
			points += 6; 
			//System.out.println ("Pegging +6 from pairs");	
		}
			else if (numPairs == 3) { 
			points += 12; 
			//System.out.println ("Pegging +12 from pairs");	
		} 
		
		//** check for runs
		
		//only check when there are more than 3 cards in play
		
		List<Card> tmpList = null;
		
		if (cardsSize >= 3) {

			int runSize = 0;
			
			//Initially evaluate the entire list of cards, and then iterate 
			//through the smaller subsets down to the smallest possible run of
			//3 cards
			for (int i = 0; i < cardsSize - 2; i++) {

				//create a new sublist of cards to evaluate
				tmpList = new ArrayList<Card>(cards.subList(i, cardsSize));
				
				//if we have a run, then set runsize and break out of loop
				if (isSingleRun(tmpList)) {
					runSize = cardsSize - i;
					break;
				} 
			}
			
			if (runSize > 0) System.out.println("Pegging points from run: " + runSize + tmpList.toString());
			
			points += runSize;
		}
		
		return points;
	}
	
	/**
	 * Method determines whether the list of cards consists solely of a single run
	 * of at least 3 cards. WARNING:  The list of cards passed in will be sorted
	 * @param cards list of cards to evaluate
	 * @return true if cards contains a single list of at least 3 cards and 
	 *         false otherwise
	 */
	private static boolean isSingleRun(List<Card> cards) {
		
		int cardsSize = cards.size();
		
		assert cardsSize >= 3 && cardsSize <= 8: "Expected cards.size() to" +
				" be between 3 and eight, but a cards of size " + cardsSize + 
				" was passed in.";
		
		if (cardsSize < 3) return false;
		
		Collections.sort(cards, Card.getNameComparator());
		
		for (int i=1; i < cardsSize; i++) {
			
			//want to do comparison on the name of the card
			int curCardOrd = cards.get(i).getName().ordinal();
			int priorCardOrd = cards.get(i-1).getName().ordinal();
			
			if (priorCardOrd != (curCardOrd - 1)) {
				//this is not a run
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Returns of the aggregate point value of the cards for pegging purposes.  
	 * Note, this is different than the score of the cards.
	 */
	public static int sumOfCards(List<Card> cards) {
		
		int sum = 0;
		
		for (Card card: cards) {
			sum += card.getPointValue();
		}
		
		return sum;
	}
	
	/**
	 * Returns the points in a hand.
	 */
	public static int pointsInHand(List<Card> cards, Card cutCard)
	{
		return calculatePoints(cards,cutCard,false);
	}
	
	/**
	 * Returns the points in the crib.
	 */
	public static int pointsInCrib(List<Card> cards, Card cutCard)
	{
		return calculatePoints(cards,cutCard,true);
	}
	
	/**
	 * Helper method to calculate the points in the hand or crib. 
	 * @param passedInCards represents the hand or crib
	 * @param cutCard represents the cut card
	 * @param isCrib set to true for crib evaluation and false for hand evaluation
	 * @return points
	 */
	private static int calculatePoints(List<Card> passedInCards, Card cutCard, boolean isCrib) {
		assert passedInCards.size() == 4: "Expected size of cards to be 5, but instead it was " + passedInCards.size();
		assert cutCard != null: "Cutcard should never be null.";
		
		//create a copy of the list as we'll want to add cut card / make other 
		//modifications below
		List<Card> cards = new ArrayList<Card>(passedInCards);
		
		int scoreNobs = scoreNobs(cards, cutCard);
		int scoreFlush = scoreFlush(cards, cutCard, isCrib);
		
		// include cut card for tallying remaining scores
		cards.add(cutCard);
		
		int scoreFifteens = scoreFifteens(cards);
		int scorePairs = scorePairs(cards);
		int scoreRuns = scoreRuns(cards);	
		
		int points = scoreNobs + scoreFlush + scoreFifteens + scorePairs + scoreRuns;
		
		System.out.println("Scored " + points + " points (Nobs=" + scoreNobs + 
			" Flush=" + scoreFlush + " Fifteens=" + scoreFifteens + 
			" Pairs=" + scorePairs + " Runs=" + scoreRuns + ")");
		
		return points;
	}
	
	/**
	 * Returns the number of points from nobs
	 */
	private static int scoreNobs(List<Card> cards, Card cutCard) {
		int score = 0;
		
		for (Card card: cards) {
			if (card.getName() == Card.Name.JACK && 
					(card.getSuit() == cutCard.getSuit())) {
				score = 1;
				break;
			}
		}
		
		return score;
	}
	
	/**
	 * Returns the number of points from flushes
	 * @param cards
	 * @param cutCard
	 * @param isCrib should be set to true for crib evaluation and false otherwise
	 */
	private static int scoreFlush(List<Card> cards, Card cutCard, boolean isCrib) {
		
		Card.Suit flushSuit = cards.get(0).getSuit();
		boolean haveFlush = true;
		for (int i = 1; i < cards.size(); i++) {
			if (flushSuit != cards.get(i).getSuit()) {
				haveFlush = false;
				break;
			}
		}
		
		//if we have a flush, check to see if crib matches the flush for extra point
		int score = 0;
		if (haveFlush) {
			if (flushSuit == cutCard.getSuit()) {
				score = 5;
			} 
			//the crib needs all 5 to score on a flush
			else if (!isCrib) {
				score = 4;
			}
		}
		
		return score;
	}
	
	/**
	 * Returns the points for fifteens
	 */
	private static int scoreFifteens(List<Card> cards) {
		
		//create a point value array to speed up processing
		int[] cardValue = new int[5];
		
		for (int i=0; i < cardValue.length; i++) {
			cardValue[i] = cards.get(i).getPointValue();
		}

		int score = 0;
		
		// 5 cards
		int pointsOf5Cards = cardValue[0] + cardValue[1] + cardValue[2] + cardValue[3] 
				+ cardValue[4];
		
		// if we score 15 on 5 cards, can't be any other 15s, return immediately
		if (pointsOf5Cards == 15) {
			score = 2;
			return score;
		}
			
		// 4 cards
		for (int i=0; i < 5; i++) {
			if (pointsOf5Cards - cardValue[i] == 15) {
				score += 2;
			}
		}
		
		// 3 cards
		for (int i = 0; i < 3; i ++) {
			for (int j = i + 1; j < 4; j ++) {
				for (int k = j + 1; k < 5; k ++) {
					if (cardValue[i] + cardValue[j] + cardValue[k] == 15) {
						score += 2;
						//System.out.println("Scored 15 on 3 cards");
					}
				}
			}
		}
		
		// 2 cards
		for (int i = 0; i < 4; i ++) {
			for (int j = i + 1; j < 5; j ++) {
				if (cardValue[i] + cardValue[j] == 15) {
					score += 2;
					//System.out.println("Scored 15 on 2 cards");
				}
			}
		}
		
		return score;
	}
	
	/**
	 * Returns the points for pairs.
	 */
	private static int scorePairs(List<Card> cards) {
		int score = 0;
		
		for (int i=0; i < cards.size() - 1; i++){
			for (int j=i+1; j < cards.size(); j++) {
				if (cards.get(i).getName() == cards.get(j).getName()) {
					score += 2;
					//System.out.println("pair +2");
				}
			}
		}
		
		return score;
	}
	
	/**
	 * Returns the points from runs.
	 */
	private static int scoreRuns (List<Card> cards) {
		
		List<Card> duplicates = new ArrayList<Card>();
		
		//sort the list
		Collections.sort(cards, Card.getNameComparator());
		
		int runLength = 1;
		int runStartIndex = -1;
		
		for (int i=1; i < cards.size(); i++) {
			
			//want to do comparison based on the name of the card
			int curCardOrd = cards.get(i).getName().ordinal();
			int priorCardOrd = cards.get(i-1).getName().ordinal();
			
			//cards increment by 1 - this is a run
			if (priorCardOrd + 1 == curCardOrd) {
				// if this is the start of a new run, keep track of starting index				
				if (runLength == 1) {
					runStartIndex = i-1;
				}
				runLength++;
			}
			//track duplicates
			else if (priorCardOrd == curCardOrd) {
				duplicates.add(cards.get(i));
			} 
			//if run length is already 3 or greater, than break out of loop as 
			//there can't be another run and don't want runLength to increment again
			else if (runLength >= 3) {
				break;
			}
			//if the runLength is less than 3, then we don't have a run, reset
			//counters
			else if (runLength < 3) {
				runLength = 1;
			}
		}
		
		//check to see if we found a run
		int score = 0;
		int numMatches = duplicates.size();
		
		assert numMatches >= 0 && numMatches <= 2 : "Invalid value of numDups (" + numMatches + "); should be between 0 and 2.";
		
		//runLength is 5
		if (runLength == 5) {
			score = 5;
		} 
		//runLength is 4
		else if (runLength == 4) {
			score = 4 * (numMatches + 1);
		} 
		//runLength is 3
		else if (runLength == 3) {
			
			if (numMatches == 0) {
				score = 3;
			} else if (numMatches == 1) {
				
				//check to see if duplicate was in run
				if (runStartIndex == 0) {
					// if last 2 cards are a pair, then duplicate was NOT in run
					if (cards.get(3).getName() == cards.get(4).getName()) {
						score = 3;
					} else {
						score = 3 * 2;
					}
				} else if (runStartIndex == 1) {
					//duplicate must be in run
					score = 3 * 2;
				} else if (runStartIndex == 2) {
					//if first 2 cards are a pair, then duplicate was NOT in run
					if (cards.get(0).getName() == cards.get(1).getName()) {
						score = 3;
					} else {
						score = 3 * 2;
					}
				}
			} else if (numMatches == 2) {
				//handle 3 of a kind
				if (duplicates.get(0).getName() == duplicates.get(1).getName()) {
					score = 3*3;
				} else
				//handle 2 pairs
				{
					score = 3*4;
				}
			}
		} 
		
		return score;
	}
	

	/**
	 * For testing purposes only.
	 */
//	public static void main(String[] args) {
//		List<Card> cards = new ArrayList<Card>();
//		
//		cards.add(new Card(Suit.CLUBS, Name.SEVEN));
//		cards.add(new Card(Suit.HEARTS, Name.SEVEN));
//		cards.add(new Card(Suit.CLUBS, Name.EIGHT));
//		cards.add(new Card(Suit.HEARTS, Name.EIGHT));
//
//		Card cutCard = new Card (Suit.DIAMONDS, Name.SIX);
//		
//		System.out.println("Cards: " + cards.toString());
//		System.out.println("-points: " + pointsInHand(cards, cutCard));
//		
//		cards.add(new Card(Suit.DIAMONDS, Name.FOUR));
//		cards.add(new Card(Suit.SPADES, Name.FIVE));
//		cards.add(new Card(Suit.DIAMONDS, Name.SIX));
//		Card cutCard = new Card(Suit.SPADES, Name.QUEEN);
//		cards.add(new Card(Suit.HEARTS, Name.KING));
//	
//		System.out.println("Cards: " + cards.toString());
//		System.out.println("-points: " + pointsInCrib(cards, cutCard));
//				
//	}

}
