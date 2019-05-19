package application;
import java.util.*; 

public class Hand {
	//Name of the player
	private String name;
	
	//An array of cards that keep track of cards currently in the hand
	private Card[] held_cards;
	
	//An int that contains the money/points the hand has
	private int wallet;
	
	//The value used in the event players have the same card eval
	private int tie_value;
	
	//If true is AI player, if fales then Human player
	private boolean ai;
	
	private boolean HasFolded;
	
	/*
	 * -Return Value Method-
	 *  returns a specific card at a index
	 */
	
	public Card get_at_index(int index)
	{
		return held_cards[index];
	}
	/*
	 * -manipulation method
	 * replaces a card at index
	 */
	public void replace_at_indec(int index, Card card)
	{
		held_cards[index] = card;
	}
	/*
	 * mutation function
	 * change specified cards suit
	 */
	public void change_cards_suit(int index, int suit)
	{
		held_cards[index].setSuit(suit);
	}
	
	
	/*
	 * -Sort Method-
	 * Sort cards in hand by Value/
	 * Aces are weighted to be first and 2's at the end
	 */
	private void sortbyValue()
	{ 
		int n = held_cards.length;
		int value1 = 0;
		int value2 = 0;
		Card temp;
		for(int i = 0; i < n-1; i++)
		{
			for(int k = 0; k < n-i-1; k++)
			{
				value1 = held_cards[k].getValue();
				value2 = held_cards[k+1].getValue();
				if(value1 == 1) value1 = 14;
				if(value2 == 1) value2 = 14;
				
				if(value1 < value2)
				{
					temp = held_cards[k];
					held_cards[k] = held_cards[k+1];
					held_cards[k+1] = temp;
				}
			}
		}
	}
	
	/*
	 * -Sort Method-
	 * Sort cards in Hand by Suit
	 */
	private void sortbySuit()
	{
		int n = held_cards.length;
		Card temp;
		for(int i = 0; i < n-1; i++)
		{
			for(int k = 0; k < n-i-1; k++)
			{
				if(held_cards[k].getSuit() > held_cards[k+1].getSuit())
				{
					temp = held_cards[k];
					held_cards[k] = held_cards[k+1];
					held_cards[k+1] = temp;
				}
			}
		}
	}
	
	/*
	 * -Evaluation Method-
	 * Return true if hand has a Royal Flush
	 * return false otherwise
	 */
	private boolean HasRoyalFlush()
	{
		this.sortbyValue();
		this.sortbySuit();
		for(int k = 0; k < 3; k++)
		{
			if(held_cards[k].getValue() == 1)
			{
				if(held_cards[k+1].getValue() == 13)
				{
					if(held_cards[k+2].getValue() == 12)
					{
						if(held_cards[k+3].getValue() == 11)
						{
							if(held_cards[k+4].getValue() == 10)
							{
								if(held_cards[k].getSuit() == held_cards[k+4].getSuit())
								{
									return true;
								}
							}
						}
					}
				}
			}
		}
			
		return false;
	}
	
	/*
	 * -Evaluation Method-
	 * Return true if hand has a Four of a kind
	 * return false otherwise
	 */
	private boolean Has4ofAKind()
	{
		this.sortbyValue();
		for(int k = 0; k < 4; k++)
		{
			if(held_cards[k].getValue() == held_cards[k+1].getValue())
			{
				if(held_cards[k].getValue() == held_cards[k+2].getValue())
				{
					if(held_cards[k].getValue() == held_cards[k+3].getValue())
					{
						tie_value = held_cards[k].getValue();
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/*
	 * -Evaluation Method-
	 * Return true if hand has a Full House
	 * return false otherwise
	 */
	private boolean HasFH()
	{
		if( this.Pair() >= 1 && this.Has3ofAKind())
		{
			return true;
		}
		return false;
	}
	
	/*
	 * -Evaluation Method-
	 * Return true if hand has a Flush
	 * return false otherwise
	 */
	private boolean HasFlush()
	{
		this.sortbyValue();
		this.sortbySuit();
		for(int k = 0; k < 3;k++)
		{
			if(held_cards[k].getSuit() == held_cards[k+4].getSuit())
			{
				tie_value = held_cards[k].getValue();
				return true;
			}
		}
		return false;
	}
	
	/*
	 * -Evaluation Method-
	 * Return true if hand has a Straight
	 * return false otherwise
	 */
	private boolean HasStraight()
	{
		this.sortbyValue();
		for(int k = 0; k < 3; k++)
		{
			if(this.ValueCount(held_cards[k].getValue()-1)  >= 1)
			{
				if(this.ValueCount(held_cards[k].getValue()-2)  >= 1)
				{
					if(this.ValueCount(held_cards[k].getValue()-3)  >= 1)
					{
						if(this.ValueCount(held_cards[k].getValue()-4)  >= 1)
						{
							tie_value = held_cards[k].getValue();
							return true;
						}
					}
				}
			}
		}
		/*
		 * BECAUSE ACE CAN BE BOTH THE HIGHEST AND LOWER VALUE
		 * THE ABOVE SECTION WON'T WORK FOR ACES
		 * BELOW CHECKS FOR ACES AND POSSIBLE STRAIGHTS WITH ACES
		 */
		if(held_cards[0].getValue() == 1)
		{
			//Check for A, K, Q, J, 10
			if(this.ValueCount(13) >= 1 && this.ValueCount(12) >= 1 && this.ValueCount(11) >= 1 && this.ValueCount(10) >= 1) 
				{
					tie_value = 1; //Highest card Ace
					return true;
				}
			//Check for 5, 4, 3, 2, A
			else if(this.ValueCount(2) >= 1 && this.ValueCount(3) >= 1 && this.ValueCount(4) >= 1 && this.ValueCount(5) >= 1)
			{
				tie_value = 5; //Highest Card 5
				return true;
			}
			
		}
		return false;
	}
	/*
	 * -Evaluation Method-
	 * Return true if hand has a Three of a kind
	 * return false otherwise
	 */
	private boolean Has3ofAKind()
	{
		this.sortbyValue();
		for(int k = 0; k < 5; k++)
		{
			if(held_cards[k].getValue() == held_cards[k+1].getValue())
			{
				if(held_cards[k].getValue() == held_cards[k+2].getValue())
				{
						tie_value = held_cards[k].getValue();
						return true;
				}
			}
		}
		return false;
	}
	
	
	/*
	 * -evaluation method
	 * counts number of pairs of 2
	 * returns count of pairs
	 */
	public int Pair()
	{
		Stack<Integer> values = new Stack<Integer>();
		int count = 0;
		this.sortbyValue();
		for (int x = 0; x < 6; x++ )
		{
			//pair of 2
			if (held_cards[x].getValue() == held_cards[x+1].getValue())
			{
				//pair of 3
				if(x == 5) //if checking last pair don't check next card
				{
					if(held_cards[x].getValue() != held_cards[x-1].getValue())
					{
						values.push(held_cards[x].getValue());
						count++;
					}
				}
				else if(held_cards[x].getValue() != held_cards[x+2].getValue())
				{
					if(x == 0) //if Checking first pair don't check previous card
					{
						values.push(held_cards[x].getValue());
						count++;
					}
					else if(held_cards[x].getValue() != held_cards[x-1].getValue())
					{
						values.push(held_cards[x].getValue());
						count++;
					}
				}
			}
		}
		//put the value of the pairs to global.
		while (values.empty() == false)
		{
			tie_value = values.pop();
		}
		//return total num pairs of 2
		return count;
	}
	
	public int giveTieValue()
	{
		return tie_value;
	}
	
	/*
	 * -Evaluation Method-
	 * Return true if hand has a StraightFlush
	 * return false otherwise
	 */
	private boolean HasStraightFlush()
	{
		this.sortbyValue();
		this.sortbySuit();
		for(int k = 0; k < 3; k++)
		{
			if(held_cards[k].getValue()-1 == held_cards[k+1].getValue())
			{
				if(held_cards[k+1].getValue()-1 == held_cards[k+2].getValue())
				{
					if(held_cards[k+2].getValue()-1 == held_cards[k+3].getValue())
					{
						if(held_cards[k+3].getValue()-1 == held_cards[k+4].getValue())
						{
							if(held_cards[k].getSuit() == held_cards[k+4].getSuit())
							{
								tie_value = held_cards[k].getValue();
								return true;
							}
						}
					}
				}
			}
		}
		//Check for Special Ace case; A 2 3 4 5 all of the same suit
		this.sortbyValue();
		for(int i = 0; i < 3; i++)
		{
			if(held_cards[i].getValue() == 1)
			{
				if(this.CardFind(held_cards[i].getSuit(), 2) != -1)
				{
					if(this.CardFind(held_cards[i].getSuit(), 3) != -1)
					{
						if(this.CardFind(held_cards[i].getSuit(), 4) != -1)
						{
							if(this.CardFind(held_cards[i].getSuit(), 5) != -1)
							{
								//In this case 5 is always the highest value
								tie_value = 5;
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/*
	 * Set tie_value equal to the value of the high-card at "index"
	 * For an example, if index = 0; It equals the highest value in the hand
	 * if index = 3; it equals the 4th highest value in the hand
	 * Index must be less than the number of cards in hand
	 */
	public void getHighCard(int index)
	{
		this.sortbyValue();
		tie_value =  held_cards[index].getValue();
	}
	
	/*
	 * -Constructor method- 
	 * Creates a empty hand
	 */
	public Hand()
	{
		held_cards = new Card[0];
		wallet = 0;
		name = "Player";
		HasFolded = false;
	}
	
	
	/*
	 * -Constructor method- 
	 * Creates a empty hand with name equal to "username"
	 */
	public Hand(String username)
	{
		held_cards = new Card[0];
		wallet = 0;
		name = username;
		HasFolded = false;
	}
	
	/*
	 * -Axillary Method-
	 * "delt" is a card to be added to the hand
	 * Create a new array that is one size bigger than held_cards
	 * then add "delt" to the hand
	 */
	public void TakeCard(Card delt)
	{
		Card[] temp;
		temp = new Card[held_cards.length + 1];
		
		for(int i = 0; i < held_cards.length; i++)
		{
			temp[i] = held_cards[i];
		}
		temp[held_cards.length] = delt;
		held_cards = temp;
	}
	
	/*
	 * -Debugging Method-
	 * prints The suit and value of every card currently in the hand
	 */
	public void PrintHand()
	{
		System.out.println(this.name + "'s Hand:");
		System.out.print("[ ");
		for(int i = 0; i < held_cards.length; i++)
		{
			System.out.print(held_cards[i].getValue_S() + ":" + held_cards[i].getSuit_S() + ", ");
		}
		System.out.println("]");
	}
	/*
	 * -Axillary Method-
	 * Removes a card from the hand
	 * First find the card "rm" 
	 * if rm is is not in the Hand then give Error
	 * if rm is in Hand, remove it and shirk held_cards array
	 */
	public void RemoveCard(Card rm)
	{
		int index = -1;
		int iterator = 0;
		Card[] temp;
		for(int i = 0; i < held_cards.length; i++)
		{
			if(held_cards[i] == rm)
			{
				index = i;
			}
		}
		
		if(index == -1)
		{
			System.out.println("ERROR: CARD NOT FOUND");
		}
		else
		{
			temp = new Card[held_cards.length - 1];
			for(int k = 0; k < held_cards.length; k++)
			{
				if(k != index)
				{
					temp[iterator] = held_cards[k];
					iterator++;
				}
			}
			held_cards = temp;
		}
	}
	
	/*
	 * -Axillary Method-
	 * Merge a hand into another hand
	 * NOTE: just copies all the cards in merger into currentHand
	 * DOES NOT DELETE/CLEAR merger hand
	 */
	public void MergeHand(Hand merger)
	{
		Card[] temp;
		Card[] merg = merger.GetHand();
		temp = new Card[held_cards.length + merger.GetCardNum()];
		for(int i = 0; i < held_cards.length; i++)
		{
			temp[i] = held_cards[i];
		}
		for(int k = held_cards.length; k < merger.GetCardNum() + held_cards.length; k++)
		{
			temp[k] = merg[k - held_cards.length];
		}
		held_cards = temp;
	}
	
	/*
	 * -Evaluation Method-
	 * NOTE THS METHOD ASSUMES THAT THE HAND HAS 7 CARDS
	 * (HAS BEEN MERGED WITH RIVER HAND)
	 * Returns a number based on the highest tier grouping a hand contains
	 * 9 = Royal Flush
	 * 8 = Straight Flush
	 * 7 = Four of a kind
	 * 6 = Full House
	 * 5 = Flush 
	 * 4 = Straight
	 * 3 = Three of a kind
	 * 2 = Two Pair
	 * 1 = Pair
	 * 0 = High Card
	 */
	public int Evaluate()
	{
		if(this.HasRoyalFlush()) return 9;
		else if(this.HasStraightFlush()) return 8;
		else if(this.Has4ofAKind()) return 7;
		else if(this.HasFH()) return 6;
		else if(this.HasFlush()) return 5;
		else if (this.HasStraight()) return 4;
		else if(this.Has3ofAKind()) return 3;
		else if(this.Pair() >= 2) return 2;
		else if(this.Pair() == 1) return 1;
		return 0; //If nothing else then return 0 for High Card
	}	
	
	/*
	 * -Accessor method-
	 * returns the number of cards of a certain value in a Hand
	 */
	public int ValueCount(int num)
	{
		int count = 0;
		for(int k = 0; k < held_cards.length; k++)
		{
			if(held_cards[k].getValue() == num)
			{
				count++;
			}
		}
		return count;
	}
	
	/*
	 * -Accessor method-
	 * returns the index of a Card with suit s and value v in this hand
	 * If card isn't in hand, return -1
	 */
	public int CardFind(int s, int v)
	{
		int index = -1;
		for(int k = 0; k < held_cards.length; k++)
		{
			if(held_cards[k].getValue() == v)
			{
				if(held_cards[k].getSuit() == s)
				{
					index = k;
				}
			}
		}
		return index;
	}
	
	/* 
	 * -Axillary Method-
	 * Remove all the cards from the hand 
	 * by setting held_cards to an empty array
	 */
	public void ClearHand()
	{
		held_cards = new Card[0];
	}
	
	
	/*
	 * -Accessor method-
	 * returns the value of wallet
	 */
	public int GetWallet()
	{
		return wallet;
	}
	
	/*
	 * -Accessor method-
	 * Sets the wallet to value
	 */
	public void SetWallet(int value)
	{
		wallet = value;
	}
	
	/* 
	 * -Axillary Method-
	 * add "value" to wallet and store the result in wallet
	 */
	public void addWallet(int value)
	{
		wallet += value;
	}
	
	/*
	 * -Accessor method-
	 * returns the Card array that contains all Cards current in Hand
	 */
	public Card[] GetHand()
	{
		return held_cards;
	}
	
	/*
	 * -Accessor method-
	 * returns the number of cards currently in the hand
	 */
	public int GetCardNum()
	{
		return held_cards.length;
	}
	
	/*
	 * -Axillary Method-
	 * sets the name of the hand equal to "username"
	 */
	public void setName(String username)
	{
		name = username;
	}
	
	public String getName()
	{
		return name;
	}
	
	/*
	 * -Axillary Method-
	 * returns boolean, True if AI hand, False if Player
	 */
	public boolean is_ai()
	{
		return ai;
	}
	
	/*
	 * -Axillary Method-
	 * sets ai to True.
	 */
	public void set_ai()
	{
		ai = true;
	}
	
	/*
	 * -Axillary Method-
	 * replaces last card in hand
	 */
	public void replace_last(Card new_card)
	{
		int last = held_cards.length;
		last -=1;
		held_cards[last] = new_card;
	}
	
	/*
	 * -Axillary Method-
	 * Sets Hasfoldedd to value "set"
	 */
	public void setFold(boolean set)
	{
		HasFolded = set;
	}
	
	/*
	 * -Axillary Method-
	 * Returns the value of HasFolded
	 */
	public boolean getFold()
	{
		return HasFolded;
	}
	
	/*
	 * -Accessor method-
	 * returns a string that indicates what the hand evals to
	 * Assumes "value" is the int obtained by running this.Evaluate()
	 */
	public String evalString(int value)
	{
		switch(value)
		{
		case 9:
			return "Royal Flush";
		case 8:
			return "Straight Flush";
		case 7:
			return "Four of a kind";
		case 6:
			return "Full House";
		case 5:
			return "Flush ";
		case 4:
			return "Straight";
		case 3:
			return "Three of a kind";
		case 2:
			return "Two Pair";
		case 1:
			return "Pair";
		case 0:
			return "High Card";
		default:
			return "INVALID VALUE";
		}
	}
	
}

	
