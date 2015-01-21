package Project.logic;

import java.util.Observable;

public class Board extends Observable {

	private Mark[][] board;

	private int width;
	private int height;

	private boolean win;

	/**
	 * Creates a standard Board (width = 7 and height = 6).
	 */
	public Board() {
		this(7, 6);
	}

	/**
	 * Creates a <code>Board</code> with the given width and height.
	 * 
	 * @param width
	 *            is the width of the board.
	 * @param heigth
	 *            is the heigth of the board.
	 */
	/*@
	 * requires width>0 && height>0;
	 * ensures win == false;
	 * loop_invariant i>=0 && i<width;
	 * loop_invariant p>=0 && p<height;
	 * ensures (\forall x; x>=0 && x<width; \forall y; y>=0 && y<height; board[x][p].equals(Mark.EMPTY)); 
	 */
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		win = false;
		board = new Mark[width][height];
		for (int i = 0; i < width; i++) {
			for (int p = 0; p < height; p++) {
				board[i][p] = Mark.EMPTY;
			}
		}
	}

	/**
	 * Puts a mark into the board. Taking the "gravity" into account.
	 * 
	 * @param column
	 *            is the column in which the mark is put
	 * @param m
	 *            is the mark that needs to be put into the board
	 * @return if the mark is placed into the board, returns the row, else
	 *         return -1.
	 */
	/*@
	 * requires column >=0 && column<width && ! m==null;
	 * loop_invariant i>=0 && i<height;
	 * ensures columnFree(column) == false ==> \result == -1;
	 * ensures columnFree(column) == true ==> \result == (\min j; j>=0 && j<getHeight() && getPlace(column, j).equals(Mark.Empty));
	 * ensures columnFree(column == true ==> win == checkWin(column, /result); 
	 */
	public int putMark(int column, Mark m) {
		if(isValidInput(column)){
		for (int i = 0; i < this.height; i++) {
			if (board[column][i].equals(Mark.EMPTY)) {
				board[column][i] = m;
				win = checkWin(column, i);
				setChanged();
				notifyObservers();
				return i;
			}
		}
		}
		return -1;
	}

	/**
	 * Puts a mark into the board at a given width and height.
	 * 
	 * @param width
	 *            is the column the mark needs to be placed into.
	 * @param height
	 *            is the row the mark needs to be placed into.
	 * @param m
	 *            is the mark that needs to be placed.
	 */
	/*@
	 * requires width>=0 && width<getWidth();
	 * requires height>=0 && height<getHeight();
	 * requires ! m==null;
	 * ensures board[width][height] = m;
	 */
	public void putMark(int width, int height, Mark m) {
		board[width][height] = m;
		win = checkWin(width, height);
		setChanged();
		notifyObservers();
	}
	


	/**
	 * @param a is the horizontal position of the stone we're checking if it's part of a connect-4.
	 * @param b	is the vertical position of the stone we're checking if it's part of a connect-4.
	 * @param toright is how many steps are taken right when checking the next stone. Typically -1, 0 or 1.
	 * @param toabove is how many steps are taken up when checking the next stone. Typically -1, 0 or 1.
	 * @return
	 */
	public int checkDirection(int a, int b, int toright, int toabove) {
		Mark checking = board[a][b];
		int testa = a;
		int testb = b;
		int stones = 0;
		while (testa <= getWidth() - 1 && testa >= 0
				&& testb <= getHeight() - 1 && testb >= 0
				&& board[testa][testb] == checking) {
			stones++;
			testa += toright;
			testb += toabove;
		}
		return stones;

	}

	// Checks for all directions if this stone is part of a four-in-a-row.
	public boolean checkWin(int a, int b) {
		if (checkDirection(a, b, 1, 0) + checkDirection(a, b, -1, 0) - 1 >= 4) {
			return true;
		} else if (checkDirection(a, b, 0, 1) + checkDirection(a, b, 0, -1) - 1 >= 4) {
			return true;
		} else if (checkDirection(a, b, 1, 1) + checkDirection(a, b, -1, -1)
				- 1 >= 4) {
			return true;
		} else if (checkDirection(a, b, -1, 1) + checkDirection(a, b, 1, -1)
				- 1 >= 4) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if the board is full.
	 * 
	 * @return returns true if the board is full, else returns false.
	 */
	/*@
	 * loop_invariant i>=0 && i<getWidth();
	 * ensures \result == (\exist j; j>=0 && j<getWidth() && board[j][getHeight-1].equals(Mark.EMPTY);
	 */
	public boolean isFull() {
		for (int i = 0; i < getWidth(); i++) {
			if(columnFree(i)){
				return false;
			}
		}return true;
	}

	/**
	 * Checks if there is a win.
	 * 
	 * @return true if there is a winner, else false.
	 */
	/*@
	 * pure
	 * ensures \result == win;
	 */
	public boolean isWin() {
		return win;
	}

	/**
	 * Checks if the board is full or there is a winner.
	 * 
	 * @return returns (isWin() or isFull()).
	 */
	/*@
	 * pure
	 * ensures \result == isWin() || isFull();
	 */
	public boolean gameOver() {
		return isWin() || isFull();
	}

	/**
	 * Checks if a column is free.
	 * 
	 * @param input
	 *            is the column to be checked
	 * @return true if column is free, else false.
	 */
	/*@
	 * requires input>=0 && input<getWidth();
	 * ensures \result == board[input][getHeight()-1].equals(Mark.EMPTY);
	 */
	public boolean columnFree(int input) {
		return (board[input][height - 1].equals(Mark.EMPTY));
	}

	/**
	 * Makes a copy of the current board.
	 * 
	 * @return returns a copy of the current board.
	 */
	/*@
	 * loop_invariant i>=0 && i<getWidht();
	 * loop_invartiant p>=0 && p<getHeight();
	 * ensures \forall((\forall x; x>=0 && x<getWidth(); \forall y; y>=0 && y<getHeight(); \result.getPlace(x,y).equals(getPlace(x,y)) 
	 */
	public Board copy() {
		Board board = new Board(width, height);
		for (int i = 0; i < width; i++) {
			for (int p = 0; p < height; p++) {
				board.putMark(i, p, getPlace(i, p));
			}
		}
		return board;
	}

	/**
	 * Checks if a given column is a valid column (in means of that the column
	 * exists in the board)
	 * 
	 * @param column
	 *            need to be checked
	 * @return true if column is valid, else false.
	 */
	/*@
	 * ensures /result == column>=0 && column<getWidth();
	 */
	public boolean isValidInput(int column) {
		return (column >= 0 && column < width);
	}

	/**
	 * Counts the empty slots in a given column.
	 * 
	 * @param column
	 *            that needs to be checked
	 * @return return the amount of empty marks in the board.
	 */
	/*@
	 * requires isValidInput(column) == true;
	 * loop_invariant i>=0 && i<getHeight();
	 * ensures \result == \sum(\forall y; y>=0 && y<getHeight() && getPlace(column,y).equals(Mark.EMPTY));
	 */
	public int emptySlotCount(int column) {
		int count = 0;
		for (int i = 0; i < this.height; i++) {
			if (board[column][i].equals(Mark.EMPTY)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Gets the width of the board.
	 * 
	 * @return width of the board.
	 */
	/*@
	 * pure
	 * ensures \result == width;
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the height of the board.
	 * 
	 * @return height of the board.
	 */
	/*@
	 * pure
	 * ensures \result == height;
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the Mark 2d array representation of the board
	 * 
	 * @return returns the mark represenation of the board
	 */
	/*@
	 * ensures \forall(\forall int x; x>=0 && x<getWidht(); \forall int y; y>=0 && y<=getHeight(); \result[x][y].equals(getPlace(x,y));
	 * ensures \result == board;
	 */
	public Mark[][] getBoard() {
		return board;
	}

	/**
	 * Gets the mark that is allocated in the given column and row.
	 * 
	 * @param column
	 *            of the place that needs to be checked
	 * @param row
	 *            of the place that needs to be checked
	 * @return the mark allocated in the given column and row
	 */
	/*@
	 * pure
	 * requires column>=0 && column<getWidth();
	 * requires row>=0; && row<getHeight();
	 * ensures \result == board[column][row];
	 */
	public Mark getPlace(int column, int row) {
		return board[column][row];
	}

	/**
	 * Resets the board.
	 */
	/*@
	 * ensures win = false;
	 * loop_invariant i>=0 && i<getWidth();
	 * loop_invariant p>=0 && p<getheight();
	 * ensures \forall(\forall int x; x>=0 && x<getWidht(); \forall int y; y>=0 && y<=getHeight(); board[x][y].equals(Mark.EMPTY));
	 */
	public void reset() {
		win = false;
		for (int i = 0; i < width; i++) {
			for (int p = 0; p < height; p++) {
				board[i][p] = Mark.EMPTY;
			}
		}
		setChanged();
		notifyObservers();
	}

	public void reset(int width, int height) {
		this.width = width;
		this.height = height;
		win = false;
		board = new Mark[width][height];
		for (int i = 0; i < width; i++) {
			for (int p = 0; p < height; p++) {
				board[i][p] = Mark.EMPTY;
			}
		}
	}

	public String networkBoard() {
		String result = "BOARD";
		result+=" "+getWidth()+" "+getHeight();
		for (int p = 0; p < getHeight(); p++) {
			for (int i = 0; i < getWidth(); i++) {
				if (getPlace(i, p) == Mark.X) {
					result += " 1";
				} else if (getPlace(i, p) == Mark.O) {
					result += " 2";
				} else if (getPlace(i, p) == Mark.EMPTY) {
					result += " 0";
				} else {
					System.err.println("No such mark found in networkBoard()");
				}
			}
		}
		return result;
	}
		
	
	
	/*public String networkBoard() {
		String result = "BOARD";
		result+=" "+getWidth()+" "+getHeight();
		for (int p = 0; p < getHeight(); p++) {
			for (int i = 0; i < getWidth(); i++) {
				if (getPlace(i, p) == Mark.X) {
					result += " 01";
				} else if (getPlace(i, p) == Mark.O) {
					result += " 02";
				} else if (getPlace(i, p) == Mark.EMPTY) {
					result += " 00";
				} else {
					System.err.println("No such mark found in networkBoard()");
				}
			}
			result += " /n";
		}
		result += " /n";
		return result;
	}
	*/
	
	
	/**
	 * Check the play the AI would play
	 * 
	 * @param m
	 *            is the mark of which the AI needs to determine the move
	 * @return the move the AI would have made.
	 */
	/*@
	 * requires ! m==null;
	 * 
	 */
	public int hint(Mark m) {
		return new ComputerPlayer(m).determineMove(this);
	}

}
