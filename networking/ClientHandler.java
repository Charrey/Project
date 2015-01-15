package Project.networking;

import java.io.BufferedReader;
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
				server.whatisthat(clientName, ontvangen);

			}
		} catch (IOException ex) {
		}
		shutdown();
	}
	
	public void sendMessage(String source) {
		
	}
	
	public void shutdown() {
		try {
			sock.close();
		} catch (IOException e) {
			System.err.println("Could not close socket");
		}
	}
	

}
