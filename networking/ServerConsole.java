package Project.networking;

public class ServerConsole extends Thread {

	private Server server;
	
	public ServerConsole(Server serv) {
		server = serv;
	}
	
	public void run() {
		server.watchInput();
	}

}
	