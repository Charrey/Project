package Project;

public class Game {

	public static int other(int i) {
		if (i == 1) {
			return 2;
		}
		return 1;
	}

	public static void main(String[] args) {
		boolean doorgaan = true;
		while(doorgaan) {
			int currentplayer = 2;
		
		Playingfield spel = new Playingfield(7, 6);
		GameView view = new GameView(spel);
		while (!view.GameendHandler(currentplayer)) {
			if (spel.isFull()) {
				System.out.println("Holy crap! A draw!");
				break;
			}
			currentplayer = other(currentplayer);
			view.showBoard();
			view.getColumnChoice(currentplayer);
			
		}
		doorgaan = view.GameendQuestionairre();

	}
		System.out.println("Have a nice day!");
		}
}
