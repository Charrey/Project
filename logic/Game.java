package Project.logic;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Scanner;
import javax.swing.JLabel;
import Project.gui.MainGui;



public class Game implements Runnable {

	private Player[] players;
	private static final int NUMBER_PLAYERS = 2;
	private int current;
	private Board board;
	private boolean running;
	private TUI tui;
	private MainGui gui;
	
	public Game(Player s0, Player s1, MainGui gui){
		this(s0, s1, gui, 7, 6);
	}
	
    public Game(Player s0, Player s1, MainGui gui, int width, int height) {
        board = new Board(width, height);
        players = new Player[NUMBER_PLAYERS];

        players[0] = s0;
        players[1] = s1;
        current = 0;
        tui = new TUI(board);
        this.gui = gui;
        
        //gui = new Gui(board, (MouseListener)((HumanPlayer)s0).getInputHandler()));

        //gui = new Gui(board, ((HumanPlayer)s0).getInputHandler());
        //Thread guiThread = new Thread(gui);
        //guiThread.start();

        //gui = new Gui(board, ((HumanPlayer)s0).getInputHandler()); //<- TO FIX
        Thread guiThread = new Thread(gui);
        guiThread.start();
        board.addObserver(gui);
        //gui.addMouseListener(this);
        
    }
    
    public Game(Player s0, Player s1) {
        board = new Board();
        players = new Player[NUMBER_PLAYERS];

        players[0] = s0;
        players[1] = s1;
        current = 0;
        tui = new TUI(board);
        //this.gui = gui;
        
        //gui = new Gui(board, (MouseListener)((HumanPlayer)s0).getInputHandler()));

        //gui = new Gui(board, ((HumanPlayer)s0).getInputHandler());
        //Thread guiThread = new Thread(gui);
        //guiThread.start();

        //gui = new Gui(board, ((HumanPlayer)s0).getInputHandler()); //<- TO FIX
        //Thread guiThread = new Thread(gui);
        //guiThread.start();
        //board.addObserver(gui);
        //gui.addMouseListener(this);
        
    }
    
    public Game(Player s0, Player s1, int width, int height) {
        board = new Board(width, height);
        players = new Player[NUMBER_PLAYERS];
        players[0] = s0;
        players[1] = s1;
        current = 0;
        tui = new TUI(board);
        //gui = new Gui(board, (MouseListener)((HumanPlayer)s0).getInputHandler()));
       // gui = new Gui(board, ((HumanPlayer)s0).getInputHandler());
        //Thread guiThread = new Thread(gui);
        //guiThread.start();

        //board.addObserver(gui);
        //gui.addMouseListener(this);
        
    }

	
	
	
    private void play() {
    	while (!board.gameOver()){
    		players[current].makeMove(board);
       		current = (current+1)%2; 
    	}
    }


    private void update() {
        //System.out.println("\ncurrent game situation: \n\n" + tui.showBoard()+ "\n");
        //gui.updateBoard();
    }

	public void reset() {
		board.reset();
	}
	
	public Board getBoard() {
		return board;
	}


	private boolean readPlayAgain() {
        String answer;

        do {
            System.out.print("Play again? (y/n)");
            Scanner in = new Scanner(System.in);
            answer = in.hasNextLine() ? in.nextLine() : null;
        } while (answer == null || (!answer.equals("y") && !answer.equals("n")));
        return answer.equals("y");
	}

	@Override
	public void run() {
		running = true;
		while(running){
			reset();
			play();
			running = readPlayAgain();
			
		}
		
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
		System.out.println("getCurrent: " + current);
		return current;
	}
	
	public void gameEnd(){
		System.out.println("Game has ended");
	}









}
