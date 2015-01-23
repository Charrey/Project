package Project.networking;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;

import Project.gui.ClientGUI;
import Project.logic.*;

import java.util.Set;
import java.util.HashSet;

public class Client implements Runnable {

	Interpreter inter;
	private Game game;
	// Gui gui;
	String name;

	// Server supports:
	Set<String> sersup;

	Socket sock;
	Set<String> lobby;
	HumanPlayer player;
	String ourname;
	int playerno;

	private BufferedReader in;
	private BufferedWriter out;

	int movetobemade;
	private Scanner scanner;

	int boardwidth;
	int boardheight;

	Map<String, int[]> invites;
	private ClientGUI gui;

	/**
	 * Takes console input and reacts accordingly.
	 */
	public void watchInput() {
		scanner = new Scanner(System.in);
		String gotten;
		while (true) {
			gotten = scanner.nextLine();
			if (gotten.startsWith("accept")) {
				String[] apart = gotten.split("\\s+");
				if (invites.keySet().contains(apart[1])) {
					sendMessage("ACCEPT " + apart[1]);
					boardwidth = invites.get(apart[1])[0];
					boardheight = invites.get(apart[1])[1];
				}
			} else if (gotten.startsWith("decline")) {
				String[] apart = gotten.split("\\s+");
				if (invites.keySet().contains(apart[1])) {
					sendMessage("DECLINE " + apart[1]);
					invites.remove(apart[1]);
				}
			} else if (gotten.startsWith("invite")) {
				String[] apart = gotten.split("\\s+");
				int[] array = {boardwidth,boardheight};
				invites.put(apart[1], array);
				sendMessage("INVITE "+apart[1]);
			} else if (gotten.equals("board")) {
				sendMessage("REQUEST");
			} else if (Server.representsInt(gotten) && gotten.length() >= 1) {
				this.notifyAll();
			} else if (gotten.startsWith("spam")) {
				while (true) {
					sendMessage(gotten.substring(5));
				}
			} else {
				sendMessage(gotten);
			}
		}
	}

	// ****************************************
	// USEFUL COMMANDS TO BE USED BY GUI
	// ****************************************

	/**
	 * Requests the lobby from the server.
	 */
	public void askLobby() {
		sendMessage(inter.kw_lobb_request);
	}

	public void moveok(String arguments) {
		String[] splitted = arguments.split("\\s+");
		if (Integer.parseInt(splitted[0]) == playerno) {
			game.getBoard().putMark(Integer.parseInt(splitted[1]), Mark.X);
		} else {
			game.getBoard().putMark(Integer.parseInt(splitted[1]), Mark.O);
		}
		// Refresh the visual board here!
	}

	public Game getGame() {
		return game;
	}

	public void refreshBoard(String stringArg) {
		String[] splitted = stringArg.split("\\s+");
		game.getBoard().reset(Integer.parseInt(splitted[0]),
				Integer.parseInt(splitted[1]));
		int teller = 0;
		for (int p = 0; p < game.getBoard().getHeight(); p++) {
			for (int i = 0; i < game.getBoard().getWidth(); i++) {

				if (Integer.parseInt(splitted[teller + 2]) == 01) {
					game.getBoard().putMark(i, Mark.X);
				} else if (Integer.parseInt(splitted[teller + 2]) != 0) {
					game.getBoard().putMark(i, Mark.O);
				}
				teller++;
			}
		}
		// Refresh the visual board here!
	}

	/**
	 * Sends a command to the server.
	 * 
	 * @param msg
	 *            is the message to be sent.
	 */
	public void sendMessage(String msg) {
		try {
			gui.addMessage("Sending message to server ("
					+ sock.getInetAddress() + ") : " + msg);
			out.write(msg);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			gui.addMessage("Could not send command (" + msg
					+ ") to server.");
		}
	}

	public void sendMove(int move) {
		sendMessage(inter.kw_game_move + " " + move);
	}

	public Boolean hasFunction(String function) {
		return sersup.contains(function);
	}

	// **********************
	// END OF USEFUL COMMANDS (I think)
	// **********************

	/**
	 * Returns the socket of this Client.
	 * 
	 * @return the Socket of this Client.
	 */
	public Socket getSocket() {
		return sock;
	}

	// Constructor, obviously
	/**
	 * @param address
	 *            is the address used to create a Socket.
	 * @param port
	 *            is the port used to create a Socket.
	 * @param name
	 *            is the name of this client.
	 * @param gui is the gui associated with this client.
	 */
	public Client(String address, int port, String name, ClientGUI gui) {
		this.gui=gui;
		this.name = name;
		try {
			sock = new Socket(address, port);
		} catch (IOException e1) {
			gui.addMessage("Could not create socket with server.");
			System.exit(0);
		}
		inter = new Interpreter(this);
		try {
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(
					sock.getOutputStream()));
		} catch (IOException alpha) {
			gui.addMessage("Error while opening streams");
			System.exit(0);
		}
		sersup = new HashSet<String>();
		invites = new HashMap<String, int[]>();
		sendMessage("CONNECT " + this.name + " CUSTOM_BOARD_SIZE");
	}

	/**
	 * Logs the features the server supports.
	 * 
	 * @param features
	 *            is a String containing the features this server supports,
	 *            separated by spaces.
	 */
	public void connectionAccepted(String features) {
		String[] splitted = features.split("\\s+");
		for (int i = 0; i < splitted.length; i++) {
			inter.whatisthatClient(splitted[i]);
		}
		gui.addMessage("CONNECTED");
	}

	public void makemove() {
		movetobemade = ((HumanPlayer) game.getFirstPlayer()).getInputHandler()
				.getMove();
		try {
			out.write("MOVE " + movetobemade + " /n/n");
			out.flush();
		} catch (IOException ex) {
			gui.addMessage("Could not send move to server");
		}
	}

	public void gamestart() {
		player = new HumanPlayer(this.ourname, Mark.X, new InputHandler());
		game = new Game(player, new HumanPlayer("That_pc", Mark.O,
				new NetworkedInputHandler(this)), boardwidth, boardheight);
		// HumanPlayer met networked handler!!!!
	}

	/**
	 * @param function
	 *            is the function to be set.
	 * @param setting
	 *            is whether the function should be enabled(true) or
	 *            disabled(false).
	 */
	public void SetSerSup(String function, Boolean setting) {
		if (sersup.contains(function) && setting == false) {
			sersup.remove(function);
		} else if (!sersup.contains(function) && setting == true) {
			sersup.add(function);
		}
	}

	/**
	 * Sets the lobby to the given argument, then prints the lobby to the
	 * console.
	 * 
	 * @param stringArg
	 *            is the lobby received by the server.
	 */
	public void setLobby(String stringArg) {
		lobby = new HashSet<String>();
		String[] splitted = stringArg.split("\\s+");
		for (int i = 0; i < splitted.length; i++) {
			lobby.add(splitted[i]);
		}
		gui.addMessage("***LOBBY***");
		if (lobby.isEmpty()) {
			gui.addMessage("* Empty lobby :(");
		} else {
			for (String i : lobby) {
				gui.addMessage("* " + i);
			}
		}
		gui.addMessage("***********");
	}

	public BufferedReader getIn() {
		return in;
	}

	public BufferedWriter getOut() {
		return out;
	}

	public void run() {
		try {
			String tussenvar = in.readLine();
			while (!sock.isClosed()) {
				gui.addMessage("Message received from server: " + tussenvar);
				inter.whatisthatClient(tussenvar);
				tussenvar = in.readLine();
			}
		} catch (IOException e) {
			gui.addMessage("Server has shut down.");
		}
		System.exit(0);
	}

	/**
	 * Ends the game.
	 */
	public void gameend() {
		game.gameEnd();
		this.askLobby();
	}

	public void inviteDeclined() {

	}

	/**
	 * Prompts the user for a reaction to being invited to a game.
	 * 
	 * @param other
	 *            are the invite parameters as defined in protocol INF-2 v1.0
	 *            separated by spaces.
	 */
	public void invited(String other) {
		String[] apart = other.split("\\s+");
		int[] dimensions = new int[2];
		if (apart.length < 3) {
			gui.addMessage("You have been invited by " + apart[0]
					+ " for a game of 6 by 7!");
			dimensions[0] = 6;
			dimensions[1] = 7;
		} else {
			gui.addMessage("You have been invited by " + apart[0]
					+ " for a game of " + apart[1] + " by " + apart[2] + "!");
			dimensions[0] = Integer.parseInt(apart[1]);
			dimensions[1] = Integer.parseInt(apart[2]);
		}
		invites.put(apart[0], dimensions);
		gui.addMessage("Type accept " + apart[0] + " to accept.");
		gui.addMessage("Type decline " + apart[0] + " to decline.");
	}
}
