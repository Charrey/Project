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
	
	/*
	 * Constructor voor een GamePanel met een of twee HumanPlayers
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
	
	public GamePanel getGamePanel(){
		return gamePanel;
	}
	public InfoPanel getInfoPanel(){
		return infoPanel;
	}
	public NamePanel getNamePanel(){
		return namePanel;
	}

	
}
