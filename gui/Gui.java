package Project.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.LayoutManager;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;

import javax.swing.*;

import Project.logic.Board;
import Project.logic.Mark;

import java.awt.Color;

public class Gui extends JFrame implements Observer {

	JLabel[][] label;
	// JLabel[] butlabel;
	Board b;
	GamePanel mainpanel;
	ButtonPanel buttonpanel;

	public Gui(Board b) {
		this.b = b;

		// DECLARE PANELS HERE (include layout and size)
		// -----------------------------------------
		mainpanel = new GamePanel(b);
		buttonpanel = new ButtonPanel(b.getWidth());
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

		// voorbeeld voor hoe je blokjes met alleen gui kan doen
		/*
		 * label[0][5].addMouseListener(new MouseAdapter(){ public void
		 * mouseClicked(MouseEvent e) { b.putMark(0, Mark.O); updateBoard(); }
		 * });
		 */

	}

	public void updateBoard() {
		mainpanel.updateBoard();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		updateBoard();

	}

}
