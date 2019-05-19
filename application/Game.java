package application;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;

public class Game {
	
	//An Deck that represents a 52 card poker deck
	private Deck gameDeck;
	
	//An array of ints that keep track how much a player has bet during a bet round
	private int[] betAmounts;
	
	//An Deck that represents the powerup cards on the board
	private Deck powerupDeck;
	
	//players 0-4 whatever.
	private Hand players[];
	
	//The Hand that is represents the poker table
	//it contains the pot and global cards
	private Hand board;
	
	//Hand used to hold power-up cards for each player
	private Card powerups[];
	
	private String updateDialog;
	
	/*
	 * Getters and setters
	 */
	public Deck getGameDeck() {
		return gameDeck;
	}

	public void setGameDeck(Deck gameDeck) {
		this.gameDeck = gameDeck;
	}

	public int[] getBetAmounts() {
		return betAmounts;
	}

	public void setBetAmounts(int[] betAmounts) {
		this.betAmounts = betAmounts;
	}

	public Deck getPowerupDeck() {
		return powerupDeck;
	}

	public void setPowerupDeck(Deck powerupDeck) {
		this.powerupDeck = powerupDeck;
	}

	public Hand[] getPlayers() {
		return players;
	}

	public void setPlayers(Hand[] players) {
		this.players = players;
	}

	public Hand getBoard() {
		return board;
	}

	public void setBoard(Hand board) {
		this.board = board;
	}

	public Card[] getPowerups() {
		return powerups;
	}

	public void setPowerups(Card[] powerups) {
		this.powerups = powerups;
	}

	public String getUpdateDialog() {
		return updateDialog;
	}

	public void setUpdateDialog(String dialog) {
		this.updateDialog = dialog;
	}

	/*
	 * -Constructor method- 
	 * Creates a Game object for num_players to play
	 */
	public Game(int num_players)
	{
		gameDeck = new Deck(0);
		board = new Hand("Board");
		powerupDeck = new Deck(1);
		betAmounts = new int[num_players];
		players = new Hand[num_players];
		powerups = new Card[num_players];
		gameDeck.shuffle();
		powerupDeck.shuffle();
		for(int i =0; i< players.length;i++)
		{
			powerups[i] = new Card(0,0);
			players[i] = new Hand("Player " + i);
			players[i].SetWallet(500);
			if(i > 0)
			{
				players[i].set_ai(); //make ai players
			}
		}
	}
	
	/*
	 * -Constructor method- 
	 * Creates a Game object for the default number of players (2)
	 */
	public Game()
	{
		gameDeck = new Deck(0);
		powerupDeck = new Deck(1);
		board = new Hand("Board");
		betAmounts = new int[2];
		players = new Hand[2];
		powerups = new Card[2];
		gameDeck.shuffle();
		powerupDeck.shuffle();
		for(int i =0; i< players.length;i++)
		{
			powerups[i] = new Card(0,0);
			players[i] = new Hand("Player " + i);
			players[i].SetWallet(500);
			if(i > 0)
			{
				players[i].set_ai(); //make ai players
			}
		}
	}
	
	/*
	 * -Game Method-
	 * if option is 0 then deal 2 cards to players
	 * if option is greater than 0 then deal "option" number of cards to board
	 * Precondition: option must be 0 or a positive int 
	 */
	public void DealCards(int option)
	{
		if(option == 0)
		{
			//Deal 2 cards to each player
			for(int i = 0; i < players.length; i++)
			{
				players[i].TakeCard(gameDeck.DealCard());
				players[i].TakeCard(gameDeck.DealCard());
			}
		}
		else
		{
			for(int i = 0; i < option; i++)
			{
				board.TakeCard(gameDeck.DealCard());
			}
		}
		
	}
	
	/*
	 * -Game Method-
	 * Sets player at index "user" bet equal to amount.
	 * This is primarily used to take blinds.
	 * Precondtions: user must be a index that exist in players/ amount should be >= 0
	 */
	public void TakeBet(int user, int amount, Text text)
	{
		
		System.out.println(user + "has the blind: " + amount);
		text.setText("Player " + user + " has the blind. " + amount + " has been added to the pot.");
		
		if(amount > players[user].GetWallet()) //if player can't match blind go all in
		{
			betAmounts[user] = players[user].GetWallet();
		}
		else
		{
			betAmounts[user] = amount;
		}	
	}
	
	
	/*
	 * -Game Method-
	 * Prints out all cards held by each player
	 */
	public void print_all_hands()
	{
		for (int i =0; i < players.length;i++)
		{
			players[i].PrintHand();
		}
	}
	
	/*
	 * -Game Method-
	 * Evaluate the hands of each player and determine the winner of the round
	 * Then distribute the pot accordingly
	 * Precondition: Players contain 2 cards and board contains 5 cards
	 */
	public void evaluate()
	{
		//Show player hands and board before giving result
		this.print_all_hands();
		board.PrintHand();
		
		//Merge hands with board to eval
		for(int i = 0; i < players.length; i++)
		{
			players[i].MergeHand(board);
		}
		
		//Store and print results of evaluation
		int[] scores = new int[]{players[0].Evaluate(),players[1].Evaluate()};
		for(int i = 0; i < players.length; i++)
		{
			System.out.println(players[i].getName() + " has a " +players[i].evalString(scores[i]));
		}
		
		//Check players folded and use it to determine winners
		if(players[0].getFold() == true) //if player0 folds/ player1 wins
		{
			System.out.println(players[1].getName() + " wins the pot of $" +  board.GetWallet());
			players[1].addWallet(board.GetWallet());
		}
		else if(players[1].getFold() == true) //if player1 folds/ player0 wins
		{
			System.out.println(players[0].getName() + " wins the pot of $" +  board.GetWallet());
			players[0].addWallet(board.GetWallet());
		}
		else //If no folds use scores to determine the winner
		{
			if(scores[0] < scores[1])
			{
				System.out.println(players[1].getName() + " wins the pot of $" +  board.GetWallet());
				players[1].addWallet(board.GetWallet());
			}
			else if (scores[1] == scores[0])
			{
				//If scores are equal use TieBreaker
				int x = this.BreakTie(scores[0], players);
				if(x == -1) //split pot
				{
					x = board.GetWallet()/players.length;
					System.out.println("Players split the pot!");
					for(int i = 0; i < players.length; i++)
					{
						players[i].addWallet(x);;
					}
				}
				else
				{
					System.out.println(players[x].getName() + " wins the pot of $" +  board.GetWallet());
					players[x].addWallet(board.GetWallet());
				}
			}
			else
			{
				System.out.println(players[0].getName() + " wins the pot of $" +  board.GetWallet());
				players[0].addWallet(board.GetWallet());
			}
		}
		
	}
	
	/*
	 * -Game Method-
	 * Goes through a betting round until all players match bets or fold
	 * Starts at currentPlayer
	 * Precondition: currentPlayer has to be a index in players
	 */
	public int BetRound(int currentPlayer, Text pltext, Text optext)
	{
		if(this.NotFolded() <= 1) //Check if number of folds enough to skip betting
		{
			return currentPlayer;
		}
		
		System.out.println("==========START_BET_ROUND==========");
		int StopHere = currentPlayer; //Stop on this player once the loop cycles around
		int turn = currentPlayer; //Player is current going
		int minimumBet; //Smallest bet player can choose
		int input; //Used to store user/ai input
		int maxBet; //used to define maximum bet
		boolean PowerupFlag = false; //Flag used to repeat turns when Powerups are played
		//Scanner Takebet;
		
		TextInputDialog dialog = new TextInputDialog("");
		
//		Alert alert = new Alert(AlertType.CONFIRMATION);
//		alert.setTitle("Confirmation Dialog with Custom Actions");
//		alert.setHeaderText("Look, a Confirmation Dialog with Custom Actions");
//		alert.setContentText("Choose your option.");
//		
//		ButtonType buttonTypeOne = new ButtonType("Raise");
//		ButtonType buttonTypeTwo = new ButtonType("Call/Check");
//		ButtonType buttonTypeThree = new ButtonType("Fold");
//		ButtonType buttonTypeFour = new ButtonType("Power-ups");
//		ButtonType buttonTypeFive = new ButtonType("Quit");
//		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeFour, buttonTypeFive);
//		  
//		Optional<ButtonType> resultButton = alert.showAndWait();
		
		
		do
		{
			if(turn != -1) //if both players fold this prevents crash
			{
			  PowerupFlag = false;
			  minimumBet = betAmounts[this.GetLastPlayer(turn)] - betAmounts[turn];
			  maxBet =  this.GetMaxBet();
			  if(minimumBet < 0) minimumBet = 0; //prevent negative minimums
			  if(maxBet < minimumBet) //Prevent minBet from being higher than maxBet
			  {
				  minimumBet = maxBet;
			  }

			  if(players[turn].is_ai() == false)
			  {
//				  System.out.println("Minimum Bet is $" + minimumBet);
//				  System.out.println("Maximum Bet is $" + maxBet);
//				  System.out.println("This round you have bet $" + betAmounts[turn]);
//				  System.out.println("Amount left in wallet $" + (players[turn].GetWallet() - betAmounts[turn]));
			  }
			  input = this.BetMenu(turn,players[turn]);
			  switch(input)
			  {
			  case 1:
				  if(players[turn].is_ai())
				  {
					  if(minimumBet == maxBet) input = minimumBet;
					  else input = ThreadLocalRandom.current().nextInt(minimumBet, (maxBet + 1)/2);
					  betAmounts[turn] += input;
				  }
				  else
				  {
					  do 
					  {
						  //dialog.show();
						  
						  //Alert betInfo = new Alert(AlertType.INFORMATION);
						  dialog.setTitle("Betting Information");
						  dialog.setHeaderText("Please enter your bet amount.");
						  dialog.setContentText("Minimum Bet is $" + minimumBet + "\nMaximum Bet is $" + maxBet + "\nThis round you have bet $" + betAmounts[turn] + "\nAmount left in wallet $" + (players[turn].GetWallet() - betAmounts[turn]));
						  //betInfo.show();
						  
						  Optional<String> result = dialog.showAndWait();
						  input = Integer.valueOf(result.get());
						  
						  //Optional<String> betInputOptional = dialog.showAndWait();
						  //betInput = Integer.parseInt(betInputOptional.get());
						  
						  //SCANNER FOR TAKING USER RAISE
						  //System.out.println("How much do you want to bet?" );
						  //Takebet = new Scanner(System.in);
						  //if(Takebet.hasNextInt())
						  //	  input = Takebet.nextInt();
						  
					  }while(input < minimumBet || input > maxBet);
					  betAmounts[turn] += input;
				  }
				  System.out.println(players[turn].getName() + " has bet $" + input );
				  if(turn == 0) pltext.setText(players[turn].getName() + " has bet $" + input);
				  else optext.setText(players[turn].getName() + " has bet $" + input);
				  StopHere = turn;
				  break;
			  case 2:
				  if(minimumBet < 0)
				  {
					  minimumBet = 0; //prevent negative betting
				  }
				  else
				  {
					  betAmounts[turn] += minimumBet; 
				  }
				  System.out.println(players[turn].getName() + " has called with $" + minimumBet );
				  if(turn == 0) pltext.setText(players[turn].getName() + " has called with $" + minimumBet);
				  else optext.setText(players[turn].getName() + " has called with $" + minimumBet);
				  break;
			  case 3:
				  System.out.println(players[turn].getName() + " has folded!");
				  if(turn == 0) pltext.setText(players[turn].getName() + " has folded!");
				  else optext.setText(players[turn].getName() + " has folded!");
				  players[turn].setFold(true);
				  if(StopHere == turn)
				  {
					  StopHere = this.GetNextPlayer(turn); 
				  }
				  break;
			  case 4:
				  input = this.playPowerup(turn);
				  if(input == -1)
				  {
					  System.out.println(players[turn].getName() + " doesn't have any power-ups to play!");
					  if(turn == 0) pltext.setText(players[turn].getName() + " doesn't have any power-ups to play!");
					  else optext.setText(players[turn].getName() + " doesn't have any power-ups to play!");
					  PowerupFlag = true;
				  }
				  else if(input == 7) //Check for revive
				  {
					  System.out.println(players[turn].getName() + " has showed their revive power-up!"); 
					  if(turn==0) pltext.setText(players[turn].getName() + " has showed their revive power-up!");
					  else optext.setText(players[turn].getName() + " has showed their revive power-up!");
					  PowerupFlag = true;
				  }
				  else if(input == 5) //Check for free call
				  {
					  betAmounts[turn] += minimumBet;
					  players[turn].addWallet(minimumBet);
					  PowerupFlag = false;
				  }
				  else
				  {
					  System.out.println(players[turn].getName() + " has played power-up " + powerups[turn].getValue_S());
					  if(turn == 0) pltext.setText(players[turn].getName() + " has played power-up " + powerups[turn].getValue_S());
					  else optext.setText(players[turn].getName() + " has played power-up " + powerups[turn].getValue_S());
					  powerups[turn] = new Card(0,0);
					  PowerupFlag = true;
				  }
				  break;
			  case 5:
				  System.out.println(players[turn].getName() + " has quit!");
				  if(turn == 0) pltext.setText(players[turn].getName() + " has quit!");
				  else optext.setText(players[turn].getName() + " has quit!");
				  System.exit(0);
				  break;
			  default:
				  System.out.println("INVALID INPUT");
				  if(turn == 0) pltext.setText("INVALID INPUT");
				  else optext.setText("NVALID INPUT");
				  break;
			  }
			}
			else
			{
				StopHere = -1; //both players have folded so terminate loop
			}
		if(PowerupFlag == false)
		{
			turn = this.GetNextPlayer(turn);
		}
		System.out.println("----------------------------------------");
		}while(StopHere != turn);
		
		return turn;
	}
	
	/* -Eval method-
	 * Returns the index of the winner from the array of Hands "players"
	 * if no winners are found then returns -1  which means the pot is to be split
	 * type should the value obtained during evaluation of hands
	 * THIS METHOD ASSUMES THE PLAYERS HAVE HAD A TIE
	 */
	public int BreakTie(int type, Hand[] players)
	{
		int value1, value2;
		if(type == 9) return -1;
		else if(type == 0)
		{
			for(int k = 0; k < 7; k++)
			{
				players[0].getHighCard(k);
			    players[1].getHighCard(k);
			    
			    value1 = players[0].giveTieValue();
			    value2 = players[1].giveTieValue();
			    //Check if high card is ace; to inflate the value for comparison
				if(value1 == 1) value1 = 14;
				if(value2 == 1) value2 = 14;
				
				if(value1 > value2) return 0;
				else if(value1 < value2) return 1;
			}
		}
		else
		{
			value1 = players[0].giveTieValue();
			value2 = players[1].giveTieValue();
			//Check if high card is ace; to inflate the value for comparison
			if(value1 == 1) value1 = 14;
			if(value2 == 1) value2 = 14;
			if(value1 > value2) return 0;
			else if(value1 < value2) return 1;
		}
		return -1;
	}
	
	//power up cards
	public static void show_opponents_cards(Hand opponent)
	{
		opponent.PrintHand();
	}
	/*
	 * -Power-up-Function-
	 * Replace cards currently on the board with new cards from the deck
	 */
	public void redrawTable()
	{
		for(int i = 0; i < board.GetCardNum(); i++)
		{
			Card temp = gameDeck.DealCard();
			board.replace_at_indec(i, temp);
		}
	}
	/*Power Up Function
	 * pass in player to have hand replaced
	 */
	public void opponent_redraw(int player)
	{
		
		players[player].ClearHand();
		players[player].TakeCard(gameDeck.DealCard());
		players[player].TakeCard(gameDeck.DealCard());
	}
	
	/* Power UP Function
	 * swaps the hands of the two players
	 */
	public void swap_hands()
	{
		Card[] user0 = new Card[]{players[0].get_at_index(0),players[0].get_at_index(1)};
		Card[] user1 = new Card[]{players[1].get_at_index(0),players[1].get_at_index(1)};
		//replace player1's cards
		players[0].replace_at_indec(0, user1[0]);
		players[0].replace_at_indec(1, user1[1]);
		//replace player2's cards
		players[1].replace_at_indec(0, user0[0]);
		players[1].replace_at_indec(1, user0[1]);
		
	}
	
	/*
	 * Power up Function
	 * set distribute everyones wallets equally
	 */
	public void communism()
	{
		//tax the rich and give to the needy
		int total = 0;
		total = players[0].GetWallet();
		total += players[1].GetWallet();
		
		total = total/2;
		
		//actual communism
		players[0].SetWallet(total);
		players[0].SetWallet(total);
		
		//clear bet history
		int temp = betAmounts.length;
		for (int x=0; x< temp; x++)
		{
			betAmounts[x]=0;
		}
		
	}
	
	/*
	 * Power Up Function
	 * Redraw last card
	 */
	public void redraw_last(int user)
	{
		players[user].replace_at_indec(1, gameDeck.DealCard());
	}
	/*
	 * Power Up Function
	 * changes a random card to a random suit
	 */
	public void change_1_suit(int user)
	{	
		Random rand = new Random();
		int card1 = rand.nextInt(2);
		int type1 = rand.nextInt(4);
		players[user].change_cards_suit(card1, type1);
	}
	/*
	 * Power Up Function
	 * suffle a players suits
	 */
	public void shuffle_suits(int user)
	{
		Random rand = new Random();
		int card1 = rand.nextInt(4);
		int card2 = rand.nextInt(4);
		players[user].change_cards_suit(0, card1);
		players[user].change_cards_suit(1, card2);
		
	}
	
	
	/*
	 * -Game Method-
	 * Returns the index of the next player, starting from current player
	 * Precondition: current needs to be a index in players
	 */
	public int GetNextPlayer(int current)
	{
		int value = current;
		int count = 0;
		while(count != players.length)
		{
			
			value++;
			//Wrap around if goes over
			if(value >= players.length)
			{
				value = 0;
			}
			if(players[value].getFold() == false)
			{
				return value;
			}
			count++;
		}
		return -1;
	}
	
	/*
	 * -Game Method-
	 * Returns the index of the previous player, starting from current player
	 * Precondition: current needs to be a index in players
	 */
	public int GetLastPlayer(int current)
	{
		int value = current;
		int count = 0;
		while(count != players.length)
		{
			value--;
			//Wrap around if goes over
			if(value < 0)
			{
				value = players.length - 1;
			}
			
			if(players[value].getFold() == false)
			{
				return value;
			}
			count++;
		}
		return -1;
	}
	
	/*
	 * -Game Method-
	 * Prints betting menu used to take user input / determine ai input
	 */
	public int BetMenu(int index, Hand player)
	{
		int temp;
		if(players[index].is_ai() == true)
		{
			if(players[0].getFold() == true) //if human player folds always check
			{
				return 2;
			}
			else
			{	//Check if wallet = 0, if return 2 else cont
				Random rand = new Random();
				temp = rand.nextInt(10);
				if(temp >= 0 && temp < 5) //call 50% of the time
				{
					return 2;
				}
				else if(temp == 6) //fold 10% of the time
				{
					return 3;
				}
				else if(temp == 7) //play power-up 10% of the time
				{
					return 4;
				}
				else if(temp == 8 || temp == 9 || temp == 5) //raise 30% of the time
				{
					return 1;
				}
				return 2; // for safety call if other options fail
			}
		}
		else 
		{
//			Scanner input = new Scanner(System.in);
			while(true)
			{
				players[index].PrintHand();
//				System.out.println(players[index].getName() + " CHOOSE:");
//				System.out.println("1: Raise");
//				System.out.println("2: Call/Check");
//				System.out.println("3: Fold");
//				if(powerups[index].getSuit() == 0 && powerups[index].getValue() == 0)
//				{
//					System.out.println("4: Power-Up <NO POWER-UP AVAILABLE>");
//				}
//				else
//				{
//					System.out.println("4: Power-Up <" + powerups[index].getValue_S() + ">");
//				}
//				
//				System.out.println("5: Quit");	
//				temp = input.nextInt();
//				input.nextLine();
//				if(1 <= temp && temp <= 5)
//				{
//					return temp;
//				}
				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Your Turn: Choose An Option.");
				alert.setHeaderText("Look, a Confirmation Dialog with Custom Actions");//change to variable w diff dialog?
				alert.setContentText("Choose your option.");
				
				ButtonType buttonTypeOne = new ButtonType("Raise");
				ButtonType buttonTypeTwo = new ButtonType("Call/Check");
				ButtonType buttonTypeThree = new ButtonType("Fold");
				ButtonType buttonTypeFour = new ButtonType("Power-ups");
				ButtonType buttonTypeFive = new ButtonType("Quit");
				alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeFour, buttonTypeFive);
				  
				Optional<ButtonType> resultButton = alert.showAndWait();
				if (resultButton.get() == buttonTypeOne)
				{
					temp = 1;
					return temp;
				}
				else if (resultButton.get() == buttonTypeTwo)
				{
					temp = 2;
					return temp;
				}
				else if (resultButton.get() == buttonTypeThree)
				{
					temp = 3;
					return temp;
				}
				else if (resultButton.get() == buttonTypeFour)
				{
					//Add in disable if no powerups available
					temp = 4;
					return temp;
				}
				else if (resultButton.get() == buttonTypeFive)
				{
					temp = 5;
					return temp;
				}
				temp = 5;
				return temp;
				
			}
			
		}	
	}
	
	/*
	 * -Game Method-
	 * Takes bets from betAmounts and puts them into the pot
	 * Then clear betAmounts
	 */
	public void collectPot()
	{
		for(int i = 0; i < betAmounts.length; i++)
		{
			board.addWallet(betAmounts[i]);
			players[i].addWallet(-1*betAmounts[i]);
			betAmounts[i] = 0;
		}
	}
	
	/*
	 * -Game Method-
	 * Clean up Game member variables so they are ready for another round
	 */
	public void clearUp()
	{
		gameDeck.shuffle(); //Reshuffle deck
		gameDeck.Reset();
		
		board.SetWallet(0); //Make sure pot is clear
		board.ClearHand();
		for(int i = 0; i < betAmounts.length; i++)
		{
			betAmounts[i] = 0; //clear bet amount
			players[i].setFold(false); //reset fold flag
			players[i].ClearHand();
		}
		
	}
	
	/*
	 * -Game Method-
	 * Returns the maximum amount the poorest player can bet
	 * This is to determine the max bet for everyone else
	 */
	public int GetMaxBet()
	{
		int wage = players[0].GetWallet() - betAmounts[0];
		int size = players.length;
//		if(players[0].GetWallet() > players[1].GetWallet()) {
//			wage = players[1].GetWallet();
//		}
//		else {
//			wage = players[0].GetWallet();
//		}
		for (int x = 1; x < size;x++)
		{
			if (wage > players[x].GetWallet() - betAmounts[x])
			{
				wage = players[x].GetWallet() - betAmounts[x];
				//System.out.println(players[x].GetWallet() - betAmounts[x]);
			}
		}
		return wage;
		
		
	}
	
	/*
	 * -Game Method-
	 * Returns the number of players that have no folded yet
	 */
	public int NotFolded()
	{
		int counter = 0;
		for(int k = 0; k < players.length; k++)
		{
			if(players[k].getFold() == false)
			{
				counter++;
			}
		}
		return counter;
	}
	
	/*
	 * -Game Method-
	 * Checks for the win condition to terminate the game
	 */
	public int CheckWin()
	{
		int winner = -1;
		int counter = 0;
		for(int i = 0; i < players.length; i++)
		{
			if(players[i].GetWallet() <= 0 && powerups[i].getValue() == 7) //Check for revive cards
			{
				players[i].SetWallet(50);
				System.out.println("<" + players[i].getName() + " has been revived!>");
				powerups[i] = new Card(0,0);
			}
			
			if(players[i].GetWallet() > 0)
			{
				winner = i;
				counter++;
			}	
		}
		if(counter > 1)
		{
			return -1;
		}
		else
		{
			System.out.println(players[winner].getName() + " has won!");
			setUpdateDialog(players[winner].getName() + " has won!");
			return winner;
		}
	}
	
	public int playPowerup(int index)
	{	
		//Check if player has Power-up
				if(powerups[index].getSuit() == 0 && powerups[index].getValue() == 0)
				{
					return -1;
				}
				
				switch(powerups[index].getValue())
				{
				case 0: //Redraw
					this.opponent_redraw(index); 
					return 0;
				case 1: //Redraw Table
					this.redrawTable();
					return 1;
				case 2: //Trade Hand
					this.swap_hands();
					return 2;
				case 3: //Communism
					this.communism();
					return 3;
				case 4: // See an opponet's card
					this.show_opponents_cards(players[this.GetNextPlayer(index)]);
					return 4;
				case 5: //Free call 
					return 5;
				case 6: //Opponent Redraw
					this.opponent_redraw(this.GetNextPlayer(index));
					return 6;
				case 7: //Revive
					return 7;
				case 8: //Replace last card
					this.redraw_last(index);
					return 8;
				case 9: //Change suit
					this.change_1_suit(index);
					return 9;
				case 10: //Shuffle opponent's suits
					this.shuffle_suits(this.GetNextPlayer(index));
					return 10;
				}
				
				//Safety return value
				return -1;
	}
	
	public void DealPowerup()
	{
		int index = ThreadLocalRandom.current().nextInt(0, players.length);
		int ChancetoDeal = ThreadLocalRandom.current().nextInt(0, 10);
		if(ChancetoDeal < 4) //tweak to change chance of powers being dealt
		{
			Card temp = powerupDeck.DealCard();
			powerups[index] = temp;
			System.out.println("<" +players[index].getName() + " has been granted the " + temp.getValue_S() + " power-up>");
		}
	}
	
	/*
	 * -Game Method-
	 * Prints out the cards on the Board
	 */
	public void printBoard()
	{
		board.PrintHand();
	}

	
	
//	public static void main(String[] args)
//	{
//		Game poker = new Game();
//		int currentPlayer = 0;
//		int HasBlind = 0;
//		boolean quitFlag = false;
//		while(quitFlag == false)
//		{
//			System.out.println("************NEW_ROUND************");
//			
//			currentPlayer = poker.GetNextPlayer(HasBlind);
//			poker.TakeBet(HasBlind, 25); //set blind
//			
//			poker.DealCards(0);
//			currentPlayer = poker.BetRound(currentPlayer);
//			poker.collectPot();
//			//poker.swap_hands();
//			
//			poker.DealCards(3);
//			poker.printBoard();
//			currentPlayer = poker.BetRound(currentPlayer);
//			poker.collectPot();
//			
//			poker.DealCards(1);
//			poker.printBoard();
//			currentPlayer = poker.BetRound(currentPlayer);
//			poker.collectPot();
//			
//			poker.DealCards(1);
//			poker.printBoard();
//			currentPlayer = poker.BetRound(currentPlayer);
//			poker.collectPot();
//			
//			poker.evaluate();
//			poker.DealPowerup();
//			poker.clearUp();
//			HasBlind = poker.GetNextPlayer(HasBlind);
//			
//			if(poker.CheckWin() != -1) quitFlag = true;
//		}
//	}
	
} //end class Board
