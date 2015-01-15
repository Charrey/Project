package Project.gui;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Project.logic.ComputerPlayer;
import Project.logic.Game;
import Project.logic.HumanPlayer;
import Project.logic.InputHandler;
import Project.logic.Mark;
import Project.logic.Player;

public class GameSelectPanel extends JPanel {

	private int choice1;
	private int choice2;
	private Player player1;
	private Player player2;
	private Gui gui;
	
	public GameSelectPanel(Gui gui){
		this.gui = gui;
	
		this.setLayout(new GridLayout(3,2));
		
		JLabel newHumanPlayerLabel1 = new JLabel("Human Player");
		JLabel newHumanPlayerLabel2 = new JLabel("Human Player");
		
		JLabel newAIPlayerLabel1 = new JLabel("AI Player");
		JLabel newAIPlayerLabel2 = new JLabel("AI Player");
		
		JLabel startGameLabel = new JLabel("Start Game");
		
		
		newHumanPlayerLabel1.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				choice1 = 0;
			}
		});
		
		newAIPlayerLabel1.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				choice1 = 1;
			}
		});
		
		newAIPlayerLabel1.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				choice2 = 1;
			}
		});
		
		startGameLabel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if (choice1 == 0){
					player1 = new HumanPlayer("name", Mark.X, new InputHandler());
				}else if(choice1==1){
					player1 = new ComputerPlayer(Mark.X);
				}
				if(choice2==1){
					player2 = new ComputerPlayer(Mark.O);
				}
				//new Game(player1, player2);
				
			}
		});
		
		
		add(newHumanPlayerLabel1);
		add(newHumanPlayerLabel2);
		add(newAIPlayerLabel1);
		add(newAIPlayerLabel2);
		add(startGameLabel);


		
		
		
		
		//this.setVisible(true);
		
	}

	
}
