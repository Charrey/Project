package Project.logic;

import java.util.Observable;

public class Board extends Observable {
	
	Mark[][] board;
	
	private int width;
	private int height;
	
	boolean win;
	
	/**
	 * Creates a standard Board (width = 7 and height = 6).
	 */
	public Board(){
		this(7,6);
	}
	
	/**
     * Creates a <code>Board</code> with the given width and height.
     * @param width is the width of the board.
     * @param heigth is the heigth of the board.
     */
	public Board(int width, int height){
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
	 * @param column is the column in which the mark is put
	 * @param m is the mark that needs to be put into the board
	 * @return if the mark is placed into the board, returns the row, else return -1.
	 */
	public int putMark(int column, Mark m){
		for(int i = 0; i<this.height; i++){
			if(board[column][i].equals(Mark.EMPTY)){
				board[column][i] = m;
				win = checkWin(column, i);
				setChanged();
				notifyObservers();
				return i;
			}
		}return -1;
	}
	
	/**
	 * Puts a mark into the board at a given width and height. 
	 * @param width is the column the mark needs to be placed into.
	 * @param height is the row the mark needs to be placed into.
	 * @param m is the mark that needs to be placed.
	 */
	public void putMark(int width, int height, Mark m){
		board[width][height] = m;
	}
	
	
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
	
	//Checks for all directions if this stone is part of a four-in-a-row.
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
	 * @return returns true if the board is full, else returns false.
	 */
	public boolean isFull(){
		for (int i=0; i<getWidth();i++) {
			if (board[i][this.height-1].equals(Mark.EMPTY)) {
				return false;
			}
		}return true;
	}
	
	/**
	 * Checks if there is a win.
	 * @return true if there is a winner, else false.
	 */
	public boolean isWin(){
		return win;
	}
	
	/**
	 * Checks if the board is full or there is a winner.
	 * @return returns (isWin() or isFull()).
	 */
	public boolean gameOver(){
		return isWin() || isFull();
	}
	
	/**
	 * Checks if a column is free.
	 * @param input is the column to be checked
	 * @return true if column is free, else false.
	 */
	public boolean columnFree(int input) {
		return(board[input][height-1].equals(Mark.EMPTY));
	}
	
	/**
	 * Makes a copy of the current board.
	 * @return returns a copy of the current board. 
	 */
	public Board copy(){
		Board board = new Board(width, height);
		for (int i = 0; i < width; i++) {
			for (int p = 0; p < height; p++) {
				board.putMark(i, p, getPlace(i,p));
			}
		}
		return board;
	}
		
	/**
	 * Checks if a given column is a valid column (in means of that the column exists in the board)
	 * @param column need to be checked
	 * @return true if column is valid, else false.
	 */
	public boolean isValidInput(int column){
		return(column>=0&&column<width);
	}
	
	/**
	 * Counts the empty slots in a given column.
	 * @param column that needs to be checked
	 * @return return the amount of empty marks in the board.
	 */
	public int emptySlotCount(int column){
		int count = 0;
		for(int i = 0; i<this.height; i++){
			if(board[column][i].equals(Mark.EMPTY)){
				count ++;
			}
		}return count;
	}
	
	/**
	 * Gets the width of the board.
	 * @return width of the board.
	 */
	public int getWidth(){
		return width;
	}
	/**
	 * Gets the height of the board.
	 * @return height of the board.
	 */
	public int getHeight(){
		return height;
	}
	/**
	 * Gets the Mark 2d array representation of the board
	 * @return returns the mark represenation of the board
	 */
	public Mark[][] getBoard(){
		return board;
	}
	/**
	 * Gets the mark that is allocated in the given column and row.
	 * @param column of the place that needs to be checked
	 * @param row of the place that needs to be checked
	 * @return the mark allocated in the given column and row
	 */
	public Mark getPlace(int column, int row){
		return board[column][row];
	}
	
	/**
	 * Resets the board. 
	 */
	public void reset() {
		win = false;
		for (int i = 0; i < width; i++) {
			for (int p = 0; p < height; p++) {
				board[i][p] = Mark.EMPTY;
			}
		}
	}


}
