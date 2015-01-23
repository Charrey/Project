package Project.gui.game;

import javax.swing.JLabel;

public class Slot extends JLabel {
	
	private int column;
	
	public Slot(int column){
		this.column = column;
	}
	
	public int getColumn(){
		return column;
	}
	

}
