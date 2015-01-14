package Project.networking;
import Project.logic.Game;

public class Interpreter {

	//SENT BY SERVER ONLY:
	private final String kw_sendboard = "BOARD";
	private final String kw_conn_gameend = "GAME_END";
	private final String kw_conn_gamestart = "GAME_START";
	private final String kw_conn_lobby = "LOBBY";
	private final String kw_game_moveok = "MOVE_OK";
	private final String kw_game_reqmove = "REQUEST_MOVE";
	
	//SENT BY CLIENT ONLY:
	private final String kw_conn_chatmessage = "CHAT";
	private final String kw_conn_requestboard = "REQUEST_BOARD";
	private final String kw_conn_acceptconnect = "ACCEPT_CONNECT";
	private final String kw_conn_leaderboard = "LEADERBOARD";// + <leaderboard>

	//SENT BY BOTH SERVER AND CLIENT:
	private final String kw_feature_chat = "CHAT";// + <message>
	private final String kw_feature_cBoardSize = "CUSTOM_BOARD_SIZE";
	private final String kw_feature_leaderboard = "LEADERBOARD";
	private final String kw_feature_multiplayer = "MULTIPLAYER";
	private final String kw_conn_invite = "INVITE";
	
	ClientHandler playerone;
	ClientHandler playertwo;
	Game game;
	Boolean areweserver;
	Server server;
	
	
	//IF WE'RE JUST A CLIENT
	public Interpreter(Client client) {
		areweserver = false;
	}
	
	//IF WE'RE A SERVER
	public Interpreter(ClientHandler a,ClientHandler b, Game game) {
		playerone = a;
		playertwo = b;
		this.game = game;
		areweserver = true;
	}
	
	public void whatisthatServer(String that, ClientHandler source) {
		
		if (areweserver==true) {
			if (that.startsWith(kw_conn_chatmessage)) {
			server.handlechatmessage(source);
			}
			else if (that.startsWith(kw_conn_requestboard)) {
			server.sendBoard(source);
			}
			else if (that.startsWith(kw_conn_acceptconnect)) {
			server.connectionAccepted();
			}
			else if (that.startsWith(kw_conn_leaderboard)) {
			server.sendLeaderboard();
			}
			else if (that.startsWith(kw_feature_chat)) {
			server.setfChat(source, true);
			}
			else if (that.startsWith(kw_feature_cBoardSize)) {
			server.setfCBoardSize(source, true);
			}
			else if (that.startsWith(kw_feature_leaderboard)) {
			server.setfLeaderboard(source, true);
			}
			else if (that.startsWith(kw_feature_multiplayer)) {
			server.setfMultiplayer(source, true);
			}
			else if (that.startsWith(kw_feature_leaderboard)) {
			server.setfLeaderboard(source, true);
			}
			else if (that.startsWith(kw_conn_invite)) {
			server.invite(that.substring(8, source));
			}			
		else{
			System.err.println("interpreter wrongly constructed");
		}
		
	}
		else{System.err.println("We are not the client, use whatisthatClient() instead.");}
	}
	
	public void whatisthatClient(Client client, String that) {
		
		if (areweserver==false) {
			if (that.startsWith(kw_sendboard)) {
			client.refreshBoard(that.substring(6));
			}
			else if (that.startsWith(kw_conn_gameend)) {
			client.gameend();
			}
			else if (that.startsWith(kw_conn_gamestart)) {
			client.gamestart();
			}
			else if (that.startsWith(kw_conn_lobby)) {
			client.setLobby(that.substring(6));
			}
			else if (that.startsWith(kw_game_moveok)) {
			client.moveok();
			}
			else if (that.startsWith(kw_game_reqmove)) {
			client.makemove();
			}			
			else if (that.startsWith(kw_feature_chat)) {
			client.SetServerchat(true);
			}
			else if (that.startsWith(kw_feature_cBoardSize)) {
			client.SetServerCBoardSize(true);
			}
			else if (that.startsWith(kw_feature_leaderboard)) {
			client.setServerLeaderboard(true);
			}
			else if (that.startsWith(kw_feature_multiplayer)) {
			client.setServerMultiplayer(true);
			}
			else if (that.startsWith(kw_conn_invite)) {
			client.invited(that.substring(8));
			}			
		else{
			System.err.println("interpreter wrongly constructed");
		}
		
	}
		else{System.err.println("We are n the server, use whatisthatClient() instead.");}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}