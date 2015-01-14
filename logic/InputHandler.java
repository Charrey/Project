package Project.logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

public class InputHandler implements MouseListener{

	private int move;
	private boolean clicked = false;
	@Override
	public void mouseClicked(MouseEvent arg0) {
		move = Integer.parseInt((((JLabel)arg0.getSource()).getText()));
		//clicked = true;
		//notify();
		
	
		
	}
	
	public int getMove(){
		return move;
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
