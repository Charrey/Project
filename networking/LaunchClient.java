package Project.networking;

import java.util.Scanner;
import java.lang.NumberFormatException;

public class LaunchClient {

	public static void main(String[] args) {
		System.out.println("Welcome to Connect-Four by René and Pim!");
		Scanner scanner = new Scanner(System.in);
		System.out.println("What is your name?");
		String name = scanner.nextLine();
		while (name.length() < 2) {
			System.out.println("Longer name please.");
			name = scanner.nextLine();
		}
		System.out.println("Welcome, " + name
				+ "! What is the IP of the server you'd like to connect to?");
		System.out.println("If you feel like local playing, just type a.");
		String ip = scanner.nextLine();
		while (!validIP(ip) && !ip.equals("a")) {
			System.out.println("Please give a valid input.");
			ip = scanner.nextLine();
		}
		if (ip.equals("1")) {
			ip = "127.0.0.1";
		}
		System.out
				.println("And what is the port of the server you'd like to connect to?");
		System.out.println("If you feel like local playing, just type a.");
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
		// 65535

		Client client = new Client(ip, intport, name);
		Thread thread = new Thread(client);
		thread.start();
		client.watchInput();

	}

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
