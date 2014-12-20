package Project;

public class ComputerPlayer extends Player {

	Strategy strategy;
	Mark mark;
	
	public ComputerPlayer(Mark m) {
		super("RandomPlayer", m);
		strategy = new RandomStrategy();
		mark=m;
	}
	
	public ComputerPlayer(Mark m, Strategy strategy) {
		super(strategy.getName()+"Player", m);
		this.strategy = strategy;
		mark=m;
	}
	
	@Override
	public int determineMove(Playingfield playingfield) {
		return strategy.playMove(playingfield, this);
	}

}
