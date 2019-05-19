package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class test {
	@FXML
	Button start;
	@FXML
	Button raise;
	@FXML
	Button check;
	@FXML
	Button fold;
	@FXML
	Button power;
	@FXML
	Button quit;
	
	@FXML
	ImageView OppPowerUp;
	@FXML
	ImageView PlayerPowerUp;
	
	@FXML
	GridPane oppDeck;
	@FXML
	GridPane plDeck;
	
	@FXML
	Text plPoints;
	@FXML 
	Text opPoints;
	@FXML
	Text pot;
	@FXML
	Text plupdates;
	@FXML
	Text pltitle;
	@FXML
	Text opupdates;
	@FXML
	Text optitle;
	
	@FXML
	ImageView OppCard1;
	@FXML
	ImageView OppCard2;
	
	@FXML
	ImageView PlayerCard1;
	@FXML
	ImageView PlayerCard2;

	@FXML
	ImageView BoardCard1;
	@FXML
	ImageView BoardCard2;
	@FXML
	ImageView BoardCard3;
	@FXML
	ImageView BoardCard4;
	@FXML
	ImageView BoardCard5;

	
	Game poker = new Game();
	int currentPlayer = 0;
	int HasBlind = 0;
	boolean quitFlag = false;
	
	@FXML
	public void initialize() {   
		start.setVisible(true);
	}
	
	@FXML 
	public void startGame(MouseEvent event) { 
		//Show buttons 
		start.setDisable(true);
		start.setVisible(false);
		pot.setVisible(true);
		pltitle.setVisible(true);
		plupdates.setVisible(true);
		optitle.setVisible(true);
		opupdates.setVisible(true);
	
		playGame();
	 }
	
	public void playGame() {
		while(quitFlag == false)
		{
		
		Hand[] hands = poker.getPlayers();
		Card[] plhand = hands[0].GetHand();
		Hand board = poker.getBoard();
		Card[] boardCards = board.GetHand();

		plPoints.setText(String.valueOf(hands[0].GetWallet()));
		opPoints.setText(String.valueOf(hands[1].GetWallet()));
		
			System.out.println("************NEW_ROUND************");
			
			currentPlayer = poker.GetNextPlayer(HasBlind);	//Pop up telling user they have the blind
			if(HasBlind == 0) poker.TakeBet(HasBlind, 25,plupdates);
			else poker.TakeBet(HasBlind, 25, opupdates);//set blind
			
			//Deal cards to players & display on board
			poker.DealCards(0);
			hands = ((Game) poker).getPlayers();
			plhand = hands[0].GetHand();
			
			Image plcard1 = getCardImage(plhand[0].getValue_S(),plhand[0].getSuit_S());
			Image plcard2 = getCardImage(plhand[1].getValue_S(),plhand[1].getSuit_S());
			PlayerCard1.setImage(plcard1);
			PlayerCard2.setImage(plcard2);
			
			//System.out.println("first curr player " + currentPlayer);
			currentPlayer = poker.BetRound(currentPlayer,plupdates,opupdates);
			//System.out.println("second curr player " + currentPlayer);
			//board = poker.getBoard();
			updateTotals();
			poker.collectPot();
			
			poker.DealCards(3);
			board = poker.getBoard();
			boardCards = board.GetHand();
			Image bCard1 = getCardImage(boardCards[0].getValue_S(),boardCards[0].getSuit_S());
			Image bCard2 = getCardImage(boardCards[1].getValue_S(),boardCards[1].getSuit_S());
			Image bCard3 = getCardImage(boardCards[2].getValue_S(),boardCards[2].getSuit_S());
			
			BoardCard1.setImage(bCard1);
			BoardCard2.setImage(bCard2);
			BoardCard3.setImage(bCard3);
			
			poker.printBoard();
			currentPlayer = poker.BetRound(currentPlayer,plupdates,opupdates);
			//updates.setText(poker.getUpdateDialog());
			updateTotals();
			poker.collectPot();
			
			poker.DealCards(1);
			poker.printBoard();

			board = poker.getBoard();
			boardCards = board.GetHand();
			
			Image bCard4 = getCardImage(boardCards[3].getValue_S(),boardCards[3].getSuit_S());
			BoardCard4.setImage(bCard4);
			
			currentPlayer = poker.BetRound(currentPlayer,plupdates,opupdates);
			//updates.setText(poker.getUpdateDialog());
			updateTotals();
			poker.collectPot();
			
			poker.DealCards(1);
			board = poker.getBoard();
			boardCards = board.GetHand();
			Image bCard5 = getCardImage(boardCards[4].getValue_S(),boardCards[4].getSuit_S());
			BoardCard5.setImage(bCard5);
			poker.printBoard();
			currentPlayer = poker.BetRound(currentPlayer,plupdates,opupdates);
			//updates.setText(poker.getUpdateDialog());
			updateTotals();
			poker.collectPot();
			
			
			//Game has reached end, evaluate then clear for next game
			poker.evaluate();
			poker.DealPowerup();
			poker.clearUp();
			resetBoard();
			
			HasBlind = poker.GetNextPlayer(HasBlind);
			
			if(poker.CheckWin() != -1) quitFlag = true;
		}
	}

	
	public void updateTotals() {
		System.out.println(pot.getText()+ " " + poker.getBetAmounts()[0]+ " " + poker.getBetAmounts()[1]);
		int val = Integer.parseInt(pot.getText()) + poker.getBetAmounts()[0] + poker.getBetAmounts()[1];
		//System.out.println(val);
		pot.setText(String.valueOf(val));
		opPoints.setText(String.valueOf(poker.getPlayers()[1].GetWallet() -poker.getBetAmounts()[1]));
		plPoints.setText(String.valueOf(poker.getPlayers()[0].GetWallet()- poker.getBetAmounts()[0]));
	}
	public void resetBoard() {
		Image back = getCardImage("","");
		OppPowerUp.setImage(back);
		PlayerPowerUp.setImage(back);
		OppCard1.setImage(back);
		OppCard2.setImage(back);
		PlayerCard1.setImage(back);
		PlayerCard2.setImage(back);
		BoardCard1.setImage(back);
		BoardCard2.setImage(back);
		BoardCard3.setImage(back);
		BoardCard4.setImage(back);
		BoardCard5.setImage(back);
		pot.setText("0000");
	}
	public Image getCardImage(String val, String suit) {
		Image card = null;
		if(suit == "CLUBS")
		{
			switch(val) {
			case "ACE":
				card = new Image("@/../images/ace_of_clubs.png");break;
			case "2":
				card = new Image("@/../images/2_of_clubs.png");break;
			case "3":
				card = new Image("@/../images/3_of_clubs.png");break;
			case "4":
				card = new Image("@/../images/4_of_clubs.png");break;
			case "5":
				card = new Image("@/../images/5_of_clubs.png");break;
			case "6":
				card = new Image("@/../images/6_of_clubs.png");break;
			case "7":
				card = new Image("@/../images/7_of_clubs.png");break;
			case "8":
				card = new Image("@/../images/8_of_clubs.png");break;
			case "9":
				card = new Image("@/../images/9_of_clubs.png");break;
			case "10":
				card = new Image("@/../images/10_of_clubs.png");break;
			case "JACK":
				card = new Image("@/../images/jack_of_clubs.png");break;
			case "QUEEN":
				card = new Image("@/../images/queen_of_clubs.png");break;
			case "KING":
				card = new Image("@/../images/king_of_clubs.png");break;
			}	
		}
		else if(suit == "DIAMONDS") {
			switch(val) {
			case "ACE":
				card = new Image("@/../images/ace_of_diamonds.png");break;
			case "2":
				card = new Image("@/../images/2_of_diamonds.png");break;
			case "3":
				card = new Image("@/../images/3_of_diamonds.png");break;
			case "4":
				card = new Image("@/../images/4_of_diamonds.png");break;
			case "5":
				card = new Image("@/../images/5_of_diamonds.png");break;
			case "6":
				card = new Image("@/../images/6_of_diamonds.png");break;
			case "7":
				card = new Image("@/../images/7_of_diamonds.png");break;
			case "8":
				card = new Image("@/../images/8_of_diamonds.png");break;
			case "9":
				card = new Image("@/../images/9_of_diamonds.png");break;
			case "10":
				card = new Image("@/../images/10_of_diamonds.png");break;
			case "JACK":
				card = new Image("@/../images/jack_of_diamonds.png");break;
			case "QUEEN":
				card = new Image("@/../images/queen_of_diamonds.png");break;
			case "KING":
				card = new Image("@/../images/king_of_diamonds.png");break;
			}	
		}
		else if(suit == "HEARTS") {
			switch(val) {
			case "ACE":
				card = new Image("@/../images/ace_of_hearts.png");break;
			case "2":
				card = new Image("@/../images/2_of_hearts.png");break;
			case "3":
				card = new Image("@/../images/3_of_hearts.png");break;
			case "4":
				card = new Image("@/../images/4_of_hearts.png");break;
			case "5":
				card = new Image("@/../images/5_of_hearts.png");break;
			case "6":
				card = new Image("@/../images/6_of_hearts.png");break;
			case "7":
				card = new Image("@/../images/7_of_hearts.png");break;
			case "8":
				card = new Image("@/../images/8_of_hearts.png");break;
			case "9":
				card = new Image("@/../images/9_of_hearts.png");break;
			case "10":
				card = new Image("@/../images/10_of_hearts.png");break;
			case "JACK":
				card = new Image("@/../images/jack_of_hearts.png");break;
			case "QUEEN":
				card = new Image("@/../images/queen_of_hearts.png");break;
			case "KING":
				card = new Image("@/../images/king_of_hearts.png");break;
			}	
		}
		else if(suit == "SPADES") {
			switch(val) {
			case "ACE":
				card = new Image("@/../images/ace_of_spades.png");break;
			case "2":
				card = new Image("@/../images/2_of_spades.png");break;
			case "3":
				card = new Image("@/../images/3_of_spades.png");break;
			case "4":
				card = new Image("@/../images/4_of_spades.png");break;
			case "5":
				card = new Image("@/../images/5_of_spades.png");break;
			case "6":
				card = new Image("@/../images/6_of_spades.png");break;
			case "7":
				card = new Image("@/../images/7_of_spades.png");break;
			case "8":
				card = new Image("@/../images/8_of_spades.png");break;
			case "9":
				card = new Image("@/../images/9_of_spades.png");break;
			case "10":
				card = new Image("@/../images/10_of_spades.png");break;
			case "JACK":
				card = new Image("@/../images/jack_of_spades.png");break;
			case "QUEEN":
				card = new Image("@/../images/queen_of_spades.png");break;
			case "KING":
				card = new Image("@/../images/king_of_spades.png");break;
			}	
		}
		else if(suit == "POWER-UP") {
			switch(val) {
			case "Redraw":
				card = new Image("@/../images/Redraw.png");break;
			case "Redraw Board":
				card = new Image("@/../images/Redraw.png");break;
			case "Trade Hands":
				card = new Image("@/../images/Redraw.png");break;
			case "Communism":
				card = new Image("@/../images/Redraw.png");break;
			case "See an opponent’s card":
				card = new Image("@/../images/Redraw.png");break;
			case "Free call":
				card = new Image("@/../images/Redraw.png");break;
			case "Opponent Redraw":
				card = new Image("@/../images/Redraw.png");break;
			case "Revive":
				card = new Image("@/../images/Redraw.png");break;
			case "Replace last card":
				card = new Image("@/../images/Redraw.png");break;
			case "Change 1 suit":
				card = new Image("@/../images/Redraw.png");break;
			case "Shuffle your opponent's suits":
				card = new Image("@/../images/Redraw.png");break;
			}
		}
		else {
			card = new Image("@/../images/back.jpg");
		}
		return card;
	}
}

