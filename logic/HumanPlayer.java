package Project.logic;

import java.util.Scanner;

public class HumanPlayer extends Player {
	private InputHandler inputHandler;

	
	public HumanPlayer(String name, Mark mark, InputHandler inputHandler) {
		super(name, mark);
		this.inputHandler = inputHandler;
		new Thread(inputHandler).start();
		
	}

	@Override
	public int determineMove(Board playingfield) {
		/*
		System.out.println("Which column would you like to pick, "+ this.getName() + "?");
		Scanner a = new Scanner(System.in);
		int pick;
		boolean processing = true;
		//String tussenvar = a.nextLine();
		while(processing){
			
			try{
				pick = Integer.parseInt(a.nextLine());
				if(pick >= 0 && pick < playingfield.getWidth() && playingfield.columnFree(pick)){
					//playingfield.putMark(pick, this.getMark());
					processing = false;
					return pick;
				}else{
					System.out.println("Pick a valid number");
				}
			
			}catch(NumberFormatException e){
				System.out.println("Not a valid input, try antoher set");
				
			}
		}return -1;
		*/
		int move = inputHandler.getMove();
		while(!playingfield.columnFree(move)){
			System.err.println("Invalid move: " + move);
			move = inputHandler.getMove();
		}
		
			
		return move;

		
		

	}
	
	public InputHandler getInputHandler(){
		return inputHandler;
	}
	

	
	

}
