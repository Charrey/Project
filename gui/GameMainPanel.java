package Project.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

import Project.logic.Game;
import Project.logic.InputHandler;

public class GameMainPanel extends JPanel {

	private GamePanel gamePanel;
	private NamePanel namePanel;
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
		add(namePanel);
		add(gamePanel);
		
		namePanel.setSize(gamePanel.getWidth(), namePanel.getHeight());
		System.out.println(namePanel.getSize());
		System.out.println(gamePanel.getSize());
		

		Dimension d = new Dimension(gamePanel.getWidth(), gamePanel.getHeight()+namePanel.getHeight());
		System.out.println(d);
		setSize(d);
	}
	
	public GamePanel getGamePanel(){
		return gamePanel;
	}
	
}
