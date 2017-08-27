package cards.variations;

import java.util.List;

import cards.core.Card;
import cards.core.Player;
import cards.util.AbstractCardGame;

/**
 * A simple variation of Whist where only a single hand is played.
 * 
 * 
 */
public class ClassicWhist extends AbstractCardGame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1065998771935374323L;

	public String getName() {
		return "Single Hand Whist";
	}
	
	public boolean isGameFinished() {
		for (Player.Direction d : Player.Direction.values()) {
			if (scores.get(d) == 5) {
				return true;
			}
		}
		return false;
	}
	
	public void deal(List<Card> deck) {	
		currentTrick = null;
		for (Player.Direction d : Player.Direction.values()) {
			players.get(d).getHand().clear();
		}
		Player.Direction d = Player.Direction.NORTH;
		for (int i = 0; i < deck.size(); ++i) {
			Card card = deck.get(i);
			players.get(d).getHand().add(card);
			d = d.next();
		}		
	}		
}
