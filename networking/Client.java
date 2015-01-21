package Project.networking;

import java.net.Socket;
import java.util.Scanner;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;

import Project.logic.*;
import Project.gui.*;

import java.util.Set;
import java.util.HashSet;

import org.junit.experimental.theories.Theories;

public class Client implements Runnable {

	Interpreter inter;
	Game game;
	// Gui gui;
	String name;

	// Server supports:
	Boolean sersup_chat = false;
	Boolean sersup_cboardsize = false;
	Boolean sersup_leaderboard = false;
	Boolean sersup_multiplayer = false;

	Socket sock;
	Set<String> lobby;
	HumanPlayer player;
	String ourname;
	int playerno;

	private BufferedReader in;
	private BufferedWriter out;

	int movetobemade;

	public static void main(String[] args) {
		/*
		 * Scanner scanner = new Scanner(System.in);
		 * System.out.println("What is your name?"); String name =
		 * scanner.nextLine(); while (name.length() < 2) {
		 * System.out.println("Longer name please."); name = scanner.nextLine();
		 * } Client client = new Client("127.0.0.1", 49999, name);
		 * client.start();
		 */
	}

	public void watchInput() {
		Scanner scanner = new Scanner(System.in);
		String gotten;
		while (true) {
			gotten = scanner.nextLine();
			if (gotten.startsWith("accept")) {
				String[] apart = gotten.split("\\s+");
				sendMessage("ACCEPT_INVITE " + apart[1]);
			} else if (gotten.startsWith("decline")) {
				String[] apart = gotten.split("\\s+");
				sendMessage("DECLINE_INVITE " + apart[1]);
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

	public Client(Socket sock) {
		this.sock = sock;
		try {
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(
					sock.getOutputStream()));
		} catch (IOException alpha) {
			System.err.println("Error while opening streams");
			System.exit(0);
		}

		watchInput();
	}

	public Socket getSocket() {
		return sock;
	}

	// Constructor, obviously
	public Client(String address, int port, String name) {
		this.name = name;
		try {
			sock = new Socket(address, port);
		} catch (IOException e1) {
			System.err.println("Could not create socket with server.");
		}
		inter = new Interpreter(this);
		try {
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(
					sock.getOutputStream()));
		} catch (IOException alpha) {
			System.err.println("Error while opening streams");
			System.exit(0);
		}
		sendMessage("CONNECT " + this.name + " LEADERBOARD");
	}

	// send a message to a ClientHandler of someone else's implementation.
	public void sendMessage(String msg) {
		try {
			System.out.println("Sending message to server ("
					+ sock.getInetAddress() + ") : " + msg);
			out.write(msg);
			out.newLine();
			out.flush();

		} catch (IOException e) {
			System.err.println("Could not send command (" + msg
					+ ") to server.");
		}

	}

	public void askLobby() {
		sendMessage("REQUEST_LOBBY");
	}

	public void connectionAccepted(String features) {
		String[] splitted = features.split("\\s+");
		for (int i = 0; i < splitted.length; i++) {
			inter.whatisthatClient(this, splitted[i]);
		}
	}

	public void setServerMultiplayer(Boolean bool) {
		sersup_multiplayer = true;
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
		// gui.updateBoard();
	}

	public void makemove() {
		movetobemade = ((HumanPlayer) game.getFirstPlayer()).getInputHandler()
				.getMove();
		try {
			out.write("MOVE " + movetobemade + " /n/n");
			out.flush();
		} catch (IOException ex) {
			System.err.println("Could not send move to server");
		}
	}

	public void setServerLeaderboard(Boolean bool) {
		sersup_leaderboard = true;
	}

	public void gamestart() {
		player = new HumanPlayer(this.ourname, Mark.X, new InputHandler());
		game = new Game(player, new HumanPlayer("That_pc", Mark.O,
				new NetworkedInputHandler(this)));
		// HumanPlayer met networked handler!!!!
	}

	public void moveok(String arguments) {
		game.getBoard().putMark(movetobemade, Mark.X);
	}

	public void SetServerchat(Boolean b) {
		sersup_chat = true;
	}

	public void SetServerCBoardSize(Boolean b) {
		sersup_cboardsize = true;
	}

	public void setLobby(String stringArg) {
		lobby = new HashSet<String>();
		String[] splitted = stringArg.split("\\s+");
		for (int i = 0; i < splitted.length; i++) {
			lobby.add(splitted[i]);
		}
		System.out.println("***LOBBY***");
		if (lobby.isEmpty()) {
			System.out.println("* Empty lobby :(");
		} else {
			for (String i : lobby) {
				System.out.println("* " + i);
			}
		}
		System.out.println("***********");
	}

	public void run() {
		try {
			String tussenvar = in.readLine();
			while (true/* tussenvar != null */) {
				System.out
						.println("Message received from server: " + tussenvar);
				inter.whatisthatClient(this, tussenvar);
				System.out.println("En dit werkt wel btw");
				tussenvar = in.readLine();
				System.err.println("line readed");
			}
		} catch (IOException e) {
			System.err.println("Could not read from server.");
		}
		System.err.println("CRASHED");

	}

	public void gameend() {
		game.gameEnd();
	}

	public void invited(String other) {
		String[] apart = other.split("\\s+");
		if (apart.length < 3) {
			System.out.println("You have been invited by " + apart[0]
					+ " for a game of 6 by 7!");
		} else {
			System.out.println("You have been invited by " + apart[0]
					+ " for a game of " + apart[1] + " by " + apart[2] + "!");
		}
		System.out.println("Type accept " + apart[0] + " to accept.");
		System.out.println("Type decline " + apart[0] + " to decline.");

	}

}
