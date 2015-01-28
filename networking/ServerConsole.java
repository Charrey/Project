package Project.networking;

public class ServerConsole extends Thread {

	private Server server;
	
	/**
	 * Creates a new Serverconsole
	 * 
	 * @param serv the server to run the console of
	 */
	public ServerConsole(Server serv) {
		server = serv;
	}
	
	public void run() {
		server.watchInput();
	}

}
	