package Project.logic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Project.logic.Board;

public class TestClass {


	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		
		Board b = new Board(7,6);
		String send = b.networkBoard();
		System.out.println(send);
		
		
		//System.out.println(b.height);
		//System.out.println(b.width);
	
		//HumanPlayer p = new HumanPlayer("René", Mark.O);
		//p.determineMove(b);
	}

}
