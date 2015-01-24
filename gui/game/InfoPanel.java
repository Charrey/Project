package Project.gui.game;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import Project.gui.MainGui;
import Project.logic.Game;
import Project.logic.Mark;

public class InfoPanel extends JPanel {

	
	private Game g;
	private JButton hintButton;
	private JLabel hasWon;
	private JButton restartButton;
	private JButton backToMainMenu;
	private MainGui gui;
	private GamePanel gamePanel;
	private boolean clicked = false;
	private InfoPanel self;
	
	//De infoPanel is het blokje onder het bord die een paar Buttons en een label herbergt
	
	/*
	 * Constructor voor de InfoPanel
	 */
	public InfoPanel(MainGui gui, Game g, GamePanel gamePanel){
		this.g = g;
		this.gui = gui;
		this.gamePanel = gamePanel;
		self = this;
		setLayout(new FlowLayout());
		
		hintButton = new JButton("Hint");
		hasWon = new JLabel("");
		restartButton = new JButton("Restart");
		backToMainMenu = new JButton("Back to Menu");
		
		addMouse();
		
		add(hintButton);
		add(hasWon);
		add(restartButton);
		add(backToMainMenu);
	}
	
	/*
	 * methode die ervoor zorgt dat er een hint verschijnt op het scherm
	 * het bord wordt eerste geupdate zodat eventuele eerdere hints verwijderd worden
	 * De hint functie van het bord geeft een array van lengte 2 terug met op positie 0 de kollom en op positie 1 de rij
	 * De hint wordt alleen weergegeven in de GamePanel, hij wordt niet doorgegeven aan het systeem.
	 */
	public void showHint(){
		//TODO maybe a different slot icon for the hint???
		gamePanel.updateBoard();
		Mark mark = g.getCurrentPlayer().getMark();
		int[] move = g.getBoard().hint(mark);
		gamePanel.setSlot(move[0], move[1], mark);

	}
	
	/*
	 * Deze methode zorgt ervoor dat alle buttons voorzien worden van een MouseListener 
	 * Is in aparte methode gedaan zodat de code iets overzichterlijker wordt
	 */
	public void addMouse(){
		hintButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				showHint();
			}
		});
		
		restartButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(hasWon.getText() != ""){
					synchronized(self){
						clicked = true;
						self.notify();
					}
				}
			}
		});
	
		backToMainMenu.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				gui.changePanel(gui.getGameSelectPanel());
			}
		});
	
	}
	
	public void setWinner(String s){
		hasWon.setText(s + " has won");
	}
	
	public void setDraw(){
		hasWon.setText("Draw");
	}
	
	public void update(){
		hasWon.setText("");
	}

	/*
	 * Het herstarten van een spel mag alleen plaatsvinden als een spel is afgelopen en er op de Restart knop is gedrukt.
	 * Deze methode wordt aangeroepen vanuit de Game klasse. 
	 * Op het moment dat de Restart knop nog niet geklikt is, dan gaat deze methode wachten totdat die wel gedrukt is. 
	 */
	public boolean getReplay(){
		synchronized(self){
			if(!clicked){
				try {
					wait();
				} catch (InterruptedException e) {
					System.err.println("Something went wrong with waiting for restart");
				}
			}
			clicked = false;
			return true;
		}
	}

	
	
	
	
	
	
	
}
