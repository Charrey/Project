package Project.networking;

import Project.logic.Board;

//import Project.logic.Game;

public class Interpreter {

	/*
	 * Implementation should still work if these are changed!
	 */

	// SENT BY SERVER ONLY:
	public static final String KW_GAME_SENDBOARD = "BOARD";
	public static final String KW_GAME_GAMEEND = "END";
	public static final String KW_CONN_GAMESTART = "START";
	public static final String KW_CONN_LOBBY = "LOBBY";
	public static final String KW_GAME_MOVEOK = "MOVE";
	public static final String KW_GAME_REQMOVE = "REQUEST";
	public static final String KW_CONN_ERROR = "ERROR";

	// SENT BY CLIENT ONLY:
	public static final String KW_CONN_WELCOME = "CONNECT";
	public static final String KW_CONN_CHATMESSAGE = "CHAT";
	public static final String KW_GAME_REQUESTBOARD = "REQUEST";
	public static final String KW_CONN_ACCEPTCONNECT = "OK";
	public static final String KW_LOBB_LEADERBOARD = "LEADERBOARD";// +
																	// <leaderboard>
	public static final String KW_GAME_MOVE = "MOVE";
	public static final String KW_LOBB_REQUEST = "LOBBY";

	// SENT BY BOTH SERVER AND CLIENT:
	public static final String KW_FEATURE_CHAT = "CHAT";// + <message>
	public static final String KW_FEATURE_CBOARDSIZE = "CUSTOM_BOARD_SIZE";
	public static final String KW_FEATURE_LEADERBOARD = "LEADERBOARD";
	public static final String KW_FEATURE_MULTIPLAYER = "MULTIPLAYER";
	public static final String KW_LOBB_INVITE = "INVITE";
	public static final String KW_LOBB_ACCEPTINVITE = "ACCEPT";
	public static final String KW_LOBB_DECLINEINVITE = "DECLINE";

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
			server.printMessage("Looking up what "+that+" is.");
			String[] splitted = that.split("\\s+");
			switch (splitted[0]) {
			case KW_CONN_CHATMESSAGE:
				if (subcommand) {
					server.setFunction(source, KW_FEATURE_CHAT, true);
				} else if (that.length() < (KW_CONN_CHATMESSAGE.length() + 2)) {
					source.sendCommand(KW_CONN_ERROR + " SyntaxError");
				} else {
					server.handlechatmessage(source,
							that.substring(KW_CONN_CHATMESSAGE.length() + 1));
				}
				break;
			case KW_CONN_WELCOME:
				if (that.length() < (KW_CONN_WELCOME.length() + 2)) {
					source.sendCommand(KW_CONN_ERROR + " SyntaxError");
				} else {
					server.acceptConnection(source,
							that.substring(KW_CONN_WELCOME.length() + 1));
				}
				break;
			case KW_GAME_REQUESTBOARD:
				server.sendBoard(source);
				break;
			case KW_LOBB_LEADERBOARD:
				if (subcommand) {
					server.setFunction(source, KW_FEATURE_LEADERBOARD, true);
				} else {
					server.sendLeaderboard();
				}
				break;
			case KW_FEATURE_CBOARDSIZE:
				server.setFunction(source, KW_FEATURE_CBOARDSIZE, true);
				break;
			case KW_FEATURE_MULTIPLAYER:
				server.setFunction(source, KW_FEATURE_MULTIPLAYER, true);
				break;
			case KW_LOBB_INVITE:
				if (that.length() < (KW_LOBB_INVITE.length() + 2)) {
					source.sendCommand(KW_CONN_ERROR + " SyntaxError");
				} else {
					server.invite(that.substring(KW_LOBB_INVITE.length() + 1),
							source);
				}
				break;
			case KW_LOBB_ACCEPTINVITE:
				server.acceptinvite(source, that.substring(KW_LOBB_ACCEPTINVITE.length()+1));
				break;
			case KW_LOBB_DECLINEINVITE:
				server.denyinvite(source, that);
				break;
			case KW_GAME_MOVE:
				if (that.length() < (KW_GAME_MOVE.length() + 2)) {
					source.sendCommand(KW_CONN_ERROR + " SyntaxError");
				} else {
					server.nextMove(source,
							that.substring(KW_GAME_MOVE.length() + 1));
				}
				break;
			case KW_LOBB_REQUEST:
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
			case KW_GAME_SENDBOARD:
				client.refreshBoard(that.substring(KW_GAME_SENDBOARD.length()+1));
				Board.printNetworkBoard(client.getGame().getBoard().networkBoard());
				break;
			case KW_CONN_ACCEPTCONNECT:
				client.connectionAccepted(that.substring(KW_CONN_ACCEPTCONNECT
						.length() + 1));
				break;
			case KW_GAME_GAMEEND:
				client.gameend();
				break;
			case KW_CONN_GAMESTART:
				client.gamestart(splitted[1],splitted[2]);
				break;
			case KW_CONN_LOBBY:
				client.setLobby(that.substring(KW_CONN_LOBBY.length() + 1));
				break;
			case KW_GAME_MOVEOK:
				client.moveok(that.substring(KW_GAME_MOVEOK.length() + 1));
				break;
			case KW_GAME_REQMOVE:
				Board.printNetworkBoard(client.getGame().getBoard().networkBoard());
				// TODO
				break;
			case KW_FEATURE_CHAT:
				client.SetSerSup(KW_FEATURE_CHAT, true);
				break;
			case KW_FEATURE_CBOARDSIZE:
				client.SetSerSup(KW_FEATURE_CBOARDSIZE, true);
				break;
			case KW_FEATURE_LEADERBOARD:
				client.SetSerSup(KW_FEATURE_LEADERBOARD, true);
				break;
			case KW_FEATURE_MULTIPLAYER:
				client.SetSerSup(KW_FEATURE_MULTIPLAYER, true);
				break;
			case KW_LOBB_INVITE:
				client.invited(that.substring(KW_LOBB_INVITE.length() + 1));
				break;
			case KW_CONN_ERROR:
				client.printMessage("");
				client.printMessage("!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!");
				client.printMessage("ERROR RECEIVED FROM SERVER: "
						+ that.substring(KW_CONN_ERROR.length() + 1));
				client.printMessage("!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!");
				client.printMessage("");
				break;
			case KW_LOBB_ACCEPTINVITE:
				client.printMessage("Your invite was accepted by "
						+ that.substring(KW_LOBB_ACCEPTINVITE.length() + 1)
						+ "!");
				break;
			case KW_LOBB_DECLINEINVITE:
				client.printMessage("Your invite was declined by "
						+ that.substring(KW_LOBB_DECLINEINVITE.length() + 1)
						+ "!");
				client.inviteDeclined();
				break;
			default:
				System.err.println("Couldn't understand: " + that);
			}
		} else {
			if (that == null) {
				client.printMessage("We have been kicked.");
				System.exit(0);
			} else {
				System.err
						.println("We are the server, use whatisthatClient() instead.");
			}
		}
	}

}