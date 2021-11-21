package br.ufrn.imd.loadbalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Dispatcher implements Runnable {
	protected Socket clientSocket;
	protected Socket  server;
	protected int [] serversPorts;
	protected PrintWriter  serverOutput;
	protected BufferedReader serverInput;
	protected int serverIndex;
	protected AtomicIntegerArray serversStatus;
	
	
	public Dispatcher(Socket incomingSocket) throws UnknownHostException, IOException {
		this.clientSocket = incomingSocket;
		this.serverIndex = 0;
		this.server = null;
		this.serversPorts = new int [2];
		this.serverInput = null;
		this.serverOutput =  null;
		this.serversStatus = null;
		
		this.serversPorts[0] = 4444;
		this.serversPorts[1] = 4445;
		
		this.findFreeServer();
		
//		this.servers[0] = new Socket ("127.0.0.1", 4444);
//		this.serversInputs[0]= new BufferedReader(new InputStreamReader(this.servers[0].getInputStream()));
//		this.serversOutputs[0] =  new PrintWriter(this.servers[0].getOutputStream(), true);
//		
//		this.servers[1] = new Socket ("127.0.0.1", 4445);
//		this.serversInputs[1]= new BufferedReader(new InputStreamReader(this.servers[1].getInputStream()));
//		this.serversOutputs[1] =  new PrintWriter(this.servers[1].getOutputStream(), true);
		
		//this.serversOutputs[0].println("TCFLB");
		System.out.println("[INFO]: Dispatcher initialized");
	}
	
	private int findFreeServer() {
		int freeServerPort = -1;
		for(int i=0; i<2; i++) {
			if(true) {
				try {
					
					this.server = new Socket ("127.0.0.1", this.serversPorts[i]);
					this.serverInput = new BufferedReader(new InputStreamReader(this.server.getInputStream()));
					this.serverOutput =  new PrintWriter(this.server.getOutputStream(), true);
					//serversList.set(i, 0);
					this.serverIndex = i;
				
					System.out.println("[INFO]: Found a free server on index: " + this.serverIndex);
					return i;
					
				} catch (Exception e) {
					
				}
			}
		}
		
		return freeServerPort;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		BufferedReader  clientInput;
		PrintWriter  clientOutput;
		
		
		if(this.serverIndex != -1) {
			try {
				clientOutput = new PrintWriter(clientSocket.getOutputStream(), true);
				clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
				String msg = "DISPATCHER, INIT";
				String serverResponse = "";
				boolean hasData = true;
				while(hasData) {
					msg = clientInput.readLine();
					
					
					if(!msg.equalsIgnoreCase("END") && (msg != null)) {
						System.out.println("[INFO]: Dispatching this data " + msg + " to the server");
						this.serverOutput.println(msg);
						this.serverOutput.flush();
						serverResponse = this.serverInput.readLine();
						clientOutput.println(serverResponse);
						clientOutput.flush();
						
					}
					else {
						hasData = false;
						System.out.println("[INFO]: Ending the connection");
					}
					
					
					
				}
				
				clientOutput.close();
				clientInput.close();
				this.clientSocket.close();
				this.serverOutput.close();
				this.serverInput.close();
				
				
				
			}	
			catch (Exception e) {
				
			}
		}
		
		System.out.println("[INFO]: Closing the dispatcher");
	}

}
