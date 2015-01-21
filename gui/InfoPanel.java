package Project.gui;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Project.logic.Game;

public class InfoPanel extends JPanel {

	
	private Game g;
	private JButton hintButton;
	private JLabel hintLabel;
	private JLabel hasWon;
	private JButton resetButton;
	private JButton backToMainMenu;
	private GamePanel gpanel;
	
	
	public InfoPanel(Game g, GamePanel gpanel){
		this.g = g;
		this.gpanel = gpanel;
		setLayout(new FlowLayout());
		
		hintButton = new JButton("Hint");
		hintLabel = new JLabel("");
		hasWon = new JLabel("");
		resetButton = new JButton("Reset");
		backToMainMenu = new JButton("Back to Menu");
		
		addMouse();
		
		add(hintButton);
		add(hintLabel);
		add(hasWon);
		add(resetButton);
		add(backToMainMenu);

	}
	
	
	public void showHint(){
		int move = g.getBoard().hint(g.getCurrentPlayer().getMark());
		hintLabel.setText("" + move);
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
	
	
	
	
	
	
	
	
}
