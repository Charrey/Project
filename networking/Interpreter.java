package Project.networking;
import Project.logic.Game;

public class Interpreter {

	//SENT BY SERVER ONLY:
	public final String kw_sendboard = "BOARD";
	public final String kw_conn_gameend = "GAME_END";
	public final String kw_conn_gamestart = "GAME_START";
	public final String kw_conn_lobby = "LOBBY";
	public final String kw_game_moveok = "MOVE_OK";
	public final String kw_game_reqmove = "REQUEST_MOVE";
	public final String kw_conn_error = "ERROR";
	
	//SENT BY CLIENT ONLY:
	public final String kw_conn_welcome = "CONNECT";
	public final String kw_conn_chatmessage = "CHAT";
	public final String kw_conn_requestboard = "REQUEST_BOARD";
	public final String kw_conn_acceptconnect = "ACCEPT_CONNECT";
	public final String kw_conn_leaderboard = "LEADERBOARD";// + <leaderboard>

	//SENT BY BOTH SERVER AND CLIENT:
	public final String kw_feature_chat = "CHAT";// + <message>
	public final String kw_feature_cBoardSize = "CUSTOM_BOARD_SIZE";
	public final String kw_feature_leaderboard = "LEADERBOARD";
	public final String kw_feature_multiplayer = "MULTIPLAYER";
	public final String kw_conn_invite = "INVITE";
	
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
	public Interpreter(Server server) {
		//playerone = a;
		//playertwo = b;
		//this.game = game;
		this.server = server;
		areweserver = true;
	}
	
	public void whatisthatServer(String that, ClientHandler source, Boolean subcommand) {
		if (areweserver==true) {
			if (that.startsWith(kw_conn_chatmessage)) {
			server.handlechatmessage(source, that.substring(5));
			}
			else if (that.startsWith(kw_conn_welcome)) {
			server.acceptConnection(source, that.substring(8));
			}
			else if (that.startsWith(kw_conn_requestboard)) {
			server.sendBoard(source);
			}
			else if (that.startsWith(kw_conn_leaderboard) && subcommand==false) {
			server.sendLeaderboard();
			}
			else if (that.startsWith(kw_feature_chat)) {
			server.setFunction(source,kw_feature_chat, true);
			}
			else if (that.startsWith(kw_feature_cBoardSize)) {
			server.setFunction(source,kw_feature_cBoardSize, true);
			}
			else if (that.startsWith(kw_feature_leaderboard)) {
			server.setFunction(source,kw_feature_leaderboard, true);
			}
			else if (that.startsWith(kw_feature_multiplayer)) {
			server.setFunction(source,kw_feature_multiplayer, true);
			}
			else if (that.startsWith(kw_conn_invite)) {
			server.invite(that.substring(8), source);
			}			
		else{
			System.err.println("Misunderstood command: "+that);
			server.sendError(source, "SyntaxError");
		}
		
	}
		else{System.err.println("We are not the client, use whatisthatClient() instead.");}
	}
	
	public void whatisthatClient(Client client, String that) {
		if (areweserver==false) {
			if (that.startsWith(kw_sendboard)) {
			client.refreshBoard(that.substring(6));
			}
			else if (that.startsWith(kw_conn_acceptconnect)) {
			client.connectionAccepted(that.substring(15));
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
			else if (that.startsWith(kw_conn_error)) {
			System.out.println("");
			System.out.println("!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!");
			System.out.println("ERROR RECEIVED FROM SERVER: "+that.substring(6));
			System.out.println("!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!");
			System.out.println("");
			}
		else{
			System.err.println("interpreter wrongly constructed");
		}
		
	}
		else{System.err.println("We are n the server, use whatisthatClient() instead.");}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}