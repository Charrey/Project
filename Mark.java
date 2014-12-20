package Project;

/**
 * Represents a mark in the Tic Tac Toe game. There three possible values:
 * Mark.XX, Mark.OO and Mark.EMPTY. Module 2 lab assignment
 * 
 * @author Theo Ruys
 * @version $Revision: 1.4 $
 */
public enum Mark {

	EMPTY, X, O;

	/*
	 * @ ensures this == Mark.XX ==> \result == Mark.OO; ensures this == Mark.OO
	 * ==> \result == Mark.XX; ensures this == Mark.EMPTY ==> \result ==
	 * Mark.EMPTY;
	 */
	/**
	 * Returns the other mark.
	 * 
	 * @return the other mark is this mark is not EMPTY or EMPTY
	 */
	public Mark other() {
		if (this == X) {
			return O;
		} else if (this == O) {
			return X;
		} else {
			return EMPTY;
		}
	}

	public boolean isEmpty() {
		return (this == EMPTY);
	}

	public char giveString() {
		if (this == EMPTY) {
			return ' ';
		} else if (this == X) {
			return 'X';
		} else {
			return 'O';
		}
	}
	
	public int getNo(){
		if (this == EMPTY) {
			return 0;
		} else if (this == X) {
			return 1;
		} else {
			return 2;
		}
	}

}
