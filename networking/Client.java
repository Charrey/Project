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
import Project.gui.MainGui;
import Project.gui.game.GameMainPanel;
import Project.logic.*;

import java.util.Set;
import java.util.HashSet;

public class Client extends Thread {

	private boolean scannerfordeterminingmoves;
	protected Interpreter inter;
	private Game game;
	protected String name;

	// Server supports:
	private Set<String> sersup;

	protected Socket sock;
	protected Set<String> lobby;
	private HumanPlayer player;
	private int playerno;

	private BufferedReader in;
	private BufferedWriter out;

	private int movetobemade;
	private Scanner scanner;

	private int boardwidth;
	private int boardheight;

	private Map<String, int[]> invites;
	private ClientGUI gui;

	public Scanner getScanner() {
		return scanner;
	}

	public void resumeScanner() {
		scanner.close();
		scanner = new Scanner(System.in);
	}

	/**
	 * Takes console input and reacts accordingly.
	 */
	public void watchInput() {
		if (gui == null) {
			if (scanner != null) {
				scanner.close();
			}
			scanner = new Scanner(System.in);
		}
		String gotten;
		String[] apart;
		while (true) {
			if (gui == null) {
				gotten = scanner.nextLine();
			} else {
				gotten = gui.waitForCommand();
			}
			apart = gotten.split("\\s+");
			if (!scannerfordeterminingmoves) {
				switch (apart[0]) {
				case "accept":
					if (invites.keySet().contains(apart[1])) {
						sendMessage(Interpreter.KW_LOBB_ACCEPTINVITE + " "
								+ apart[1]);
						// System.out.println("boardwidth set to "+
						// invites.get(apart[1])[0] + " in 70 for "+ name);
						// System.out.println("boardheight set to "+
						// invites.get(apart[1])[1] + " in 71" + name);
						boardwidth = invites.get(apart[1])[0];
						boardheight = invites.get(apart[1])[1];
					}
					break;
				case "decline":
					if (invites.keySet().contains(apart[1])) {
						sendMessage("DECLINE " + apart[1]);
						invites.remove(apart[1]);
					}
					break;
				case "invite":
					int[] array = new int[2];
					// System.out.println("Apart length is " + apart.length);
					if (apart.length == 4 && Server.representsInt(apart[1])
							&& Server.representsInt(apart[2])) {
						array[0] = Integer.parseInt(apart[1]);
						array[1] = Integer.parseInt(apart[2]);
					} else if (apart.length == 2) {
						array[0] = 7;
						array[1] = 6;
					}

					invites.put(apart[1], array);
					// System.out.println(apart[1] + " and dimensions " +
					// array[0] + array[1] + " in 97");
					sendMessage("INVITE " + apart[1]);
					break;
				case "board":
					sendMessage(Interpreter.KW_GAME_REQUESTBOARD);
					break;
				case "spam":
					while (true) {
						sendMessage(gotten.substring(5));
					}
				case "help":
					printMessage("----HELP--------------");
					printMessage("help -- view this help screen");
					printMessage("accept <name> -- accept an invite");
					printMessage("decline <name> -- decline an invite");
					printMessage("invite <name> -- invite a player");
					printMessage("----DEBUG-ONLY--------");
					printMessage("board -- refresh the board");
					printMessage("spam <command> -- spam the server");
					printMessage("<command> -- send a command to the server");
					printMessage("----------------------");
					break;
				default:
					sendMessage(gotten);
				}
			} else {
				sendMove(player.determineMove(getGame().getBoard(), gotten,
						scanner));
				UseScannerForDeterminingMoves(false);
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
		sendMessage(Interpreter.KW_LOBB_REQUEST);
	}

	/**
	 * Places a mark when a move is accepted by the server
	 * 
	 * @param arguments
	 *            contains both the player number and the move, separated by a
	 *            space.
	 */
	public void moveok(String arguments) {
		String[] splitted = arguments.split("\\s+");
		if (Integer.parseInt(splitted[0]) == playerno) {
			game.getBoard().putMark(Integer.parseInt(splitted[1]), Mark.X);
		} else {
			game.getBoard().putMark(Integer.parseInt(splitted[1]), Mark.O);
		}
		// Refresh the visual board here!
	}

	/**
	 * @return the Game this client is playing
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Refreshes the board based on server
	 * 
	 * @param stringArg
	 *            is the board in INF-2 protocol format.
	 */
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
			printMessage("Sending message to server (" + sock.getInetAddress()
					+ ") : " + msg);
			out.write(msg);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			printMessage("Could not send command (" + msg + ") to server.");
		}
	}

	/**
	 * @param move
	 *            is the move to be sent to the server.
	 */
	public void sendMove(int move) {
		sendMessage(Interpreter.KW_GAME_MOVE + " " + move);
	}

	/**
	 * Checks whether the server supports a certain function.
	 * 
	 * @param function
	 *            is a by the interpreter approved function.
	 * @return whether the server supports the given function.
	 */
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

	/**
	 * Prints a message in the gui if there is one, otherwise prints it to
	 * System.out.
	 * 
	 * @param message
	 *            is the message to be printed
	 */
	void printMessage(String message) {
		if (gui == null) {
			System.out.println(message);
		} else {
			gui.addMessage(message);
		}
	}

	/**
	 * @param address
	 *            is the address used to create a Socket.
	 * @param port
	 *            is the port used to create a Socket.
	 * @param name
	 *            is the name of this client.
	 * @param gui
	 *            is the gui associated with this client.
	 */
	public Client(String address, int port, String name) {
		this.name = name;
		try {
			sock = new Socket(address, port);
		} catch (IOException e1) {
			printMessage("Could not create socket with server.");
			System.exit(0);
		}
		inter = new Interpreter(this);
		try {
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(
					sock.getOutputStream()));
		} catch (IOException alpha) {
			printMessage("Error while opening streams");
			System.exit(0);
		}
		sersup = new HashSet<String>();
		invites = new HashMap<String, int[]>();
		sendMessage("CONNECT " + this.name + " "
				+ Interpreter.KW_FEATURE_CBOARDSIZE);
	}

	/**
	 * Do nothing for a specified time.
	 * 
	 * @param time
	 *            is the number of milliseconds to wait.
	 */
	public static void hold(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.out.println("Impatient bastard");
		}
	}

	public void setClientname(String name) {
		this.name = name;
	}

	// Constructor, obviously
	/**
	 * @param address
	 *            is the address used to create a Socket.
	 * @param port
	 *            is the port used to create a Socket.
	 * @param name
	 *            is the name of this client.
	 * @param gui
	 *            is the gui associated with this client.
	 */
	public Client(String address, int port, String name, ClientGUI gui) {
		this.gui = gui;
		this.name = name;
		try {
			sock = new Socket(address, port);
		} catch (IOException e1) {
			printMessage("Could not create socket with server.");
			System.exit(0);
		}
		inter = new Interpreter(this);
		try {
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(
					sock.getOutputStream()));
		} catch (IOException alpha) {
			printMessage("Error while opening streams");
			System.exit(0);
		}
		sersup = new HashSet<String>();
		invites = new HashMap<String, int[]>();
		sendMessage(Interpreter.KW_CONN_WELCOME + " " + this.name + " "
				+ Interpreter.KW_FEATURE_CBOARDSIZE);
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
		printMessage("CONNECTED");
	}

	/**
	 * Asks the client to do a move and sends it to the server.
	 */
	public void makemove() {
		UseScannerForDeterminingMoves(true);
		printMessage("Please type your move.");
		/*
		 * if (playerno == 1) { movetobemade =
		 * game.getFirstPlayer().determineMove(game.getBoard()); } else {
		 * movetobemade = game.getSecondPlayer()
		 * .determineMove(game.getBoard()); }
		 */

	}

	/**
	 * @param firstname
	 *            is the name of player #1
	 * @param secondname
	 *            is the name of player #2
	 */
	public void gamestart(String firstname, String secondname,
			boolean standardsize) {
		if (!standardsize) {
			if (invites.containsKey(firstname)) {
				this.setDimensions(invites.get(firstname)[0],
						invites.get(firstname)[1]);
			} else {
				this.setDimensions(invites.get(secondname)[0],
						invites.get(secondname)[1]);
			}
		} else {
			this.setDimensions(7, 6);
		}
		if (gui == null) {
			player = new HumanPlayer(name, Mark.X, this);
			if (name.equals(firstname)) {
				playerno = 1;
				printMessage("Board width: " + boardwidth);
				printMessage("Board height: " + boardheight);
				game = new Game(player, new HumanPlayer(secondname, Mark.O,
						new NetworkedInputHandler(this)), boardwidth,
						boardheight, true);
			} else {
				playerno = 2;
				printMessage("Board width: " + boardwidth);
				printMessage("Board height: " + boardheight);
				game = new Game(new HumanPlayer(secondname, Mark.O,
						new NetworkedInputHandler(this)), player, boardwidth,
						boardheight, true);
			}
			TUI thetui = new TUI(game.getBoard());
		} else {
			player = new HumanPlayer(name, Mark.X, new InputHandler());
			MainGui thegui = new MainGui();
			if (name.equals(firstname)) {
				playerno = 1;
				printMessage("Board width: " + boardwidth);
				printMessage("Board height: " + boardheight);
				game = new Game(player, new HumanPlayer(secondname, Mark.O,
						new NetworkedInputHandler(this)), thegui, boardwidth,
						boardheight, true);
			} else {
				playerno = 2;
				printMessage("Board width: " + boardwidth);
				printMessage("Board height: " + boardheight);
				game = new Game(new HumanPlayer(secondname, Mark.O,
						new NetworkedInputHandler(this)), player, thegui,
						boardwidth, boardheight, true);
			}
			// thegui.changePanel(new GameMainPanel(thegui, game, player
			// .getInputHandler()));

		}
		// game.getGamePanel();

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
		if (stringArg.length() == 0) {
			lobby.add("Empty");
		}
		String[] splitted = stringArg.split("\\s+");
		for (int i = 0; i < splitted.length; i++) {
			lobby.add(splitted[i]);
		}
		printMessage("***LOBBY***");
		for (String i : lobby) {
			printMessage("* " + i);
		}
		printMessage("***********");
	}

	/**
	 * Returns the BufferedReader of a client.
	 * 
	 * @return the InputReader associated with this Client's socket.
	 */
	public BufferedReader getIn() {
		return in;
	}

	/**
	 * Returns the BufferedWriter of a client.
	 * 
	 * @return the OutputStream associated with this Client's socket.
	 */
	public BufferedWriter getOut() {
		return out;
	}

	public void run() {
		try {
			// printMessage("Ready to read new command ||||||||||||||||||||||||");
			String tussenvar = in.readLine();
			while (!sock.isClosed()) {
				printMessage("Message received from server: " + tussenvar);
				inter.whatisthatClient(tussenvar);
				// printMessage("Ready to read new command ||||||||||||||||||||||||");
				tussenvar = in.readLine();
			}
		} catch (IOException e) {
			printMessage("Server has shut down.");
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

	/**
	 * Informs the Client of an invite being declined.
	 */
	public void inviteDeclined() {
		printMessage("Your invite has been declined! Poor you :'(");
	}

	public void setDimensions(int width, int height) {
		// System.out.println("Width set to " + width + " in 438 for " + name);
		// System.out.println("Height set to " + height + " in 439 for " +
		// name);
		boardwidth = width;
		boardheight = height;
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
			printMessage("You have been invited by " + apart[0]
					+ " for a game of 6 by 7!");
			dimensions[0] = 6;
			dimensions[1] = 7;
		} else {
			printMessage("You have been invited by " + apart[0]
					+ " for a game of " + apart[1] + " by " + apart[2] + "!");
			dimensions[0] = Integer.parseInt(apart[1]);
			dimensions[1] = Integer.parseInt(apart[2]);
		}
		invites.put(apart[0], dimensions);
		// System.out.println(apart[0] + " and dimensions " + dimensions[0] +
		// dimensions[1] + " in 466");
		printMessage("Type accept " + apart[0] + " to accept.");
		printMessage("Type decline " + apart[0] + " to decline.");
	}

	/**
	 * Quits the client.
	 */
	public void shutDown() {
		scanner.close();
		try {
			sock.close();
		} catch (IOException e) {
			printMessage("Could not close server");
		}
	}

	public void UseScannerForDeterminingMoves(boolean b) {
		scannerfordeterminingmoves = b;

	}

	/*
	 * public void inviteAccepted(String name) {
	 * this.setDimensions(invites.get(name)[0], invites.get(name)[1]);
	 * 
	 * }
	 */

}
