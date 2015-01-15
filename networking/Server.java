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
	Interpreter interpreter;
	
		
	
	public Server() {
	lobby = new HashMap<ClientHandler, Set<String>>();
	gamesgames = new HashSet<Game>();
	}
	
	public void joinServer(ClientHandler client, Set<String> features) {
		lobby.put(client, features);
	}
	
	public void handlechatmessage(ClientHandler source, String message){
	
	}
	
	
	public String sendBoard(ClientHandler source) {
	return "";
	};
	
	public void connectionAccepted() {
		
	}
	
	public void sendLeaderboard(){
		
	}
	
	public void setfChat(ClientHandler source, Boolean bool) {
	if(lobby.get(source).contains(interpreter.kw_feature_chat) && bool==false) {
	lobby.get(source).remove(interpreter.kw_feature_chat);}
	else if (!lobby.get(source).contains(interpreter.kw_feature_chat) && bool == true) {
	lobby.get(source).add(interpreter.kw_feature_chat);
	}
	}
	
	public void setfCBoardSize(ClientHandler source, Boolean bool) {
		
	}
	
	public void setfLeaderboard(ClientHandler source, Boolean bool) {
		
	}
	
	public void setfMultiplayer(ClientHandler source, Boolean bool) {
		
	}
	
	public void invite(String target, ClientHandler source){
		
	}
	

	
	public void whatisthat(String clientname, String that) {
				
	}
	
	
	
	
	
	
	
	
	
	

}
