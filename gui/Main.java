package Project.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.*;

import Project.logic.Board;
import Project.logic.Mark;


public class Main extends JPanel {
	
		public Main(Board b){
			JFrame j = new JFrame();
			j.setLayout(new GridLayout(b.getHeight(), b.getWidth()));
			
			JLabel[][] label = new JLabel[b.getWidth()][b.getHeight()]; 
			String testString;
			b.putMark(2,Mark.O);
			
			
			int boardHeight = 0;
			int boardWidth = 0;
			Icon img;
			
			//TODO bord op scherm gelijk als bord in tui
			for (int p = 0; p < b.getHeight(); p++) {
				for (int i = 0; i < b.getWidth(); i++) {
					img = getIcon(b.getPlace(i, p));
					label[i][p] = new JLabel(img);
					j.add(label[i][p]);

				}
			}
			j.setSize(b.getWidth()*label[0][0].getIcon().getIconHeight(), b.getHeight()*label[0][0].getIcon().getIconHeight());
			j.setVisible(true);
		
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

}
