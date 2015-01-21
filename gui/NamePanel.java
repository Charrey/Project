package Project.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

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
	private int currentPlayer = 0;
	private JLabel arrowLabel1;
	private JLabel arrowLabel2;
	
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
		
		arrowLabel1 = new JLabel();
		arrowLabel1.setFont(fnt);
		arrowLabel2 = new JLabel();
		arrowLabel2.setFont(fnt);
		
		
		this.setLayout(new GridLayout(1,4));
		add(label1);
		add(arrowLabel1);
		add(arrowLabel2);
		add(label2);

		update();
		setSize(getPreferredSize());
	}

	public void update() {
		if(currentPlayer == 0){
			arrowLabel1.setText("<----");
			arrowLabel2.setText("");
			currentPlayer = (currentPlayer+1)%2;
		}else{
			arrowLabel2.setText("---->");
			arrowLabel1.setText("");
			currentPlayer = (currentPlayer+1)%2;
		}
		
	}
	
}
