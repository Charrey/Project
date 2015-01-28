package Project.logic;

import java.util.LinkedList;
import java.util.List;
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
	 * @param height
	 *            is the heigth of the board.
	 */
	/*@
	  requires width>0 && height>0; 
	  ensures win == false;
	  ensures (\forall int x; x>=0 && x<width; \forall int y; y>=0 && y<height; getPlace(x,y).equals(Mark.EMPTY));
	@*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		win = false;
		board = new Mark[width][height];
		//@ loop_invariant i>=0 && i < getWidth();
		for (int i = 0; i < width; i++) {
			//@ loop_invariant p>=0 && p<height;
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
		requires column >=0 && column<getWidth() && m!=null; 
		ensures columnFree(column) == false ==> \result == -1;
 		ensures columnFree(column) == true ==> \result == (\min int j; j>=0 && j<getHeight() && getPlace(column, j).equals(Mark.EMPTY)); 
 		ensures columnFree(column) == true ==> isWin() == checkWin(column, \result);
	 */
	public int putMark(int column, Mark m) {
		if (isValidInput(column)) {
			//@ loop_invariant i>=0 && i<height;
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
 		requires width>=0 && width<getWidth(); 
 		requires height>=0 && height<getHeight(); 
 		requires m!=null; 
 		ensures getPlace(width,height) == m;
	 */
	public void putMark(int width, int height, Mark m) {
		board[width][height] = m;
		win = checkWin(width, height);
		setChanged();
		notifyObservers();
	}

	/**
	 * @param a
	 *            is the horizontal position of the stone we're checking if it's
	 *            part of a connect-4.
	 * @param b
	 *            is the vertical position of the stone we're checking if it's
	 *            part of a connect-4.
	 * @param toright
	 *            is how many steps are taken right when checking the next
	 *            stone. Typically -1, 0 or 1.
	 * @param toabove
	 *            is how many steps are taken up when checking the next stone.
	 *            Typically -1, 0 or 1.
	 * @return
	 */
	private int checkDirection(int a, int b, int toright, int toabove) {
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
	
	/*@ pure
	 	ensures \result == (\exist int j; j>=0 && j<getWidth(); getPlace(j,(getHeight()-1)).equals(Mark.EMPTY));
	 @*/
	public boolean isFull() {
		//@loop_invariant  i>=0 && i<getWidth();
		for (int i = 0; i < getWidth(); i++) {
			if (columnFree(i)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if there is a win.
	 * 
	 * @return true if there is a winner, else false.
	 */
	//@	pure 
	public boolean isWin() {
		return win;
	}

	/**
	 * Checks if the board is full or there is a winner.
	 * 
	 * @return returns (isWin() or isFull()).
	 */
	//@ pure 
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
		requires input>=0 && input<getWidth(); 
		ensures \result == getPlace(input,getHeight()-1).equals(Mark.EMPTY);
	 @*/
	public boolean columnFree(int input) {
		return (board[input][height - 1].equals(Mark.EMPTY));
	}

	/**
	 * Makes a copy of the current board.
	 * 
	 * @return returns a copy of the current board.
	 */
	/*@
		ensures (\forall(\forall int x; x>=0 && x<getWidth(); \forall int y; y>=0 && y<getHeight(); \result.getPlace(x,y).equals(getPlace(x,y)););
	 @*/
	public Board copy() {
		Board board = new Board(width, height);
		//@ loop_invariant i>=0 && i<getWidth();
		for (int i = 0; i < width; i++) {
			//@ loop_invariant p>=0 && p<getHeight();
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
	//@ pure
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
	 	requires isValidInput(column) == true; 
	 	ensures \result == (\sum(\forall int y; y>=0 && y<getHeight() && getPlace(column,y).equals(Mark.EMPTY);));
	 @*/
	public int emptySlotCount(int column) {
		int count = 0;
		//@ loop_invariant i>=0 && i<getHeight(); 
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
	//@ pure 
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the height of the board.
	 * 
	 * @return height of the board.
	 */
	//@ pure 
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the Mark 2d array representation of the board
	 * 
	 * @return returns the mark represenation of the board
	 */
	/*@ pure
	 	ensures (\forall(\forall int x; x>=0 && x<getWidht(); \forall int y; y>=0 && y<=getHeight(); \result[x][y].equals(getPlace(x,y)););); 
	 	ensures \result.equals(getBoard());
	 @*/
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
	/*@ pure 
 		requires column>=0 && column<getWidth() && row>=0 && row<getHeight(); ; 
 		ensures \result == getPlace(column,row);
 	@*/
	public Mark getPlace(int column, int row) {
		return board[column][row];
	}

	/**
	 * Resets the board.
	 */
	/*@
	 	ensures isWin() == false; 
	 	ensures (\forall(\forall int x; x>=0 && x<getWidth(); \forall int y; y>=0 && y<=getHeight(); getPlace(x,y).equals(Mark.EMPTY)));
	 @*/
	public void reset() {
		reset(width, height);
	}
	

	/*@
	 	ensures isWin() == false; 
	 	ensures width == getWidth();
	 	ensures height == getHeight();
	 	ensures (\forall(\forall int x; x>=0 && x<width; \forall int y; y>=0 && y<=getHeight(); getPlace(x,y).equals(Mark.EMPTY)));
	 @*/
	/**
	 * Resets the board.
	 * 
	 * @param width is the width of the new board.
	 * @param height is the height of the new board.
	 */
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
		setChanged();
		notifyObservers();
	}

	public String networkBoard() {
		String result = "BOARD";
		result += " " + getWidth() + " " + getHeight();
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

	public static void printNetworkBoard(String nwb) {
		String[] apart = nwb.split("\\s+");
		int width = Integer.parseInt(apart[1]);
		List<Integer> boardlist = new LinkedList<Integer>();
		for (int i = apart.length - 1; i > 2; i--) {
			boardlist.add(Integer.parseInt(apart[i]));
			if (boardlist.size() == width) {
				String result = "|";
				for (int p = boardlist.size() - 1; p > 0; p--) {
					result += boardlist.get(p) + "|";
				}
				System.out.println(result);
				boardlist.removeAll(boardlist);
			}
		}

	}


	/**
	 * Check the play the AI would play
	 * 
	 * @param m
	 *            is the mark of which the AI needs to determine the move
	 * @return the move the AI would have made.
	 */
	/*
	 * @ requires m!=null;
	 */
	public int[] hint(Mark m) {
		int column = new ComputerPlayer(m).determineMove(this);
		Board b = copy();
		int height = b.putMark(column, m);
		int[] array = new int[2];
		array[0] = column;
		array[1] = height;
		return array;
	}

}
