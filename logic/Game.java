package Project.logic;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Scanner;

import javax.swing.JLabel;

import Project.gui.Gui;



public class Game implements Runnable {

	private Player[] players;
	private static final int NUMBER_PLAYERS = 2;
	private int current;
	private Board board;
	private boolean running;
	private TUI tui;
	private Gui gui;
	
    public Game(Player s0, Player s1) {
        board = new Board();
        players = new Player[NUMBER_PLAYERS];
        players[0] = s0;
        players[1] = s1;
        current = 0;
        tui = new TUI(board);
        //gui = new Gui(board, (MouseListener)((HumanPlayer)s0).getInputHandler()));
        gui = new Gui(board, ((HumanPlayer)s0).getInputHandler());
        Thread guiThread = new Thread(gui);
        guiThread.start();
        board.addObserver(gui);
        //gui.addMouseListener(this);
        
    }

	
	
	
    private void play() {
    	update();
    	while (!board.gameOver()){
    		players[current].makeMove(board);
    		current = (current+1)%2;    		
    		update();
    		//board.setField(players[current].makeMove(board), players[current].getMark());
    		
    	}
    }


    private void update() {
        System.out.println("\ncurrent game situation: \n\n" + tui.showBoard()+ "\n");
        //gui.updateBoard();
    }

	private void reset() {
		board.reset();
	}

	//TODO readplay again stays
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









}
