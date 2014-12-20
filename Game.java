package Project;

public class Game {

	static Player p1;
	static Player p2;
	
	public static Player other(Player player) {
		if (player == p1) {
			return p2;
		}
		return p1;
	}

	public static void main(String[] args) {
		String name1;
		String name2;
		if (args.length!=2) {
			name1 = "Default player 1";
			name2 = "Default player 2";
		}
		else{
			name1=args[0];
			name2=args[1];
		}
		p1 = new HumanPlayer(name1, Mark.X);
		p2 = new HumanPlayer(name2, Mark.O);
		
		boolean doorgaan = true;
		while(doorgaan) {
			Player currentplayer = p1;
		
		Playingfield spel = new Playingfield(7, 6);
		GameView view = new GameView(spel);
		while (!view.GameendHandler(currentplayer)) {
			if (spel.isFull()) {
				System.out.println("Holy crap! A draw!");
				break;
			}
			currentplayer = other(currentplayer);
			view.showBoard();
			currentplayer.makeMove(spel);
			
		}
		doorgaan = view.GameendQuestionairre();

	}
		System.out.println("Have a nice day!");
		}
}
