package Project.networking;

import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import Project.logic.Game;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Server {
	
	
	public static void main(String[] args) throws IOException {
		int port = 49999;
		ServerSocket ssocket = new ServerSocket(port);
		System.out.println("Port has been opened");
		Socket socket = ssocket.accept();
		System.out.println("Connection accepted");
		
		
	}

	// Set of string contains features
	public Map<ClientHandler, Set<String>> lobby;

	public Set<Game> gamesgames;
	Interpreter interpreter;

	public Server() {
		lobby = new HashMap<ClientHandler, Set<String>>();
		gamesgames = new HashSet<Game>();
	}

	public void joinServer(ClientHandler client, Set<String> features) {
		lobby.put(client, features);
	}

	public void handlechatmessage(ClientHandler source, String message) {

	}

	public void acceptConnection(ClientHandler source, String features) {
		String[] splitted = features.split("\\s+");
		for (int i = 0; i < splitted.length; i++) {
			interpreter.whatisthatServer(splitted[i], source);
		}
		source.sendCommand(interpreter.kw_conn_acceptconnect
				+ " CHAT CUSTOM_BOARD_SIZE");
	}

	public String sendBoard(ClientHandler source) {
		Iterator<Game> it = gamesgames.iterator();
		//for(Iterator<Game> i = it; i.hasNext()) {
		return "";	
		}
	

	public void sendLeaderboard() {
		//
		// IMPLEMENT LEADERBOARD IF WE WANT TO
		//

	}

	public void setFunction(ClientHandler source, String function, Boolean bool) {
		if (lobby.get(source).contains(function) && bool == false) {
			lobby.get(source).remove(function);
		} else if (!lobby.get(source).contains(function) && bool == true) {
			lobby.get(source).add(function);
		}
	}

	public void invite(String target, ClientHandler source) {

	}
}
