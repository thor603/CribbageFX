package application;

import application.UserInput.InputType;


/**
 * Class controls the overall state and logic of the game.
 * 
 */
public final class Cribbage {
	
	//number of points to win game
	public final static int WINNING_SCORE = 10;
	
	public enum GameState {
		PLAYER_DISCARD,
		PEGGING,
		PEGGING_WAITING_FOR_NEXT_ROUND,
		COUNT_POINTS,
		GAME_OVER;
	}
	
	//keep track of the state of the game
	private GameState gameState;
	
	//deck, hands, crib and cut card
	private Deck deck = new Deck();
	private Hand computerHand = new Hand();
	private Hand playerHand = new Hand();
	private Hand crib = new Hand();
	private PeggingCards peggingCards = new PeggingCards();
	private Card cutCard;
	
	//keep track of who dealt; player deals first; setting this to false as
	//because this gets flipped in initializeRound()
	private boolean playersDeal = false;
	
	//keep track of who pegged the last card
	private boolean playerPeggedLast = true;
	
	//keep track of points
	private int playerScore = 0;
	private int computerScore = 0;
	
	//for updating GUI
	private MainController controller;
	
	/**
	 * Creates instance of Cribbage class for managing game state.
	 * 
	 * @param controller class that controls the display state
	 */
	Cribbage (MainController controller) {
		this.controller = controller;
		controller.setCribbage(this);
		initializeGame();
	}
	
	//initialize for a new game
	private void initializeGame() {
		
		//winner of last game gets to deal
		if (playerScore != 0 && computerScore !=0) {
			//not that setting this to opposite of what appears obvious as it 
			//will be flipped in initializeRound()
			playersDeal = playerScore > computerScore ? false : true;
		}
		
		//initialize scores
		playerScore = 0;
		computerScore = 0;
		
		initializeRound();
	}
	
	//initialize for a new round of pegging
	private void initializeRound() {
		
		//shuffle the deck
		deck.shuffle();
		
		//clear the cut card
		cutCard = null;
		
		//clear the pegging cards
		peggingCards.clear();
		
		//initialize and draw computer hand
		computerHand.clear();
		computerHand.setFaceUpHand(false);
		computerHand.addCards(deck.drawCards(6));
		//System.out.println("Computer Hand: " + computerHand.toString());
		
		//initialize and draw player hand
		playerHand.setFaceUpHand(true);
		playerHand.clear();
		playerHand.addCards(deck.drawCards(6));
		//System.out.println("Player Hand: " + playerHand.toString());
		
		//initialize crib
		crib.clear();
		crib.setFaceUpHand(false);
		
		//computer discards 2 cards to crib
		//TODO computer discards at random -- make computer smarter
		crib.addCard(computerHand.remove(0));
		crib.addCard(computerHand.remove(0));
		
		setGameState(GameState.PLAYER_DISCARD);
		
		//alternate who deals; player goes first (playersDeal initially set to false)
		playersDeal = !playersDeal;

		//update display
		controller.updateDisplay();
	}

	/**
	 * Function called by controller when user input is selected.
	 * 
	 * @param input
	 */
	void handleUserInput(UserInput input) {
		
		//if player clicks on a panel that doesn't currently hold a card, 
		//nothing to do - return silently
		if (input.getType() == InputType.CARD_CLICK 
				&& input.getCardIndex() >=  playerHand.size()) {
			return;
		}		
		
		//handle user input based on current game state
		switch (gameState) {

			case PLAYER_DISCARD:
			
				if (input.getType() != InputType.CARD_CLICK) {
					controller.appendStatusText("Invalid input. Select card to discard.");
					break;
				}
				
				assert playerHand.size() >= 4: "Expecting playerHand.size() > 4,"
						+ "but instead value is " + playerHand.size();
				
				int cardIndex = input.getCardIndex();
				
				//stay in discard state until player has 4 cards
				if (playerHand.size() > 4) {
					Card card = playerHand.remove(cardIndex);
					//set card to display face down
					card.setFaceUp(false);
					//add card to crib
					crib.addCard(card);
				}
			
				//transition to pegging state if 4 cards in hand
				if (playerHand.size() == 4) {
					setGameState(GameState.PEGGING);
					
					//set the cut card
					cutCard = deck.getCutCard();
					cutCard.setFaceUp(true);
				
					//check for and score nobs
					if (cutCard.getName() == Card.Name.JACK) {
						if (playersDeal) {
							boolean win = addPlayerPoints(2);
							controller.appendStatusText("Player scores 2 for jack cut card.");
							if (win) { playerWon();	break; }
						} else {
							boolean win = addComputerPoints(2);
							controller.appendStatusText("Computer scores 2 for jack cut card.");
							if (win) { computerWon(); break; }
						}
					}
					
					//computer plays if computer's turn
					if (playersDeal) {
						controller.appendStatusText("Player's deal - computer goes first.");
						if (computerPlay()) {
							//if computer has won, then break out early
							break;
						}
					} else {
						controller.appendStatusText("Computer's deal - player goes first.");
					}
				}
				
				break;
			
			case PEGGING:
			
				if (input.getType() != InputType.CARD_CLICK) {
					controller.appendStatusText("Invalid input. Select a card to "
							+ "continue pegging");
					break;
				}
				
				Card card = playerHand.peekAtCard(input.getCardIndex());
				
				//check to see if playing card would result in peg cards value
				//exceeding 31 points
				if (!peggingCards.canAdd(card)) {
					controller.appendStatusText("Can't play card! Total points would exceed 31.");
					break;
				}
				
				playerPeggedLast = true;
				
				int playerPoints = peggingCards.addCard(playerHand.pegCard(input.getCardIndex()));
				if (playerPoints > 0) {			
					boolean win = addPlayerPoints(playerPoints);
					controller.appendStatusText("Player pegged " + playerPoints + " points.");
					if (win) { playerWon(); break; }
				}
					
				//computer plays at least once (if able), and continues playing
				//while player can't play and computer can play
				if (computerPlay()) {
					//if comptuer won, then break out
					break;
				}
					
				//check to see if this is the last pegging card, and if so allocate 
				//points and change state to PEGGING_WAITING_FOR_NEXT_ROUND
				handleLastPeggingCard();
				
				break;
			
			case PEGGING_WAITING_FOR_NEXT_ROUND:
			
				//don't take any action until next round button is pushed
				if (input.getType() != InputType.BUTTON_ACTION) {
					controller.appendStatusText("Invalid selection. Click on Next Round button to continue game.");
					break;
				} 
				
				//Next round button was pressed. As applicable:
				// 1. if no cards left, then score hands and move to COUNT_POINTS or 
				//	  GAME_OVER state
				// 2. player goes (if its turn)
				// 3. computer goes (if its turn)
				
				peggingCards.clear();
				
				//if both players are out of cards, then need to score hand and move
				//to either COUNT_POINTS state or GAME_OVER state
				if (playerHand.size() == 0 && computerHand.size() == 0) {
					 boolean win = scoreHands();
					 
					 if (win) {
						 setGameState(GameState.GAME_OVER);
					} else {
						setGameState(GameState.COUNT_POINTS);
					}
					 
					break;
				} 
				
				controller.appendStatusText("Commencing next pegging round.");
				
				setGameState(GameState.PEGGING);
				
				//if it's computer's turn or player has no cards left, then computer should play
				if ((playerPeggedLast && computerCanPeg()) ||
						(playerHand.size() == 0)) {
					
					//computer plays at least once (if able), and continues playing
					//while player can't play and computer can play
					if (computerPlay()) {
						//if computer won, then break out
						break;
					}
				}
				
				//check to see if this is the last pegging card, and if so allocate 
				//points and change state to PEGGING_WAITING_FOR_NEXT_ROUND
				handleLastPeggingCard();
				
				break;			
			
			case COUNT_POINTS:
				
				//don't take any action until next round button is pushed
				if (input.getType() != InputType.BUTTON_ACTION) {
					controller.appendStatusText("Invalid selection. Click on Next Round button to continue game.");
					break;
				} 
				
				//initialize next round
				initializeRound();
				
				break;
				
			case GAME_OVER:
				
				//don't take any action until next round button is pushed
				if (input.getType() != InputType.BUTTON_ACTION) {
					controller.appendStatusText("Invalid selection. Click on Next Round button to start a new game.");
					break;
				} 
				
				//initialize next game
				initializeGame();
				
				break;
				
			default:
				System.out.println("WARNING: Cribbage.handleUserInput() made it to default case.");
		}
		
		//update the display
		controller.updateDisplay();
		
		//System.out.println("GameState = " + getGameState().name());
	}
	
	/**
	 * Checks to see if this is the last pegging card (1) adds a point for the 
	 * final card accordingly and (2) advances state to PEGGING_WAITING_FOR_NEXT_ROUND
	 * 
	 * @return true if a player or computer has won, and false otherwise
	 */
	private boolean handleLastPeggingCard() {
		
		//if no one can peg, then allocate points for last card
		if (!playerCanPeg() && !computerCanPeg()) {
			
			//allocate points for last card, BUT not for a 31, as these points
			//would have been allocated when card was added to stack
			if (peggingCards.getPointValue() != 31) {
				if (playerPeggedLast) {
					boolean win = addPlayerPoints(1); 
					controller.appendStatusText("Player pegged 1 point for last card.");
					if (win) {
						playerWon();
						return true;
					}
				} else {
					boolean win = addComputerPoints(1);
					controller.appendStatusText("Computer pegged 1 point for last card.");
					if (win) {
						computerWon();
						return true;
					}
				}
			}
			
			controller.appendStatusText("Pegging round completed.  Select "
					+ "next round button to continue.");	
			
			setGameState(GameState.PEGGING_WAITING_FOR_NEXT_ROUND);
			
		}
			
		return false;
	}

	/**
	 * Adds points to player total. 
	 * @param points to add
	 * @return true if player wins and false otherwise
	 */
	private boolean addPlayerPoints(int points) {
		playerScore += points;
		return checkForWin();
	}
	
	/**
	 * Adds points to computer total.
	 * @param points to add
	 * @return true if player wins and false otherwise
	 */
	private boolean addComputerPoints(int points) {
		computerScore += points;
		return checkForWin();
	}
	
	/**
	 * Checks whether player or compuer has won
	 * @return true if either player or computer won, false otherwise
	 */
	private boolean checkForWin() {
		if (computerScore >= WINNING_SCORE) {
			//controller.appendStatusText("COMPUTER WINS!");
			return true;
		} else if (playerScore >= WINNING_SCORE) {
			//controller.appendStatusText("PLAYER WINS!");
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * computer pegs once (if able), and continues to peg while player can't peg
	 * and computer can peg. 
	 * @return  true if computer wins, and false otherwise
	 */
	//TODO: computer selects first playable card in hand - make computer smarter
	private boolean computerPlay() {
		
		if (!computerCanPeg()) return false;
		
		do { 
			//computer plays first card in its hand that is playable
			for (int i=0; i < computerHand.size(); i++) {
				Card card = computerHand.peekAtCard(i);
				
				if (card.getPointValue() + peggingCards.getPointValue() <=
						PeggingCards.MAX_PEG_VALUE) {
					
					playerPeggedLast = false;
					int points = peggingCards.addCard(computerHand.pegCard(i));
					
					if (points > 0) {
						controller.appendStatusText("Computer pegged " + points + " points.");
						boolean win = addComputerPoints(points);
						if (win) { computerWon(); return true; }
					}
					//break out of for loop once card is played
					break;
				}
			}
		} while (!playerCanPeg() && computerCanPeg());
		
		return false;
	}
	
	/**
	 * Returns true if computer can peg
	 */
	private boolean computerCanPeg() {
		if (computerHand.size() == 0) return false;
		
		for (Card card: computerHand.getHand()) {
			if (card.getPointValue() + peggingCards.getPointValue() <= 
					PeggingCards.MAX_PEG_VALUE) 
				return true;
		}
		
		return false;
	}
	
	/** 
	 * Returns true if player can peg
	 */
	private boolean playerCanPeg() {
		if (playerHand.size() == 0) return false;
		
		for (Card card: playerHand.getHand()) {
			if (card.getPointValue() + peggingCards.getPointValue() <= 
					PeggingCards.MAX_PEG_VALUE) 
				return true;
		}
		
		return false;
	}
	
	/**
	 * Calculates points in the player and computer hands and crib and updates
	 * the internal variables for tracking points.
	 *  
	 * @return true if player or computer has won, false otherwise
	 */
	private boolean scoreHands() {
		
		//clear the pegging cards
		peggingCards.clear();
		
		//return all pegging cards to hand for scoring
		computerHand.returnPeggedCardsToHand();
		playerHand.returnPeggedCardsToHand();
		
		//flip over computer cards and crib cards
		computerHand.turnCardsFaceUp();
		crib.turnCardsFaceUp();
		
		//tally points in this order if it's player's deal
		if (playersDeal) {
			
			//tally computer hand points
			int computerPoints = Scoring.pointsInHand(computerHand.getHand(), cutCard);
			controller.appendStatusText("Computer scored " + computerPoints + " in its hand.");
			
			if (addComputerPoints(computerPoints)) {
				//handle win
				computerWon();
				return true;
			}
			
			//tally player hand points
			int playerPoints = Scoring.pointsInHand(playerHand.getHand(), cutCard);
			controller.appendStatusText("Player scored " + playerPoints + " in its hand.");
			
			if (addPlayerPoints(playerPoints)) {
				//handle win
				playerWon();
				return true;
			}
			
			//tally crib points for player
			int cribPoints = Scoring.pointsInCrib(crib.getHand(), cutCard);
			controller.appendStatusText("Player scored " + cribPoints + " in the crib.");
			
			if (addPlayerPoints(cribPoints)) {
				//handle win
				playerWon();
				return true;
			}
				
		} 
		
		//tally points in this order if it's computer's deal
		else {
			
			//tally player hand points
			int playerPoints = Scoring.pointsInHand(playerHand.getHand(), cutCard);
			controller.appendStatusText("Player scored " + playerPoints + " in its hand.");
			
			if (addPlayerPoints(playerPoints)) {
				//handle win
				playerWon();
				return true;
			}
			
			//tally computer hand points
			int computerPoints = Scoring.pointsInHand(computerHand.getHand(), cutCard);
			controller.appendStatusText("Computer scored " + computerPoints + " in its hand.");
			
			if (addComputerPoints(computerPoints)) {
				//handle win
				computerWon();
				return true;
			}
			
			//tally crib points for computer
			int cribPoints = Scoring.pointsInCrib(crib.getHand(), cutCard);
			controller.appendStatusText("Computer scored " + cribPoints + " in the crib.");
			
			if (addComputerPoints(cribPoints)) {
				//handle win
				computerWon();
				return true;
			}
		}
		
		return false;
	}
	
	private void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	
	GameState getGameState() {
		return gameState;
	}
	
	public void computerWon() {
		controller.appendStatusText("Computer WON!!");
		setGameState(GameState.GAME_OVER);
	}
	
	void playerWon() {
		controller.appendStatusText("Player WON!!");
		setGameState(GameState.GAME_OVER);
	}
	
	Hand getCrib() {
		return crib;
	}
	
	Hand getComputerHand() {
		return computerHand;
	}
	
	Hand getPlayerHand() {
		return playerHand;
	}
	
	PeggingCards getPeggingCards() {
		return peggingCards;
	}
	
	Card getCutCard() {
		return cutCard;
	}
	
	int getComputerScore() {
		return computerScore;
	}
	
	int getPlayerScore() {
		return playerScore;
	}

	int getPeggingScore() {
		return peggingCards.getPointValue();
	}
	
	boolean isPlayerDealer() {
		return playersDeal;
	}

}
