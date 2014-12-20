package Project;

public class Game {

	static Player[] players;
	
	
	public static int other(int player) {
		if (player == 1) {
			return 0;
		}
		return 1;
	}

	public static void main(String[] args) {
		
		String[] names = new String[2];
		Player[] players = new Player[2];
		for (int i=0; i<2; i++) {		
		if (args.length!=2) {
			names[i] = "Default player "+(i+1);
			System.out.println("bad");
		}
		else{
			System.out.println(i);
			System.out.println(args[i]);
			if (args[i].equals("random")) {
				players[i]=new ComputerPlayer(Mark.X);
				names[i]=players[i].getName();
				System.out.println("new ai");
			}
			else if (args[i].equals("oneahead")) {
				players[i]=new ComputerPlayer(Mark.X, new OneAhead());
				names[i]=players[i].getName();
				System.out.println("new ai");
			}
			
			else{
			names[i]=args[i];
			players[i] = new HumanPlayer(names[i], Mark.X);
			System.out.println("new man");
		}
		}}
		players[0].setOtherPlayer(players[1]);
		players[1].setOtherPlayer(players[0]);
		boolean doorgaan = true;
		while(doorgaan) {
			players[1].mark = Mark.O;
			int currentplayer = 0;
		
		Playingfield spel = new Playingfield(7, 6);
		GameView view = new GameView(spel);
		while (!view.GameendHandler(players[currentplayer])) {
			if (spel.isFull()) {
				System.out.println("Holy crap! A draw!");
				break;
			}
			currentplayer = other(currentplayer);
			view.showBoard();
			players[currentplayer].determineMove(spel);
			
		}
		doorgaan = view.GameendQuestionairre();

	}
		System.out.println("Have a nice day!");
		}
}
