package Project.networking;

import java.net.Socket;
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

public class Client {

	Interpreter inter;
	Game game;
	Gui gui;

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
		Client client = new Client("127.0.0.1",49999,"Pim");
	}
	

	//Constructor, obviously
	public Client(String address, int port, String name) {
		try {
			Socket socket = new Socket(InetAddress.getByName("127.0.0.1"),49999);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		game = new Game(new HumanPlayer(name, Mark.X, new InputHandler()), new HumanPlayer("other_pc", Mark.O, new NetworkedInputHandler()));
		player = (HumanPlayer)game.getFirstPlayer();
		gui = new Gui(game.getBoard(), player.getInputHandler());
		inter = new Interpreter(this);
		try {
			sock = new Socket(InetAddress.getByName(address), port);
		} catch (UnknownHostException e) {
			System.err.println("ERROR: unknown host");
			System.exit(0);
		} catch (IOException e) {
			System.err.println("Connection failed");
			System.exit(0);
		}
		try {
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(
					sock.getOutputStream()));
		} catch (IOException alpha) {
			System.err.println("Error while opening streams");
			System.exit(0);
		}
	}

	// send a message to a ClientHandler of someone else's implementation.
	public void sendMessage(String msg) {
		try {
			out.write(msg);
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void connectionAccepted(String features) {
	String[] splitted = features.split("\\s+");
	for (int i = 0; i<splitted.length; i++) {
	inter.whatisthatClient(this, splitted[i]);
	}
	}

	public void setServerMultiplayer(Boolean bool) {
		sersup_multiplayer = true;
	}

	public void refreshBoard(String stringArg) {
		String[] splitted = stringArg.split("\\s+");
		game.getBoard().reset();
		int teller = 0;
		for (int p = 0; p < game.getBoard().getHeight(); p++) {
			for (int i = 0; i < game.getBoard().getWidth(); i++) {
				if (!splitted[teller].equals("/n")) {
					if (Integer.parseInt(splitted[teller]) == 01) {
						game.getBoard().putMark(i, Mark.X);
					} else if (Integer.parseInt(splitted[teller]) != 0) {
						game.getBoard().putMark(i, Mark.O);
					}
				}
				teller++;

			}

		}
		gui.updateBoard();
	}

	public void makemove() {
		movetobemade = game.getFirstPlayer().determineMove(game.getBoard());
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

		game = new Game(player, new HumanPlayer("That_pc", Mark.O, new NetworkedInputHandler()));
		//HumanPlayer met networked handler!!!!
	}

	public void moveok() {
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
	}

	public void run() {
		try {
			String tussenvar = in.readLine();
			while (tussenvar != null) {
				inter.whatisthatClient(this, tussenvar);
				tussenvar = in.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void gameend() {
		game.gameEnd();
	}

	public void invited(String other) {

	}

}
