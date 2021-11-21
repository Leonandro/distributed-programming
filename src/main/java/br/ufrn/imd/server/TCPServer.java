package br.ufrn.imd.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPServer extends Thread {
	
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private Socket databaseSocket;
	private PrintWriter out;
	private BufferedReader in;
	boolean isConnected;
	
	public TCPServer (int portNumber) {
		this.isConnected = false;
		try {
			this.serverSocket = new ServerSocket(portNumber);
			System.out.println("[INFO]: Socket initialized on port: " + portNumber);
		}catch (Exception e) {
			System.err.println("[ERROR]: Unable to initialize the socket on the port: " + portNumber);
		}
	}
	
	private void sendToDatabase (String data) {
		
	
		//Connect to database
		try {
			this.databaseSocket = new Socket("127.0.0.1", 8080);
			PrintWriter databaseOut = new PrintWriter(databaseSocket.getOutputStream(), true);
			BufferedReader databaseIn = new BufferedReader(new InputStreamReader(databaseSocket.getInputStream()));
			
			databaseOut.println(data);
			databaseOut.flush();
		}catch(Exception e) {
			System.err.println("[ERROR]: Unnable to connect to the database");
		}
	}
	
	
	public void run () {
		
		while(true) {
			try {
				
				clientSocket = serverSocket.accept();
				System.out.println("[INFO]: Connection established with the socket");
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				
				
				String msg = "INIT, SERVER";
//				(line = in.readLine()) != null
				while(true) {
					System.out.println(msg);
					if(msg.equalsIgnoreCase("END")) {
						break;
					}
					
					//out.println(msg);
					this.sendToDatabase(msg);
					msg = in.readLine();
				}
			}catch (Exception e) {
				
			}
			out.close();
			//in.close();
		}
	}
	
	
}
