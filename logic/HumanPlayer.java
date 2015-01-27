package Project.logic;

import java.util.Scanner;

import Project.networking.Client;

public class HumanPlayer extends Player {
	private InputHandler inputHandler;
	private Scanner a;
	private Client client;

	public HumanPlayer(String name, Mark mark, InputHandler inputHandler) {
		super(name, mark);
		this.inputHandler = inputHandler;
	}

	public HumanPlayer(String name, Mark mark) {
		super(name, mark);
	}

	public HumanPlayer(String name, Mark mark, Client client) {
		super(name, mark);
		this.client = client;
	}

	@Override
	public int determineMove(Board playingfield) {
		if (inputHandler == null) {
			client.UseScannerForDeterminingMoves(true);
			System.out.println("Which column would you like to pick, "
					+ this.getName() + "? " + "(type hint for hint)");
			if (client != null) {
				a = new Scanner(System.in);
			}
			String pick;
			boolean processing = true;
			// String tussenvar = a.nextLine();
			while (processing) {
				try {
					pick = a.nextLine();
					if (pick.equals("hint")) {
						int[] hint = playingfield.hint(getMark());
						System.out.println("Hint: " + hint[0]);
					} else {
						int move = Integer.parseInt(pick);
						if (move >= 0 && move < playingfield.getWidth()
								&& playingfield.columnFree(move)) {
							// playingfield.putM ark(pick, this.getMark());
							processing = false;
							return move;
						} else {
							System.out.println("Pick a valid number");
						}
					}
				} catch (NumberFormatException e) {
					System.out.println("Not a valid input, try antoher set");
				}
			}
			return -1;
		} else {
			int move = inputHandler.getMove();
			while (!playingfield.columnFree(move)) {
				System.err.println("Invalid move: " + move);
				move = inputHandler.getMove();
			}
			a.close();
			client.resumeScanner();
			return move;
		}

	}

	public int determineMove(Board playingfield, String pick, Scanner scan) {
		String toPick = pick;
		boolean processing = true;
		do {
			try {
				if (toPick.equals("hint")) {
					int[] hint = playingfield.hint(getMark());
					System.out.println("Hint: " + hint[0]);
				} else {
					int move = Integer.parseInt(toPick);
					if (move >= 0 && move < playingfield.getWidth()
							&& playingfield.columnFree(move)) {
						processing = false;
						return move;
					} else {
						System.out.println("Pick a valid number");
						toPick = scan.nextLine();
					}
				}
			} catch (NumberFormatException e) {
				System.out.println("Not a valid input, try antoher set");
				toPick = scan.nextLine();
			}
		} while (processing);
		return -1;
	}

	public InputHandler getInputHandler() {
		return inputHandler;
	}
}
