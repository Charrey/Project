package Project;


public class ComputerPlayer extends Player {

	String name = "Unbeatable";
	Mark mark;
	

	
	public ComputerPlayer(String name, Mark m) {
		super(name, m);
		mark=m;
	}
	
	
	@Override
	public int determineMove(Board board) {
	//als zelf 4 op rij kan maken

	if(winMove(board, mark)>=0){
		return winMove(board, mark);
	}
	//Als tegenpartij vier op rij kan maken
	else if(winMove(board, mark.other())>=0){
		return winMove(board, mark.other());
	}
	//als andere speler 4 op rij kan maken
	/*
	else if(winMove(board, mark.other())>=0){
		return winMove(board, mark.other());
	}*/
	
	else{
		return 2;
	}
	//TODO else if hPlayer can win, play move
		
	//TODO else play random
		
		
	
		
		
		
		
	//return 0;
	}
	
	public int winMove(Board board, Mark mark){
		Board b = new Board(board.getWidth(), board.getHeight());
		b = board.copy();
		int height;
		for(int i = 0; i<b.width; i++){
			height = b.putMark(i, mark);
			//System.out.println(b.isWin());
			if(b.isWin()){
				b.putMark(i, height, Mark.EMPTY);
				return i;
			}
			b.putMark(i, height, Mark.EMPTY);

			/*
			if(b.isWin()){
				b.putMark(i, Mark.EMPTY);
				//System.out.println(i);
				return i;
			}*/
			
			
		}
		return -1;
		
	}
	
}
