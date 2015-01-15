package Project.logic;

import static org.junit.Assert.*;
import Project.networking.*;

import org.junit.Before;
import org.junit.Test;

public class Testing {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Board b = new Board(7, 6);
		b.putMark(1, Mark.X);
		String send = b.networkBoard();
		String subsend = send.substring(6);
		System.out.println(send);
		System.out.println(subsend);

		String[] splitted = subsend.split("\\s+");
		b.reset();
		int teller = 0;
		for (int p = 0; p < b.getHeight(); p++) {
			for (int i = 0; i < b.getWidth(); i++) {

				if (!splitted[teller].equals("/n")) {

					if (Integer.parseInt(splitted[teller]) == 01) {
						b.putMark(i, Mark.X);
					} else if (Integer.parseInt(splitted[teller]) != 0) {
						b.putMark(i, Mark.O);
					}
				}
				teller++;

			}

		}
		System.out.println(b.networkBoard());

	}
}
