package Project;
import java.util.Random;
public class RandomStrategy implements Strategy {

	@Override
	public int playMove(Playingfield p, Player player) {
		Random r = new Random();
		int play = r.nextInt(p.width);
		p.play(play, player, false);
		return play;
	}

	@Override
	public String getName() {
		return "Random";
	}

}
