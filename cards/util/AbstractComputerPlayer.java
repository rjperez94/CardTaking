package cards.util;

import cards.core.*;

/**
 * Represents an computer player in the game. This class can be overriden with
 * different implementations that use different kinds of A.I. to determine
 * appropriate moves.
 * 
 * 
 */
public abstract class AbstractComputerPlayer {
	protected Player player;
	
    public AbstractComputerPlayer(Player player) {
    	this.player = player;
    }

	abstract public Card getNextCard(Trick trick);
	
	public void setPlayer(Player player) {
		this.player = player;
	}
}
