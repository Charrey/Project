package Project.networking;

import java.io.IOException;

import Project.gui.ClientGUI;

public class ClientBot extends Client {

	public static final String NAME = "RoboCop";

	public ClientBot(String address, int port, String name) {
		super(address, port, NAME);
	}
	
	
	public ClientBot(String address, int port, String name, ClientGUI gui) {
		super(address, port, NAME, gui);
	}

	@Override
	public void connectionAccepted(String features) {
		super.connectionAccepted(features);
	}

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
	

	public void hold(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.out.println("Impatient bastard");
		}
	}

	@Override
	public void inviteDeclined() {
		sendMessage(Interpreter.kw_lobb_request);
		hold(500);
		while (lobby.size() < 2) {
			hold(2000);
			sendMessage(Interpreter.kw_lobb_request);
		}
		for (String i : lobby) {
			if (!i.equals("RoboCop")) {
				sendMessage(Interpreter.kw_lobb_invite + " " + i+" 7 6");
			}
		}
	}

}
