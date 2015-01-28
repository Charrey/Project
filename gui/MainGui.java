package Project.gui;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainGui extends JFrame{

	private JPanel currentPanel;
	private MainMenuPanel mainMenuPanel;
	private GameSelectPanel gameSelectPanel;
	
	
	/**
	 * Creates a new MainGui.
	 */
	public MainGui(){
		mainMenuPanel = new MainMenuPanel(this);
		currentPanel = mainMenuPanel;
		changePanel(mainMenuPanel);
		gameSelectPanel = new GameSelectPanel(this);
		setVisible(true);
		
		
	}
	
	/**
	 * Changes the active panel in this JFrame.
	 * 
	 * @param panel is the panel to change to.
	 */
	public void changePanel(JPanel panel){
		remove(currentPanel);
		currentPanel = panel;
		add(currentPanel);
		setSize(new Dimension(currentPanel.getWidth(), currentPanel.getHeight()));
		pack();
		revalidate();
	}
	
	
	/**
	 * Gives the mainMenuPanel of this MainGui.
	 * 
	 * @return the mainMenuPanel.
	 */
	public MainMenuPanel getMainMenuPanel(){
		return mainMenuPanel;
	}
	
	/**
	 * Gives the GameSelectPanel of this MainGui.
	 * 
	 * @return the gameSelectPanel.
	 */
	public GameSelectPanel getGameSelectPanel(){
		return gameSelectPanel;
	}
	
}
