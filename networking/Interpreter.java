package Project.networking;

import Project.logic.Board;

//import Project.logic.Game;

public class Interpreter {

	/*
	 * Implementation should still work if these are changed!
	 */

	// SENT BY SERVER ONLY:
	public static final String kw_game_sendboard = "BOARD";
	public static final String kw_game_gameend = "END";
	public static final String kw_conn_gamestart = "START";
	public static final String kw_conn_lobby = "LOBBY";
	public static final String kw_game_moveok = "MOVE";
	public static final String kw_game_reqmove = "REQUEST";
	public static final String kw_conn_error = "ERROR";

	// SENT BY CLIENT ONLY:
	public static final String kw_conn_welcome = "CONNECT";
	public static final String kw_conn_chatmessage = "CHAT";
	public static final String kw_game_requestboard = "REQUEST";
	public static final String kw_conn_acceptconnect = "OK";
	public static final String kw_lobb_leaderboard = "LEADERBOARD";// +
																	// <leaderboard>
	public static final String kw_game_move = "MOVE";
	public static final String kw_lobb_request = "LOBBY";

	// SENT BY BOTH SERVER AND CLIENT:
	public static final String kw_feature_chat = "CHAT";// + <message>
	public static final String kw_feature_cBoardSize = "CUSTOM_BOARD_SIZE";
	public static final String kw_feature_leaderboard = "LEADERBOARD";
	public static final String kw_feature_multiplayer = "MULTIPLAYER";
	public static final String kw_lobb_invite = "INVITE";
	public static final String kw_lobb_acceptinvite = "ACCEPT";
	public static final String kw_lobb_declineinvite = "DECLINE";

	Boolean areweserver;

	// server.equals(null) <=> !client.equals(null)
	// !server.equals(null) <=> client.equals(null)
	Server server;
	Client client;

	// IF WE'RE JUST A CLIENT
	/**
	 * @param client
	 *            is the client using this interpreter.
	 */
	public Interpreter(Client client) {
		this.client = client;
		areweserver = false;
	}

	// IF WE'RE A SERVER
	/**
	 * @param server
	 *            is the server using this interpreter.
	 */
	public Interpreter(Server server) {
		this.server = server;
		areweserver = true;
	}

	/**
	 * Analyzed a command from a client and executes appropiately.
	 * 
	 * @param that
	 *            is the string the interpreter is to analyze.
	 * @param source
	 *            is the ClientHandler which send the message to be analyzed.
	 * @param subcommand
	 *            is false if 'that' is the first word in a line, and is 'true'
	 *            if it's not.
	 */
	public void whatisthatServer(String that, ClientHandler source,
			Boolean subcommand) {
		if (areweserver == true) {
			String[] splitted = that.split("\\s+");
			switch (splitted[0]) {
			case kw_conn_chatmessage:
				if (subcommand) {
					server.setFunction(source, kw_feature_chat, true);
				} else if (that.length() < (kw_conn_chatmessage.length() + 2)) {
					source.sendCommand(kw_conn_error + " SyntaxError");
				} else {
					server.handlechatmessage(source,
							that.substring(kw_conn_chatmessage.length() + 1));
				}
				break;
			case kw_conn_welcome:
				if (that.length() < (kw_conn_welcome.length() + 2)) {
					source.sendCommand(kw_conn_error + " SyntaxError");
				} else {
					server.acceptConnection(source,
							that.substring(kw_conn_welcome.length() + 1));
				}
				break;
			case kw_game_requestboard:
				server.sendBoard(source);
				break;
			case kw_lobb_leaderboard:
				if (subcommand) {
					server.setFunction(source, kw_feature_leaderboard, true);
				} else {
					server.sendLeaderboard();
				}
				break;
			case kw_feature_cBoardSize:
				server.setFunction(source, kw_feature_cBoardSize, true);
				break;
			case kw_feature_multiplayer:
				server.setFunction(source, kw_feature_multiplayer, true);
				break;
			case kw_lobb_invite:
				if (that.length() < (kw_lobb_invite.length() + 2)) {
					source.sendCommand(kw_conn_error + " SyntaxError");
				} else {
					server.invite(that.substring(kw_lobb_invite.length() + 1),
							source);
				}
				break;
			case kw_lobb_acceptinvite:
				server.acceptinvite(source, that);
				break;
			case kw_lobb_declineinvite:
				server.denyinvite(source, that);
				break;
			case kw_game_move:
				if (that.length() < (kw_game_move.length() + 2)) {
					source.sendCommand(kw_conn_error + " SyntaxError");
				} else {
					server.nextMove(source,
							that.substring(kw_game_move.length() + 1));
				}
				break;
			case kw_lobb_request:
				server.sendLobby(source);
				break;

			default:
				System.err.println("Misunderstood command: " + that);
				server.sendError(source, "SyntaxError");
			}

		} else {
			System.err
					.println("We are not the client, use whatisthatClient() instead.");
		}
	}

	/**
	 * Analyzes a command from the server and executes appropiately.
	 * 
	 * @param that
	 *            is the String to be analyzed.
	 */
	public void whatisthatClient(String that) {
		if (areweserver == false && that != null) {
			String[] splitted = that.split("\\s+");
			switch (splitted[0]) {
			case kw_game_sendboard:
				client.refreshBoard(that.substring(kw_game_sendboard.length()+1));
				Board.printNetworkBoard(client.getGame().getBoard().networkBoard());
				break;
			case kw_conn_acceptconnect:
				client.connectionAccepted(that.substring(kw_conn_acceptconnect
						.length() + 1));
				break;
			case kw_game_gameend:
				client.gameend();
				break;
			case kw_conn_gamestart:
				client.gamestart();
				break;
			case kw_conn_lobby:
				client.setLobby(that.substring(kw_conn_lobby.length() + 1));
				break;
			case kw_game_moveok:
				client.moveok(that.substring(kw_game_moveok.length() + 1));
				break;
			case kw_game_reqmove:
				client.getGame().getBoard().printNetworkBoard(client.getGame().getBoard().networkBoard());
				// TODO
				break;
			case kw_feature_chat:
				client.SetSerSup(kw_feature_chat, true);
				break;
			case kw_feature_cBoardSize:
				client.SetSerSup(kw_feature_cBoardSize, true);
				break;
			case kw_feature_leaderboard:
				client.SetSerSup(kw_feature_leaderboard, true);
				break;
			case kw_feature_multiplayer:
				client.SetSerSup(kw_feature_multiplayer, true);
				break;
			case kw_lobb_invite:
				client.invited(that.substring(kw_lobb_invite.length() + 1));
				break;
			case kw_conn_error:
				System.out.println("");
				System.out.println("!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!");
				System.out.println("ERROR RECEIVED FROM SERVER: "
						+ that.substring(kw_conn_error.length() + 1));
				System.out.println("!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!");
				System.out.println("");
				break;
			case kw_lobb_acceptinvite:
				System.out.println("Your invite was accepted by "
						+ that.substring(kw_lobb_acceptinvite.length() + 1)
						+ "!");
				break;
			case kw_lobb_declineinvite:
				System.out.println("Your invite was declined by "
						+ that.substring(kw_lobb_declineinvite.length() + 1)
						+ "!");
				client.inviteDeclined();
				break;
			default:
				System.err.println("Couldn't understand: " + that);
			}
		} else {
			if (that == null) {
				System.out.println("We have been kicked.");
				System.exit(0);
			} else {
				System.err
						.println("We are the server, use whatisthatClient() instead.");
			}
		}
	}

}