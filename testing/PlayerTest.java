package Project.testing;


import org.junit.Before;
import org.junit.Test;

import Project.logic.ComputerPlayer;
import Project.logic.HumanPlayer;
import Project.logic.InputHandler;
import Project.logic.Mark;
import Project.logic.Player;

public class PlayerTest {

	Player cPlayer;
	Player hPlayer;
	@Before
	
	public void setUp() throws Exception {
		cPlayer = new ComputerPlayer(Mark.X);
		hPlayer = new HumanPlayer("test", Mark.O, new InputHandler());
	}

	@Test
	public void test() {
		ifExpected("Get computer Mark", Mark.X, cPlayer.getMark());
		ifExpected("Get computer name", "AI", cPlayer.getName());
		
		ifExpected("get player Mark", Mark.O, hPlayer.getMark());
		ifExpected("get player name", "test", hPlayer.getName());
		System.out.println("test done");
		
	}

	public void ifExpected(String description, Object expected, Object result){
		if(!expected.equals(result)){
			System.out.println(description + "Expected: "+ expected.toString()+ "Result: " + result.toString());
		}
	}
	
}
