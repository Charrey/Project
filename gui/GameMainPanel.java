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
	//private ChatPanel chatPanel;
	
	
	//TODO size shit goed regelen
	GameMainPanel(Game g){
		
		gamePanel = new GamePanel(g);
		namePanel = new NamePanel(g);
		add(namePanel);
		add(gamePanel);
	}
	
	GameMainPanel(Game g, InputHandler i){
		gamePanel = new GamePanel(g, i);
		namePanel = new NamePanel(g);
		messagePanel = new MessagePanel(g);
		setLayout(new BorderLayout());

		add(namePanel, BorderLayout.PAGE_START);
		add(gamePanel, BorderLayout.CENTER);
		add(messagePanel, BorderLayout.LINE_END);
		
		namePanel.setSize(gamePanel.getWidth(), namePanel.getHeight());

		

	}
	
	public GamePanel getGamePanel(){
		return gamePanel;
	}
	
}
