package Project.gui;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainGui extends JFrame{

	private JPanel currentPanel;
	private MainMenuPanel mainMenuPanel;
	private GameSelectPanel gameSelectPanel;
	
	
	public MainGui(){
		mainMenuPanel = new MainMenuPanel(this);
		currentPanel = mainMenuPanel;
		changePanel(mainMenuPanel);
		gameSelectPanel = new GameSelectPanel(this);
		setVisible(true);
		
		
	}
	
	public void changePanel(JPanel panel){
		remove(currentPanel);
		currentPanel = panel;
		add(currentPanel);
		setSize(new Dimension(currentPanel.getWidth(), currentPanel.getHeight()));
		pack();
		revalidate();
	}
	
	
	public MainMenuPanel getMainMenuPanel(){
		return mainMenuPanel;
	}
	
	public GameSelectPanel getGameSelectPanel(){
		return gameSelectPanel;
	}
	
}
