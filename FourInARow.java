package Project;

public class FourInARow {

	public static void main(String[] args) {
		new Game(new HumanPlayer(args[0], Mark.O), new ComputerPlayer(args[1], Mark.X)).start();
	}
//test
}
