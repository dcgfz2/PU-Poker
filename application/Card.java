package application;
public class Card {
	
	/*
	 * Card value is indicated by a int ranging from 1-13 where:
	 * 1 = ACE
	 * 2-10 = 2-10 : NO SPECAL NAME
	 * 11 = JACK
	 * 12 = QUEEN
	 * 13 = KING
	 * THESE VALUES DON'T HOLD TRUE IF THE CARD IS A POWER-UP CARD, 
	 * THEN THE VALUE IS TIED TO IDENTIFYING THE POWER UP
	 */
	private final int value;
	
	/*
	 * Card suit is indicated by a int ranging from 0-4 where:
	 *  0 = CLUBS
	 *  1 = DIAMONDS
	 *  2 = HEARTS
	 *  3 = SPADES
	 *  4 = POWERUP CARD
	 */
	private int suit;

	
	/*
	 * -Constructor method- 
	 * Creates a card with a suit equal to num_suit 
	 * and value equal to num_value
	 */
	public Card(int num_suit, int num_value)
	{
			value = num_value;
			suit = num_suit;
	}
	
	/*
	 * -Accessor method-
	 * returns the value of a card
	 */
	public int getValue()
	{
		return value;
	}
	
	/*
	 * -Accessor method-
	 * returns the suit of a card
	 */
	public int getSuit()
	{
		return suit;
	}
	
	/*
	 * -Accessor method-
	 * returns the suit of a card as a string
	 */
	public String getSuit_S()
	{
		switch(suit)
		{
		case 0:
			return "CLUBS";
		case 1:
			return "DIAMONDS";
		case 2:
			return "HEARTS";
		case 3:
			return "SPADES";
		case 4:
			return "POWER-UP";
		default:
			return "INVALID SUIT";
		}
	}
	/*
	 * mutation function
	 * sets cards suit to specifed type.
	 */
	public void setSuit(int type)
	{
		suit = type;
	}
	
	
	/*
	 * -Accessor method-
	 * returns the value of a card as a string
	 */
	public String getValue_S()
	{
		if(suit != 4)
		{
			switch(value)
			{
			case 1:
				return "ACE";
			case 11:
				return "JACK";
			case 12:
				return "QUEEN";
			case 13:
				return "KING";
			default:
				return Integer.toString(value);
			}
		}
		else
		{
			switch(value)
			{
			case 0:
				return "Redraw";
			case 1:
				return "Redraw Board";
			case 2:
				return "Trade Hands";
			case 3:
				return "Communism";
			case 4:
				return "See an opponent’s card";
			case 5:
				return "Free call";
			case 6:
				return "Opponent Redraw";
			case 7:
				return "Revive";
			case 8:
				return "Replace last card";
			case 9:
				return "Change 1 suit";
			case 10:
				return "Shuffle your opponent's suits";
			default:
				return "INVALID VALUE";
			}
			
		}
	}
	
} //end class Card
