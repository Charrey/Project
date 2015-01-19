package Project.gui;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Project.logic.Game;

public class NamePanel extends JPanel {

	Game g;
	String name1;
	String name2;
	JLabel label1;
	JLabel label2;
	
	public NamePanel(Game g){
		this.g = g;
		name1 = g.getFirstPlayer().getName();
		name2 = g.getSecondPlayer().getName();
		
		label1 = new JLabel(name1, SwingConstants.LEFT);
		label2 = new JLabel(name2, SwingConstants.RIGHT);
		this.setLayout(new GridLayout(1,2));
		add(label1);
		add(label2);
		setSize(getPreferredSize());
	}
	
}
