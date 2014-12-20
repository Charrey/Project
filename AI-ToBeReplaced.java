package Project;

public class AI-ToBeReplaced {

	Playingfield field;

	public AI-ToBeReplaced(Playingfield field) {
		this.field = field;
	}

	public int makePlay(int player, Playingfield field) {
		Playingfield testfield = new Playingfield(field.getWidth(),
				field.getHeight());
		int[] results = new int[field.getWidth()];
		for (int i = 0; i < field.getWidth(); i++) {
			results[i] = getScore(player, testfield, i);
		}
		int bestplay = 0;
		for (int i = 1; i < field.getWidth(); i++) {
			System.out.println(results[i]);
			if (results[i] > results[i - 1]) {
				bestplay = i;
			}
		}
		System.out.println(bestplay);
		return field.play(bestplay, player, false);
	}

	public int getScore(int player, Playingfield fielder, int column) {
		int score = 0;
		if (fielder.layout[column][fielder.getHeight() - 1] == 0) {
		}

		if (fielder.layout[column][fielder.getHeight() - 1] == 0) {
			int played = fielder.play(column, player, true);
			if (fielder.checkWin(column, played % 100)) {
				score++;
			}
		else {
			for (int i = 0; i < fielder.getWidth(); i++) {
				if (fielder.layout[i][fielder.getHeight() - 1] == 0) {
					int enemyplayed = fielder.play(i, 2, true);
					if (fielder.checkWin(i, enemyplayed % 100)) {
						score--;
						System.out.println("Lose..");
					}
				 else {
					int[] scorelijst = new int[fielder.getWidth()];
					fielder.stringPrint();
					for (int p = 0; p < fielder.getWidth(); p++) {
						scorelijst[p] = getScore(player, fielder, p);
					}
					int sum = 0;
					for (int q : scorelijst) {
						sum += scorelijst[q];
					}
					score += sum;
				}}
			}

		}}
		return score;

	}
}
