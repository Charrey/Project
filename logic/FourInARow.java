package Project.logic;

import Project.gui.Gui;
import Project.gui.MainGui;

public class FourInARow {

	public static void main(String[] args) {
		new Thread(new MainGui()).start();
		//new Gui(new Board());
		//new Thread(new Game(new HumanPlayer(args[0], Mark.O), new ComputerPlayer(Mark.X))).start();
		//InputHandler handler = new InputHandler();
		//new Thread(new Game(new HumanPlayer("test1", Mark.X, handler), new HumanPlayer("test2", Mark.O, handler))).start();
	}

}
