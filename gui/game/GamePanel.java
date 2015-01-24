package Project.gui.game;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import javax.swing.*;
import Project.logic.Board;
import Project.logic.Game;
import Project.logic.Mark;


public class GamePanel extends JPanel {

	private Board b;
	private Slot[][] slots;

	//GamePanel is de Panel die alle slots herbergt
	
	/*
	 * Constructor voor een GamePanel zonder HumanPlayer
	 * Maakt de slots aan en vult ze meteen met het juiste Icon
	 */
	public GamePanel(Game g){
		this.b = g.getBoard();
		slots = new Slot[b.getWidth()][b.getHeight()];
		Icon img;
		for (int p = b.getHeight()-1; p >=0 ; p--) {
			for (int i = 0; i < b.getWidth(); i++) {
				img = getIcon(b.getPlace(i, p));
				slots[i][p] = new Slot(i);
				slots[i][p].setMinimumSize(new Dimension(img.getIconWidth(), img.getIconHeight()));
				slots[i][p].setIcon(img);
				add(slots[i][p]);
			}
		}
		setLayout(new GridLayout(b.getHeight(), b.getWidth()));
	}
	
	/*
	 * Constructor voor een GamePanel met HumanPlayer
	 * Slots worden gemaakt, gevult en de MouseListener wordt toegevoegd aan elke Slot.
	 */
	public GamePanel(Game g, MouseListener mouseListener){
		this.b = g.getBoard();
		slots = new Slot[b.getWidth()][b.getHeight()];
		Icon img;
		for (int p = b.getHeight()-1; p >=0 ; p--) {
			for (int i = 0; i < b.getWidth(); i++) {
				img = getIcon(b.getPlace(i, p));
				slots[i][p] = new Slot(i);
				slots[i][p].setIcon(img);
				slots[i][p].setMinimumSize(new Dimension(img.getIconWidth(), img.getIconHeight()));
				slots[i][p].addMouseListener(mouseListener);
				add(slots[i][p]);
			}
		}
		setLayout(new GridLayout(b.getHeight(), b.getWidth()));
	}
	
	/*
	 * Geeft het Icon terug die bij de meegegeven Mark hoort
	 */
	public Icon getIcon(Mark m){ 
		if(m==Mark.X){
			return new ImageIcon("bin/Project/gui/game/images/red.png");
		}else if(m==Mark.O){
			return new ImageIcon("bin/Project/gui/game/images/yellow.png");
		}else{
			return new ImageIcon("bin/Project/gui/game/images/empty.png");
		}
		
	}
	
	/* 
	 * update de Slots door ieder plekje langs te gaan en deze opnieuw te zetten
	 * het is een inefficiente manier maar het werkt prima voor borden die niet al te groot zijn
	 */
	public void updateBoard() {
		for (int p = b.getHeight()-1; p >=0 ; p--) {
			for (int i = 0; i < b.getWidth(); i++) {
				slots[i][p].setIcon(getIcon(b.getPlace(i, p)));
			}
		}
	}
	/*
	 * plaatst een Icon in een specifieke plaats in het bord
	 * Wordt gebruikt voor de hint functie; 
	 * plaatst een icoon zichtbaar in de GUI, maar wordt niet doorgegeven aan het systeem zodat het niet als zet gezien wordt
	 */
	public void setSlot(int row, int column, Mark m){
		slots[row][column].setIcon(getIcon(m));
	}
	

}
