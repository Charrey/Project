package Project.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Project.logic.Board;
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
	private MainGui gui;
	
	public GameSelectPanel(MainGui gui){
		this.gui = gui;
	
		this.setLayout(new GridLayout(3,2));
		Dimension d = new Dimension(100,50);
		JLabel newHumanPlayerLabel1 = new JLabel("Human Player");
		newHumanPlayerLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		newHumanPlayerLabel1.setMinimumSize(d);
		
		JLabel newHumanPlayerLabel2 = new JLabel("Human Player");
		newHumanPlayerLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		newHumanPlayerLabel2.setMinimumSize(d);
		
		JLabel newAIPlayerLabel1 = new JLabel("AI Player");
		newAIPlayerLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		newAIPlayerLabel1.setMinimumSize(d);
		
		JLabel newAIPlayerLabel2 = new JLabel("AI Player");
		newAIPlayerLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		newAIPlayerLabel2.setMinimumSize(d);
		
		JLabel startGameLabel = new JLabel("Start Game");
		startGameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		startGameLabel.setMinimumSize(d);
		
		newHumanPlayerLabel1.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				choice1 = 0;
				System.out.println("1 = 0");
			}
		});
		
		newHumanPlayerLabel2.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				choice2 = 0;
				System.out.println("2 = 0");
			}
		});
		
		newAIPlayerLabel1.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				choice1 = 1;
				System.out.println("1 = 1");

			}
		});
		
		newAIPlayerLabel2.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				choice2 = 1;
				System.out.println("2 = 1");

			}
		});
		
		startGameLabel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(choice1 == 0 && choice2 ==0){
					InputHandler handler = new InputHandler();
					player1 = new HumanPlayer("Speler1", Mark.X, handler);
					player2 = new HumanPlayer("Speler2", Mark.O, handler);
					//Game newGame = new Game(player1, player2, gui);
					Game newGame = new Game(player1, player2, gui);
					gui.changePanel(new GameMainPanel(newGame, handler));
					new Thread(newGame).start();
				}else if(choice1==1&&choice2==1){
					player1 = new ComputerPlayer(Mark.X);
					player2 = new ComputerPlayer(Mark.O);
					Game newGame = new Game(player1, player2, gui);
					gui.changePanel(new GameMainPanel(newGame));
					new Thread(newGame).start();
				}else {
					InputHandler handler = new InputHandler();
					player1 = new HumanPlayer("Speler1", Mark.X, handler);
					player2 = new ComputerPlayer(Mark.O);
					Game newGame = new Game(player1, player2, gui);
					gui.changePanel(new GameMainPanel(newGame, handler));
					new Thread(newGame).start();
					
				}
				
				
				//Game newGame = new Game(player1, player2, gui);
				//Game newGame = new Game(new ComputerPlayer(Mark.O), new ComputerPlayer(Mark.X), gui);
				//gui.changePanel(new GamePanel(newGame));
				//new Thread(newGame).start();
				System.out.println("choice1: " + choice1 + "choice2: " + choice2);
				
			}
		});
		
		
		add(newHumanPlayerLabel1);
		add(newHumanPlayerLabel2);
		add(newAIPlayerLabel1);
		add(newAIPlayerLabel2);
		add(startGameLabel);
		System.out.println(getPreferredSize());
		//this.setSize(this.getPreferredSize());
		setSize(gui.getSize());

		
		
		
		
		//this.setVisible(true);
		
	}

	
}
