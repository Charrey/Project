package Project.networking;

import java.io.BufferedReader;
import Project.logic.HumanPlayer;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {

	private Server server;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private String clientName;
	private HumanPlayer player;
	private int playerno;

	public ClientHandler(Server serverArg, Socket sockArg) throws IOException {
		server = serverArg;
		sock = sockArg;
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		clientName = sock.getInetAddress().getHostName();
	}

	public void run() {
		try {
			String ontvangen;
			while (!sock.isClosed()) {
				//System.out.println("Ready to read new command from "+ clientName);
				ontvangen = in.readLine();
				server.getGUI().addMessage("New command from " + clientName + ": "
						+ ontvangen);
				server.interpreter.whatisthatServer(ontvangen, this, false);
			}
		} catch (IOException ex) {shutdown();}
	}

	public void sendCommand(String command) {
		try {
			server.getGUI().addMessage("Writing to "+getClientName()+": "+command);
			out.write(command);
			out.newLine();
			out.flush();
		} catch (IOException ex) {
			server.getGUI().addMessage("Unable to send command");
		}
	}
	
	public int getPlayerno() {
		return playerno;
	}
	
	public void setPlayerno(int no) {
		playerno = no;
	}

	public String getClientName() {
		return clientName;
	}

	public Socket getSocket() {
		return sock;
	}

	public void setClientName(String nameArg) {
		clientName = nameArg;
	}

	/*public void sendMessage(String source, String message) {
		try {
			out.write("CHAT " +source. +": "+message);
			out.flush();
		} catch (IOException ex) {
			System.err.println("Unable to send chat message");
		}
	}*/

	public void shutdown() {
		
		server.getGUI().addMessage("Client "+clientName+" has left the server.");
		if (server.invites.containsKey(this)){
			server.invites.remove(this);
		}
		for (ClientHandler i : server.invites.keySet()) {
			if (server.invites.get(i)[0].equals(clientName)) {
				server.invites.remove(i);
				i.sendCommand("DECLINE_INVITE "+clientName);
			}
		}
		server.lobby.remove(this);
		
		
		
		try {
			sock.close();
		} catch (IOException e) {
			server.getGUI().addMessage("Could not close socket");
		}
	}

}
