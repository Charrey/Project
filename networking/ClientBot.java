package Project.networking;

import java.io.IOException;

import Project.gui.ClientGUI;
import Project.logic.ComputerPlayer;
import Project.logic.Mark;

public class ClientBot extends Client {

	public static final String NAME = "RoboCop";
	private ComputerPlayer cplayer;

	public ClientBot(String address, int port, String name) {
		super(address, port, NAME);
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

	

	public void makemove() {
		sendMove(cplayer.determineMove(getGame().getBoard()));
	}

	@Override
	public void inviteDeclined() {
		sendMessage(Interpreter.KW_LOBB_REQUEST);
		Client.hold(500);
		while (lobby==null) {
			hold(2000);
			sendMessage("CONNECT " + this.name + " "
					+ Interpreter.KW_FEATURE_CBOARDSIZE);
		}
		while (lobby.size() < 2) {
			hold(2000);
			sendMessage(Interpreter.KW_LOBB_REQUEST);
		}
		for (String i : lobby) {
			if (!i.equals("RoboCop")) {
				sendMessage(Interpreter.KW_LOBB_INVITE + " " + i + " 7 6");
			}
		}
	}

}
