package Project.networking;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import Project.logic.Game;

public class Server {
	
	//Set of string contains features
	public Map<ClientHandler, Set<String>> lobby;
	public Set<Game> gamesgames;
	
		
	
	public Server() {
		lobby = new HashMap<ClientHandler, Set<String>>();
	}
	
	public void joinServer(ClientHandler client, Set<String> features) {
		lobby.put(client, features);
	}
	
	
	public String sendBoard() {
		
	};
	
	public void whatisthat(String clientname, String that) {
				
	}
	
	
	
	
	
	
	
	
	
	

}
