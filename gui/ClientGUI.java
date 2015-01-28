package Project.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import Project.networking.Client;
import Project.networking.ClientBot;
import Project.networking.ClientConsole;
import Project.networking.ClientHandler;
import Project.networking.Server;

public class ClientGUI extends JFrame {

	
	private JTextArea textArea;
	private JButton hostButton;
	private JTextField portField;
	private JTextField ipAdressLabel;
	private JTextField hostAdress;
	private Server server;
	private JScrollPane scrollPane;
	private ClientGUI self;
	private Font fnt = new Font("", Font.BOLD, 20);
	private boolean connected = false;
	private JLabel nameLabel;
	private JTextField nameField;
	private JTextField commandField;
	private JButton sentCommandButton;
	
	private String ip;
	private int portNumber;
	private String name;
	private Client client;
	private boolean clicked = false;
	

	/**
	 * Creates a new ClientGUI.
	 */

	public ClientGUI(){
	
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(3, 2));
		
		JPanel topButPanel = new JPanel();
		topButPanel.setLayout(new BorderLayout());
		
		JPanel commandPanel = new JPanel();
		commandPanel.setLayout(new FlowLayout());
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		hostButton = new JButton("Connect");
		hostButton.setFont(fnt);

		scrollPane = new JScrollPane(textArea);
		
		JLabel ipLabel = new JLabel("Enter IP: ");
		ipLabel.setFont(fnt);
		ipAdressLabel = new JTextField();
		ipAdressLabel.setFont(fnt);
		topPanel.add(ipLabel);
		topPanel.add(ipAdressLabel);
		
		JLabel portLabel = new JLabel("Enter port: ");
		portLabel.setFont(fnt);
		portField = new JTextField("");
		portField.setFont(fnt);
		topPanel.add(portLabel);
		topPanel.add(portField);
		
		JLabel nameLabel = new JLabel("Enter name: ");
		nameLabel.setFont(fnt);
		nameField= new JTextField();
		nameField.setFont(fnt);
		topPanel.add(nameLabel);
		topPanel.add(nameField);
		
		topButPanel.add(topPanel, BorderLayout.LINE_START);
		topButPanel.add(hostButton, BorderLayout.LINE_END);
		
		
		commandField = new JTextField(30);
		sentCommandButton = new JButton("Send");
		commandPanel.add(commandField);
		commandPanel.add(sentCommandButton);
		
		setLayout(new BorderLayout());
		add(topButPanel, BorderLayout.PAGE_START);
		add(scrollPane, BorderLayout.CENTER);
		add(commandPanel, BorderLayout.PAGE_END);
		
		
		setVisible(true);
		Dimension d = new Dimension(500, 800);
		setSize(d);
		self = this;
		
		
		
		/* voegt een mouse lisstener toe aan de hostButton
		 * Als de client nog niet geconnect is, dan wordt de host button gebruikt
		 * om een verbinding proberen te maken.
		 * Als de client al wel geconnect is, dan wordt de hosbutton 'verandert' in een
		 * disconnect button om de client te sluiten
		 */
		hostButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(!connected){
					try{
					portNumber = Integer.parseInt(portField.getText());
					ip = ipAdressLabel.getText();
					name = nameField.getText();
					if(name.equals(ClientBot.NAME)){
						client = new ClientBot(ip, portNumber, ClientBot.NAME, self);
					}else{
						client = new Client(ip, portNumber, name, self);
					}
					client.start();
					ClientConsole clientconsole = new ClientConsole(client);
					clientconsole.start();

					hostButton.setText("Disconnect");
					connected = true;
					}catch(NumberFormatException n){
						addMessage("Please enter a valid portnumber");
					}
				}else{
					//TODO client.shutdown
					hostButton.setText("Connect");
					connected = false;
				}
			}
		});
		
		/*Als er op de sent knop gedrukt wordt, dan wordt de thread die op de input wacht
		 * genotifyt
		 */
		sentCommandButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				synchronized(self){
					clicked = true;
					System.out.println("clicked");
					self.notifyAll();
				}
			}
		});
		
	}


	
	/**
	 * Adds a message to the textArea of this ClientGUI.
	 * 
	 * @param msg is the message to be added.
	 */

	public void addMessage(String msg){
		textArea.append(msg + "\n");
	}
	


	/**
	 * Waits for a command to be typed in the ClientGUI.
	 * 
	 * @return the String typed in the commandField and entered.
	 */

	public String waitForCommand(){
		synchronized(this){
			if(!clicked){
				try {
					wait();
				} catch (InterruptedException e) {
					System.err.println("Something went wrong");
				}
			}
			clicked = false;
			String command = commandField.getText();
			addMessage(command);
			commandField.setText("");
			return command;
		}
	}
	
	/**
	 * Checks whether a string is a valid IPv4 address. All credit to user
	 * prmatta on stackoverflow.com for this method.
	 * 
	 * @param ip
	 *            is the String to be checked.
	 * @return true if the given String represents a valid IPv4 address or false
	 *         if it doesn't.
	 */
	public static boolean validIP(String ip) {
		try {
			if (ip == null || ip.isEmpty()) {
				return false;
			}

			String[] parts = ip.split("\\.");
			if (parts.length != 4) {
				return false;
			}

			for (String s : parts) {
				int i = Integer.parseInt(s);
				if ((i < 0) || (i > 255)) {
					return false;
				}
			}
			if (ip.endsWith(".")) {
				return false;
			}

			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	public static void main(String[] args){
		new ClientGUI();
	}

	
}
