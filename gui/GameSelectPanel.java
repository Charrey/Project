package Project.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import Project.logic.ComputerPlayer;
import Project.logic.Game;
import Project.logic.HumanPlayer;
import Project.logic.InputHandler;
import Project.logic.Mark;
import Project.logic.Player;

public class GameSelectPanel extends JPanel {
	private static final long serialVersionUID = 7183300383835862909L;
	private Font fnt0 = new Font("", Font.BOLD, 15);
	private Font fnt = new Font("Serif", Font.BOLD, 30);
	private Font fnt1 = new Font("Serif", Font.ITALIC, 15);
	private Font titleFont = new Font("", Font.BOLD, 60);
	private EmptyBorder padding = new EmptyBorder(10,10,10,10);
	private EmptyBorder titlePadding = new EmptyBorder(40,40,40,40);
	
	private JLabel optionsLabel;
		private JLabel player1Label;
			private JLabel infoLabel1;
			private JTextField player1Field;
		private JLabel player2Label;
			private JLabel infoLabel2;
			private JTextField player2Field;
		private JLabel boardSizeLabel;
			private JLabel widthLabel;
			private JTextField widthField;
			private JLabel heightLabel;
			private JTextField heightField;
	private JButton startButton;
	private InputHandler handler;
	

	/**
	 * Creates a new GameSelectPanel.
	 * 
	 * @param gui is the MainGui this panel is added to.
	 */

	public GameSelectPanel(MainGui gui){
	
		setLayout(new BorderLayout());
		
		//begin toevoegen items
		JPanel combinePanel = new JPanel();
		combinePanel.setLayout(new BorderLayout()); //om playerLabels en playerSelectPanel te mergen.
		
		JPanel combinePanel2 = new JPanel();
		combinePanel2.setLayout(new BorderLayout()); //om combinePanel en boardPanel te mergen
		
		optionsLabel = new JLabel("Options");
		optionsLabel.setFont(titleFont);
		optionsLabel.setBorder(titlePadding);
		add(optionsLabel, BorderLayout.PAGE_START);
		
		JPanel playerLabels = new JPanel();
		playerLabels.setLayout(new GridLayout(1,2));
		
		player1Label = new JLabel("Select player 1");
		player1Label.setFont(fnt);
		player1Label.setBorder(padding);
		playerLabels.add(player1Label);
		
		player2Label = new JLabel("Select player 2");
		player2Label.setFont(fnt);
		player2Label.setBorder(padding);
		playerLabels.add(player2Label);
		
		
		JPanel playerSelectPanel = new JPanel();
		playerSelectPanel.setLayout(new GridLayout(2,2));
		infoLabel1 = new JLabel("(Keep empty for AI)");
		infoLabel1.setFont(fnt1);
		//infoLabel1.setBorder(padding);
		playerSelectPanel.add(infoLabel1);

		infoLabel2 = new JLabel("(Keep empty for AI)");
		infoLabel2.setFont(fnt1);
		//infoLabel2.setBorder(padding);
		playerSelectPanel.add(infoLabel2);
		
		player1Field = new JTextField();
		player1Field.setFont(fnt0);
		//player1Field.setBorder(padding);
		playerSelectPanel.add(player1Field);
		
		player2Field = new JTextField();
		player2Field.setFont(fnt0);
		//player2Field.setBorder(padding);
		playerSelectPanel.add(player2Field);
		
		combinePanel.add(playerLabels, BorderLayout.PAGE_START);
		combinePanel.add(playerSelectPanel, BorderLayout.CENTER);
		
		//add(combinePanel, BorderLayout.CENTER);
		
		
		JPanel boardPanel = new JPanel();
		boardPanel.setLayout(new BorderLayout());
		
		boardSizeLabel = new JLabel("Board Settings");
		boardSizeLabel.setFont(fnt);
		boardSizeLabel.setBorder(padding);
		boardPanel.add(boardSizeLabel, BorderLayout.PAGE_START);
		
		JPanel boardGridPanel = new JPanel();
		boardGridPanel.setLayout(new GridLayout(2,2));
		
		widthLabel = new JLabel("Widht of Field");
		widthLabel.setFont(fnt0);
		boardGridPanel.add(widthLabel);
		
		widthField = new JTextField("7");
		widthField.setFont(fnt0);
		boardGridPanel.add(widthField);
		
		heightLabel = new JLabel("Height of Field");
		heightLabel.setFont(fnt0);
		boardGridPanel.add(heightLabel);
		
		heightField = new JTextField("6");
		heightField.setFont(fnt0);
		boardGridPanel.add(heightField);
		
		boardPanel.add(boardGridPanel, BorderLayout.CENTER);
		
		combinePanel2.add(combinePanel, BorderLayout.PAGE_START);
		combinePanel2.add(boardPanel, BorderLayout.CENTER);
		
		add(combinePanel2, BorderLayout.CENTER);
		
		startButton = new JButton("Start Game");
		startButton.setFont(fnt0);
		
		add(startButton, BorderLayout.PAGE_END);
		//einde toevoegen items
		
		/* voegt mouse listener toe die ervoor zorgt dat er een nieuwe game wordt aangemaakt
		 * met de ingevulde opties als er op de startButton geklikt wordt.
		 */
		startButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				String player1 = player1Field.getText();
				String player2 = player2Field.getText();
				
				int width = Integer.parseInt(widthField.getText());
				int height = Integer.parseInt(heightField.getText());
				Player p1;
				Player p2;
				

				if(!player1.isEmpty() && !player2.isEmpty()){
					handler = new InputHandler();
					p1 = new HumanPlayer(player1, Mark.X, handler);
					p2 = new HumanPlayer(player2, Mark.O, handler);
				}else if(!player1.isEmpty() && player2.isEmpty()){
					handler = new InputHandler();
					p1 = new HumanPlayer(player1, Mark.X, handler);
					p2 = new ComputerPlayer(Mark.O);
				}else if(player1.isEmpty() && !player2.isEmpty()){
					handler = new InputHandler();
					p1 = new ComputerPlayer(Mark.X);
					p2 = new HumanPlayer(player1, Mark.O, handler);
				}else{
					p1 = new ComputerPlayer(Mark.X);
					p2 = new ComputerPlayer(Mark.O);
				}
				new Game(p1, p2, gui, width, height, false);
			}
		});

	}

	
}
