package Project.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Project.logic.Game;

public class MessagePanel extends JPanel{

	private final int WIDTH = 30;
	
	private Game g;
	private JPanel invoerButtonPanel;
	
	public MessagePanel(Game g){
		this.g = g;
		
		setLayout(new BorderLayout());
		
		
		invoerButtonPanel = new JPanel();
		invoerButtonPanel.setLayout(new GridLayout(1,2));
		
		JTextField textfield = new JTextField("Je kan hier typen", WIDTH);
		JButton button = new JButton("Verzend");
		//TODO button smaller
		//TODO add textarea
		button.setMaximumSize(new Dimension(textfield.getHeight(), WIDTH/10));
		
		invoerButtonPanel.add(textfield);
		invoerButtonPanel.add(button);
		add(invoerButtonPanel, BorderLayout.PAGE_END);
	}
	
}
