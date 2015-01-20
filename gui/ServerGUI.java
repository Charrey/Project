package Project.gui;

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
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Project.networking.ClientHandler;
import Project.networking.Server;

public class ServerGUI extends JFrame{

	
	private JTextArea textArea;
	private JButton hostButton;
	private JTextField portField;
	private JLabel ipAdressLabel;
	private String hostAdress;
	private int portNumber;
	private Server server;
	
	
	public ServerGUI(){
		textArea = new JTextArea();
		hostButton = new JButton("Host");
		portField = new JTextField("typ port");

		hostAdress = getIP();

		System.out.println(hostAdress);
		ipAdressLabel = new JLabel(hostAdress);
		
		this.setLayout(new GridLayout(4, 1));
		
		add(ipAdressLabel);
		add(portField);
		add(hostButton);
		add(textArea);
		
		setVisible(true);
		setSize(this.getMinimumSize());
		
		hostButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				portNumber = Integer.parseInt(portField.getText());
				
				server = new Server(portNumber);
			}
		});
		
		
	}
	
	public String getIP(){
		try{
		URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
		String ip = in.readLine(); //you get the IP as a String
		//System.out.println(ip);
		return ip;
		}catch(Exception e){
			return null;
		}
	}
	
	
	public static void main(String[] args){
		new ServerGUI();
	}
	
	
}
