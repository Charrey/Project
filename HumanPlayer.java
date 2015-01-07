package Project;

import java.util.Scanner;

public class HumanPlayer extends Player {
	
	public HumanPlayer(String name, Mark mark) {
		super(name, mark);
	}

	@Override
	public int determineMove(Board playingfield) {
		System.out.println("Which column would you like to pick, "+ this.getName() + "?");
		Scanner a = new Scanner(System.in);
		int pick;
		boolean processing = true;
		
		while(processing){
			pick = Integer.parseInt(a.nextLine());
			if(pick >= 0 && pick < playingfield.getWidth() && playingfield.columnFree(pick)){
				//playingfield.putMark(pick, this.getMark());
				processing = false;
				return pick;
			}
			System.out.println("Not a valid input, try antoher set");
		}return -1;
		
		

	}
	

	
	

}
