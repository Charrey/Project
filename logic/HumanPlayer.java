package Project.logic;

import java.util.Scanner;

public class HumanPlayer extends Player {
	private InputHandler inputHandler;
	
	public HumanPlayer(String name, Mark mark) {
		super(name, mark);
		inputHandler = new InputHandler();
		
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
			*/
		
		if(!inputHandler.getClicked()){
			try {
				//while(!inputHandler.getClicked()){
				System.err.println("waiting");	
				wait();
					
					
				//}
			} catch (InterruptedException e) {
				System.err.println("Something went wrong");
			}
		}
		System.err.println(inputHandler.getMove());
		inputHandler.setClicked(false);
		return inputHandler.getMove();	

		
		
		

	}
	
	public InputHandler getInputHandler(){
		return inputHandler;
	}
	

	
	

}
