package Project.networking;

import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import Project.logic.*;

public class Client {

	Interpreter inter;
	Game game;

	// Server supports:
	Boolean sersup_chat = false;
	Boolean sersup_cboardsize = false;
	Boolean sersup_leaderboard = false;
	Boolean sersup_multiplayer = false;
	Socket sock;

	private BufferedReader in;
	private BufferedWriter out;

	public Client(String address, int port) {
		inter = new Interpreter(this);
		try {
			sock = new Socket(InetAddress.getByName(address), port);
		} catch (UnknownHostException e) {
			System.err.println("ERROR: unknown host");
			System.exit(0);
		} catch (IOException e) {
			System.err.println("Connection failed");
			System.exit(0);
		}
		try {
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(
					sock.getOutputStream()));
		} catch (IOException alpha) {
			System.err.println("Error while opening streams");
			System.exit(0);
		}
	}
	
	//send a message to a ClientHandler.
	 public void sendMessage(String msg) {
	  try {
	   out.write(msg);
	   out.flush();
	   
	  } catch (IOException e) {
	   e.printStackTrace();
	  }
	  
	 }
	 
	 public void setServerMultiplayer(Boolean bool) {
		 sersup_multiplayer = true;
	 }
	 
	 public void makemove() {
		 // ????
	 }
	 
	 public void setServerLeaderboard(Boolean bool) {
		 sersup_leaderboard = true;
	 }	 
	 
	public void gamestart() {
		game = new Game(new HumanPlayer("This_pc", Mark.X), new NetworkPlayer("That_pc", Mark.O));
	}
	
	public void run() {
		  try {
		  String tussenvar = in.readLine();
		   while(tussenvar!=null){
		    inter.whatisthatClient(this, tussenvar);
		    tussenvar = in.readLine();
		   }
		  } catch (IOException e) {
		   e.printStackTrace();
		  }
		  
		 }
	
	

	public void invited(String other) {

	}

}
