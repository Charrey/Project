package Project.logic;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Project.gui.game.Slot;

public class InputHandler extends MouseAdapter{

	private int move;
	private boolean clicked = false;
	
	

	
	@Override
	public void mouseClicked(MouseEvent arg0) {
			synchronized(this){
				if(arg0.getSource() instanceof Slot){
					move = ((Slot)arg0.getSource()).getColumn();
				}
				clicked = true;
				notifyAll();
			}
	}

	public int getMove(){
		synchronized(this){
			if(!clicked){
				try {
					wait();
				} catch (InterruptedException e) {
					System.err.println("Something went wrong");
				}
			}
			clicked = false;
			return move;
		}
	}
	
	public boolean getClicked(){
		return clicked;
	}
	public void setClicked(boolean b){
		b = clicked;
	}

	






}
