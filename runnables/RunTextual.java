package Project.runnables;

import java.util.Scanner;

import Project.gui.ServerGUI;
import Project.logic.ComputerPlayer;
import Project.logic.Game;
import Project.logic.HumanPlayer;
import Project.logic.Mark;
import Project.logic.Player;
import Project.networking.Client;
import Project.networking.ClientBot;
import Project.networking.ClientConsole;
import Project.networking.Server;
import Project.networking.ServerConsole;

public class RunTextual {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("To stay local pick A, to go online pick B");
		String gotten = scanner.nextLine();
		while (!gotten.equals("A") && !gotten.equals("B") && !gotten.equals("a") && !gotten.equals("b")) {
			System.out.println("Pick a valid choice.");
			gotten = scanner.nextLine();
		}
		if (gotten.equals("A") || gotten.equals("a")) {
			System.out.println("Pick name for player 1 (empty for AI)");
			gotten = scanner.nextLine();
			Player p1;
			Player p2;
			if (gotten.equals("")) {
				p1 = new ComputerPlayer(Mark.X);
			} else {
				p1 = new HumanPlayer(gotten, Mark.X);
			}
			System.out.println("Pick name for player 2 (empty for AI)");
			gotten = scanner.nextLine();
			if (gotten.equals("")) {
				p2 = new ComputerPlayer(Mark.O);
			} else {
				p2 = new HumanPlayer(gotten, Mark.O);
			}
			new Game(p1, p2, 7, 6);

		} else if (gotten.equals("B") || gotten.equals("b")) {
			System.out.println("Pick one: Server(A) or Client(B)");
			gotten = scanner.nextLine();
			while (!gotten.equals("A") && !gotten.equals("B")
					&& !gotten.equals("a") && !gotten.equals("b")) {
				System.out.println("That's not a valid choice.");
				gotten = scanner.nextLine();
			}
			if (gotten.equals("A") || gotten.equals("a")) {
				System.out.println("Port?");
				gotten = scanner.nextLine();
				while (!Server.representsInt(gotten)
						|| Integer.parseInt(gotten) > 65535
						|| Integer.parseInt(gotten) < 1) {
					System.out.println("That's not a valid port.");
					gotten = scanner.nextLine();
				}

				Server server = new Server(Integer.parseInt(gotten));
				server.watchInput();
			} else {
				System.out.println("What is your name?");
				System.out.println("If you're an AI, type a.");
				String name = scanner.nextLine();
				while (name.length() < 2 && !name.equals("a")) {
					System.out.println("Longer name please.");
					name = scanner.nextLine();
				}
				if (name.equals("a")) {
					name = ClientBot.NAME;
				}
				System.out
						.println("Welcome, "
								+ name
								+ "! What is the IP of the server you'd like to connect to?");
				System.out.println("If you feel like local, just type a.");
				String ip = scanner.nextLine();
				while (!validIP(ip) && !ip.equals("a")) {
					System.out.println("Please give a valid input.");
					ip = scanner.nextLine();
				}
				if (ip.equals("a")) {
					ip = "127.0.0.1";
				}
				System.out
						.println("And what is the port of the server you'd like to connect to?");
				System.out.println("If you feel like 49999, just type a.");
				int intport = 49999;
				Boolean portokay;
				String port;

				do {
					port = scanner.nextLine();
					portokay = true;
					if (!port.equals("a")) {
						try {
							intport = Integer.parseInt(port);
							if (intport > 65535 || intport < 0) {
								System.out.println("That's not a valid port.");
								portokay = false;
							}
						} catch (NumberFormatException lel) {

							portokay = false;
							System.out.println("That's not a valid port.");
						}
					}
				} while (portokay == false);

				if (port.equals("a")) {
					intport = 49999;
				}

				Client client;
				if (name.equals(ClientBot.NAME)) {
					client = new ClientBot(ip, intport, name);
				} else {
					client = new Client(ip, intport, name);
				}
				client.start();
				ClientConsole clientconsole = new ClientConsole(client);
				clientconsole.start();
			}
		}
	}

	/**
	 * Checks whether a string is a valid IPv4 address. All credit to user
	 * prmatta on stackoverflow.com for this method.
	 * 
	 * @param ip
	 *            is the String to be checked.
	 * @return true if the given String represents a valid IPv4 address or false
	 *         if it doesn't.
	 */
	public static boolean validIP(String ip) {
		try {
			if (ip == null || ip.isEmpty()) {
				return false;
			}

			String[] parts = ip.split("\\.");
			if (parts.length != 4) {
				return false;
			}

			for (String s : parts) {
				int i = Integer.parseInt(s);
				if ((i < 0) || (i > 255)) {
					return false;
				}
			}
			if (ip.endsWith(".")) {
				return false;
			}

			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}

	}
}
