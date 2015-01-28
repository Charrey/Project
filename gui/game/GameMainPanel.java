package Project.gui.game;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import Project.gui.MainGui;
import Project.logic.Game;
import Project.logic.InputHandler;

public class GameMainPanel extends JPanel implements Observer {

	private GamePanel gamePanel;
	private NamePanel namePanel;
	private MessagePanel messagePanel;
	private InfoPanel infoPanel;
	private Game g;
	
	
	/* 
	 * Constructor voor een spel zonder HumanPlayers. 
	 * Dit is omdat de een ComputerPlayer geen Inputhandler nodig heeft.
	 */
	/**
	 * Constructor for a game without human Players. 
	 * This usage is required, as a ComputerPlayer does not require an InputHandler.
	 * 
	 * @param gui is the MainGui this GameMainPanel is added to.
	 * @param g is the game to represent.
	 */
	public GameMainPanel(MainGui gui, Game g){
		this.g = g;
		gamePanel = new GamePanel(g);
		namePanel = new NamePanel(g);
		infoPanel = new InfoPanel(gui, g, gamePanel);
		setLayout(new BorderLayout());
		add(namePanel, BorderLayout.PAGE_START);
		add(gamePanel);
		add(infoPanel, BorderLayout.PAGE_END);
	}
	
	
	/**
	 * Constructor for a GameMainPanel with one or more HumanPlayers.
	 * 
	 * @param gui is the MainGui this GameMainPanel is added to.
	 * @param g is the game to represent.
	 * @param i is the Inputhandler used to gain information about player's moves.
	 */
	public GameMainPanel(MainGui gui, Game g, InputHandler i){
		this.g = g;
		gamePanel = new GamePanel(g, i);
		namePanel = new NamePanel(g);
		messagePanel = new MessagePanel(g);
		infoPanel = new InfoPanel(gui, g, gamePanel);
		setLayout(new BorderLayout());

		add(namePanel, BorderLayout.PAGE_START);
		add(gamePanel, BorderLayout.CENTER);
		add(infoPanel, BorderLayout.PAGE_END);
		//*********************** (Alleen voor online games) ******************
		//add(messagePanel, BorderLayout.LINE_END);
		
		namePanel.setSize(gamePanel.getWidth(), namePanel.getHeight());
	}
	

	/*
	 * Deze methode wordt aangeroepen als de klasse Board een notify geeft.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		gamePanel.updateBoard();
		namePanel.update();
		infoPanel.update();
		if(g.getBoard().isWin()){
			System.out.println("winner " + g.getCurrent());
			infoPanel.setWinner(g.getCurrentPlayer().getName());
		}else if(g.getBoard().isFull()){
			infoPanel.setDraw();
		}
		
	}
	
	/**
	 * Gives the GamePanel that's inside this GameMainPanel.
	 * 
	 * @return the GamePanel that's inside this GameMainPanel.
	 */
	public GamePanel getGamePanel(){
		return gamePanel;
	}
	/**
	 * Gives the InfoPanel that's inside this GameMainPanel.
	 * 
	 * @return the InfoPanel that's inside this GameMainPanel.
	 */
	public InfoPanel getInfoPanel(){
		return infoPanel;
	}
	/**
	 * @return the namePanel that's inside this GameMainPanel.
	 */
	public NamePanel getNamePanel(){
		return namePanel;
	}

	
}
