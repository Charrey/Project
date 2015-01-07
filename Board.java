package Project;

public class Board {
	
	Mark[][] board;
	
	int width;
	int height;
	
	boolean win;
	
	public Board(){
		this(7,6);
	}
	
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
	
	public int putMark(int column, Mark m){
		for(int i = 0; i<this.height; i++){
			if(board[column][i].equals(Mark.EMPTY)){
				board[column][i] = m;
				win = checkWin(column, i);
				return i;
			}
		}return -1;
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
	
	
	public boolean isFull(){
		for (int i=0; i<getWidth();i++) {
			if (board[i][this.height-1].equals(Mark.EMPTY)) {
				return false;
			}
		}return true;
	}
	
	public boolean isWin(){
		return win;
	}
	
	public boolean gameOver(){
		return isWin() || isFull();
	}
	
	public boolean columnFree(int input) {
		return(board[input][height-1].equals(Mark.EMPTY));
	}
	
	public Board copy(){
		Board board = new Board(width, height);
		for (int i = 0; i < width; i++) {
			for (int p = 0; p < height; p++) {
				board.putMark(i, p, getPlace(i,p));
			}
		}
		return board;
	}
	
	public void putMark(int width, int height, Mark m){
		board[width][height] = m;
	}
	
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	public Mark[][] getBoard(){
		return board;
	}
	public Mark getPlace(int column, int row){
		return board[column][row];
	}

	public void reset() {
		win = false;
		for (int i = 0; i < width; i++) {
			for (int p = 0; p < height; p++) {
				board[i][p] = Mark.EMPTY;
			}
		}
	}


}
