package Project.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import Project.logic.Board;
import Project.logic.Mark;


public class Gui extends JPanel {
	
	JLabel[][] label;
	Board b;
	JFrame mainFrame;
	
	public Gui(Board b){
		this.b = b;	
		
		mainFrame = new JFrame();
		mainFrame.setLayout(new GridLayout(b.getHeight(), b.getWidth()));
		Icon img;
			
		label = new JLabel[b.getWidth()][b.getHeight()]; 

		for (int p = b.getHeight()-1; p >=0 ; p--) {
			for (int i = 0; i < b.getWidth(); i++) {
				img = getIcon(b.getPlace(i, p));
				label[i][p] = new JLabel(img);
				mainFrame.add(label[i][p]);
				
				
				
			}
		}
		mainFrame.setSize(b.getWidth()*label[0][0].getIcon().getIconHeight(), b.getHeight()*label[0][0].getIcon().getIconHeight());
		mainFrame.setVisible(true);
		
		
		//voorbeeld voor hoe je blokjes met alleen gui kan doen
		/*
		label[0][5].addMouseListener(new MouseAdapter(){
	        public void mouseClicked(MouseEvent e) {
	        	b.putMark(0, Mark.O);
	        	updateBoard();
	        }
		});*/
		
		
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
		
		public void updateBoard(){
			for (int p = b.getHeight()-1; p >=0 ; p--) {
				for (int i = 0; i < b.getWidth(); i++) {
					label[i][p].setIcon(getIcon(b.getPlace(i, p)));
				}
			}
		}



}
