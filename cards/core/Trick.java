package cards.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cards.core.Card.Suit;
import cards.core.Player.Direction;

/**
 * Represents a trick being played. This includes the cards that have been
 * played so far, as well as what the suit of trumps is for this trick.
 * 
 * 
 */
public class Trick implements Serializable {
	//make this class Serializable to allow for the deep cloning
	/**
	 * 
	 */
	private static final long serialVersionUID = -1724445508093807500L;
	private Card[] cards = new Card[4];
	private Player.Direction lead;
	private Card.Suit trumps;
	
	/**
	 * Construct a new trick with a given lead player and suit of trumps.
	 * 
	 * @param lead
	 *            --- lead player for this trick.
	 * @param trumps
	 *            --- maybe null if no trumps.
	 */
	public Trick(Player.Direction lead, Card.Suit trumps) {
		this.lead = lead;
		this.trumps = trumps;
	}
	
	/**
	 * Constructor used for deep copy
	 * 
	 * @param lead - lead player for this trick.
	 * @param trumps - current trumps
	 * @param cards - current cards
	 */
	public Trick(Direction lead, Suit trumps, Card[] cards) {
		this.lead = lead;
		this.trumps = trumps;
		this.cards = cards;
	}

	/**
	 * Determine who the lead player for this trick is.
	 * 
	 * @return
	 */
	public Player.Direction getLeadPlayer() {
		return lead;
	}
	
	/**
	 * Determine which suit are trumps for this trick, or null if there are no
	 * trumps.
	 * 
	 * @return
	 */
	public Card.Suit getTrumps() {
		return trumps;
	}
	
	/**
	 * Get the list of cards played so far in the order they were played.
	 * 
	 * @return
	 */
	public List<Card> getCardsPlayed() {
		ArrayList<Card> cs = new ArrayList<Card>();
		for(int i=0;i!=4;++i) {
			if(cards[i] != null) {
				cs.add(cards[i]);
			} else {
				break;
			}
		}
		return cs;
	}
	
	/**
	 * Get the card played by a given player, or null if that player has yet to
	 * play.
	 * 
	 * @param p --- player
	 * @return
	 */
	public Card getCardPlayed(Player.Direction p) {		
		Player.Direction player = lead;
		for(int i=0;i!=4;++i) {
			if(player.equals(p)) {
				return cards[i];
			}
			player = player.next();
		}
		// deadcode
		return null;
	}
	
	/**
	 * Determine the next player to play in this trick.
	 * 
	 * @return
	 */
	public Player.Direction getNextToPlay() {
		Player.Direction dir = lead;
		for(int i=0;i!=4;++i) {
			if(cards[i] == null) {
				return dir;
			}
			dir = dir.next();			
		}
		return null;
	}
	
	/**
	 * Determine the winning player for this trick. This requires looking to see
	 * which player led the highest card that followed suit; or, was a trump.
	 * 
	 * @return
	 */
	public Player.Direction getWinner() {
		Player.Direction player = lead;
		Player.Direction winningPlayer = null;
		Card winningCard = cards[0];
		for (int i = 0; i != 4; ++i) {
			if (cards[i].suit() == winningCard.suit()
					&& cards[i].compareTo(winningCard) >= 0) {
				winningPlayer = player;
				winningCard = cards[i];
			} else if (trumps != null && cards[i].suit() == trumps
					&& winningCard.suit() != trumps) {
				// in this case, the winning card is a trump
				winningPlayer = player;
				winningCard = cards[i];
			}
			player = player.next();
		}
		return winningPlayer;
	}
	
	/**
	 * Player attempts to play a card. This method checks that the given player
	 * is entitled to play, and that the played card follows suit. If either of
	 * these are not true, it throws an IllegalMove exception.
	 */
	public void play(Player p, Card c) throws IllegalMove {
		//check null so we can call method on them
		if (p == null || c == null) {
			throw new IllegalMove("Player and Card must not be null");
		}
		//player must have card
		if (!p.getHand().contains(c)) {
			throw new IllegalMove("Player hand does not contain this card");
		}
		//it must be player's turn
		if (p.getDirection() != getNextToPlay()) {
			throw new IllegalMove("Player is not the next to play");
		}
		
		//check if  player's hand has a card with same suit as leader
		Card leadCard = cards[0];	//leader's card
		if (leadCard != null) {	//if card exists
			//inspect player hand, if any matches
			//if it gets past above condition, match leadCard's suit to player hand's card, there should be one
			//if c not same as lead suit, then illegal move
			if (!p.getHand().matches(leadCard.suit()).isEmpty()) {
				if (!c.suit().equals(leadCard.suit())) {
					throw new IllegalMove("Card doesn't follow suit");
				}
			}
		}
		
		// Finally, play the card.
		for (int i = 0; i != 4; ++i) {
			if (cards[i] == null) {
				cards[i] = c;
				p.getHand().remove(c);
				break;
			}
		}
	}
	
	/**
	 * True if this trick contains a card with a trump suit
	 * @param trump -  - the suit of the card to match
	 * @return
	 */
	public boolean containsSuit(Suit trump) {
		for (int i=0; i < cards.length; i++) {		//inspect all cards
			if (cards[i] == null) {	//skip null
				continue;
			} else {
				if (cards[i].suit().equals(trump)) {	//suit match, return true
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * List of cards in this trick which matches trump suit
	 * @param trump - the suit of the card to match
	 * @return
	 */
	public List<Card> matchCardsPlayed(Suit trump) {
		List<Card> matches = new ArrayList<Card>();
		for (int i=0; i < cards.length; i++) {	//inspect all cards
			if (cards[i] == null) {	//skip null
				continue;
			} else {
				if (cards[i].suit().equals(trump)) {	//suit match
					matches.add(cards[i]);	//add to list
				}
			}
		}
		return matches;
	}
	
	/**
	 * @return a deep copy of this Card
	 */
	public Trick copy () {
		return new Trick(lead, trumps, cards);
	}
	
}
