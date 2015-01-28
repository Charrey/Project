package Project.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainMenuPanel extends JPanel {

	private JLabel fourInRowLabel;
	private JLabel localGameLabel;
	private JLabel clientLabel;
	private JLabel serverLabel;
	private Font fntTitel = new Font("Serif", Font.BOLD, 60);
	private Font fnt = new Font("Serif", Font.BOLD, 30);
	private EmptyBorder padding = new EmptyBorder(10,10,10,10);
	private EmptyBorder padding2 = new EmptyBorder(40,40,40,40);

	
	
	/**
	 * Creates a new MainMenuPanel.
	 * 
	 * @param g is the MainGui to add this panel to.
	 */
	public MainMenuPanel(MainGui g){
		//begin labels
		fourInRowLabel = new JLabel("Vier op een Rij");
		localGameLabel = new JLabel("Start Local Game");
		clientLabel = new JLabel("Join Server");
		serverLabel = new JLabel("Start new Server");
		//eind elabels
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//this.setAlignmentX(CENTER_ALIGNMENT);
		//this.setAlignmentY(CENTER_ALIGNMENT);
		
		//begin toevoegen labels
		add(fourInRowLabel);
		fourInRowLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		fourInRowLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
		fourInRowLabel.setFont(fntTitel);
		fourInRowLabel.setBorder(padding2);
	
		
		add(localGameLabel);
		localGameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		localGameLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
		localGameLabel.setFont(fnt);
		localGameLabel.setBorder(padding);
		localGameLabel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				g.changePanel(new GameSelectPanel(g));
			}
		});
		
		add(clientLabel);
		clientLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		clientLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
		clientLabel.setFont(fnt);
		clientLabel.setBorder(padding);
		clientLabel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				new ClientGUI();
			}
		});
		
		add(serverLabel);
		serverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		serverLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
		serverLabel.setFont(fnt);
		serverLabel.setBorder(padding);
		serverLabel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				new ServerGUI();
			}
		});
		//einde toevoegen labels
		
	}
	
	
}
