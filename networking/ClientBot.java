package Project.networking;

import java.io.IOException;
import java.util.Random;

import Project.gui.ClientGUI;
import Project.logic.ComputerPlayer;
import Project.logic.Mark;

public class ClientBot extends Client {

	public static final String NAME = "DoodAanJML";
	private ComputerPlayer cplayer;
	private boolean connected;
	private boolean invitemode;
	private final boolean ONLYONEGAME = false;
	
	@Override
	public String getFeatures() {
		return "";
	}

	public ClientBot(String address, int port, String name) {
		super(address, port, NAME);
		connected = false;
		cplayer = new ComputerPlayer(Mark.X);
	}

	public ClientBot(String address, int port, String name, ClientGUI gui) {
		super(address, port, NAME, gui);
	}

	/*
	 * @Override public void connectionAccepted(String features) {
	 * super.connectionAccepted(features); }
	 */

	@Override
	public void watchInput() {
		hold(100);
		inviteDeclined();
	}

	@Override
	public void run() {
		try {
			String tussenvar = getIn().readLine();
			while (!sock.isClosed()) {
				System.out
						.println("Message received from server: " + tussenvar);
				inter.whatisthatClient(tussenvar);
				tussenvar = getIn().readLine();
			}
		} catch (IOException e) {
			System.err.println("Server has shut down.");
		}
		System.exit(0);
	}

	@Override
	public void makemove() {
		hold(100);
		sendMove(cplayer.determineMove(getGame().getBoard()));
	}

	@Override
	public void invited(String args) {
		String[] apart = args.split("\\s+");
		hold(111);
		if (!connected) {
			sendMessage(Interpreter.KW_LOBB_ACCEPTINVITE + " " + apart[0]);
		} else {
			sendMessage(Interpreter.KW_LOBB_DECLINEINVITE + " " + apart[0]);
		}
	}

	@Override
	public void gameend() {
		super.gameend();
		connected = false;
		inviteDeclined();
	}

	@Override
	public void gamestart(String firstname, String secondname,
			boolean standardsize) {
		super.gamestart(firstname, secondname, true);
		connected = true;
		invitemode = false;
	}

	@Override
	public void inviteDeclined() {
		if (!invitemode) {
			invitemode = true;
			Client.hold(100);
			sendMessage(Interpreter.KW_LOBB_REQUEST);
			Client.hold(550);
			while (lobby == null) {
				hold(1350);
				sendMessage("CONNECT " + this.name + " "
						+ Interpreter.KW_FEATURE_CBOARDSIZE);
			}
			hold(1350);
			while (lobby.size() < 2 && !connected) {
				hold(2550);
				sendMessage(Interpreter.KW_LOBB_REQUEST);
				hold(2550);
			}
			if (!connected) {
				hold(100);
				for (String i : lobby) {
					if (!i.equals(name)) {
						sendMessage(Interpreter.KW_LOBB_INVITE + " " + i
								+ "");
					}
				}
				invitemode = false;
			}
		}
	}

	public void retryConnection() {
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random rng = new Random();
		char[] text = new char[10];
		for (int i = 0; i < 10; i++) {
			text[i] = characters.charAt(rng.nextInt(characters.length()));
		}
		super.setClientname(new String(text));
		super.sendMessage(Interpreter.KW_CONN_WELCOME + " " + name + " "
				+ Interpreter.KW_FEATURE_CBOARDSIZE);
	}

}
