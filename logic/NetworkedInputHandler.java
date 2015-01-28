package Project.logic;

import Project.networking.ClientHandler;
import Project.networking.Client;

public class NetworkedInputHandler extends InputHandler implements Runnable {

	private Client client;
	private ClientHandler clienthandler;
	private int move;

	public static Object monitor;

	// WE ARE CLIENT
	public NetworkedInputHandler(Client client) {
		this.client = client;
		monitor = new Object();
	}

	// WE ARE SERVER
	public NetworkedInputHandler(ClientHandler clienthandler) {
		this.clienthandler = clienthandler;
	}

	public Client getClient() {
		return client;
	}

	public ClientHandler getClientHandler() {
		return clienthandler;
	}

	public void run() {
		//getMove();
	}
	
	public int getMove() {
	
		if (monitor==null) {
			monitor = new Object();
		}
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println("ERROR: Stopped waiting on move");
			}
			return move;
		}
	}
	
	public int getMove(Board board) {
		return 5;
	}

	public void setMove(int move) {
		this.move = move;
	}

	public int getMoveClient(Client client) {
		return 0;

	}

	public int getMoveServer(ClientHandler clienthandler) {
		return 0;
	}

}
