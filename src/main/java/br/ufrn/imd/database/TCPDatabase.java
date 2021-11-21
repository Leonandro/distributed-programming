package br.ufrn.imd.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPDatabase extends Thread{
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	private ArrayList <String []> database;
	
	public TCPDatabase (int portNumber, boolean isPrimary) {
		this.database = new ArrayList <String[]> ();
		try {
			this.serverSocket = new ServerSocket(portNumber);
			System.out.println("[INFO]: Database Socket initialized on port: " + portNumber + " - " + isPrimary);
		}catch (Exception e) {
			System.err.println("[ERROR]: Unable to initialize the socket on the port: " + portNumber);
		}
	}
	
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				clientSocket = serverSocket.accept();
				System.out.println("[INFO]: Connection established with the socket");
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				out = new PrintWriter(clientSocket.getOutputStream(), true);
			}catch (Exception e) {
				System.err.println("[ERROR]: Unnable to connect to the client");
			}
			
			try {
				String [] data = in.readLine().split(",");
				this.database.add(data);
				System.out.println("[INFO]: Storing the data - " + data[1]);
			}catch (Exception e) {
				System.err.println("[ERROR]: Unnable to parse the data");
			}
			
			out.close();
			
		}
	}
	
}
