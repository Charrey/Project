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
	}

	public void run() {
		try {
			String ontvangen;
			while (!sock.isClosed()) {
				ontvangen = in.readLine();
				server.interpreter.whatisthatServer(ontvangen, this);

			}
		} catch (IOException ex) {
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

	
	
	public void sendMessage(String source, String message) {
		try {
			out.write(source + ": " + message);
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
