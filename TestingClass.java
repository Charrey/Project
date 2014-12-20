package Project;

public class TestingClass {

	public static void main(String[] args) {
		Playingfield spel = new Playingfield(7, 6);
		GameView a = new GameView(spel);
		a.showBoard();
		spel.play(1, 1, false);
		spel.play(2, 1, false);
		spel.play(3, 1, false);
		spel.play(4, 1, false);
		a.showBoard();

	}

}
