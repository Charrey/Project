package Project.gui.game;

import javax.swing.JLabel;

public class Slot extends JLabel {
	
	private int column;
	
	//Slot is een Jlabel waaraan een variabel column is toegevoegd.
	//dit is gekozen zodat als je om een slot klikt, je ook gemakkelijk terugkrijgt uit welke kollom de slot komt
	
	public Slot(int column){
		this.column = column;
	}
	
	public int getColumn(){
		return column;
	}
	

}
