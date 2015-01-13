package Project.logic;

import Project.gui.Gui;

public class FourInARow {

	public static void main(String[] args) {
		//new Gui(new Board());
		new Game(new HumanPlayer(args[0], Mark.O), new ComputerPlayer(Mark.X)).start();
		
	}

}
