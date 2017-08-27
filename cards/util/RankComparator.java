package cards.util;

import java.util.Comparator;

import cards.core.Card;
/**
 * Class for comparing rank, then suit
 */
public class RankComparator implements Comparator<Card> {

	@Override
	public int compare(Card c1, Card c2) {
		//compare rank first
		int rank = c1.rank().compareTo(c2.rank());
		//if they're equal
		if (rank == 0) {
			//return result of suit comparison
			return c1.suit().compareTo(c2.suit());
		}

		//return result of rank comparison if suit of both cards not equal 
		return rank;
	}

}
