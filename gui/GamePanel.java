package Project.gui;

import java.awt.GridLayout;
import java.awt.event.MouseListener;

import javax.swing.*;

import Project.logic.Board;
import Project.logic.Mark;


public class GamePanel extends JPanel {

	public Board b;
	//public JLabel[][] labels;
	public Slot[][] slots;

	
	
	public GamePanel(Board b, MouseListener mouseListener){
		this.b = b;


		slots = new Slot[b.getWidth()][b.getHeight()];
		Icon img;
		for (int p = b.getHeight()-1; p >=0 ; p--) {
			for (int i = 0; i < b.getWidth(); i++) {
				img = getIcon(b.getPlace(i, p));
				slots[i][p] = new Slot(i);
				slots[i][p].setIcon(img);
				slots[i][p].addMouseListener(mouseListener);
				add(slots[i][p]);
			}
		}
		setLayout(new GridLayout(b.getHeight(), b.getWidth()));
		setSize(b.getWidth()*slots[0][0].getIcon().getIconHeight(), b.getHeight()*slots[0][0].getIcon().getIconHeight());
		
		this.setVisible(true);
	}
	
	
	public Icon getIcon(Mark m){ 
		if(m==Mark.X){
			return new ImageIcon("bin/Project/gui/images/red.png");
		}else if(m==Mark.O){
			return new ImageIcon("bin/Project/gui/images/yellow.png");
		}else{
			return new ImageIcon("bin/Project/gui/images/empty.png");
		}
		
	}
	
	public void updateBoard() {
		for (int p = b.getHeight()-1; p >=0 ; p--) {
			for (int i = 0; i < b.getWidth(); i++) {
				slots[i][p].setIcon(getIcon(b.getPlace(i, p)));
			}
		}
	}
	
	

}
