package Project.gui;

import java.awt.GridLayout;

import javax.swing.*;

import Project.logic.Board;
import Project.logic.Mark;


public class GamePanel extends JPanel {

	public Board b;
	public JLabel[][] labels;
	
	
	public GamePanel(Board b){
		this.b = b;
		labels = new JLabel[b.getWidth()][b.getHeight()];
		
		Icon img;
		for (int p = b.getHeight()-1; p >=0 ; p--) {
			for (int i = 0; i < b.getWidth(); i++) {
				img = getIcon(b.getPlace(i, p));
				labels[i][p] = new JLabel(img);
				add(labels[i][p]);
				System.out.println("yes");
				
			}
		}
		
		setLayout(new GridLayout(b.getHeight(), b.getWidth()));
		setSize(b.getWidth()*labels[0][0].getIcon().getIconHeight(), b.getHeight()*labels[0][0].getIcon().getIconHeight());
		
		
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
				labels[i][p].setIcon(getIcon(b.getPlace(i, p)));
			}
		}
	}
	
	

}
