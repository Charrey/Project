package Project.networking;

import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import Project.gui.ServerGUI;
import Project.logic.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Server extends Thread {

	// Set of string contains features
	public Map<ClientHandler, Set<String>> lobby;
	public Map<ClientHandler, Boolean> playing;
	public Map<Game, Integer> gamesgames;
	Interpreter interpreter;
	ServerSocket serversocket;
	private boolean running;
	private int portNumber;
	private ServerGUI gui;

	public Map<ClientHandler, String[]> invites; // First string = target,
													// second = toString(width),
													// third = toString(height)

	// public Map<ClientHandler, ClientHandler> invites; THIS DOES NOT SUPPORT
	// CBOARDSIZE
	/*
	 * public static void main(String[] args) throws IOException { Server server
	 * = new Server(49999); while (true) {
	 * System.out.println("Port 49999 has been opened"); Socket socket =
	 * server.getServerSocket().accept(); System.out.println("Connection from "
	 * + socket.getInetAddress() + " accepted"); ClientHandler clienthandler =
	 * server.addClientHandler(socket); clienthandler.start(); }
	 * 
	 * }
	 */
	Scanner scanner;

	public void watchInput() {
		scanner = new Scanner(System.in);
		String gotten;
		gotten = scanner.nextLine();
		while (true) {
			gui.addMessage("Read command: "+gotten);
			String[] splitted = gotten.split("\\s+");
			if (splitted[0].equals("kick")) {
				try {
					findClientHandler(gotten.substring(5)).getSocket().close();
				} catch (IOException e) {
					gui.addMessage("Could not kick this player.");
				}
			} else if (splitted[0].equals("error")) {
				sendError(findClientHandler(splitted[1]),gotten.substring(6+splitted[1].length()));
			} else if (splitted[0].equals("help")) {
				gui.addMessage("----HELP--------------");
				gui.addMessage("kick <name> -- Kick a player");
				gui.addMessage("error <name> <error> -- Send an error");
				gui.addMessage("hello <name> <message> -- Send a message");
				gui.addMessage("----------------------");
			} else if (splitted[0].equals("hello")) {
				findClientHandler(splitted[1]).sendCommand("CHAT "+gotten.substring(6+splitted[1].length()));
			} else {
				gui.addMessage("Use 'help' to view console commands.");
			}
			gotten = scanner.nextLine();
		}
	}

	@Override
	public void run() {
		while (running) {
			try {
				gui.addMessage("Port " + portNumber + " has been opened");
				Socket socket = getServerSocket().accept();
				gui.addMessage("Connection from " + socket.getInetAddress()
						+ " accepted");
				ClientHandler clienthandler = addClientHandler(socket);
				clienthandler.start();
			} catch (IOException e) {
				gui.addMessage("Something wrong went O_O");
			}
		}

	}

	/**
	 * @param sockArg
	 *            is the Socket this client is connected with.
	 * @return a new ClientHandler which represents the client associated with
	 *         this socket.
	 */
	public ClientHandler addClientHandler(Socket sockArg) {
		ClientHandler clienthandler = new ClientHandler(this, sockArg);
		return clienthandler;
	}

	/**
	 * Gives the ServerSocket of this server.
	 * 
	 * @return the ServerSocket of this server.
	 */
	public ServerSocket getServerSocket() {
		return serversocket;
	}

	/**
	 * Creates a new Server.
	 * 
	 * @param port
	 *            is the port this server accepts new clients on.
	 * @param gui
	 *            is the gui this server uses as output console.
	 */
	public Server(int port, ServerGUI gui) {
		try {
			this.gui = gui;
			portNumber = port;
			lobby = new HashMap<ClientHandler, Set<String>>();
			playing = new HashMap<ClientHandler, Boolean>();
			gamesgames = new HashMap<Game, Integer>();
			serversocket = new ServerSocket(portNumber);
			interpreter = new Interpreter(this);
			invites = new HashMap<ClientHandler, String[]>();
			running = true;
			this.start();
		} catch (IOException e) {
			gui.addMessage("Server couldn't be setup");
		}
	}

	/**
	 * Adds a ClientHandler to the lobby and refreshes the lobby for every
	 * player.
	 * 
	 * @param client
	 *            is the ClientHandler who's joining the server.
	 * @param features
	 *            is a set of features approved by the interpreter.
	 */
	public void joinServer(ClientHandler client, Set<String> features) {
		lobby.put(client, features);
		playing.put(client, false);
		for (ClientHandler i : lobby.keySet()) {
			if (!playing.get(i)) {
				sendLobby(i);
			}
		}
	}

	/**
	 * 
	 * To do
	 * 
	 * @param source
	 * @param message
	 */
	public void handlechatmessage(ClientHandler source, String message) {

	}

	/**
	 * Plays a move for a ClientHandler if it's valid.
	 * 
	 * @param source
	 *            is the ClientHandler who sent this move.
	 * @param move
	 *            is a String-representation of a move.
	 */
	public void nextMove(ClientHandler source, String move) {
		Game game = getGame(source);
		if (game == null) {
			sendError(source, "NotInGame");
		} else {

			ClientHandler opponent = getOpponent(source);

			if (!representsInt(move)) {
				sendError(source, "SyntaxError");
			} else if (gamesgames.get(game) != source.getPlayerno()) {
				sendError(source, "NotUrTurn");
			} else if (!game.getBoard().isValidInput(Integer.parseInt(move))
					|| !game.getBoard().columnFree(Integer.parseInt(move))) {
				sendError(source, "BadMove");
			} else {
				game.getBoard().putMark(Integer.parseInt(move),
						Mark.fromInt(source.getPlayerno()));
				source.sendCommand(Interpreter.kw_game_moveok + " "
						+ source.getPlayerno() + " " + Integer.parseInt(move)
						+ " " + source.getClientName());
				opponent.sendCommand(Interpreter.kw_game_moveok + " "
						+ source.getPlayerno() + " " + Integer.parseInt(move)
						+ " " + source.getClientName());
				if (game.getBoard().isWin()) {
					source.sendCommand(Interpreter.kw_game_gameend + " WIN "
							+ source.getClientName());
					opponent.sendCommand(Interpreter.kw_game_gameend + " WIN "
							+ source.getClientName());
					playing.put(source, false);
					playing.put(opponent, false);
					gamesgames.remove(game);
				} else if (game.getBoard().isFull()) {
					source.sendCommand(Interpreter.kw_game_gameend + " DRAW");
					opponent.sendCommand(Interpreter.kw_game_gameend + " DRAW");
					playing.put(source, false);
					playing.put(opponent, false);
					gamesgames.remove(game);
				} else {
					opponent.sendCommand(Interpreter.kw_game_reqmove);
					gamesgames.put(game, opponent.getPlayerno());
					gui.addMessage("Move by " + source.getClientName() + ": "
							+ move);
					// System.out.println(game.getBoard().networkBoard());
				}
			}
		}

	}

	/**
	 * Gives the ServerGUI this server is using.
	 * 
	 * @return the ServerGUI this server is using.
	 */
	public ServerGUI getGUI() {
		return gui;
	}

	/**
	 * Finds the Opponent of an in-game ClientHandler. *
	 * 
	 * @param friend
	 *            is the ClientHandler who's playing against the ClientHandler
	 *            we're attempting to find.
	 * @return the ClientHandler-opponent of 'friend' or null if he's not
	 *         in-game.
	 */
	public ClientHandler getOpponent(ClientHandler friend) {
		Game game = getGame(friend);
		if (game == null) {
			gui.addMessage("critical error in getGame");
		}
		for (ClientHandler i : lobby.keySet()) {
			if (getGame(i).equals(game) && !i.equals(friend)) {
				return i;
			}
		}
		return null;
	}

	/**
	 * Finds the Game a certain ClientHandler is playing.
	 * 
	 * @param clienthandler
	 *            is the ClientHandler that's in the game we're looking for.
	 * @return the Game this ClientHandler is in, or null if he's not in-game.
	 */
	public Game getGame(ClientHandler clienthandler) {
		for (Game i : gamesgames.keySet()) {
			ClientHandler one = ((NetworkedInputHandler) ((HumanPlayer) i
					.getFirstPlayer()).getInputHandler()).getClientHandler();
			ClientHandler two = ((NetworkedInputHandler) ((HumanPlayer) i
					.getSecondPlayer()).getInputHandler()).getClientHandler();
			if (one.equals(clienthandler) || two.equals(clienthandler)) {
				return i;
			}
		}
		return null;
	}

	/**
	 * Accepts a connection from a certain ClientHandler, and adds him to the
	 * lobby.
	 * 
	 * @param source
	 *            is the ClientHandler who's trying to connect.
	 * @param features
	 *            is a String of all features the client supports, separated by
	 *            spaces.
	 */
	public void acceptConnection(ClientHandler source, String features) {
		gui.addMessage("Accepted connection command from "
				+ source.getClientName());
		String[] splitted = features.split("\\s+");
		for (ClientHandler i : lobby.keySet()) {
			if (i.getClientName().equals(splitted[0])) {
				gui.addMessage("Connection denied due to duplicate name from "+source.getClientName());
				return;
			}
		}
		source.setClientName(splitted[0]);
		gui.addMessage(splitted[0] + " (" + source.getSocket().getInetAddress()
				+ ") has connected.");
		joinServer(source, new HashSet<String>());
		for (int i = 1; i < splitted.length; i++) {
			interpreter.whatisthatServer(splitted[i], source, true);
		}
		source.sendCommand(interpreter.kw_conn_acceptconnect
				+ " CHAT CUSTOM_BOARD_SIZE");
	}

	/**
	 * Sends an error message to the specified ClientHandler.
	 * 
	 * @param target
	 *            the destination of this error message.
	 * @param msg
	 *            the description of this error.
	 */
	public void sendError(ClientHandler target, String msg) {
		target.sendCommand(interpreter.kw_conn_error + " " + msg);
	}

	public void sendBoard(ClientHandler source) {
		if (getGame(source) != null) {
			source.sendCommand(getGame(source).getBoard().networkBoard());
		} else {
			sendError(source, "NotIngame");
		}

	}

	/**
	 * Sends a lobby to the specified ClientHandler. *
	 * 
	 * @param target
	 *            is the ClientHanlder the lobby will be sent to.
	 */
	public void sendLobby(ClientHandler target) {
		String result = "";
		for (ClientHandler i : lobby.keySet()) {
			if (!playing.get(i)) {
				result += " " + i.getClientName();
			}
		}
		target.sendCommand(interpreter.kw_conn_lobby + result);

	}

	public void sendLeaderboard() {
		//
		// IMPLEMENT LEADERBOARD IF WE WANT TO
		//

	}

	/**
	 * Enables or disables a function in the log for a certain client.
	 * 
	 * @param source
	 *            is the ClientHandler who declared to support a function.
	 * @param function
	 *            is the String-form of a function. Must be part of the
	 *            Interpreter.feature String tree.
	 * @param bool
	 *            is whether the function must be enabled or disabled.
	 */
	public void setFunction(ClientHandler source, String function, Boolean bool) {
		gui.addMessage("Function " + function + " set to " + bool
				+ " for client " + source.getClientName());
		if (lobby.get(source).contains(function) && bool == false) {
			lobby.get(source).remove(function);
		} else if (!lobby.get(source).contains(function) && bool == true) {
			lobby.get(source).add(function);
		}
	}

	/**
	 * @param name
	 *            is the name of the Client to be found.
	 * @return the ClientHandler with the given name, or null if there's no such
	 *         ClientHandler.
	 */
	public ClientHandler findClientHandler(String name) {
		for (ClientHandler i : lobby.keySet()) {
			if (i.getClientName().equals(name)) {
				return i;
			}
		}
		return null;
	}

	/**
	 * Checks whether a String represents a numeral. That is, every char of the
	 * string is a digit.
	 * 
	 * @param thestring
	 *            is the String to be examined.
	 * @return true if the String represents a number, false if it doesn't.
	 */
	public static Boolean representsInt(String thestring) {
		char[] array = thestring.toCharArray();
		for (int i = 0; i < array.length; i++) {
			if (!Character.isDigit(array[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks whether an ACCEPT_INVITE or DENY_INVITE is viable.
	 * 
	 * @param source
	 *            is the ClientHandler who's 'responding' to an invite.
	 * @param arguments
	 *            is "INVITE " + the name of the invitation source.
	 * @return is "true" if the command is viable, or an error command if it's
	 *         not.
	 */
	public String validInvite(ClientHandler source, String arguments) {
		String[] apart = arguments.split("\\s+");
		if (apart.length < 2) {
			return interpreter.kw_conn_error + " SyntaxError";
			// } else if (!representsInt(apart[2]) || !representsInt(apart[3]))
			// {
			// return interpreter.kw_conn_error + " SyntaxError";
		} else if (findClientHandler(apart[1]) == null) {
			return interpreter.kw_conn_error + " NoSuchClient";
		} else if (!invites.containsKey(findClientHandler(apart[1]))) {
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
			invites.remove(invitesource);			
			for (ClientHandler i : invites.keySet()) {
				if (i.equals(source)) {
					invites.remove(i);
				}
				else if (invites.get(i)[0].equals(source.getClientName())) {
					i.sendCommand(Interpreter.kw_lobb_declineinvite+" "+source.getClientName());
				}
			}			
			findClientHandler(apart[1]).sendCommand(
					interpreter.kw_conn_gamestart + " "
							+ source.getClientName() + " " + apart[1]);
			source.sendCommand(interpreter.kw_conn_gamestart + " "
					+ source.getClientName() + " " + apart[1]);
			source.setPlayerno(1);
			invitesource.setPlayerno(2);
			StartGame(source, findClientHandler(apart[1]), boardwidth,
					boardheight);
		}
	}

	/**
	 * @param arguments
	 */
	public void denyinvite(ClientHandler source, String arguments) {
		if (!validInvite(source, arguments).equals("true")) {
			source.sendCommand(validInvite(source, arguments));
		} else {
			String[] apart = arguments.split("\\s+");
			gui.addMessage("Invite removed due to decline: from " + apart[1]
					+ " to " + source.getClientName());
			invites.remove(findClientHandler(apart[1]));
		}

	}

	public void StartGame(ClientHandler playerone, ClientHandler playertwo,
			int width, int height) {
		gamesgames.put(new Game(new HumanPlayer(playerone.getClientName(),
				Mark.X, new NetworkedInputHandler(playerone)), new HumanPlayer(
				playertwo.getClientName(), Mark.O, new NetworkedInputHandler(
						playertwo)), width, height), 1);
		playerone.sendCommand(Interpreter.kw_game_reqmove);
		playing.put(playerone, true);
		playing.put(playertwo, true);
	}

	/**
	 * Checks if an invite is viable, then records the invite and notifies the
	 * invited player.
	 * 
	 * @param targetandxy
	 *            is a String containing the name of the invitation target and
	 *            the coordinates of the game he/she is invited for, separated
	 *            by spaces.
	 * @param source
	 *            is the ClientHandler who sent the invite.
	 */
	public void invite(String targetandxy, ClientHandler source) {
		String[] apart = targetandxy.split("\\s+");
		if (apart.length != 3 || !representsInt(apart[1])
				|| !representsInt(apart[2])) {
			sendError(source, "BadInviteSyntax");
		} else if (Integer.parseInt(apart[1]) < 1
				|| Integer.parseInt(apart[2]) < 1) {
			sendError(source, "InvalidBounds");
		} else if (findClientHandler(apart[0]) == null) {
			sendError(source, "NoSuchPlayer");
		} else if (playing.get((findClientHandler(apart[0])))) {
			sendError(source, "AlreadyIngame");
		} else if (apart[0].equals(source.getClientName())) {
			sendError(source, "SelfInvite");
		} else if (invites.containsKey(source)) {
			sendError(source, "AlreadyInvited");

		} else {
			invites.put(source, apart);
			findClientHandler(apart[0]).sendCommand(
					interpreter.kw_lobb_invite + " " + source.getClientName()
							+ " " + apart[1] + " " + apart[2]);
		}
	}
}
