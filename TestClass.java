package Project;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestClass {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		
		//Board b = new Board();
		Board b = new Board(7,6);
		TUI t = new TUI(b);
		b.putMark(6, Mark.O);
		b.putMark(6, Mark.X);
		t.showBoard();
		
		
		//System.out.println(b.height);
		//System.out.println(b.width);
	
		//HumanPlayer p = new HumanPlayer("René", Mark.O);
		//p.determineMove(b);
	}

}
