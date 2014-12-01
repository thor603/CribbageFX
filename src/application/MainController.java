package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.Cribbage.GameState;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * Class updates, controls and handles input from the user interface
 * 
 */

public class MainController extends GridPane {

	//reference provided via setCribbage() method in Cribbage constructor
	private Cribbage cribbage;
	
	//create lists of cards to make it easier to update the views using loops
	private List<ImageView> playerCardsView = new ArrayList<ImageView>();
	private List<ImageView> computerCardsView = new ArrayList<ImageView>();
	private List<ImageView> peggingCardsView = new ArrayList<ImageView>();
	private List<ImageView> cribCardsView = new ArrayList<ImageView>();
	
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="computerCard1"
    private ImageView computerCard1; // Value injected by FXMLLoader

    @FXML // fx:id="computerCard2"
    private ImageView computerCard2; // Value injected by FXMLLoader
    
    @FXML // fx:id="computerCard3"
    private ImageView computerCard3; // Value injected by FXMLLoader
    
    @FXML // fx:id="computerCard4"
    private ImageView computerCard4; // Value injected by FXMLLoader
    
    @FXML // fx:id="computerCard5"
    private ImageView computerCard5; // Value injected by FXMLLoader
    
    @FXML // fx:id="computerCard6"
    private ImageView computerCard6; // Value injected by FXMLLoader
    
    @FXML // fx:id="playerCard1"
    private ImageView playerCard1; // Value injected by FXMLLoader

    @FXML // fx:id="playerCard2"
    private ImageView playerCard2; // Value injected by FXMLLoader
    
    @FXML // fx:id="playerCard3"
    private ImageView playerCard3; // Value injected by FXMLLoader
    
    @FXML // fx:id="playerCard4"
    private ImageView playerCard4; // Value injected by FXMLLoader
    
    @FXML // fx:id="playerCard5"
    private ImageView playerCard5; // Value injected by FXMLLoader
    
    @FXML // fx:id="playerCard6"
    private ImageView playerCard6; // Value injected by FXMLLoader
    
    @FXML // fx:id="cribCard1"
    private ImageView cribCard1; // Value injected by FXMLLoader

    @FXML // fx:id="cribCard2"
    private ImageView cribCard2; // Value injected by FXMLLoader

    @FXML // fx:id="cribCard3"
    private ImageView cribCard3; // Value injected by FXMLLoader
    
    @FXML // fx:id="cribCard4"
    private ImageView cribCard4; // Value injected by FXMLLoader
    
    @FXML // fx:id="cutCard"
    private ImageView cutCard; // Value injected by FXMLLoader
    
    @FXML // fx:id="peggingCard1"
    private ImageView peggingCard1; // Value injected by FXMLLoader

    @FXML // fx:id="peggingCard2"
    private ImageView peggingCard2; // Value injected by FXMLLoader

    @FXML // fx:id="peggingCard3"
    private ImageView peggingCard3; // Value injected by FXMLLoader

    @FXML // fx:id="peggingCard4"
    private ImageView peggingCard4; // Value injected by FXMLLoader
    
    @FXML // fx:id="peggingCard5"
    private ImageView peggingCard5; // Value injected by FXMLLoader

    @FXML // fx:id="peggingCard6"
    private ImageView peggingCard6; // Value injected by FXMLLoader
    
    @FXML // fx:id="peggingCard7"
    private ImageView peggingCard7; // Value injected by FXMLLoader

    @FXML // fx:id="peggingCard8"
    private ImageView peggingCard8; // Value injected by FXMLLoader
    
    @FXML // fx:id="computerScoreLabel"
    private Label computerScoreLabel; // Value injected by FXMLLoader

    @FXML // fx:id="playerScoreLabel"
    private Label playerScoreLabel; // Value injected by FXMLLoader
    
    @FXML // fx:id="peggingScoreLabel"
    private Label peggingScoreLabel; // Value injected by FXMLLoader

    @FXML // fx:id="nextRoundButton"
    private Button nextRoundButton; // Value injected by FXMLLoader
    
    @FXML // fx:id="textArea"
    private TextArea textArea; // Value injected by FXMLLoader
    
    public MainController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        //add computer cards, player cards, pegging cards and crib cards to 
        //lists for easier access
        computerCardsView.add(computerCard1);
        computerCardsView.add(computerCard2);
        computerCardsView.add(computerCard3);
        computerCardsView.add(computerCard4);
        computerCardsView.add(computerCard5);
        computerCardsView.add(computerCard6);
        
        playerCardsView.add(playerCard1);
        playerCardsView.add(playerCard2);
        playerCardsView.add(playerCard3);
        playerCardsView.add(playerCard4);
        playerCardsView.add(playerCard5);
        playerCardsView.add(playerCard6);
        
        peggingCardsView.add(peggingCard1);
        peggingCardsView.add(peggingCard2);
        peggingCardsView.add(peggingCard3);
        peggingCardsView.add(peggingCard4);
        peggingCardsView.add(peggingCard5);
        peggingCardsView.add(peggingCard6);
        peggingCardsView.add(peggingCard7);
        peggingCardsView.add(peggingCard8);

        cribCardsView.add(cribCard1);
        cribCardsView.add(cribCard2);
        cribCardsView.add(cribCard3);
        cribCardsView.add(cribCard4);
    }
    
    void setCribbage(Cribbage cribbage) {
    	this.cribbage = cribbage;
    }
    
    /**
     * Updates the user display
     */
    void updateDisplay() {
    	
    	//update computer hand
    	Hand computerHand = cribbage.getComputerHand();
    	for (int i=0; i < computerCardsView.size(); i++) {
    		if (i < computerHand.size()) {
    			computerCardsView.get(i).setImage(computerHand.peekAtCard(i).getImage());
    		} else {
    			
    			computerCardsView.get(i).setImage(null);
    		}
    	}
    	
    	//update player hand
    	Hand playerHand = cribbage.getPlayerHand();
    	for (int i=0; i < playerCardsView.size(); i++) {
    		if (i < playerHand.size()) {
    			playerCardsView.get(i).setImage(playerHand.peekAtCard(i).getImage());
    		} else {
    			playerCardsView.get(i).setImage(null);
    		}
    	}
    	
    	//update pegging cards
    	
    	PeggingCards peggingCards = cribbage.getPeggingCards();
    	for (int i=0; i < peggingCardsView.size(); i++) {
    		if (i < peggingCards.size()) {
    			peggingCardsView.get(i).setImage(peggingCards.getCard(i).getImage());
    		} else {
    			peggingCardsView.get(i).setImage(null);
    		}
    	}    	
    	
    	//update crib
    	
    	Hand crib= cribbage.getCrib();
    	for (int i=0; i < cribCardsView.size(); i++) {
    		if (i < crib.size()) {
    			cribCardsView.get(i).setImage(crib.peekAtCard(i).getImage());
    		} else {
    			cribCardsView.get(i).setImage(null);
    		}
    	}
    	
    	//update cut card
    	if (cribbage.getCutCard() != null) {
    		cutCard.setImage(cribbage.getCutCard().getImage());
    	} else {
    		cutCard.setImage(null);
    	}
    	
    	//update scores
    	
    	if (cribbage.isPlayerDealer()){
    		computerScoreLabel.setText(Integer.toString(cribbage.getComputerScore()));
    		playerScoreLabel.setText(Integer.toString(cribbage.getPlayerScore()) + " (dealer)");
    	} else {
    		computerScoreLabel.setText(Integer.toString(cribbage.getComputerScore()) + " (dealer)");
    		playerScoreLabel.setText(Integer.toString(cribbage.getPlayerScore()));
    	}
    	peggingScoreLabel.setText(Integer.toString(cribbage.getPeggingScore()));
    	
    	//update next round button
    	if (cribbage.getGameState() == GameState.PEGGING_WAITING_FOR_NEXT_ROUND ||
    			cribbage.getGameState() == GameState.COUNT_POINTS ||
    			cribbage.getGameState() == GameState.GAME_OVER) {
    		nextRoundButton.setDisable(false);
    	} else {
    		nextRoundButton.setDisable(true);
    	}
    }
    
    /**
     * Sends user input to cribbage class for processing
     */
    private void handleUserInput(UserInput input) {
    	cribbage.handleUserInput(input);
    }
    
    @FXML
    //void playerCard1Clicked(ActionEvent event) {
    
    void playerCard1Clicked(Event event) {
    	//System.out.println("card 1 clicked");
    	handleUserInput(UserInput.getInstanceCardClick(0));
    }
    
    @FXML
    //void playerCard2Clicked(ActionEvent event) {
    
    void playerCard2Clicked(Event event) {
    	//System.out.println("card 2 clicked");
    	handleUserInput(UserInput.getInstanceCardClick(1));
    }
    
    @FXML
    //void playerCard3Clicked(ActionEvent event) {
    
    void playerCard3Clicked(Event event) {
    	//System.out.println("card 3 clicked");
    	handleUserInput(UserInput.getInstanceCardClick(2));
    }
    
    @FXML
    //void playerCard4Clicked(ActionEvent event) {
    
    void playerCard4Clicked(Event event) {
    	//System.out.println("card 4 clicked");
    	handleUserInput(UserInput.getInstanceCardClick(3));
    }
    
    @FXML
    //void playerCard5Clicked(ActionEvent event) {
    
    void playerCard5Clicked(Event event) {
    	//System.out.println("card 5 clicked");
    	handleUserInput(UserInput.getInstanceCardClick(4));
    }

    @FXML
    //void playerCard6Clicked(ActionEvent event) {
    
    void playerCard6Clicked(Event event) {
    	//System.out.println("card 6 clicked");
    	handleUserInput(UserInput.getInstanceCardClick(5));
    }
    
    @FXML
    void nextRoundButtonClicked(ActionEvent event) {
    	//System.out.println("NextRoundButtonClicked!");
    	handleUserInput(UserInput.getInstanceButtonAction());
    }
    
    //add text to status area
    void appendStatusText(String string) {
    	textArea.appendText(string + '\n');
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert cutCard != null : "fx:id=\"cutCard\" was not injected: check your FXML file 'Main.fxml'.";
        assert peggingCard3 != null : "fx:id=\"peggingCard3\" was not injected: check your FXML file 'Main.fxml'.";
        assert peggingCard4 != null : "fx:id=\"peggingCard4\" was not injected: check your FXML file 'Main.fxml'.";
        assert computerScoreLabel != null : "fx:id=\"computerScoreLabel\" was not injected: check your FXML file 'Main.fxml'.";
        assert peggingCard1 != null : "fx:id=\"peggingCard1\" was not injected: check your FXML file 'Main.fxml'.";
        assert peggingCard2 != null : "fx:id=\"peggingCard2\" was not injected: check your FXML file 'Main.fxml'.";
        assert peggingCard5 != null : "fx:id=\"peggingCard5\" was not injected: check your FXML file 'Main.fxml'.";
        assert peggingCard6 != null : "fx:id=\"peggingCard6\" was not injected: check your FXML file 'Main.fxml'.";
        assert peggingCard7 != null : "fx:id=\"peggingCard7\" was not injected: check your FXML file 'Main.fxml'.";
        assert peggingCard8 != null : "fx:id=\"peggingCard8\" was not injected: check your FXML file 'Main.fxml'.";
        assert playerCard3 != null : "fx:id=\"playerCard3\" was not injected: check your FXML file 'Main.fxml'.";
        assert playerCard4 != null : "fx:id=\"playerCard4\" was not injected: check your FXML file 'Main.fxml'.";
        assert playerCard1 != null : "fx:id=\"playerCard1\" was not injected: check your FXML file 'Main.fxml'.";
        assert playerCard2 != null : "fx:id=\"playerCard2\" was not injected: check your FXML file 'Main.fxml'.";
        assert playerCard5 != null : "fx:id=\"playerCard5\" was not injected: check your FXML file 'Main.fxml'.";
        assert playerCard6 != null : "fx:id=\"playerCard6\" was not injected: check your FXML file 'Main.fxml'.";
        assert playerScoreLabel != null : "fx:id=\"playerScoreLabel\" was not injected: check your FXML file 'Main.fxml'.";
        assert computerCard5 != null : "fx:id=\"computerCard5\" was not injected: check your FXML file 'Main.fxml'.";
        assert computerCard6 != null : "fx:id=\"computerCard6\" was not injected: check your FXML file 'Main.fxml'.";
        assert computerCard3 != null : "fx:id=\"computerCard3\" was not injected: check your FXML file 'Main.fxml'.";
        assert computerCard4 != null : "fx:id=\"computerCard4\" was not injected: check your FXML file 'Main.fxml'.";
        assert computerCard1 != null : "fx:id=\"computerCard1\" was not injected: check your FXML file 'Main.fxml'.";
        assert cribCard4 != null : "fx:id=\"cribCard4\" was not injected: check your FXML file 'Main.fxml'.";
        assert computerCard2 != null : "fx:id=\"computerCard2\" was not injected: check your FXML file 'Main.fxml'.";
        assert cribCard1 != null : "fx:id=\"cribCard1\" was not injected: check your FXML file 'Main.fxml'.";
        assert cribCard2 != null : "fx:id=\"cribCard2\" was not injected: check your FXML file 'Main.fxml'.";
        assert cribCard3 != null : "fx:id=\"cribCard3\" was not injected: check your FXML file 'Main.fxml'.";
        assert textArea != null : "fx:id=\"textArea\" was not injected: check your FXML file 'Main.fxml'.";
        assert peggingScoreLabel != null : "fx:id=\"peggingScoreLabel\" was not injected: check your FXML file 'Main.fxml'.";
        assert nextRoundButton != null : "fx:id=\"nextRoundButton\" was not injected: check your FXML file 'Main.fxml'.";
    }

}

