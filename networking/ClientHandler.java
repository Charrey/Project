package Project.networking;

import java.io.BufferedReader;
//import Project.logic.HumanPlayer;
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
	//private HumanPlayer player;
	private int playerno;

	public ClientHandler(Server serverArg, Socket sockArg) {
		server = serverArg;
		sock = sockArg;
		try {
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(
					sock.getOutputStream()));
		} catch (IOException io) {
			server.printMessage("IOE in constructor");
			this.shutdown();
		}
		clientName = sock.getInetAddress().getHostName();
	}

	public void run() {
		try {
			String ontvangen;
			while (!sock.isClosed()) {
				server.printMessage("Ready for new command.");
				ontvangen = in.readLine();
				server.printMessage("New command from " + clientName + ": " + ontvangen);
				server.getInterpreter().whatisthatServer(ontvangen, this, false);
			}
		} catch (IOException ex) {
			server.printMessage("IOE in CH.run");
			shutdown();
		}
	}

	public void sendCommand(String command) {
		try {
			server.printMessage(
					"Writing to " + getClientName() + ": " + command);
			out.write(command);
			out.newLine();
			out.flush();
		} catch (IOException ex) {
			server.printMessage("Unable to send command");
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

	/*
	 * public void sendMessage(String source, String message) { try {
	 * out.write("CHAT " +source. +": "+message); out.flush(); } catch
	 * (IOException ex) { System.err.println("Unable to send chat message"); } }
	 */

	public void shutdown() {

		server.printMessage(
				"Client " + clientName + " has left the server.");
		if (server.invites.containsKey(this)) {
			server.invites.remove(this);
		}
		for (ClientHandler i : server.invites.keySet()) {
			if (server.invites.get(i)[0].equals(clientName)) {
				server.invites.remove(i);
				i.sendCommand("DECLINE_INVITE " + clientName);
			}
		}
		server.getLobby().remove(this);		
		if (server.getPlaying().get(this)) {
			if (server.getOpponent(this)!=null) {
			server.getOpponent(this).sendCommand(Interpreter.KW_GAME_GAMEEND + " DISCONNECT"+" "+server.getOpponent(this).getClientName());}
			server.getGames().remove(server.getGame(this));
		}
		server.getPlaying().remove(this);
		try {
			sock.close();
		} catch (IOException e) {
			server.printMessage("Could not close socket");
		}
	}

}
