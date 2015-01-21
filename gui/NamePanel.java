package Project.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Project.logic.Game;

public class NamePanel extends JPanel {

	private Game g;
	private String name1;
	private String name2;
	private JLabel label1;
	private JLabel label2;
	private Font fnt = new Font("", Font.BOLD, 30);
	//private int currentPlayer = 0;
	private JLabel arrowLabel1;
	private JLabel arrowLabel2;
	private boolean justStarted = true;
	
	public NamePanel(Game g){
		this.g = g;
		name1 = g.getFirstPlayer().getName();
		name2 = g.getSecondPlayer().getName();
		
		label1 = new JLabel(name1, SwingConstants.LEFT);
		label1.setFont(fnt);
		label1.setForeground(Color.RED);
		label2 = new JLabel(name2, SwingConstants.RIGHT);
		label2.setFont(fnt);
		label2.setForeground(Color.YELLOW);
		
		
		
		arrowLabel1 = new JLabel("", SwingConstants.LEFT);
		arrowLabel1.setFont(fnt);

		arrowLabel2 = new JLabel("", SwingConstants.RIGHT);
		arrowLabel2.setFont(fnt);

		this.setLayout(new GridLayout(1,4));
		add(label1);
		add(arrowLabel1);
		//add(hintButton);
		add(arrowLabel2);
		add(label2);
		
		
		


		setSize(getPreferredSize());
	}

	
	//TODO fix reset bug, where arrow changes while it shouldn't
	public void update() {
		if(justStarted){
			arrowLabel1.setText("<-");
			arrowLabel2.setText("");
			justStarted=false;
		}
		else if(g.getCurrent() == 1){
			arrowLabel1.setText("<-");
			arrowLabel2.setText("");
		}else{
			arrowLabel2.setText("->");
			arrowLabel1.setText("");
		}
		
	}
	
}
