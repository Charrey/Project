package Project;

import java.util.Random;

public class OneAhead implements Strategy {

	public boolean winCondition(Playingfield p, Player player, int column) {
		Playingfield testfield = new Playingfield(p.width, p.height);
		testfield.play(column, player, true);
		return(testfield.getWon());
	}
	
	@Override
	public int playMove(Playingfield p, Player player) {
		for (int i=0; i<p.width; i++) {
			if (winCondition(p, player,i)) {
				p.play(i, player, false);
				return i;
			}
		}
		for (int i=0; i<p.width; i++) {
			if (winCondition(p, player.getOtherPlayer(),i)) {
				
				while(true){System.out.println("a");}
				
				//p.play(i, player, false);
				//return i;
			}
		}
		Random r = new Random();
		int play = r.nextInt(p.width);
		p.play(play, player, false);
		return play;
		
	}

	@Override
	public String getName() {
		return "OneAhead";
	}

}
