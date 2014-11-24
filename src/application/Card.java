package application;
import java.io.File;
import java.util.Comparator;

import javafx.scene.image.Image;


/**
 * Class represents a single playing card.
 * 
 */

public final class Card {

	/**
	 * Enum represents suit of card.
	 */
	public enum Suit {
		HEARTS   ("H"), 
		DIAMONDS ("D"),
		SPADES   ("S"), 
		CLUBS    ("C");
		
		private final String stringName;
		
		private Suit(String stringName) {
			this.stringName = stringName;
		}
		
		@Override
		public String toString() { 
			return stringName; 
		}
	};
	
	/**
	 * Enum represents name of card
	 */
	public enum Name { 
		//create each card and pass in a point value and string name
		ACE   (1, "A"),
		TWO   (2, "2"), 
		THREE (3, "3"),
		FOUR  (4, "4"),
		FIVE  (5, "5"),
		SIX   (6, "6"),
		SEVEN (7, "7"),
		EIGHT (8, "8"),
		NINE  (9, "9"), 
		TEN   (10, "10"),
		JACK  (10, "J"),
		QUEEN (10, "Q"),
		KING  (10, "K");
		
		private final int value;
		private final String stringName;
	
		//Constructor
		private Name (int value, String stringName) {
			this.value = value;
			this.stringName = stringName;
		}
	
		//returns the point value of the card
		public int pointValue() { 
			return value; 
		}
		
		@Override
		public String toString() { 
			return stringName; 
		}
	};
		
	private final Suit suit;
	private final Name name;
	private final Image frontOfCardImage;
	private final Image backOfCardImage;
	private boolean isFaceUp = false;
	
	//constructor
	public Card (Suit suit, Name name) {
		this.suit = suit;
		this.name = name;
		
		this.frontOfCardImage = generateFrontOfCardImage(suit, name);
		this.backOfCardImage = generateBackOfCardImage();
	}
	
	//generates image to be displayed on the front of card
	private static Image generateFrontOfCardImage(Suit suit, Name name) {
		
		String fileName = null;
		
		switch (name) {
		case TWO:
		case THREE:
		case FOUR:
		case FIVE:
		case SIX:
		case SEVEN:
		case EIGHT:
		case NINE:
		case TEN:
				fileName = name.toString();
				break;
		case ACE:
		case JACK:
		case QUEEN:
		case KING:
				fileName = name.name();
				break;
		}
		
		fileName = ("images/" + fileName + "_of_" + suit.name() + ".png").toLowerCase();
		File file = new File(fileName);
		
	    //System.out.println("File exists (" + fileName + "):  " + file.exists());
	    Image frontOfCardImage = new Image(file.toURI().toString());
		
		return frontOfCardImage;
	}
	
	//generates image to be displayed on the back of the card
	private static Image generateBackOfCardImage() {
		
		String fileName = ("images/card_back.png");
		
		File file = new File(fileName);
		
	    //System.out.println("File exists (" + fileName + "):  " + file.exists());
	    Image image = new Image(file.toURI().toString());
		
		return image;
	}
	
	/**
	 * Return the name of card.
	 */
	Name getName() {
		return name;
	}
	
	/**
	 * Return the suit of card.
	 */
	Suit getSuit() {
		return suit;
	}
		
	/**
	 * Return point value of card.
	 */
	int getPointValue() {
		return name.pointValue();
	}
	
	/**
	 * Set to true to display the card face-up and false to display the back of
	 * the card.
	 */
	void setFaceUp(boolean faceUp) {
		isFaceUp = faceUp;
	}
	
	/**
	 * Returns true if the front of card should be displayed and false if the
	 * back of card should be displayed.
	 */
	boolean isFaceUp() {
		return isFaceUp;
	}

	/**
	 * Return a comparator to sort the value of cards giving first priority 
	 * to the name of the card and second priority to the suit of the card.
	 */
	public static Comparator<Card> getNameComparator() {
		return new Comparator<Card>() {
		    public int compare(Card card1, Card card2) {

		    	int nameResult = card1.getName().compareTo(card2.getName());
		        
		    	//if cards have different names, then return compare results based on names
		    	if (nameResult != 0) {
		        	return nameResult;
		        }
		    	//if cards have same name, compare results based on suit
		        else {
		        	return card1.getSuit().compareTo(card2.getSuit());
		        }
		    }
		};
	}
	
	/**
	 * Return the image representing either the front or back of this card,
	 * depending whether isFaceUp is set to true or false.
	 */
	Image getImage () {
		if (isFaceUp()) {
			return frontOfCardImage;
		} else {
			return backOfCardImage;
		}
	}
	
	@Override
	public String toString() {
		return name.toString() + " " + suit.toString();
	}		
}
