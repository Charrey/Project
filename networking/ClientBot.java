package Project.networking;

import java.io.IOException;

import Project.gui.ClientGUI;
import Project.logic.ComputerPlayer;
import Project.logic.Mark;

public class ClientBot extends Client {

	public static final String NAME = "RoboCop";
	private ComputerPlayer cplayer;
	private boolean connected;
	private boolean invitemode;

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
	public void gamestart(String firstname, String secondname, boolean standardsize) {
		super.gamestart(firstname, secondname, true);
		connected=true;
	}

	@Override
	public void inviteDeclined() {
		invitemode=true;
		sendMessage(Interpreter.KW_LOBB_REQUEST);
		Client.hold(550);
		while (lobby == null) {
			hold(2700);
			sendMessage("CONNECT " + this.name + " "
					+ Interpreter.KW_FEATURE_CBOARDSIZE);
		}
		while (lobby.size() < 2 && !connected) {
			hold(5100);
			sendMessage(Interpreter.KW_LOBB_REQUEST);
		}
		if (!connected) {
			hold(100);
			for (String i : lobby) {
				if (!i.equals("RoboCop")) {
					sendMessage(Interpreter.KW_LOBB_INVITE + " " + i + " 7 6");
				}
			}
		}
	}

}
