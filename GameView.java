package Project;

import java.util.Scanner;

public class GameView {

	Playingfield playingfield;

	public GameView(Playingfield playingfield) {
		this.playingfield = playingfield;
	}

	public int getColumnChoice(int player) {
		System.out.println("Which column would you like to pick, player "
				+ player + "?");
		Scanner a = new Scanner(System.in);
		String pickstring;
		pickstring = a.nextLine();
		boolean accepted;
		
		do  {
			accepted = true;
			if(!validInput(pickstring)) {
				System.out.println("Please tick an integer between 0 and 6.");
				pickstring = a.nextLine();
				accepted = false;
				pickstring = a.nextLine();
			}else if (Integer.parseInt(pickstring) < 0 || Integer.parseInt(pickstring) > 6
				|| playingfield.play(Integer.parseInt(pickstring), player, false) == -1) {
			System.out.println("No valid move");
			accepted = false;
			pickstring = a.nextLine();
		}
		
	} while (!accepted);
		return Integer.parseInt(pickstring);}

	public boolean GameendHandler(int player) {
		if (playingfield.getWon()) {
			System.out.println("The game has been won by player "
					+player+"! Congratulations!");
			return true;
		}
		return false;
	}
	
	public boolean GameendQuestionairre() {
		String answer;
		do {
			System.out.print("Would you like to play again? Y/N");
			Scanner in = new Scanner(System.in);
			answer = in.hasNextLine() ? in.nextLine() : null;
		} while (answer == null || (!answer.equals("y") && !answer.equals("n")));
		return answer.equals("y");
	}
	
	public boolean validInput(String input) {
		if (input.length()!=1 && !Character.isDigit(input.charAt(0))) {
			return false;
		}
		return true;
	}

	public void showBoard() {
		int width = playingfield.getWidth();
		int height = playingfield.getHeight();
		System.out.println("Board layout: ");
		System.out.println("");
		String lineresult = "";
		for (int r = 0; r < width; r++) {
			lineresult += "----";
		}
		System.out.println(lineresult);

		for (int i = height - 1; i >= 0; i--) {
			String resultline = "|";
			for (int p = 0; p < width; p++) {
				resultline += " "
						+ playingfield
								.playerString(playingfield.getValue(p, i))
						+ " |";
			}
			System.out.println(resultline);

			System.out.println(lineresult);
		}
		String guidance = "  ";
			for (int i =0; i<width; i++) {
				guidance += i+"   ";
			}
		System.out.println(guidance);
		System.out.println("");
	}

}
