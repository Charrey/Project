package Project.logic;

import java.util.Scanner;

public class HumanPlayer extends Player {
	private InputHandler inputHandler;
	private Scanner a;

	
	public HumanPlayer(String name, Mark mark, InputHandler inputHandler) {
		super(name, mark);
		this.inputHandler = inputHandler;
	}
	
	public HumanPlayer(String name, Mark mark){
		super(name, mark);
	}

	@Override
	public int determineMove(Board playingfield) {
		if(inputHandler==null){
			System.out.println("Which column would you like to pick, "+ this.getName() + "? " + "(type hint for hint)");
			a = new Scanner(System.in);
			String pick;
			boolean processing = true;
			//String tussenvar = a.nextLine();
			while(processing){
				try{
					pick = a.nextLine();
					if(pick.equals("hint")){
						int[] hint = playingfield.hint(getMark());
						System.out.println("Hint: " + hint[0]);
					}else{
						int move = Integer.parseInt(pick);
						if(move >= 0 && move < playingfield.getWidth() && playingfield.columnFree(move)){
							//playingfield.putMark(pick, this.getMark());
							processing = false;
							return move;
						}else{
							System.out.println("Pick a valid number");
						}
					}
				}catch(NumberFormatException e){
					System.out.println("Not a valid input, try antoher set");	
				}
			}return -1;
		}else{
		int move = inputHandler.getMove();
		while(!playingfield.columnFree(move)){
			System.err.println("Invalid move: " + move);
			move = inputHandler.getMove();
		}
		return move;
		}
			

	}
	
	public InputHandler getInputHandler(){
		return inputHandler;
	}
	

	
	

}
