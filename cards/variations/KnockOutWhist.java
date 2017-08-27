package cards.variations;

import java.util.List;
import cards.core.Card;
import cards.core.Player;
import cards.util.AbstractCardGame;

public class KnockOutWhist extends AbstractCardGame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 145884971331357007L;
	private int hand = 13;

	public String getName() {
		return "Knock-Out Whist";
	}
	
	public boolean isGameFinished() {
		return hand == 0;		
	}
		
	public void deal(List<Card> deck) {		
		currentTrick = null;
		for (Player.Direction d : Player.Direction.values()) {
			players.get(d).getHand().clear();
		}
		Player.Direction d = Player.Direction.NORTH;
		for (int i = 0; i < hand * 4; ++i) {
			Card card = deck.get(i);
			players.get(d).getHand().add(card);
			d = d.next();
		}			
	}	
	
	public void endHand() {
		super.endHand();
		hand = hand - 1;
	}
}
