package Project.testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Project.logic.Board;
import Project.logic.ComputerPlayer;
import Project.logic.Mark;

public class ComputerPlayerTest {

	
	Mark o = Mark.O;
	Mark x = Mark.X;
	Board fullBoard = new Board(3,3);
	Board canLose = new Board();
	Board canWin = new Board();
	ComputerPlayer AI = new ComputerPlayer(x);
	
	Board safeChecker = new Board(4,4);
	Board certainDefeat = new Board(4,4);
	
	
	
	
	@Before
	public void setUp() throws Exception {
		
		

		fullBoard.putMark(0, o);
		fullBoard.putMark(1, o);
		fullBoard.putMark(2, o);
		fullBoard.putMark(0, o);
		fullBoard.putMark(1, o);
		fullBoard.putMark(2, o);
		fullBoard.putMark(0, o);
		fullBoard.putMark(1, o);
		
		canWin.putMark(0, x);
		canWin.putMark(0, x);
		canWin.putMark(0, x);
		
		canLose.putMark(2, o);
		canLose.putMark(2, o);
		canLose.putMark(2, o);

	
		
	
	}

	@Test
	public void test() {
		ifExpected("Vol op 2 na", 2, AI.determineMove(fullBoard));
		ifExpected("Can win", 0, AI.determineMove(canWin));
		ifExpected("Can lose", 2, AI.determineMove(canLose));
		ifExpected("Go for safe place", 2, AI.determineMove(canLose));

		
	}
	
	public void ifExpected(String description, Object expected, Object result){
		if(!expected.equals(result)){
			System.out.println(description + "Expected: "+ expected.toString()+ "Result: " + result.toString());
		}
	}

}
