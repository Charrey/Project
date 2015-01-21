package Project.gui;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Project.logic.Game;
import Project.logic.Mark;

public class InfoPanel extends JPanel {

	
	private Game g;
	private JButton hintButton;
	private JLabel hasWon;
	private JButton resetButton;
	private JButton backToMainMenu;
	private GamePanel gpanel;
	
	
	public InfoPanel(Game g, GamePanel gpanel){
		this.g = g;
		this.gpanel = gpanel;
		setLayout(new FlowLayout());
		
		hintButton = new JButton("Hint");
		hasWon = new JLabel("");
		resetButton = new JButton("Reset");
		backToMainMenu = new JButton("Back to Menu");
		
		addMouse();
		
		add(hintButton);
		add(hasWon);
		add(resetButton);
		add(backToMainMenu);

	}
	
	
	public void showHint(){
		//TODO maybe a different slot icon for the hint???
		gpanel.updateBoard();
		Mark mark = g.getCurrentPlayer().getMark();
		int[] move = g.getBoard().hint(mark);
		gpanel.setSlot(move[0], move[1], mark);

	}
	
	public void addMouse(){
		hintButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				showHint();
			}
		});
		
		resetButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				g.reset();
			}
		});
	
	
	}
	
	public void setWinner(String s){
		hasWon.setText(s + " heeft gewonnen");
	}
	
	public void update(){
		hasWon.setText("");
	}
	
	
	
	
	
	
	
	
}
