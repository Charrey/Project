package Project.logic;


import Project.gui.MainGui;
import Project.gui.game.GameMainPanel;



public class Game implements Runnable {

	private Player[] players;
	private static final int NUMBER_PLAYERS = 2;
	private int current;
	private Board board;
	private boolean running;
	private TUI tui;
	private MainGui gui;
	private GameMainPanel gamePanel;
	
	
	public Game(Player s0, Player s1, int widht, int height){
		board = new Board(widht, height);
		players = new Player[NUMBER_PLAYERS];
		players[0] = s0;
	    players[1] = s1;
	    current = 0;
	    this.tui = new TUI(board);
	    board.addObserver(tui);
	    new Thread(this).start();
	}
	
    public Game(Player s0, Player s1, MainGui gui, int width, int height) {
        board = new Board(width, height);
        players = new Player[NUMBER_PLAYERS];

        players[0] = s0;
        players[1] = s1;
        current = 0;
        this.gui = gui;
        
        if(s0 instanceof HumanPlayer || s1 instanceof HumanPlayer){
        	if(s0 instanceof HumanPlayer){
        		gamePanel =new GameMainPanel(gui, this, ((HumanPlayer) s0).getInputHandler());
        	}else if(s1 instanceof HumanPlayer){
        		gamePanel = new GameMainPanel(gui, this, ((HumanPlayer) s1).getInputHandler());
        	}
        }else{
        	gamePanel = new GameMainPanel(gui, this);
        }
        board.addObserver(gamePanel);
        gui.changePanel(gamePanel);
        new Thread(this).start();
        
    }
		
    private void play() {
    	while (!board.gameOver()){
    		players[current].makeMove(board);
       		current = (current+1)%2; 
    	}
    }

	public void reset() {
		board.reset();
	}
	
	@Override
	public void run() {
		running = true;
		while(running){
			reset();
			play();
			if(tui==null){
				running = gamePanel.getInfoPanel().getReplay();
			}else{
				running = TUI.readPlayAgain();
			}
		}
		
	}
	
	public Board getBoard() {
		return board;
	}
	public Player getFirstPlayer(){
		return players[0];
	}
	public Player getSecondPlayer(){
		return players[1];
	}
	public Player getCurrentPlayer(){
		return players[current];
	}
	public int getCurrent(){
		return current;
	}
	
	public GameMainPanel getGamePanel() {
		return gamePanel;
	}
	
	public void gameEnd(){
		System.out.println("Game has ended");
	}









}
