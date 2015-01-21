package Project.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import Project.logic.Game;
import Project.logic.InputHandler;

public class GameMainPanel extends JPanel {

	private GamePanel gamePanel;
	private NamePanel namePanel;
	private MessagePanel messagePanel;
	private InfoPanel infoPanel;
	private Game g;
	
	

	GameMainPanel(Game g){
		this.g = g;
		gamePanel = new GamePanel(g);
		namePanel = new NamePanel(g);
		infoPanel = new InfoPanel(g, gamePanel);
		setLayout(new BorderLayout());
		add(namePanel, BorderLayout.PAGE_START);
		add(gamePanel);
		add(infoPanel, BorderLayout.PAGE_END);
	}
	
	GameMainPanel(Game g, InputHandler i){
		this.g = g;
		gamePanel = new GamePanel(g, i);
		namePanel = new NamePanel(g);
		messagePanel = new MessagePanel(g);
		infoPanel = new InfoPanel(g, gamePanel);
		setLayout(new BorderLayout());

		add(namePanel, BorderLayout.PAGE_START);
		add(gamePanel, BorderLayout.CENTER);
		add(infoPanel, BorderLayout.PAGE_END);
		//*********************** (Alleen voor online games) ******************
		//add(messagePanel, BorderLayout.LINE_END);
		
		namePanel.setSize(gamePanel.getWidth(), namePanel.getHeight());

		

	}
	
	public GamePanel getGamePanel(){
		return gamePanel;
	}
	
	public void update(){
		gamePanel.updateBoard();
		namePanel.update();
		infoPanel.update();
		if(g.getBoard().isWin()){
			System.out.println("winner " + g.getCurrent());
			infoPanel.setWinner(g.getCurrentPlayer().getName());
		}
		
	}

	
}
