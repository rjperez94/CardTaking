package cards.core;

import java.io.Serializable;


public class Card implements Comparable<Card>, Serializable {
	//make this class Serializable to allow for the deep cloning
	/**
	 * 
	 */
	private static final long serialVersionUID = -7205070669409211769L;

	/**
	 * Represents a card suit.
	 * 
	 *
	 */
	public enum Suit {
		HEARTS,
		CLUBS,
		DIAMONDS,
		SPADES;
	}
	
	/**
	 * Represents the different card "numbers".
	 * 
	 *
	 */
	public enum Rank {
		TWO,
		THREE,
		FOUR,
		FIVE,
		SIX,
		SEVEN,
		EIGHT,
		NINE,
		TEN,
		JACK,
		QUEEN,
		KING,
		ACE;
	}
	
	// =======================================================
	// Card stuff
	// =======================================================
	
	private Suit suit; // HEARTS, CLUBS, DIAMONDS, SPADES
	private Rank rank; // 2 <= number <= 14 (ACE)
	
	/**
	 * Construct a card in the given suit, with a given number
	 * 
	 * @param suit
	 *            --- between 0 (HEARTS) and 3 (SPADES)
	 * @param number
	 *            --- between 2 and 14 (ACE)
	 */
	public Card(Suit suit, Rank number) {				
		this.suit = suit;
		this.rank = number;
	}

	/**
	 * Get the suit of this card, between 0 (HEARTS) and 3 (SPADES).
	 * 
	 * @return
	 */
	public Suit suit() {
		return suit;
	}

	/**
	 * Get the number of this card, between 2 and 14 (ACE).
	 * 
	 * @return
	 */
	public Rank rank() {
		return rank;
	}	
		
	private static String[] suits = { "Hearts","Clubs","Diamonds","Spades"};
	private static String[] ranks = { "2 of ", "3 of ", "4 of ",
			"5 of ", "6 of ", "7 of ", "8 of ", "9 of ", "10 of ", "Jack of ",
			"Queen of ", "King of ", "Ace of " };
	
	public String toString() {
		return ranks[rank.ordinal()] + suits[suit.ordinal()];		
	}

	/**
	 * Compares this card and card o
	 * @param o - other card
	 * @return 
	 * 	1 if this card is greater than o card
	 * 	-1 if this card is less than o card
	 * 	0 if this card is equal to o card
	 * 
	 */
	public int compareTo(Card o) {
		//compare suit first
		int suitRank = this.suit().compareTo(o.suit());
		//if they're equal
		if (suitRank == 0) {
			//return result of rank comparison
			return this.rank().compareTo(o.rank());
		}
		
		//return result of suit comparison if suit of both cards not equal 
		return suitRank;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rank == null) ? 0 : rank.hashCode());
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Card))
			return false;
		Card other = (Card) obj;
		if (rank != other.rank)
			return false;
		if (suit != other.suit)
			return false;
		return true;
	}
	
	/**
	 * For debugging
	 * @return a deep copy of this Card
	 */
	public Card copy () {
		return new Card (suit, rank);
	}
}
