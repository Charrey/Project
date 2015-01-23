package Project.networking;

public class ClientConsole extends Thread {

	private Client client;
	
	public ClientConsole(Client cli) {
		client = cli;
	}
	
	public void run() {
		client.watchInput();
	}

}