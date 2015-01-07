package Project;



public abstract class Player {
	
	String name;
	Mark mark;
	//Player otherplayer;
	
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


	
	public abstract int determineMove(Board playingfield);
	
    public void makeMove(Board board) {
        int keuze = determineMove(board);
        board.putMark(keuze, getMark());
    }
	
}
