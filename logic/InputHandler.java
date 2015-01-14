package Project.logic;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import Project.gui.Slot;

public class InputHandler implements MouseListener, Runnable{

	private int move;
	private boolean clicked = false;
	
	
	@Override
	public void run() {
		getMove();
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
			synchronized(this){
				if(arg0.getSource() instanceof Slot){
					move = ((Slot)arg0.getSource()).getColumn();
				}else{
					move = Integer.parseInt((((JLabel)arg0.getSource()).getText()));
				}
				clicked = true;
				notify();
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

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}







}
