package Project;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Project.Playingfield;

public class Classusedfortesting {
	
	GameView a;
	@Before
	public void setUp() throws Exception {
		Playingfield spel = new Playingfield(7, 6);
		a = new GameView(spel);
		a.showBoard();
		spel.play(1, 1, false);
		spel.play(2, 1, false);
		spel.play(3, 1, false);
		spel.play(4, 1, false);
		a.showBoard();
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
