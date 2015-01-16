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
				System.out.println("Ready to read new command from "
						+ clientName);
				ontvangen = in.readLine();
				System.out.println("Read new command from " + clientName + ": "
						+ ontvangen);
				server.interpreter.whatisthatServer(ontvangen, this, false);
			}
		} catch (IOException ex) {
			System.err.println("Error while recieving command");
		}
		shutdown();
	}

	public void sendCommand(String command) {
		try {
			out.write(command);
			out.flush();
		} catch (IOException ex) {
			System.err.println("Unable to send command");
		}
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

	public void sendMessage(String source, String message) {
		try {
			out.write("CHAT " + message);
			out.flush();
		} catch (IOException ex) {
			System.err.println("Unable to send chat message");
		}
	}

	public void shutdown() {
		try {
			sock.close();
		} catch (IOException e) {
			System.err.println("Could not close socket");
		}
	}

}
