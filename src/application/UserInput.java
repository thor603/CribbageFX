package application;

/**
 * Immutable object for holding user input.
 *
 */

public class UserInput {
	
	public enum InputType {
		BUTTON_ACTION,
		CARD_CLICK;
	};
	
	private final InputType inputType;
	private final int cardIndex;
	
	//private constructors to make sure BUTTON_ACTION input type is only
	//created with a cardIndex
	private UserInput(InputType type) {
		inputType = type;
		cardIndex = -1;
	}
	
	private UserInput(InputType type, int index) {
		inputType = type;
		cardIndex = index;
	}
	
	/**
	 * Generates a new UserInput for a Button_Action
	 * @return
	 */
	public static UserInput getInstanceButtonAction() {
		return new UserInput(InputType.BUTTON_ACTION);
	}
	
	/**
	 * Generates a new UserInput for a Button Click
	 * @param cardIndex
	 * @return
	 */
	public static UserInput getInstanceCardClick(int cardIndex) {
		assert cardIndex >= 0 && cardIndex <= 5;
		
		return new UserInput(InputType.CARD_CLICK, cardIndex);
	}
	
	InputType getType() {
		return inputType;
	}
	
	int getCardIndex() { 
		return cardIndex;
	}
	
}
