package Project;

public abstract class Player {
	
	String name;
	Mark mark;
	
	public Player(String name, Mark mark) {
	this.name=name;
	this.mark=mark;
	}
	
	public String getName() {
        return name;
    }
	
	public Mark getMark() {
        return mark;
    }
	
	public abstract int determineMove(Playingfield playingfield);
	
	public void makeMove(Playingfield playingfield) {
        int keuze = determineMove(playingfield);
    }
	
}
