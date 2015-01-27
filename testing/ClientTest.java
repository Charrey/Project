package Project.testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Project.logic.Game;
import Project.networking.Client;
import Project.networking.Server;

public class ClientTest {

	Client client1;
	Client client2;
	Server server;
	
	@Before
	public void setUp() throws Exception {
		server = new Server(49999);
		client1 = new Client("127.0.0.1", 49999, "client1");
		client2 = new Client("127.0.0.1", 49999, "client2");
		
	}

	@Test
	public void test() {
		Client.hold(1000);
		ifExpectedNull("Game client nog niet gestart", client1.getGame());
		client1.sendMessage("LOBBY");
		Client.hold(1000);
		
		client1.sendMessage("INVITE client2");
		Client.hold(1000);
		client2.sendMessage("ACCEPT client1");
		Client.hold(250);
		ifExpectedNotNull("Game niet null", client1.getGame());
		Client.hold(250);
		client2.sendMove(3);
		Client.hold(250);
		client1.sendMove(2);
		
		Client.hold(250);
		client2.sendMove(3);
		Client.hold(250);
		client1.sendMove(2);

		Client.hold(250);
		client2.sendMove(3);
		Client.hold(250);
		client1.sendMove(2);
		
		Client.hold(250);
		client2.sendMove(3);

		client1.setLobby("test");
		client2.setLobby("");
		
		client1.setDimensions(6, 7);
		
		ifExpected("breedte is 6?", 6, client1.getBoardWidth());
		ifExpected("hoogte is 7?", 7, client1.getBoardHeight());
		
		client1.shutDown();
		
		System.err.println("Test End");

		
	}

	public void ifExpectedNotNull(String string, Object result) {
		if(result == null){
			System.err.println(string + " Result: " + null);
		}
	}

	public void ifExpected(String description, Object expected, Object result){
		if(!expected.equals(result)){
			System.err.println(description + "Expected: "+ expected.toString()+ "Result: " + result.toString());
		}
	}
	
	public void ifExpectedNull(String description, Object result){
		if(!(result == null)){
			System.err.println(description + "Result: " + result.toString());
		}
	}
	
}
