package Project.logic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Testing {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Board b = new Board(7,6);
		b.putMark(1, Mark.X);
		String send = b.networkBoard();
		System.out.println(send);
	}

}
