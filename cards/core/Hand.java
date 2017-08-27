package cards.core;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a hand of cards held by a player. As the current round proceeds,
 * the number of cards in the hand will decrease. When the round is over, new
 * cards will be delt and added to this hand.
 * 
 * 
 */
public class Hand implements Cloneable, Iterable<Card>, Serializable {
	//make this class Serializable to allow for the deep cloning
	/**
	 * 
	 */
	private static final long serialVersionUID = 635578506478229447L;
	private SortedSet<Card> cards = new TreeSet<Card>();
	

	public Iterator<Card> iterator() {
		return cards.iterator();
	}
	
	/**
	 * Check with a given card is contained in this hand, or not.
	 * 
	 * @param card
	 * @return
	 */
	public boolean contains(Card card) {
		return cards.contains(card);
	}
	
	/**
	 * Return all cards in this hand which match the given suit.
	 * @param suit
	 * @return
	 */
	public Set<Card> matches(Card.Suit suit) {
		HashSet<Card> r = new HashSet<Card>();
		for(Card c : cards) {
			if(c.suit() == suit) {
				r.add(c);
			}
		}
		return r;
	}
	
	
	/**
	 * Add a card to the hand.
	 */
	public void add(Card card) {		
		cards.add(card);
	}
	
	/**
	 * Remove a card from the hand.
	 */
	public void remove(Card card) {		
		cards.remove(card);
	}
	
	/**
	 * Get number of cards in this hand.
	 * 
	 * @return
	 */
	public int size() {
		return cards.size();
	}
	
	/**
	 * Remove all cards from this hand.
	 */
	public void clear() {
		cards.clear();
	}	
	
	/**
	 * For debugging
	 * @return a deep copy of this Card
	 */
	public Hand copy() {
		Hand handCopy = new Hand();
		for (Card c: cards) {
			handCopy.add(c);
		}
		return handCopy;
	}
	
	/**
	 * Helper method in SimpleComputerPlayer
	 * @return SortedSet of cards
	 */
	public SortedSet<Card> cardsInHand () {
		return cards;
	}
}
