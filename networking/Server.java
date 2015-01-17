package Project.networking;

import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import Project.logic.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Server {

	// Set of string contains features
	public Map<ClientHandler, Set<String>> lobby;

	public Set<Game> gamesgames;
	Interpreter interpreter;
	ServerSocket serversocket;
	
	public Map<ClientHandler, String[]> invites; //First string = target, second = toString(width), third = toString(height)

	//public Map<ClientHandler, ClientHandler> invites; THIS DOES NOT SUPPORT CBOARDSIZE

	public static void main(String[] args) throws IOException {
		Server server = new Server(49999);
		while (true) {
			System.out.println("Port 49999 has been opened");
			Socket socket = server.getServerSocket().accept();
			System.out.println("Connection from " + socket.getInetAddress()
					+ " accepted");
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
		invites = new HashMap<ClientHandler, String[]>();
	}

	public void joinServer(ClientHandler client, Set<String> features) {
		lobby.put(client, features);
	}

	public void handlechatmessage(ClientHandler source, String message) {

	}

	public void acceptConnection(ClientHandler source, String features) {
		System.out.println("Accepted connection command from "
				+ source.getClientName());
		String[] splitted = features.split("\\s+");
		source.setClientName(splitted[0]);
		System.out.println(splitted[0] + " ("
				+ source.getSocket().getInetAddress() + ") has connected.");
		joinServer(source, new HashSet<String>());
		for (int i = 1; i < splitted.length; i++) {
			interpreter.whatisthatServer(splitted[i], source, true);
		}
		source.sendCommand(interpreter.kw_conn_acceptconnect
				+ " CHAT CUSTOM_BOARD_SIZE");
		sendLobby(source);
	}

	public void sendError(ClientHandler target, String msg) {
		target.sendCommand("ERROR " + msg);
	}

	public void sendBoard(ClientHandler source) {
		for (Game i : gamesgames) {
			ClientHandler one = ((NetworkedInputHandler) ((HumanPlayer) i
					.getFirstPlayer()).getInputHandler()).getClientHandler();
			ClientHandler two = ((NetworkedInputHandler) ((HumanPlayer) i
					.getSecondPlayer()).getInputHandler()).getClientHandler();
			if (one.equals(source) || two.equals(source)) {
				source.sendCommand(i.getBoard().networkBoard());
			} else {
				source.sendCommand(interpreter.kw_conn_error + " NotIngame");
			}
		}
	}

	public void sendLobby(ClientHandler source) {
		String result = "";
		for (ClientHandler i : lobby.keySet()) {
			result += " " + i.getClientName();
		}
		source.sendCommand(interpreter.kw_conn_lobby + result);

	}

	public void sendLeaderboard() {
		//
		// IMPLEMENT LEADERBOARD IF WE WANT TO
		//

	}

	public void setFunction(ClientHandler source, String function, Boolean bool) {
		System.out.println("Function " + function + " set to " + bool
				+ " for client " + source.getClientName());
		if (lobby.get(source).contains(function) && bool == false) {
			lobby.get(source).remove(function);
		} else if (!lobby.get(source).contains(function) && bool == true) {
			lobby.get(source).add(function);
		}
	}

	public ClientHandler findClientHandler(String name) {
		for (ClientHandler i : lobby.keySet()) {
			if (i.getClientName().equals(name)) {
				return i;
			}
		}
		return null;
	}
	
	public Boolean representsInt(String thestring) {
		char[] array = thestring.toCharArray();
		for (int i = 0; i<array.length; i++) {
			if (!Character.isDigit(i)) {
				return false;
			}
		}
		return true;
	}
	
	
	public String validInvite(ClientHandler source, String arguments) {
		String[] apart = arguments.split("\\s+");
		if (apart.length < 2) {
			return interpreter.kw_conn_error + " SyntaxError";
//		} else if (!representsInt(apart[2]) || !representsInt(apart[3])) {
//			return interpreter.kw_conn_error + " SyntaxError";
		} else if (findClientHandler(apart[1]) == null) {
			return interpreter.kw_conn_error + " NoSuchClient";
		} else  if (!invites.containsKey(findClientHandler(apart[1]))) {
			return interpreter.kw_conn_error + " NoOpenInvite";
		} else {
			return "true";
		}
	}
	
	
	public void acceptinvite(ClientHandler source, String arguments) {
	
		String[] apart = arguments.split("\\s+");
		if (!validInvite(source, arguments).equals("true")) {
			source.sendCommand(validInvite(source, arguments));
		}
		
		else {
			ClientHandler invitesource = findClientHandler(apart[1]);
			int boardwidth = Integer.parseInt(invites.get(invitesource)[2]);
			int boardheight = Integer.parseInt(invites.get(invitesource)[2]);
			
			
			findClientHandler(apart[1]).sendCommand(
					interpreter.kw_conn_gamestart + " " + source.getClientName()
							+ " " + apart[1]);
			source.sendCommand(interpreter.kw_conn_gamestart + " " + source.getClientName() + " " +apart[1]);
			StartGame(source, findClientHandler(apart[1]), boardwidth, boardheight);
		}
	}
	
	public void denyinvite(ClientHandler source, String arguments) {
		if (!validInvite(source, arguments).equals("true")) {
			source.sendCommand(validInvite(source, arguments));
		} else {
			String[] apart = arguments.split("\\s+");
			System.out.println("Invite removed due to decline: from "+apart[1]+" to "+source.getClientName());
			invites.remove(findClientHandler(apart[1]));
		}
		
	}

	public void StartGame(ClientHandler playerone, ClientHandler playertwo, int width, int height) {
		gamesgames.add(new Game(new HumanPlayer(playerone.getClientName(),
				Mark.X, new NetworkedInputHandler(playerone)), new HumanPlayer(
				playertwo.getClientName(), Mark.O, new NetworkedInputHandler(
						playertwo)), width, height));
	playerone.sendCommand(interpreter.kw_game_reqmove);
	}

	public void invite(String targetandxy, ClientHandler source) {
		String[] apart = targetandxy.split("\\s+");
		if (apart.length != 3) {
			source.sendCommand(interpreter.kw_conn_error + " BadInviteSyntax");
		} else if (findClientHandler(apart[0]) == null) {
			source.sendCommand(interpreter.kw_conn_error + " NoSuchPlayer");
		} else if (invites.containsKey(source)) {
			source.sendCommand(interpreter.kw_conn_error + " AlreadyInvited");
		
		} else {
			invites.put(source, apart);
			findClientHandler(apart[0]).sendCommand(
					interpreter.kw_conn_invite + " " + source.getClientName()
							+ " " + apart[1] + " " + apart[2]);
		}
	}
}
