package Project.logic;

import Project.gui.Main;

public class FourInARow {

	public static void main(String[] args) {
		new Main(new Board());
		new Game(new HumanPlayer(args[0], Mark.O), new ComputerPlayer(args[1], Mark.X)).start();
		
	}

}