package Project.gui.game;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.io.File;

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
	/**
	 * Creates a new GamePanel without HumanPlayer.
	 * This creates all Slots and sets their icons.
	 * 
	 * @param g is the game this GamePanel is representing.
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
	

	/**
	 * Constructor for a GamePanel with one or more HumanPlayers.
	 * It creates all slots, fills them with an Icon 
	 * 
	 * @param g is the game this GamePanel is representing.
	 * @param mouseListener is the MouseListener to be used to gain move information.
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
	
	/**
	 * Gives the Icon used to represent given Mark.
	 * 
	 * @param m is the mark of which you'd like to know it's Icon.
	 * @return the Icon associated with this Mark.
	 */
	public Icon getIcon(Mark m){ 
		if(m==Mark.X){
			return new ImageIcon("bin"+File.separator+"Project"+File.separator+"gui"+File.separator+"game"+File.separator+"images"+File.separator+"red.png");
		}else if(m==Mark.O){
			return new ImageIcon("bin"+File.separator+"Project"+File.separator+"gui"+File.separator+"game"+File.separator+"images"+File.separator+"yellow.png");
		}else{
			return new ImageIcon("bin"+File.separator+"Project"+File.separator+"gui"+File.separator+"game"+File.separator+"images"+File.separator+"empty.png");
		}
		
	}
	
	/**
	 * Updates the board by resetting all Icons.
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
	/**
	 * Changes a certain Icon in this GamePanl.
	 * 
	 * @param row is the row of the Icon to refresh
	 * @param column is the column of the Icon to refresh
	 * @param m is the mark whose Icon should be made visible.
	 */
	public void setSlot(int row, int column, Mark m){
		slots[row][column].setIcon(getIcon(m));
	}
	

}
