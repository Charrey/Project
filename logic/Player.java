package Project.logic;



public abstract class Player {
	
	private String name;
	private Mark mark;
	
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
