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

	Map<String, int[]> invites;
	private ClientGUI gui;

	/**
	 * Takes console input and reacts accordingly.
	 */
	public void watchInput() {
		if (gui == null) {
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
			switch (apart[0]) {
			case "accept":
				if (invites.keySet().contains(apart[1])) {
					sendMessage(Interpreter.KW_LOBB_ACCEPTINVITE + " "
							+ apart[1]);
					System.out.println("boardwidth set to "
							+ invites.get(apart[1])[0] + " in 70 for " + name);
					System.out.println("boardheight set to "
							+ invites.get(apart[1])[1] + " in 71" + name);
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
				System.out.println("Apart length is "+apart.length);
				if (apart.length == 4 && Server.representsInt(apart[1])
						&& Server.representsInt(apart[2])) {
					array[0] = Integer.parseInt(apart[1]);
					array[1] = Integer.parseInt(apart[2]);
				} else if (apart.length == 2) {
					array[0] = 7;
					array[1] = 6;
				}

				invites.put(apart[1], array);
				System.out.println(apart[1] + " and dimensions " + array[0]
						+ array[1] + " in 97");
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
			printMessage("Sending message to server (" + sock.getInetAddress()
					+ ") : " + msg);
			out.write(msg);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			printMessage("Could not send command (" + msg + ") to server.");
		}
	}

	public void sendMove(int move) {
		sendMessage(Interpreter.KW_GAME_MOVE + " " + move);
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

	public static void hold(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.out.println("Impatient bastard");
		}
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
		sendMessage("CONNECT " + this.name + " "
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

	public void makemove() {
		if (playerno == 1) {
			movetobemade = game.getFirstPlayer().determineMove(game.getBoard());
		} else {
			movetobemade = game.getSecondPlayer()
					.determineMove(game.getBoard());
		}
		this.sendMove(movetobemade);
	}

	public void gamestart(String firstname, String secondname) {
		if (invites.containsKey(firstname)) {
			this.setDimensions(invites.get(firstname)[0],
					invites.get(firstname)[1]);
		} else {
			this.setDimensions(invites.get(secondname)[0],
					invites.get(secondname)[1]);
		}

		player = new HumanPlayer(name, Mark.X, new InputHandler());
		if (gui == null) {
			if (name.equals(firstname)) {
				playerno = 1;
				printMessage("Board width: " + boardwidth);
				printMessage("Board height: " + boardheight);
				game = new Game(player, new HumanPlayer(secondname, Mark.O,
						new NetworkedInputHandler(this)), boardwidth,
						boardheight);
			} else {
				playerno = 2;
				printMessage("Board width: " + boardwidth);
				printMessage("Board height: " + boardheight);
				game = new Game(new HumanPlayer(secondname, Mark.O,
						new NetworkedInputHandler(this)), player, boardwidth,
						boardheight);
			}
			TUI thetui = new TUI(game.getBoard());
		} else {
			MainGui thegui = new MainGui();
			if (name.equals(firstname)) {
				playerno = 1;
				printMessage("Board width: " + boardwidth);
				printMessage("Board height: " + boardheight);
				game = new Game(player, new HumanPlayer(secondname, Mark.O,
						new NetworkedInputHandler(this)), thegui, boardwidth,
						boardheight);
			} else {
				playerno = 2;
				printMessage("Board width: " + boardwidth);
				printMessage("Board height: " + boardheight);
				game = new Game(new HumanPlayer(secondname, Mark.O,
						new NetworkedInputHandler(this)), player, thegui,
						boardwidth, boardheight);
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
				printMessage("Message received from server: " + tussenvar);
				inter.whatisthatClient(tussenvar);
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

	public void inviteDeclined() {

	}

	public void setDimensions(int width, int height) {
		System.out.println("Width set to " + width + " in 438 for " + name);
		System.out.println("Height set to " + height + " in 439 for " + name);
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
		System.out.println(apart[0] + " and dimensions " + dimensions[0]
				+ dimensions[1] + " in 466");
		printMessage("Type accept " + apart[0] + " to accept.");
		printMessage("Type decline " + apart[0] + " to decline.");
	}

	public void shutDown() {
		scanner.close();
		try {
			sock.close();
		} catch (IOException e) {
			printMessage("Could not close server");
		}
	}

	/*
	 * public void inviteAccepted(String name) {
	 * this.setDimensions(invites.get(name)[0], invites.get(name)[1]);
	 * 
	 * }
	 */

}
