package Project.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

import Project.networking.ClientHandler;
import Project.networking.Server;
import Project.networking.ServerConsole;

public class ServerGUI extends JFrame{

	
	private JTextArea textArea;
	private JButton hostButton;
	private JTextField portField;
	private JLabel ipAdressLabel;
	private String hostAdress;
	private int portNumber;
	private Server server;
	private JScrollPane scrollPane;
	private ServerGUI self;
	private Font fnt = new Font("", Font.BOLD, 20);
	private boolean hosting = false;
	
	public ServerGUI(){
	
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(2, 2));
		
		JPanel topButPanel = new JPanel();
		topButPanel.setLayout(new BorderLayout());
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		hostButton = new JButton("Host");
		hostButton.setFont(fnt);

		scrollPane = new JScrollPane(textArea);
		
		JLabel ipLabel = new JLabel("Your IP is: ");
		ipLabel.setFont(fnt);
		hostAdress = getIP();
		ipAdressLabel = new JLabel(hostAdress);
		ipAdressLabel.setFont(fnt);
		topPanel.add(ipLabel);
		topPanel.add(ipAdressLabel);
		
		JLabel portLabel = new JLabel("Typ port in");
		portLabel.setFont(fnt);
		portField = new JTextField("");
		portField.setFont(fnt);
		topPanel.add(portLabel);
		topPanel.add(portField);
		//topPanel.add(new JLabel());
		//topPanel.add(hostButton);
		
		topButPanel.add(topPanel, BorderLayout.LINE_START);
		topButPanel.add(hostButton, BorderLayout.LINE_END);
		
		setLayout(new BorderLayout());
		add(topButPanel, BorderLayout.PAGE_START);
		add(scrollPane, BorderLayout.CENTER);
		
		
		

		
		
		//this.setLayout(new GridLayout(4, 1));
		/*
		add(ipAdressLabel);
		add(portField);
		add(hostButton);
		add(scrollPane);
		*/
		setVisible(true);
		Dimension d = new Dimension(500, 800);
		setSize(d);
		self = this;
		hostButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(!hosting){
					try{
					portNumber = Integer.parseInt(portField.getText());
					server = new Server(portNumber, self);
					hostButton.setText("Disconnect");
					hosting = true;
					}catch(NumberFormatException n){
						addMessage("Please enter a valid portnumber");
					}
				}else{
					//TODO server.shutdown
					hostButton.setText("Host");
					hosting = false;
				}
				portNumber = Integer.parseInt(portField.getText());
				server = new Server(portNumber, self);
				ServerConsole sc = new ServerConsole(server);
				sc.start();
			}
		});
		
		
	}
	
	public static String getIP(){
		try{
		URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
		String ip = in.readLine();
		return ip;
		}catch(Exception e){
			return null;
		}
	}
	
	public void addMessage(String msg){
		textArea.append(msg + "\n");
	}
	
	public static void main(String[] args){
		new ServerGUI();
	}
	
	
}
