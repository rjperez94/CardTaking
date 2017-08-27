package cards.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import cards.core.Card;
import cards.core.Card.Suit;
import cards.core.Player;
import cards.core.Trick;

/**
 * Implements a simple computer player who plays the highest card available when
 * the trick can still be won, otherwise discards the lowest card available. In
 * the special case that the player must win the trick (i.e. this is the last
 * card in the trick), then the player conservatively plays the least card
 * needed to win.
 * 
 * 
 */
public class SimpleComputerPlayer extends AbstractComputerPlayer {

	public SimpleComputerPlayer(Player player) {
		super(player);
	}

	/**
	 * Returns next highest and conservative eligible card on computer's hand
	 * @param trick - this trick (round) being played
	 * @return
	 */
	public Card getNextCard(Trick trick) {
		//cards in trick
		List <Card> cards = trick.getCardsPlayed();
		Card.Suit leadSuit = null;
		Set <Card> cardsSameLead = null;

		//has cards played in trick
		if (!cards.isEmpty()) {
			leadSuit = cards.get(0).suit();		//get lead suit
			cardsSameLead = player.getHand().matches(leadSuit);	 //get same-suit cards in player's hands
		}
		Card.Suit currentTrumps = trick.getTrumps(); 	//get trumps suit
		Set <Card> cardsSameTrumps = player.getHand().matches(currentTrumps); //get same-suit cards in player's hands

		//has same suit of card in hand, must play highest in that suit
		if (cardsSameLead != null) {
			if (!cardsSameLead.isEmpty()) {
				return playSameAsLead(cardsSameLead, trick, currentTrumps, cards, leadSuit);
			}
		}

		//if it gets here then no same suit card as leader in hand
		//has a 'trump' card, play the highest
		if (!cardsSameTrumps.isEmpty()) {
			return playSameAsTrump(cardsSameTrumps, trick, currentTrumps, cards);
		}

		//either has:
		//no leader card or trumps card
		return playNoLeadNoTrump(trick, currentTrumps, cards);
	}

	/**
	 *  Returns card to play when there's no 'lead' suit in hand AND no trump suit in hand i.e. PRIORITY 3 (lowest pr)
	 * @param trick -- this trick that we're making decisions on
	 * @param currentTrumps -- trumps suit in trick . could be null
	 * @param cards -- cards played so far i.e. trick.getCardsPlayed()
	 * @return 
	 */
	private Card playNoLeadNoTrump(Trick trick, Suit currentTrumps, List<Card> cards) {
		//transfer to list so we can order cards
		List<Card> cardList = new ArrayList<Card>(player.getHand().cardsInHand());
		Collections.sort(cardList, new RankComparator());
		//check if we can win this move
		boolean canWin = canWin(trick, cardList, currentTrumps);

		if (canWin ) {	//if we can
			if (cards.size() != 3) {	//if it's not the last play, pick highest card in hand
				return cardList.get(cardList.size()-1);
			}
			//last play in trick so play conservatively
			Card highestInPlayed = highestSoFar(cards);
			return conservativePick(trick, highestInPlayed, cardList);
		} else {
			//System.out.println("NONE. CAN NOT WIN "+cardList.get(0).toString());
			return cardList.get(0);
		}
	}

	/**
	 * Returns card to play when a trump suit in hand i.e. PRIORITY 2
	 * @param cardsSameTrumps -- Set of cards which matches currentTrumps i.e. player.getHand().matches(currentTrumps)
	 * @param trick  -- this trick that we're making decisions on
	 * @param currentTrumps -- trumps suit in trick . could be null
	 * @param cards -- cards played so far i.e. trick.getCardsPlayed()
	 * @return
	 */
	private Card playSameAsTrump(Set <Card> cardsSameTrumps, Trick trick, Suit currentTrumps, List<Card> cards) {
		//transfer to list so we can order cards
		List<Card> cardList = new ArrayList<Card>(cardsSameTrumps);
		Collections.sort(cardList, new RankComparator());
		boolean canWin = canWin(trick, cardList, currentTrumps);

		if (canWin) {
			if (cards.size() != 3) {
				//System.out.println("HAS TRUMPS. RETURN HIGHEST "+highest (cardsSameTrumps).toString());
				return highestSoFar (cardList);
			}
			//last play in trick so play conservatively
			Card highestInPlayed = highestSoFar(trick.matchCardsPlayed(currentTrumps));
			return conservativePick(trick, highestInPlayed, cardList);
		} else {
			//System.out.println("HAS TRUMPS. RETURN LOWEST "+ cardList.get(0).toString());
			return cardList.get(0);
		}
	}

	/**
	 * Returns card to play when a 'lead' suit in hand i.e. PRIORITY 1 (highest pr)
	 * @param cardsSameLead -- Set of cards which matches 'lead' suit i.e. player.getHand().matches(leadSuit)
	 * @param trick  -- this trick that we're making decisions on
	 * @param currentTrumps -- trumps suit in trick . could be null
	 * @param cards -- cards played so far i.e. trick.getCardsPlayed()
	 * @return
	 */
	private Card playSameAsLead(Set <Card> cardsSameLead, Trick trick, Suit currentTrumps, List<Card> cards, Suit leadSuit) {
		//transfer to list so we can order cards
		List<Card> cardList = new ArrayList<Card>(cardsSameLead);
		Collections.sort(cardList);
		boolean canWin = canWin(trick, cardList, currentTrumps);

		if (canWin) {
			if (cards.size() != 3) {
				//System.out.println("HAS SAME AS LEAD. RETURN HIGHEST "+highest (cardsSameLead).toString());
				return highestSoFar (cardList);
			}
			//last play in trick so play conservatively
			Card highestInPlayed = highestSoFar(trick.matchCardsPlayed(leadSuit));
			return conservativePick(trick, highestInPlayed, cardList);
		} else {
			//System.out.println("HAS SAME AS LEAD. RETURN LOWEST "+ cardList.get(0).toString());
			return cardList.get(0);
		}
	}
	
	/**
	 * Returns a card to play in any of the cases, where this player needs to play conservative
	 * i.e. plays the least card needed to win
	 * @param trick -- this trick that we're making decisions on
	 * @param highestInPlayed -- the card that has highest value/suit in trick i.e.
	 * 	highestSoFar(trick.matchCardsPlayed(leadSuit || trumpsSuit || ALL))
	 * @param cardInHand -- List of SOME or ALL cards in player's hand
	 * 	i.e. 3 cases, if:
	 * 	PRIORITY 1 (refer to javadoc) means hand has 'lead' suit, then list contains ONLY cards that matches 'lead' suit
	 * 	PRIORITY 2 (refer to javadoc) means hand has trumps suit but no 'lead' suit, 
	 * 	then list contains ONLY cards that matches 'trumps' suit
	 * 	PRIORITY 3 (refer to javadoc) means hand has NO trumps suit and NO 'lead' suit, 
	 * 	then list contains ALL cards in hand
	 * @return
	 */
	private Card conservativePick(Trick trick, Card highestInPlayed, List<Card> cardInHand) {
		//assume sorted cardInhand i.e. cardInhand has already been sorted in ascending order 
		//go through each card in hand
		for (Card c: cardInHand) {
			if (c.compareTo(highestInPlayed) > 0) {
				return c;
			}
		}
		//dead code
		return null;
	}
	
	/**
	 * Returns true if this player can win a trick based on what cards have been played and what cards it has on its hand
	 * @param trick -- this trick that we're making decisions on
	 * @param cardInHand -- List of SOME or ALL cards in player's hand
	 * 	i.e. 3 cases, if:
	 * 	PRIORITY 1 (refer to javadoc) means hand has 'lead' suit, then list contains ONLY cards that matches 'lead' suit
	 * 	PRIORITY 2 (refer to javadoc) means hand has trumps suit but no 'lead' suit, 
	 * 	then list contains ONLY cards that matches 'trumps' suit
	 * 	PRIORITY 3 (refer to javadoc) means hand has NO trumps suit and NO 'lead' suit, 
	 * 	then list contains ALL cards in hand
	 * @param currentTrumps -- trumps suit in trick . could be null
	 * @return
	 */
	private boolean canWin(Trick trick, List<Card> cardInHand, Card.Suit currentTrumps) {
		if (trick.containsSuit(currentTrumps)) {		//cards played contains trump suit
			//match cards played that are trump suit
			List <Card> cardsPlayed = trick.matchCardsPlayed(currentTrumps);
			//match cards in hand that are trump suit
			List <Card> matchesCardsInHand = new ArrayList<Card>(player.getHand().matches(currentTrumps));

			//there are matches in hand
			if (!matchesCardsInHand.isEmpty()) {
				//get highest in hand and highest in played cards
				Card highestInHand = highestSoFar(matchesCardsInHand);
				Card highestInPlayed = highestSoFar(cardsPlayed);

				if (highestInHand.compareTo(highestInPlayed) > 0) {
					return true;	//can beat highest
				}
				return false;	//cannot beat the highest
			} 
			return false;	//no matches in hand so cannot win
		} else {		//cards played has no trump suit
			Card highestPlayedSoFar = highestSoFar(trick.getCardsPlayed());
			Card highestInHand = cardInHand.get(cardInHand.size()-1);

			if (highestPlayedSoFar != null) {
				//check if able to follow suit
				Suit suit = trick.getCardsPlayed().get(0).suit();
				if (player.hand.matches(suit).isEmpty()) {
					return false;
				}

				if (highestInHand.compareTo(highestPlayedSoFar) > 0) {
					return true;	//can beat highest
				} else if (highestInHand.compareTo(highestPlayedSoFar) < 0) {
					return false;	//cannot beat highest
				}
			} else if (highestPlayedSoFar == null) {
				return true;		//no cards played yet
			}
		}
		//dead code
		return true;
	}

	/**
	 * Returns the card with highest rank, suit in cards
	 * This is used by different methods in this class
	 * @param cards -- the list of cards to inspect
	 * @return
	 */
	private Card highestSoFar(List<Card> cards) {
		Collections.sort(cards, new RankComparator());
		if (!cards.isEmpty()) {
			return cards.get(cards.size()-1);
		}
		return null;
	}
}
