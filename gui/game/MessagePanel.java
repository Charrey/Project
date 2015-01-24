package Project.gui.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Project.logic.Game;

public class MessagePanel extends JPanel{

	private final int WIDTH = 30;
	
	private Game g;
	private JPanel invoerButtonPanel;
	private JTextArea textarea;
	private JButton button;
	private JTextField textfield;
	private JScrollPane scrollPane;
	
	//MessagePanel is een chatWindow dat naast de GamePanel komt. Deze staat uit voor lokale spellen
	
	/*
	 * Constructor van MessagePanel
	 */
	public MessagePanel(Game g){
		this.g = g;
		
		setLayout(new BorderLayout());
		
		
		invoerButtonPanel = new JPanel();
		invoerButtonPanel.setLayout(new FlowLayout());
		
		textfield = new JTextField("Je kan hier typen", WIDTH);
		button = new JButton("Verzend");
		textarea = new JTextArea();
		scrollPane = new JScrollPane(textarea);

		button.setMaximumSize(new Dimension(textfield.getHeight(), WIDTH/2));
		textarea.setEditable(false);
		
		invoerButtonPanel.add(textfield);
		invoerButtonPanel.add(button);
		add(invoerButtonPanel, BorderLayout.PAGE_END);
		add(scrollPane, BorderLayout.CENTER);

		button.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				addMessage(textfield.getText());
				textfield.setText("");
			}
		});
	}
	
	/*
	 * Deze methode voegt een bericht toe aan het textfield
	 */
	public void addMessage(String msg){
		textarea.append(msg + "\n");
	}
	
}
