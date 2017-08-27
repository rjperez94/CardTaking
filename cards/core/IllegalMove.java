package cards.core;

/**
 * Use to signal an illegal move has occurred. This typically happens when a
 * human play attempts to play out of turn and/or with a card that doesn't
 * follow suit, etc.
 * 
 * 
 */
public class IllegalMove extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2386369563408035945L;

	public IllegalMove(String e) {
		super(e);
	}
}
