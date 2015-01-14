package Project.gui;


import javax.swing.BoxLayout;
import java.awt.event.MouseListener;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import Project.logic.Board;


public class Gui extends JFrame implements Observer, Runnable {

	Board b;
	GamePanel mainpanel;
	ButtonPanel buttonpanel;
	MouseListener mouseListener;

	public Gui(Board b, MouseListener mouseListener) {
		this.b = b;
		this.mouseListener = mouseListener;
		mainpanel = new GamePanel(b, mouseListener);
		buttonpanel = new ButtonPanel(b.getWidth(), mouseListener);
	
	}

	public void updateBoard() {
		mainpanel.updateBoard();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		updateBoard();

	}

	@Override
	public void run() {

		// ------------------------------------------

		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		// RESIZE IF NECESSARY
		Dimension d = new Dimension(64 * b.getWidth(), 64 * b.getWidth() - 10);
		setSize(d);

		// ADD PANELS HERE
		// ------------------------------------------
		add(mainpanel);
		add(buttonpanel);
		// -------------------------------------------

		this.setResizable(false);
		setVisible(true);
		
	}

}
