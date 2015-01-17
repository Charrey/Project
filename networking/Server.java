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
	
	// Set of string contains features
		public Map<ClientHandler, Set<String>> lobby;
		
		public Set<Game> gamesgames;
		Interpreter interpreter;
		ServerSocket serversocket;
		
		public Map<ClientHandler, ClientHandler> invites;
		
		
		
		
		
	
	public static void main(String[] args) throws IOException {
		Server server = new Server(49999);
		while(true) {
		System.out.println("Port 49999 has been opened");
		Socket socket = server.getServerSocket().accept();
		System.out.println("Connection from "+socket.getInetAddress()+" accepted");
		ClientHandler clienthandler = server.addClientHandler(socket);
		clienthandler.start();
		}
		
		
		
	}
	
	public ClientHandler addClientHandler(Socket sockArg) throws IOException {
		ClientHandler clienthandler = new ClientHandler(this, sockArg);
		return clienthandler;
	}

	
	public ServerSocket getServerSocket() {
	return serversocket;
	}
	

	public Server(int port) throws IOException {
		lobby = new HashMap<ClientHandler, Set<String>>();
		gamesgames = new HashSet<Game>();
		serversocket = new ServerSocket(port);
		interpreter = new Interpreter(this);
	}

	public void joinServer(ClientHandler client, Set<String> features) {
		lobby.put(client, features);
	}

	public void handlechatmessage(ClientHandler source, String message) {

	}

	public void acceptConnection(ClientHandler source, String features) {
		System.out.println("Accepted connection command from "+source.getClientName());
		String[] splitted = features.split("\\s+");
		source.setClientName(splitted[0]);
		System.out.println(splitted[0]+" ("+source.getSocket().getInetAddress()+") has connected.");
		joinServer(source, new HashSet<String>());
		for (int i = 1; i < splitted.length; i++) {
		interpreter.whatisthatServer(splitted[i], source, true);
		}
		source.sendCommand(interpreter.kw_conn_acceptconnect
				+ " CHAT CUSTOM_BOARD_SIZE");
		sendLobby(source);
	}
	
	public void sendError(ClientHandler target, String msg) {
	target.sendCommand("ERROR "+msg);
	}

	public String sendBoard(ClientHandler source) {
		Iterator<Game> it = gamesgames.iterator();
		//for(Iterator<Game> i = it; i.hasNext()) {
		return "";	
		}
	
		public void sendLobby(ClientHandler source) {
		String result = "";
		for (ClientHandler i : lobby.keySet()) {
			result += " "+i.getClientName();
		}
		source.sendCommand(interpreter.kw_conn_lobby+result);
		
	}
	

	public void sendLeaderboard() {
		//
		// IMPLEMENT LEADERBOARD IF WE WANT TO
		//

	}

	public void setFunction(ClientHandler source, String function, Boolean bool) {
		System.out.println("Function "+function+" set to "+bool+" for client "+source.getClientName());		
		if (lobby.get(source).contains(function) && bool == false) {
			lobby.get(source).remove(function);
		} else if (!lobby.get(source).contains(function) && bool == true) {
			lobby.get(source).add(function);
		}
	}
	
	public ClientHandler findClientHandler() throws 

	public void invite(String target, ClientHandler source) {
	invites.put(source, target);
		
	}
}
