package Project.networking;

import java.util.Scanner;
import java.lang.NumberFormatException;

import Project.gui.ServerGUI;

public class LaunchClientABSOLETE {

	private static Scanner scanner;

	/**
	 * @param args
	 *            is meaningless, but implemented for future use.
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to Connect-Four by René and Pim!");
		scanner = new Scanner(System.in);
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
		System.out.println("Welcome, " + name
				+ "! What is the IP of the server you'd like to connect to?");
		System.out.println("If you feel like local, just type a.");
		String ip = scanner.nextLine();
		while (!validIP(ip) && !ip.equals("a")) {
			System.out.println("Please give a valid input.");
			ip = scanner.nextLine();
		}
		if (ip.equals("a")) {
			ip = ServerGUI.getIP();
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
		Thread thread = new Thread(client);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		thread.start();
		client.watchInput();

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
