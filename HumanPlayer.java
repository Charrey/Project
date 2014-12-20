package Project;

import java.util.Scanner;

public class HumanPlayer extends Player {
	
	public HumanPlayer(String name, Mark mark) {
		super(name, mark);
	}

	@Override
	public int determineMove(Playingfield playingfield) {
		System.out.println("Which column would you like to pick, "+ this.getName() + "?");
		Scanner a = new Scanner(System.in);
		String pickstring;
		pickstring = a.nextLine();
		boolean accepted;
		
		do  {
			accepted = true;
			if(!Playingfield.validInput(pickstring)) {
				System.out.println("Please tick an integer.");
				pickstring = a.nextLine();
				accepted = false;
				pickstring = a.nextLine();
			}else if (Integer.parseInt(pickstring) < 0 || Integer.parseInt(pickstring) > 6
				|| playingfield.play(Integer.parseInt(pickstring), this, false) == -1) {
			System.out.println("That is not a possible play!");
			accepted = false;
			pickstring = a.nextLine();
		}
		
	} while (!accepted);
		return Integer.parseInt(pickstring);
	}
	

	
	

}
