package Project.logic;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class TUI implements Observer {

	private Board playingfield;
	
	public TUI(Board board){
		playingfield = board;
	}
	

	public String showBoard() {
		String result = "Board layout: \n\n";
		int width = playingfield.getWidth();
		int height = playingfield.getHeight();
		String lineresult = "";
		for (int r = 0; r < width; r++) {
			lineresult += "----";
		}
		result += lineresult + "\n";
		for (int i = height - 1; i >= 0; i--) {
			String resultline = "|";
			for (int p = 0; p < width; p++) {
				resultline += " "
						+ playingfield.getPlace(p, i).toString()
						+ " |";
			}
			result += resultline + "\n";
			result += lineresult + "\n";
		}
		String guidance = "  ";
			for (int i =0; i<width; i++) {
				guidance += i+"   ";
			}
		result += guidance + "\n\n";
		return result;
	}


	@Override
	public void update(Observable arg0, Object arg1) {
		System.out.println(showBoard());
	}
	
	public static boolean readPlayAgain() {
        String answer;

        do {
            System.out.print("Play again? (y/n)");
            Scanner in = new Scanner(System.in);
            answer = in.hasNextLine() ? in.nextLine() : null;
        } while (answer == null || (!answer.equals("y") && !answer.equals("n")));
        return answer.equals("y");
	}
}
