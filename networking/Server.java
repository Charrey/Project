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
import java.math.BigInteger;

public class Server extends Thread {

	// Set of string contains features
	private Map<ClientHandler, Set<String>> lobby;
	private Map<ClientHandler, Boolean> playing;
	private Map<Game, Integer> gamesgames;
	private Interpreter interpreter;
	private ServerSocket serversocket;
	private boolean running;
	private ServerGUI gui;
	private Scanner scanner;
	public Map<ClientHandler, String[]> invites;

	/*@
	 pure
	 ensures \result.equals(lobby);
	 @*/
	/**
	 * Gives the lobby keeping track of ClientHandlers and their features.
	 * 
	 * @return the lobby
	 */
	public Map<ClientHandler, Set<String>> getLobby() {
		return lobby;
	}

	/*@
	 pure
	 ensures \result.equals(playing);
	 @*/
	/**
	 * Gives the map keeping track which ClientHandler is playing and which isn't
	 * 
	 * @return the map with information on who's playing.
	 */
	public Map<ClientHandler, Boolean> getPlaying() {
		return playing;
	}

	/*@
	 pure
	 ensures \result.equals(gamesgames);
	 @*/
	/**
	 * Gives the map keeping track of all games an the player whose turn it is.
	 * 
	 * @return the map of games.
	 */
	public Map<Game, Integer> getGames() {
		return gamesgames;
	}

	/*@
	 pure
	 ensures \result.equals(interpreter);
	 @*/
	/**
	 * Gives the Interpreter associated with this server.
	 * 
	 * @return the Interpreter.
	 */
	public Interpreter getInterpreter() {
		return interpreter;
	}

	
	/**
	 * Watches the console for human input, for administration purposes. 
	 */
	public void watchInput() {
		scanner = new Scanner(System.in);
		String gotten;
		gotten = scanner.nextLine();
		/*@
		 loop_invariant true == true;
		 @*/
		while (true) {
			printMessage("Read command: " + gotten);
			String[] splitted = gotten.split("\\s+");

			switch (splitted[0]) {
			case "kick":
				try {
					findClientHandler(gotten.substring(5)).getSocket().close();
				} catch (IOException e) {
					printMessage("Could not kick this player.");
				}
				break;
			case "error":
				sendError(findClientHandler(splitted[1]),
						gotten.substring(6 + splitted[1].length()));
				break;
			case "help":
				printHelp();
				break;
			case "hello":
				findClientHandler(splitted[1]).sendCommand(
						"CHAT " + gotten.substring(6 + splitted[1].length()));
				break;
			case "clear":
				gui.clearScreen();
				break;
			default:
				printMessage("Use 'help' to view console commands.");
			}
			gotten = scanner.nextLine();
		}
	}
	
	/**
	 * @param message is the message to be printed
	 */
	
	public void printMessage(String message) {
		if (gui == null) {
			System.out.println(message);
		} else {
			gui.addMessage(message);
		}
	}
	
	//@pure
	/**
	 * Prints a help document.
	 */
	public void printHelp() {
		printMessage("----HELP-------------------------------");
		printMessage("");
		printMessage("kick <name> -- Kicks a player");
		printMessage("> Make sure the specified player exists.");
		printMessage("");
		printMessage("error <name> <error> -- Sends an error");
		printMessage("> Make sure the specified player exists.");
		printMessage("");
		printMessage("hello <name> <message> -- Sends a message");
		printMessage("> Make sure the specified player exists.");
		printMessage("");
		printMessage("clear -- Clears your GUI screen");
		printMessage("> Make sure you have a GUI.");
		printMessage("");
		printMessage("help -- Shows this help screen");
		printMessage("");
		printMessage("----DISCLAIMER-------------------------");
		printMessage("It is presumed that you as admin use");
		printMessage("these features correctly. Failure to do");
		printMessage("so may result in inconsistency and/or");
		printMessage("program failure.");
		printMessage("---------------------------------------");
	}

	
	
	//@requires getServerSocket() != null;
	@Override
	public void run() {
		while (running) {
			try {
				printMessage("Port " + serversocket.getLocalPort() + " has been opened");
				Socket socket = getServerSocket().accept();
				printMessage("Connection from " + socket.getInetAddress()
						+ " accepted");
				ClientHandler clienthandler = addClientHandler(socket);
				clienthandler.start();
			} catch (IOException e) {
				printMessage("Server shut down.");
			}
		}

	}

	/**
	 * @param sockArg
	 *            is the Socket this client is connected with.
	 * @return a new ClientHandler which represents the client associated with
	 *         this socket.
	 */
	//@requires sockArg !=null;
	public ClientHandler addClientHandler(Socket sockArg) {
		ClientHandler clienthandler = new ClientHandler(this, sockArg);
		return clienthandler;
	}

	/**
	 * Gives the ServerSocket of this server.
	 * 
	 * @return the ServerSocket of this server.
	 */
	//@pure
	public ServerSocket getServerSocket() {
		return serversocket;
	}

	/**
	 * Creates a TUI server.
	 * 
	 * @param port is the port this server accepts clients from.
	 * @throws IOException if the specified port is preoccupied.
	 */
	//@requires port<65535 && port>0;
	public Server(int port) throws IOException {
			lobby = new HashMap<ClientHandler, Set<String>>();
			playing = new HashMap<ClientHandler, Boolean>();
			gamesgames = new HashMap<Game, Integer>();
			serversocket = new ServerSocket(port);
			interpreter = new Interpreter(this);
			invites = new HashMap<ClientHandler, String[]>();
			running = true;
			printHelp();
			this.start();
	}

	/**
	 * Creates a new Server.
	 * 
	 * @param port
	 *            is the port this server accepts new clients on.
	 * @param gui
	 *            is the gui this server uses as output console.
	 */
	//@requires port<65535 && port>0;
	public Server(int port, ServerGUI gui) {
		try {
			this.gui = gui;
			lobby = new HashMap<ClientHandler, Set<String>>();
			playing = new HashMap<ClientHandler, Boolean>();
			gamesgames = new HashMap<Game, Integer>();
			serversocket = new ServerSocket(port);
			interpreter = new Interpreter(this);
			invites = new HashMap<ClientHandler, String[]>();
			running = true;
			printHelp();
			this.start();
		} catch (IOException e) {
			printMessage("Server couldn't be setup");
		}
	}

	/**
	 * Adds a ClientHandler to the lobby and refreshes the lobby for every
	 * player.
	 * 
	 * @param client
	 *            is the ClientHandler who's joining the server.
	 * @param features
	 *            is a set of features approved by the Interpreter.
	 */
	//@requires client !=null && features!=null;
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
	 * Handles a chat message request.
	 * 
	 * @param source is the Clienthandler who sent the chat message.
	 * @param message is the message sent.
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
	//requires source !=null && move!=null;
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
				source.sendCommand(Interpreter.KW_GAME_MOVEOK + " "
						+ source.getPlayerno() + " " + Integer.parseInt(move)
						+ " " + source.getClientName());
				opponent.sendCommand(Interpreter.KW_GAME_MOVEOK + " "
						+ source.getPlayerno() + " " + Integer.parseInt(move)
						+ " " + source.getClientName());
				if (game.getBoard().isWin()) {
					source.sendCommand(Interpreter.KW_GAME_GAMEEND + " WIN "
							+ source.getClientName());
					opponent.sendCommand(Interpreter.KW_GAME_GAMEEND + " WIN "
							+ source.getClientName());
					playing.put(source, false);
					playing.put(opponent, false);
					gamesgames.remove(game);
				} else if (game.getBoard().isFull()) {
					source.sendCommand(Interpreter.KW_GAME_GAMEEND + " DRAW");
					opponent.sendCommand(Interpreter.KW_GAME_GAMEEND + " DRAW");
					playing.put(source, false);
					playing.put(opponent, false);
					gamesgames.remove(game);
				} else {
					opponent.sendCommand(Interpreter.KW_GAME_REQMOVE);
					gamesgames.put(game, opponent.getPlayerno());
					printMessage("Move by " + source.getClientName() + ": "
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
	/*@ pure
	 	ensures \result.equals(gui);
	 @*/
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
	//@requires friend != null;
	public ClientHandler getOpponent(ClientHandler friend) {
		Game game = getGame(friend);
		if (game == null) {
			printMessage("Game query: Client not in-game.");
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
	//@pure
	//@ensures \return == 0;
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
	//@requires source!=null && features !=null;
	public void acceptConnection(ClientHandler source, String features) {
		printMessage("Accepted connection command from "
				+ source.getClientName());
		String[] splitted = features.split("\\s+");
		//loop_invariant lobby.keySet().contains(i);
		for (ClientHandler i : lobby.keySet()) {
			if (i.getClientName().equals(splitted[0])) {
				printMessage("Connection denied due to duplicate name from "
						+ source.getClientName());
				sendError(source, "DuplicateName");
				return;
			}
		}
		source.setClientName(splitted[0]);
		printMessage(splitted[0] + " (" + source.getSocket().getInetAddress()
				+ ") has connected.");
		joinServer(source, new HashSet<String>());
		for (int i = 1; i < splitted.length; i++) {
			interpreter.whatisthatServer(splitted[i], source, true);
		}
		source.sendCommand(Interpreter.KW_CONN_ACCEPTCONNECT
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
	//@requires target != null;
	public void sendError(ClientHandler target, String msg) {
		target.sendCommand(Interpreter.KW_CONN_ERROR + " " + msg);
	}

	/**
	 * @param source is the ClientHandler to send the board to.
	 */
	//requires source != null;
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
	//@ requires target != null;
	public void sendLobby(ClientHandler target) {
		String result = "";
		for (ClientHandler i : lobby.keySet()) {
			if (!playing.get(i)) {
				result += " " + i.getClientName();
			}
		}
		target.sendCommand(Interpreter.KW_CONN_LOBBY + result);

	}

	/**
	 * Does nothing yet.
	 */
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
	//@requires source != null;
	//@ensures bool == false ==> getLobby().get(source).contains(function);
	//@ensures bool == true ==> !getLobby().get(source).contains(function);
	public void setFunction(ClientHandler source, String function, Boolean bool) {
		printMessage("Function " + function + " set to " + bool
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
	//@pure
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
	/*@requires thestring != null;
	ensures ((\forall char p; thestring.contains(String.valueOf(p)); Character.isDigit(p)) && thestring.length()<=(int)(Math.log10(Integer.MAX_VALUE)+1) && Long.parseLong(thestring)<Integer.MAX_VALUE) ==> \result == true;
	@*/ 
	public static Boolean representsInt(String thestring) {
		char[] array = thestring.toCharArray();
		for (int i = 0; i < array.length; i++) {
			if (!Character.isDigit(array[i])) {
				return false;
			}
		}
		return (new BigInteger(thestring).compareTo(new BigInteger(Integer
				.toString(Integer.MAX_VALUE))) <= 0);
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
	//@requires source !=null;
	//@ensures \result.equals(true) || \result.startsWith(Interpreter.KW_CONN_ERROR);
	public String validInvite(ClientHandler source, String arguments) {
		String[] apart = arguments.split("\\s+");
		if (apart.length < 1) {
			return Interpreter.KW_CONN_ERROR + " SyntaxError";
			// } else if (!representsInt(apart[2]) || !representsInt(apart[3]))
			// {
			// return Interpreter.KW_CONN_ERROR + " SyntaxError";
		} else if (findClientHandler(apart[0]) == null) {
			return Interpreter.KW_CONN_ERROR + " NoSuchClient";
		} else if (!invites.containsKey(findClientHandler(apart[0]))) {
			return Interpreter.KW_CONN_ERROR + " NoOpenInvite";
		} else {
			return "true";
		}
	}

	/**
	 * @param source is the ClientHandler whose associated client sent
	 * 		this accept command.
	 * @param arguments is the name of the inviter, given the client
	 * 		is using the command correctly.
	 */
	//@requires source != null;
	//@ensures validInvite(source,arguments).equeals("true") ==> getGames().size() = \old(getGames()).size() + 1;
	public void acceptinvite(ClientHandler source, String arguments) {
		String[] apart = arguments.split("\\s+");
		if (!validInvite(source, arguments).equals("true")) {
			source.sendCommand(validInvite(source, arguments));
		}

		else {
			ClientHandler invitesource = findClientHandler(apart[0]);
			int boardwidth = Integer.parseInt(invites.get(invitesource)[1]);
			int boardheight = Integer.parseInt(invites.get(invitesource)[2]);
			invites.remove(invitesource);
			for (ClientHandler i : invites.keySet()) {
				if (i.equals(source)) {
					invites.remove(i);
				} else if (invites.get(i)[0].equals(source.getClientName())) {
					i.sendCommand(Interpreter.KW_LOBB_DECLINEINVITE + " "
							+ source.getClientName());
				}
			}
			findClientHandler(apart[0]).sendCommand(
					Interpreter.KW_CONN_GAMESTART + " "
							+ source.getClientName() + " " + apart[0]);
			source.sendCommand(Interpreter.KW_CONN_GAMESTART + " "
					+ source.getClientName() + " " + apart[0]);
			source.setPlayerno(1);
			invitesource.setPlayerno(2);
			StartGame(source, findClientHandler(apart[0]), boardwidth,
					boardheight);
		}
	}

	/**
	 * Registers a declination of an invite.
	 * 
	 * @param source is the ClientHandler whose associated client sent
	 * 		this decline command.
	 * 
	 * @param arguments arguments is the name of the inviter, given the client
	 * 		is using the command correctly.
	 */
	//@pure
	//@requires source != null;
	//@requires validInvite(source,arguments);
	public void denyinvite(ClientHandler source, String arguments) {
		if (!validInvite(source, arguments).equals("true")) {
			source.sendCommand(validInvite(source, arguments));
		} else {
			String[] apart = arguments.split("\\s+");
			printMessage("Invite removed due to decline: from " + apart[1]
					+ " to " + source.getClientName());
			invites.remove(findClientHandler(apart[1]));
		}

	}

	//@requires playerone!= null && playertwo != null && width > 0 && height > 0;
	public void StartGame(ClientHandler playerone, ClientHandler playertwo,
			int width, int height) {
		printMessage("Start of startgame");
		gamesgames.put(new Game(new HumanPlayer(playerone.getClientName(),
				Mark.X, new NetworkedInputHandler(playerone)), new HumanPlayer(
				playertwo.getClientName(), Mark.O, new NetworkedInputHandler(
						playertwo)), width, height, true), 1);
		playerone.sendCommand(Interpreter.KW_GAME_REQMOVE);
		playing.put(playerone, true);
		playing.put(playertwo, true);
		printMessage("End of startgame");
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
	//@requires source != null && targetandxy != null;
	public void invite(String targetandxy, ClientHandler source) {
		String[] apart = targetandxy.split("\\s+");
		if (apart.length != 1) {
			if (apart.length != 3 || !representsInt(apart[1])
					|| !representsInt(apart[2])) {
				sendError(source, "BadInviteSyntax");
			} else if (Integer.parseInt(apart[1]) < 1
					|| Integer.parseInt(apart[2]) < 1) {
				sendError(source, "InvalidBounds");
			}
		}
		if (findClientHandler(apart[0]) == null) {
			sendError(source, "NoSuchPlayer");
		} else if (playing.get((findClientHandler(apart[0])))) {
			sendError(source, "AlreadyIngame");
		} else if (apart[0].equals(source.getClientName())) {
			sendError(source, "SelfInvite");
		} else if (invites.containsKey(source)) {
			sendError(source, "AlreadyInvited");
		} else {
			if (apart.length == 1) {
				String[] toput = { apart[0], "7", "6" };
				invites.put(source, toput);
				findClientHandler(apart[0]).sendCommand(
						Interpreter.KW_LOBB_INVITE + " "
								+ source.getClientName() + " 7 6");
			} else {
				if (lobby.get(findClientHandler(apart[0])).contains(
						Interpreter.KW_FEATURE_CBOARDSIZE)) {
					invites.put(source, apart);
					findClientHandler(apart[0]).sendCommand(
							Interpreter.KW_LOBB_INVITE + " "
									+ source.getClientName() + " " + apart[1]
									+ " " + apart[2]);
				} else {
					sendError(source, "TargetNoCBoardSize");
				}
			}
		}
	}

	/**
	 * Shuts down the server.
	 */
	//@requires getLobby() != null;
	public void shutDown() {
		running=false;
		if (serversocket != null) {
			try {
				serversocket.close();
			} catch (IOException e) {
				printMessage("Could not close serversocket");
			}
		}
		while(lobby.size()>0) {
			for (ClientHandler i : lobby.keySet()) {
				i.shutdown();
				break;
			}
		}
	if (gui==null) {
		printMessage("No GUI detected, terminating program.");
		System.exit(0);
	} else {
		printMessage("Presence of GUI detected: Not terminating program.");
	}
	}
}
