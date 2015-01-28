package Project.testing;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import Project.networking.Client;
import Project.networking.Server;

public class ServerTest {

	Server server;
	Server portTakenServer;
	
	Client client1;
	Client client2;
	Client dubClient;
	
	@Before
	public void setUp() throws Exception {
	server = new Server(49999);
	//portTakenServer = new Server(49999);

	client1 = new Client("127.0.0.1", 49999, "client1");
	client2 = new Client("127.0.0.1", 49999, "client2");
	dubClient = new Client("127.0.0.1", 49999, "client1");
	
	
	}

	@Test
	public void test() {
		Client.hold(2000);
		ifExpected("clienthandler bij client", "client1", server.findClientHandler("client1").getClientName());
		Client.hold(2000);
		client1.sendMessage("INVITE client2");
		Client.hold(2000);
		
		client1.setDimensions(7, 6);
		client2.setDimensions(7, 6);
		
		client2.sendMessage("ACCEPT client1");
		Client.hold(1000);
		ifExpected("get opponent client1", "client2", server.getOpponent(server.findClientHandler("client1")).getClientName());
		Client.hold(1000);
		ifExpected("get opponent client2", "client1", server.getOpponent(server.findClientHandler("client2")).getClientName());
		
		Client.hold(1000);
		ifExpected("Game of client2 same as client1 in server", server.getGame(server.findClientHandler("client2")), server.getGame(server.findClientHandler("client1")));
		ifExpected("Game of client2 same as client1 in server", server.getGame(server.findClientHandler("client1")), server.getGame(server.findClientHandler("client2")));
		
		ifExpected("3 is string", true, Server.representsInt("3"));
		ifExpected("a is string", false, Server.representsInt("a"));
		
		try {
			client2.getSocket().close();
		} catch (IOException e) {
			System.out.println("Can't close socket!");
			System.exit(0);
		}
		Client.hold(1000);
		
		ifExpected("Client 2 is no longer part of the lobby", false,server.getLobby().containsKey(client2));
		
		System.err.println("Test End");
		
		server.shutDown();
	}
	
	public void ifExpected(String description, Object expected, Object result){
		if(!expected.equals(result)){
			System.err.println(description + "Expected: "+ expected.toString()+ "Result: " + result.toString());
		}
	}

}
