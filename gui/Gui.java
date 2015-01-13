package Project.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.LayoutManager;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;

import javax.swing.*;

import Project.logic.Board;
import Project.logic.Mark;

import java.awt.Color;


public class Gui extends JFrame implements Observer {
	
	JLabel[][] label;
	JLabel[] butlabel;
	Board b;
<<<<<<< HEAD
	JFrame mainFrame;
	JPanel board;
=======
	JPanel mainpanel;
<<<<<<< HEAD
	JPanel buttonpanel;
=======
>>>>>>> origin/master
>>>>>>> origin/master
	
	public Gui(Board b){
		this.b = b;	
		
<<<<<<< HEAD
		mainFrame = new JFrame();
		
		board = new JPanel();
		
		
		board.setLayout(new GridLayout(b.getHeight(), b.getWidth()));
=======
		mainpanel = new JPanel();
		buttonpanel = new JPanel();
		this.setLayout(new BorderLayout());
		mainpanel.setLayout(new GridLayout(b.getHeight(), b.getWidth()));
<<<<<<< HEAD
		buttonpanel.setLayout(new GridLayout(1, b.getWidth()));
		
=======
>>>>>>> origin/master
>>>>>>> origin/master
		Icon img;
		
			
		label = new JLabel[b.getWidth()][b.getHeight()]; 

		for (int p = b.getHeight()-1; p >=0 ; p--) {
			for (int i = 0; i < b.getWidth(); i++) {
				img = getIcon(b.getPlace(i, p));
				label[i][p] = new JLabel(img);
<<<<<<< HEAD
				board.add(label[i][p]);
=======
				mainpanel.add(label[i][p]);
<<<<<<< HEAD
			}
		}
		butlabel = new JLabel[b.getWidth()];
		LayoutManager tempborder = new BorderLayout();
		for (int i=0; i<b.getWidth(); i++) {
			butlabel[i] = new JLabel("test");
			butlabel[i].setLayout(tempborder);
			Color color = new Color(50,255,50);
			butlabel[i].setForeground(color);
			buttonpanel.add(butlabel[i], BorderLayout.CENTER);
		}
=======
>>>>>>> origin/master
				

				
			}
		}
<<<<<<< HEAD
		board.setSize(b.getWidth()*label[0][0].getIcon().getIconHeight(), b.getHeight()*label[0][0].getIcon().getIconHeight());
		mainFrame.setVisible(true);
		mainFrame.add(board);
		mainFrame.setSize(1000, 1000);
=======
>>>>>>> origin/master
		mainpanel.setSize(b.getWidth()*label[0][0].getIcon().getIconHeight(), b.getHeight()*label[0][0].getIcon().getIconHeight());
		setSize(mainpanel.getPreferredSize());
		buttonpanel.setSize(b.getWidth()*label[0][0].getIcon().getIconHeight(), 100);
		add(mainpanel, BorderLayout.NORTH);
		add(buttonpanel, BorderLayout.SOUTH);
		setVisible(true);
>>>>>>> origin/master
		
		
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

		@Override
		public void update(Observable arg0, Object arg1) {
			updateBoard();
			
		}



}
