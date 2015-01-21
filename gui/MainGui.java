package Project.gui;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainGui extends JFrame implements Observer, Runnable {

	JPanel currentPanel;
	
	
	public MainGui(){
		//currentPanel = new GameSelectPanel(this);
		currentPanel = new MainMenuPanel(this);
		//add(currentPanel);
		//setSize(500,800);
		changePanel(currentPanel);
		
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
	
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(currentPanel instanceof GameMainPanel){
			((GameMainPanel)currentPanel).update();
		}
	}

}
