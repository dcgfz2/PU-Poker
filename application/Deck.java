package application;
import java.util.Random;
import java.util.Scanner;
public class Deck {
	
	 // An array of cards that represent the deck
	private Card[] card_deck;
	
	// An int that represents the amount of cards that have been dealt out
	// Starts at 0
	private int cards_dealt;
	
	/*
	 * -Constructor method- 
	 * if decktype is 0 then a standard 52 card deck is generated
	 * if decktype is not 0 then a power-up deck is generated
	 */
	public Deck(int decktype)
	{
		cards_dealt = 0;
		int deck_index = 0;
		if(decktype == 0)
		{
			card_deck = new Card[52];
			for(int i = 0; i < 4; i++) //Cycle through suits
			{
				for(int k = 1; k < 14; k++) //Make 13 cards of each suit
				{
					card_deck[deck_index] = new Card(i,k);
					deck_index++;
				}
			}
		}
		else //Make power-up deck
		{
			card_deck = new Card[33];
			for(int i = 0; i < 11; i++)
			{
				for(int k = 0; k < 3; k++)
				{
					card_deck[deck_index] =  new Card(4,i);
					deck_index++;
				}
			}
		}
	}
	
	/*
	 * Burn Card
	 * Burns a card (Increments the cards dealt counter)
	 */
	public void burnCard()
	{
		cards_dealt++;
	}
	
	/*
	 * -Axillary Method-
	 * Shuffles a deck by cycling through each Card
	 * generating a random index value in the range of deck length 
	 * and swapping Cards until every Card has been swapped at least once
	 */
	public void shuffle() 
	{
		Random rand = new Random();
		for(int i = 0; i < card_deck.length; i++)
		{
			int x = rand.nextInt(card_deck.length);
			Card temp = card_deck[i];
			card_deck[i] = card_deck[x];
			card_deck[x] = temp;
		}
	}
	
	/*
	 * -Axillary Method-
	 * Return a card from the deck based on how many cards have already been dealt out
	 * Increment the amount of cards dealt
	 */
	public Card DealCard()
	{
		cards_dealt++;
		return card_deck[cards_dealt - 1];
	}
	
	/*
	 * -Axillary Method-
	 * Reset the number of cards that has been dealt
	 * do this at the start of each round
	 */
	public void Reset()
	{
		cards_dealt = 0;
	}
	
	/*
	 * -Debugging Method-
	 * prints The suit and value of every card currently in the deck
	 */
	public void print_deck()
	{
		System.out.print("[ ");
		for(int i = 0; i < card_deck.length; i++)
		{
			System.out.print(card_deck[i].getValue_S() + ":" + card_deck[i].getSuit_S() + ", ");
		}
		System.out.println("]");
	}
	
	
} //end class Deck
