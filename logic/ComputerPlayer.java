package Project.logic;

import java.util.ArrayList;
import java.util.List;


public class ComputerPlayer extends Player {

	String name = "Unbeatable";
	Mark mark;
   
	/**
     * Creates a <code>ComputerPlayer</code> with the given name and mark.
     * @param name is the name given to the ComputerPlayer.
     * @param m is the mark given to the ComputerPlayer
     */
	public ComputerPlayer(String name, Mark m) {
		super(name, m);
		mark=m;
	}
	
	/**
	 * Determines the move of the AI. If the AI can make a winning move, he makes it. 
	 * If the opponent can make a winning move, the AI blocks this.
	 * If the AI can make a winning move next time with the current move, he will make this move
	 * @param board is the board where the AI is playing on.
	 * @return if the AI can win by putting his mark into a column, return this column.
	 * @return else if the opponent can win by putting his mark into a column, return this column.
	 * @return else if the AI can win next time with help of the current move (only for horizontal winning), he will return this column
	 * @return else return random int from 0 up to board.getWidth()
	 */
	@Override
	public int determineMove(Board board) {
	//als zelf 4 op rij kan maken

	if(winMove(board, mark)>=0){
		return winMove(board, mark);
	}
	//Als tegenpartij vier op rij kan maken
	else if(winMove(board, mark.other())>=0){
		return winMove(board, mark.other());
	}
	
	
	//als tegenstander op zijne gooit hij volgende beurt kan winnen
	else if(getSafeSpots(board).contains(oneAheadChecker(board))){
		return oneAheadChecker(board);
	}
	//TODO AI must not "help" opponent with random move
	else if(getSafeSpots(board).size()>0){
		return getRandomElement(getSafeSpots(board));
		}
	else{
		return getRandomElement(getRemainingSpots(board));
	}
	}
	
	/**
	 * Checks if there is a move which where the given mark can win the game.
	 * @param board is the board where the AI is playing on.
	 * @param mark is the mark you want to test for winning.
	 * @return return column if such move exist, else -1. 
	 */
	public int winMove(Board board, Mark mark){
		Board b = new Board(board.getWidth(), board.getHeight());
		b = board.copy();
		int height;
		for(int i = 0; i<b.getWidth(); i++){
			height = b.putMark(i, mark);

			if(b.isWin()){
				b.putMark(i, height, Mark.EMPTY);
				return i;
			}
			if(height!=-1){
				b.putMark(i, height, Mark.EMPTY);
			}
		}
		return -1;
		
	}
	
	/**
	 * Checks if there is a move where if the opponent plays on top of the AI's move, the AI can win
	 * @param board is the board where the AI is playing on.
	 * @return return column if such move exist, else -1. 
	 */
	public int oneAheadChecker(Board board){
		Board b = new Board(board.getWidth(), board.getHeight());
		b = board.copy();
		int[] height = new int[3];
		for(int i = 0; i<b.getWidth(); i++){
			if(b.emptySlotCount(i)>=3){
				height[0] = b.putMark(i, mark);
				height[1] = b.putMark(i, mark.other());
				height[2] = b.putMark(i, mark);
			
				if(b.isWin()){
					return i;
				}else{
					b.putMark(i, height[0], Mark.EMPTY);
					b.putMark(i, height[1], Mark.EMPTY);
					b.putMark(i, height[2], Mark.EMPTY);
				}
			}
		}
		return -1;
	}
	
	/**
	 * Checks if a move is safe to play (meaning the opponent can't win next turn because of this turn)
	 * @param board is the board where the AI is playing on. 
	 * @return return a List of the columns that are safe to play
	 */
	public List<Integer> getSafeSpots(Board board){
		List<Integer> lijst = new ArrayList<Integer>();
		int height;
		Board b = new Board(board.getWidth(), board.getHeight());
		b = board.copy();
		for(int i = 0; i<b.getWidth(); i++){
			height = b.putMark(i, mark);
			if((!(winMove(b, mark.other())>=0))&&height>=0){
				lijst.add(i);
			}
			if(height>=0){
				b.putMark(i, height, Mark.EMPTY);
			}
		}
		//System.err.println(lijst);
		
		return lijst;
	}
	
	/**
	 * Picks a random element in a given List of integers. 
	 * @param lijst from which the method needs to choose a random element.
	 * @return return random integer in the given list.
	 */
	public int getRandomElement(List<Integer> lijst){
		//List<Integer> lijst = getSafeSpots(board);
		return lijst.get((int)(Math.random() * (lijst.size()-1) ));
	}
	
	/**
	 * Gets the remaining playable spots (meaning there is a empty spot in a column) of a board
	 * @param board is the board where the AI is playing on.
	 * @return returns a list of the remaining spots op the board.
	 */
	public List<Integer> getRemainingSpots(Board board){
		List<Integer> list = new ArrayList<Integer>();
		Board b = board.copy();
		for(int i=0; i<b.getWidth(); i++){
			if(b.columnFree(i)){
				list.add(i);
			}
		}return list;
	}

	
	
	
	
	
	
}
